package com.mdrlzy.budgetwise.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class AccountDto(
    val id: Long,
    val userId: Long,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
)