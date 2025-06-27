package com.mdrlzy.budgetwise.feature.transactions.di

import com.mdrlzy.budgetwise.feature.transactions.data.TransactionRepoImpl
import com.mdrlzy.budgetwise.feature.transactions.domain.repo.TransactionRepo
import dagger.Binds
import dagger.Module

@Module
abstract class TransactionsModule {

    @TransactionsScope
    @Binds
    abstract fun transactionsRepo(impl: TransactionRepoImpl): TransactionRepo
}