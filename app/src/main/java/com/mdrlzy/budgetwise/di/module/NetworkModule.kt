package com.mdrlzy.budgetwise.di.module

import com.mdrlzy.budgetwise.data.network.BWApi
import com.mdrlzy.budgetwise.data.network.HttpClientBuilder
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun httpClient() = HttpClientBuilder.build()

    @Singleton
    @Provides
    fun bwApi(client: HttpClient) = BWApi(client)
}