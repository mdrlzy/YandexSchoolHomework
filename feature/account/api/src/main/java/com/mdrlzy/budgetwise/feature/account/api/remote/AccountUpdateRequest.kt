package com.mdrlzy.budgetwise.feature.account.api.remote

import kotlinx.serialization.Serializable

@Serializable
data class AccountUpdateRequest(
    val name: String,
    val balance: String,
    val currency: String,
)