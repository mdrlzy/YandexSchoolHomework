package com.mdrlzy.budgetwise.core.di.module

import android.app.Application
import com.mdrlzy.budgetwise.core.db.BWDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBModule {

    @Singleton
    @Provides
    fun database(app: Application) = BWDatabase.build(app)

    @Provides
    fun accountDao(db: BWDatabase) = db.accountDao()

    @Provides
    fun categoryDao(db: BWDatabase) = db.categoryDao()

    @Provides
    fun transactionDao(db: BWDatabase) = db.transactionDao()

    @Provides
    fun pendingTransactionDao(db: BWDatabase) = db.pendingTransactionDao()
}