package com.mdrlzy.budgetwise.presentation.model

import java.time.OffsetDateTime

data class TransactionUiModel(
    val id: Long,
    val accountId: Long,
    val categoryId: Long,
    val categoryName: String,
    val amount: String,
    val transactionDate: OffsetDateTime,
    val emoji: String,
    val comment: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)