package com.mdrlzy.budgetwise.feature.transactions.impl.data.remote

import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class TransactionSimpleDto(
    val id: Long,
    val accountId: Long,
    val categoryId: Long,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String,
)