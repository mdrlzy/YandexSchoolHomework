package com.mdrlzy.budgetwise.feature.transactions.impl.domain.model

import java.time.OffsetDateTime

data class TransactionSimple(
    val id: Long,
    val accountId: Long,
    val categoryId: Long,
    val amount: String,
    val transactionDate: OffsetDateTime,
    val comment: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)