package com.mdrlzy.budgetwise.di

import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.feature.account.impl.data.remote.AccountRemoteDataSource
import com.mdrlzy.budgetwise.feature.account.impl.data.AccountRepoImpl
import com.mdrlzy.budgetwise.feature.account.impl.data.local.AccountLocalDataSource
import com.mdrlzy.budgetwise.feature.account.impl.data.remote.BWAccountApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun accountRepo(
        remote: AccountRemoteDataSource,
        local: AccountLocalDataSource,
    ): AccountRepo =
        AccountRepoImpl(remote, local)
}
