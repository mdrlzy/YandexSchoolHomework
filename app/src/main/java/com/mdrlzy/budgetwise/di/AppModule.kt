package com.mdrlzy.budgetwise.di

import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.core.network.BWApi
import com.mdrlzy.budgetwise.feature.account.data.AccountRepoImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun accountRepo(api: BWApi): AccountRepo = AccountRepoImpl(api)
}
