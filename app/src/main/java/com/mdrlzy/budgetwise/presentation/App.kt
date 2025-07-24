package com.mdrlzy.budgetwise.presentation

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.mdrlzy.budgetwise.app.BuildConfig
import com.mdrlzy.budgetwise.core.di.CoreComponent
import com.mdrlzy.budgetwise.core.di.CoreComponentProvider
import com.mdrlzy.budgetwise.core.domain.BuildConfigFields
import com.mdrlzy.budgetwise.di.DaggerAppComponent
import com.mdrlzy.budgetwise.feature.categories.api.di.CategoriesFeatureApiProvider
import com.mdrlzy.budgetwise.feature.categories.impl.di.CategoriesComponentHolder
import com.mdrlzy.budgetwise.feature.transactions.api.di.TransactionFeatureApiProvider
import com.mdrlzy.budgetwise.feature.transactions.api.di.TransactionsFeatureApi
import com.mdrlzy.budgetwise.feature.transactions.impl.di.TransactionsComponentHolder
import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.worker.SyncTransactionsWorker
import com.mdrlzy.budgetwise.presentation.worker.AppWorkerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class App :
    Application(), CoreComponentProvider, CategoriesFeatureApiProvider,
    TransactionFeatureApiProvider, Configuration.Provider {
    lateinit var component: CoreComponent
        private set

    private val appIOScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        val buildConfigFields = BuildConfigFields(BuildConfig.BEARER_TOKEN)
        component =
            DaggerAppComponent
                .factory()
                .create(this, this.applicationContext, buildConfigFields)
        appIOScope.launch {
            TransactionsComponentHolder.provide(this@App).syncTransactionsUseCase().invoke()
        }
        setupSyncOnNetworkAvailable()
        initWorker()
    }

    private fun setupSyncOnNetworkAvailable() {
        component.networkStatus().onlineStatus.drop(1).onEach { online ->
            if (online) {
                TransactionsComponentHolder.provide(this@App).syncTransactionsUseCase().invoke()
            }
        }.launchIn(appIOScope)
    }

    private fun initWorker() {
        val workManager = WorkManager.getInstance(this)
        val constraints =
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val workRequest =
            PeriodicWorkRequest.Builder(
                SyncTransactionsWorker::class.java,
                2,
                TimeUnit.HOURS,
            ).setConstraints(constraints)
                .build()

        workManager.enqueueUniquePeriodicWork(
            SyncTransactionsWorker.NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest,
        )
    }

    override fun provideCoreComponent() = component

    override fun provideCategoriesFeatureApi() = CategoriesComponentHolder.provide(this)

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(AppWorkerFactory())
            .build()

    override fun provideTransactionFeatureApi() = TransactionsComponentHolder.provide(this)
}
