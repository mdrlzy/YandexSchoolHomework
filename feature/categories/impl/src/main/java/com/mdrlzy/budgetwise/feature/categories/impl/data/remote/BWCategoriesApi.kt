package com.mdrlzy.budgetwise.feature.categories.impl.data.remote

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.network.BWApiClient
import com.mdrlzy.budgetwise.feature.categories.api.remote.CategoryResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import javax.inject.Inject

class BWCategoriesApi @Inject constructor(
    private val client: BWApiClient
) {
    suspend fun getCategories(): EitherT<List<CategoryResponse>> =
        client.makeRequest { httpClient, baseUrl ->
            httpClient.get {
                url("$baseUrl/categories")
            }.body()
        }
}