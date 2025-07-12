package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.model

import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.TransactionResponse
import java.time.OffsetDateTime

data class TransactionUiModel(
    val id: Long,
    val categoryName: String,
    val amount: String,
    val transactionDate: OffsetDateTime,
    val emoji: String,
    val comment: String?,
)

fun TransactionResponse.toUiModel() =
    TransactionUiModel(
        id = id,
        categoryName = category.name,
        amount = amount,
        transactionDate = transactionDate,
        emoji = category.emoji,
        comment = comment,
    )
