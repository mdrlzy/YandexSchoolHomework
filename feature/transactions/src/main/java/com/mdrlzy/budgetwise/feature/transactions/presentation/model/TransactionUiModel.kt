package com.mdrlzy.budgetwise.feature.transactions.presentation.model

import com.mdrlzy.budgetwise.feature.transactions.domain.model.Transaction
import java.time.OffsetDateTime

data class TransactionUiModel(
    val id: Long,
    val categoryName: String,
    val amount: String,
    val transactionDate: OffsetDateTime,
    val emoji: String,
    val comment: String?,
)

fun com.mdrlzy.budgetwise.feature.transactions.domain.model.Transaction.toUiModel() =
    TransactionUiModel(
        id = id,
        categoryName = category.name,
        amount = amount,
        transactionDate = transactionDate,
        emoji = category.emoji,
        comment = comment,
    )
