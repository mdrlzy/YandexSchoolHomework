package com.mdrlzy.budgetwise.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class AccountBriefResponse(
    val id: Long,
    val name: String,
    val balance: String,
    val currency: String
)