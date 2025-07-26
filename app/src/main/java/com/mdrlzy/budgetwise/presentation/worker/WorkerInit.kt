package com.mdrlzy.budgetwise.presentation.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.worker.SyncTransactionsWorker
import java.util.concurrent.TimeUnit

object WorkerInit {
    fun initSyncWorker(context: Context, repeatInterval: Long) {
        val workManager = WorkManager.getInstance(context)
        val constraints =
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val workRequest =
            PeriodicWorkRequest.Builder(
                SyncTransactionsWorker::class.java,
                repeatInterval,
                TimeUnit.HOURS,
            ).setConstraints(constraints)
                .build()

        workManager.enqueueUniquePeriodicWork(
            SyncTransactionsWorker.NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest,
        )
    }
}