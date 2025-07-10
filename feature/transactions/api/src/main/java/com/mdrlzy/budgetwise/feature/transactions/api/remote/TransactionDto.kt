package com.mdrlzy.budgetwise.feature.transactions.api.remote

import com.mdrlzy.budgetwise.feature.account.api.remote.AccountBriefResponse
import com.mdrlzy.budgetwise.feature.categories.api.remote.CategoryResponse
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
