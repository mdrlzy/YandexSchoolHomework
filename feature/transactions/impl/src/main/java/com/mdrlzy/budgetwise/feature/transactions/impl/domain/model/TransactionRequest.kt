package com.mdrlzy.budgetwise.feature.transactions.impl.domain.model

import java.time.OffsetDateTime

data class TransactionRequest(
    val accountId: Long,
    val categoryId: Long,
    val amount: String,
    val transactionDate: OffsetDateTime,
    val comment: String? = null,
)