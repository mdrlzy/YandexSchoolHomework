package com.mdrlzy.budgetwise.core.di.module

import android.content.Context
import com.mdrlzy.budgetwise.core.domain.BuildConfigFields
import com.mdrlzy.budgetwise.core.domain.repo.NetworkStatus
import com.mdrlzy.budgetwise.core.network.HttpClientBuilder
import com.mdrlzy.budgetwise.core.network.NetworkStatusImpl
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun httpClient(buildConfigFields: BuildConfigFields) =
        HttpClientBuilder.build(buildConfigFields)

    @Singleton
    @Provides
    fun bwApi(client: HttpClient, networkStatus: NetworkStatus) = BWClient(client, networkStatus)

    @Singleton
    @Provides
    fun networkStatus(ctx: Context): NetworkStatus = NetworkStatusImpl(ctx)
}