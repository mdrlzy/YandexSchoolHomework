package com.mdrlzy.budgetwise.core.network

import arrow.core.Either
import arrow.core.left
import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.expection.NoInternetException
import com.mdrlzy.budgetwise.core.domain.repo.NetworkStatus
import io.ktor.client.HttpClient

class BWApiClient(
    private val client: HttpClient,
    private val networkStatus: NetworkStatus,
) {
    suspend fun <T> makeRequest(
        block: suspend (httpClient: HttpClient, baseUrl: String) -> T
    ): EitherT<T> {
        if (networkStatus.isOnline().not()) {
            return NoInternetException().left()
        }
        return Either.catch {
            block(client, baseUrl)
        }
    }

    companion object {
        val baseUrl = "https://shmr-finance.ru/api/v1/"
    }
}