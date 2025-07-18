package com.mdrlzy.budgetwise.feature.account.api.remote

import kotlinx.serialization.Serializable

@Serializable
data class AccountDto(
    val id: Long,
    val userId: Long,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String,
)
