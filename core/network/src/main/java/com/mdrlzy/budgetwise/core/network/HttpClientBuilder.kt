package com.mdrlzy.budgetwise.core.network

import android.util.Log
import com.mdrlzy.budgetwise.core.domain.BuildConfigFields
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private const val NETWORK_TIME_OUT = 6_000L

object HttpClientBuilder {
    fun build(config: BuildConfigFields): HttpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    explicitNulls = false
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(accessToken = config.bearerToken, refreshToken = "")
                }
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = NETWORK_TIME_OUT
            connectTimeoutMillis = NETWORK_TIME_OUT
            socketTimeoutMillis = NETWORK_TIME_OUT
        }

        install(HttpRequestRetry) {
            maxRetries = 3
            retryIf { request, response ->
                response.status.value in 500..599
            }
            retryOnExceptionIf { _, _ -> false }
            delayMillis { retry ->
                Log.d("Ktor", "Retry")
                2000L
            }
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Logger Ktor =>", message)
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status:", "${response.status.value}")
            }
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }
}