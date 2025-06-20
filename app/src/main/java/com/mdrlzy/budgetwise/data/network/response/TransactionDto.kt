package com.mdrlzy.budgetwise.data.network.response

import com.mdrlzy.budgetwise.domain.model.AccountBrief
import com.mdrlzy.budgetwise.domain.model.Category
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
    val updatedAt: String
)