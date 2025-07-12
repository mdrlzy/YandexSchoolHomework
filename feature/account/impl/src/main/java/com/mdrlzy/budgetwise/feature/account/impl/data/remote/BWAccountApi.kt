package com.mdrlzy.budgetwise.feature.account.impl.data.remote

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.network.BWApiClient
import com.mdrlzy.budgetwise.feature.account.api.remote.AccountDto
import com.mdrlzy.budgetwise.feature.account.api.remote.AccountUpdateRequest
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import javax.inject.Inject

class BWAccountApi @Inject constructor(
    private val client: BWApiClient
) {
    suspend fun getAccounts(): EitherT<List<AccountDto>> =
        client.makeRequest { httpClient, baseUrl ->
            httpClient.get {
                url("$baseUrl/accounts")
            }.body()
        }

    suspend fun updateAccount(id: Long, body: AccountUpdateRequest): EitherT<AccountDto> =
        client.makeRequest { httpClient, baseUrl ->
            httpClient.put {
                url("$baseUrl/accounts/$id")
                setBody(body)
            }.body()
        }
}