package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.analyze

import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.model.TransactionUiModel
import java.math.BigDecimal
import java.time.OffsetDateTime

sealed class AnalyzeTransactionsScreenState {
    data object Loading : AnalyzeTransactionsScreenState()

    data class Success(
        val sum: BigDecimal = BigDecimal.ZERO,
        val currency: String = "",
        val start: OffsetDateTime,
        val end: OffsetDateTime,
        val categories: List<CategorySummary>
    ) : AnalyzeTransactionsScreenState()

    data class Error(val error: Throwable?) : AnalyzeTransactionsScreenState()
}

sealed class AnalyzeTransactionsScreenEffect