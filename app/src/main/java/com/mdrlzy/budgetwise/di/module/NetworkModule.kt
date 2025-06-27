package com.mdrlzy.budgetwise.di.module

import android.content.Context
import com.mdrlzy.budgetwise.data.network.BWApi
import com.mdrlzy.budgetwise.core.network.HttpClientBuilder
import com.mdrlzy.budgetwise.core.network.NetworkStatusImpl
import com.mdrlzy.budgetwise.core.domain.repo.NetworkStatus
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun httpClient() = com.mdrlzy.budgetwise.core.network.HttpClientBuilder.build()

    @Singleton
    @Provides
    fun bwApi(client: HttpClient, networkStatus: NetworkStatus) = BWApi(client, networkStatus)

    @Singleton
    @Provides
    fun networkStatus(ctx: Context): NetworkStatus =
        com.mdrlzy.budgetwise.core.network.NetworkStatusImpl(ctx)
}