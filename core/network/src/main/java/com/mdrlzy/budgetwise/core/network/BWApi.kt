package com.mdrlzy.budgetwise.core.network

import arrow.core.Either
import arrow.core.left
import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.expection.NoInternetException
import com.mdrlzy.budgetwise.core.domain.repo.NetworkStatus
import com.mdrlzy.budgetwise.core.network.response.AccountDto
import com.mdrlzy.budgetwise.core.network.response.AccountResponse
import com.mdrlzy.budgetwise.core.network.response.AccountUpdateRequest
import com.mdrlzy.budgetwise.core.network.response.CategoryResponse
import com.mdrlzy.budgetwise.core.network.response.TransactionDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import java.time.format.DateTimeFormatter

class BWApi(
    private val client: HttpClient,
    private val networkStatus: NetworkStatus,
) {
    suspend fun getAccounts(): EitherT<List<AccountDto>> =
        safeRequest {
            client.get {
                url("$baseUrl/accounts")
            }.body()
        }

    suspend fun getAccountById(id: Long): EitherT<AccountResponse> =
        safeRequest {
            client.get {
                url("$baseUrl/accounts/$id")
            }.body()
        }

    suspend fun updateAccount(id: Long, body: AccountUpdateRequest): EitherT<AccountDto> =
        safeRequest {
            client.put {
                url("$baseUrl/accounts/$id")
                setBody(body)
            }.body()
        }

    suspend fun getCategories(): EitherT<List<CategoryResponse>> =
        safeRequest {
            client.get {
                url("$baseUrl/categories")
            }.body()
        }

    suspend fun getTransactionsByPeriod(
        accountId: Long,
        startDate: String?,
        endDate: String?,
    ): EitherT<List<TransactionDto>> =
        safeRequest {
            client.get {
                url("$baseUrl/transactions/account/$accountId/period")
                parameter("startDate", startDate?.format(DateTimeFormatter.ISO_LOCAL_DATE))
                parameter("endDate", endDate?.format(DateTimeFormatter.ISO_LOCAL_DATE))
            }.body()
        }

    suspend fun getTransactionById(id: Long): EitherT<TransactionDto> =
        safeRequest {
            client.get {
                url("$baseUrl/transactions/$id")
            }.body()
        }

    private inline fun <T> safeRequest(block: () -> T): EitherT<T> {
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
