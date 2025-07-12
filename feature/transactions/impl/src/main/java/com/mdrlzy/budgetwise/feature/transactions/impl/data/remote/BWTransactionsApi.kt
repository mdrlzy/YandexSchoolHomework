package com.mdrlzy.budgetwise.feature.transactions.impl.data.remote

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.network.BWApiClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class BWTransactionsApi @Inject constructor(
    private val client: BWApiClient
) {
    suspend fun getTransactionsByPeriod(
        accountId: Long,
        startDate: String?,
        endDate: String?,
    ): EitherT<List<TransactionDto>> =
        client.makeRequest { httpClient, baseUrl ->
            httpClient.get {
                url("$baseUrl/transactions/account/$accountId/period")
                parameter("startDate", startDate?.format(DateTimeFormatter.ISO_LOCAL_DATE))
                parameter("endDate", endDate?.format(DateTimeFormatter.ISO_LOCAL_DATE))
            }.body()
        }

    suspend fun getTransactionById(id: Long): EitherT<TransactionDto> =
        client.makeRequest { httpClient, baseUrl ->
            httpClient.get {
                url("$baseUrl/transactions/$id")
            }.body()
        }

    suspend fun createTransaction(
        transactionRequest: TransactionRequestDto
    ): EitherT<Unit> =
        client.makeRequest { httpClient, baseUrl ->
            httpClient.post {
                url("$baseUrl/transactions")
                setBody(transactionRequest)
            }.body()
        }

    suspend fun updateTransaction(
        id: Long,
        transactionRequest: TransactionRequestDto
    ): EitherT<TransactionDto> =
        client.makeRequest { httpClient, baseUrl ->
            httpClient.put {
                url("$baseUrl/transactions/$id")
                setBody(transactionRequest)
            }.body()
        }

    suspend fun deleteTransaction(id: Long): EitherT<Unit> =
        client.makeRequest { httpClient, baseUrl ->
            httpClient.delete {
                url("$baseUrl/transactions/$id")
            }.body()
        }
}