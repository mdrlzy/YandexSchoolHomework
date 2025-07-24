package com.mdrlzy.budgetwise.feature.transactions.impl.di

import com.mdrlzy.budgetwise.feature.transactions.impl.data.TransactionRepoImpl
import com.mdrlzy.budgetwise.feature.transactions.api.TransactionRepo
import dagger.Binds
import dagger.Module

@Module
abstract class TransactionsModule {
    @TransactionsScope
    @Binds
    abstract fun transactionsRepo(impl: TransactionRepoImpl): TransactionRepo
}
