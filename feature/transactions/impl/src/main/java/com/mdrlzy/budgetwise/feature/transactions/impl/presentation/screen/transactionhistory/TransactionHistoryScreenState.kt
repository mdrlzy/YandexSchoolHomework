package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.transactionhistory

import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.model.TransactionUiModel
import java.math.BigDecimal
import java.time.OffsetDateTime

sealed class TransactionHistoryScreenState {
    data object Loading : TransactionHistoryScreenState()

    data class Success(
        val sum: BigDecimal = BigDecimal.ZERO,
        val currency: String = "",
        val transactions: List<TransactionUiModel> = emptyList(),
        val startDate: OffsetDateTime,
        val endDate: OffsetDateTime,
    ) : TransactionHistoryScreenState()

    data class Error(val error: Throwable?) : TransactionHistoryScreenState()
}

sealed class TransactionHistoryScreenEffect
