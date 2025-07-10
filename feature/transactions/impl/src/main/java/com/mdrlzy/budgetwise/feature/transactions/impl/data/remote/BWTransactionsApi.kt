package com.mdrlzy.budgetwise.feature.transactions.impl.data.remote

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.network.BWApiClient
import com.mdrlzy.budgetwise.feature.transactions.api.remote.TransactionDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
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
}