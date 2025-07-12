package com.mdrlzy.budgetwise.feature.transactions.impl.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequestDto(
    val accountId: Long,
    val categoryId: Long,
    val amount: String,
    val transactionDate: String,
    val comment: String? = null,
)