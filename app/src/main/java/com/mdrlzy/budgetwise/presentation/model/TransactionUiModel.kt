package com.mdrlzy.budgetwise.presentation.model

import com.mdrlzy.budgetwise.domain.model.Transaction
import java.time.OffsetDateTime

data class TransactionUiModel(
    val id: Long,
    val categoryName: String,
    val amount: String,
    val transactionDate: OffsetDateTime,
    val emoji: String,
    val comment: String?,
)

fun Transaction.toUiModel() = TransactionUiModel(
    id = id,
    categoryName = category.name,
    amount = amount,
    transactionDate = transactionDate,
    emoji = category.emoji,
    comment = comment,
)