package com.mdrlzy.budgetwise.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val id: Long,
    val account: AccountBriefResponse,
    val category: CategoryResponse,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String,
)
