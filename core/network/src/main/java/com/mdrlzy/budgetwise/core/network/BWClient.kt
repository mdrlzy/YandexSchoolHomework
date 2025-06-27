package com.mdrlzy.budgetwise.core.network

import arrow.core.Either
import arrow.core.left
import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.expection.NoInternetException
import com.mdrlzy.budgetwise.core.domain.repo.NetworkStatus
import io.ktor.client.HttpClient

class BWClient(
    private val client: HttpClient,
    val networkStatus: NetworkStatus,
)  {

    inline fun <T> makeRequest(block: () -> T): EitherT<T> {
        if (networkStatus.isOnline().not()) {
            return NoInternetException().left()
        }
        return Either.catch {
            block()
        }
    }

    companion object {
        val baseUrl = "https://shmr-finance.ru/api/v1/"
    }
}