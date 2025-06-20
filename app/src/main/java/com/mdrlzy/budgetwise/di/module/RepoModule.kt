package com.mdrlzy.budgetwise.di.module

import com.mdrlzy.budgetwise.data.repo.AccountRepoImpl
import com.mdrlzy.budgetwise.data.repo.TransactionRepoImpl
import com.mdrlzy.budgetwise.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.domain.repo.TransactionRepo
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepoModule {
    @Singleton
    @Binds
    abstract fun transactionRepo(impl: TransactionRepoImpl): TransactionRepo

    @Singleton
    @Binds
    abstract fun accountRepo(impl: AccountRepoImpl): AccountRepo

}