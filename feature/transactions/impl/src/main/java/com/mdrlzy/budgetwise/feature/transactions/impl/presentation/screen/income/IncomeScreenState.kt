package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.income

import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.model.TransactionUiModel
import java.math.BigDecimal

sealed class IncomeScreenState {
    data object Loading : IncomeScreenState()

    data class Success(
        val sum: BigDecimal = BigDecimal.ZERO,
        val currency: String = "",
        val transactions: List<TransactionUiModel> = emptyList(),
    ) : IncomeScreenState()

    data class Error(val error: Throwable?) : IncomeScreenState()
}

sealed class IncomeScreenEffect
