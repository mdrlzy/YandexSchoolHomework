package com.mdrlzy.budgetwise.di.module

import com.mdrlzy.budgetwise.feature.account.data.AccountRepoImpl
import com.mdrlzy.budgetwise.feature.categories.data.repo.CategoryRepoImpl
import com.mdrlzy.budgetwise.feature.transactions.data.TransactionRepoImpl
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.feature.categories.domain.repo.CategoryRepo
import com.mdrlzy.budgetwise.feature.transactions.domain.repo.TransactionRepo
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepoModule {
    @Singleton
    @Binds
    abstract fun transactionRepo(impl: com.mdrlzy.budgetwise.feature.transactions.data.TransactionRepoImpl): com.mdrlzy.budgetwise.feature.transactions.domain.repo.TransactionRepo

    @Singleton
    @Binds
    abstract fun accountRepo(impl: com.mdrlzy.budgetwise.feature.account.data.AccountRepoImpl): AccountRepo

    @Singleton
    @Binds
    abstract fun categoryRepo(impl: com.mdrlzy.budgetwise.feature.categories.data.repo.CategoryRepoImpl): com.mdrlzy.budgetwise.feature.categories.domain.repo.CategoryRepo

}