package com.mdrlzy.budgetwise.feature.transactions.impl.domain.model

import com.mdrlzy.budgetwise.core.domain.model.AccountBrief
import com.mdrlzy.budgetwise.core.domain.model.Category
import java.time.OffsetDateTime

data class TransactionResponse(
    val id: Long,
    val account: AccountBrief,
    val category: Category,
    val amount: String,
    val transactionDate: OffsetDateTime,
    val comment: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
