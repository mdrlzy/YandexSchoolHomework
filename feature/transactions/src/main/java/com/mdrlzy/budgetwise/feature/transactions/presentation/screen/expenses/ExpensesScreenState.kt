package com.mdrlzy.budgetwise.feature.transactions.presentation.screen.expenses

import com.mdrlzy.budgetwise.feature.transactions.presentation.model.TransactionUiModel
import java.math.BigDecimal

sealed class ExpensesScreenState {
    data object Loading : ExpensesScreenState()

    data class Success(
        val sum: BigDecimal = BigDecimal.ZERO,
        val currency: String = "",
        val transactions: List<TransactionUiModel> = emptyList(),
    ) : ExpensesScreenState()

    data class Error(val error: Throwable?) : ExpensesScreenState()
}

sealed class ExpensesScreenEffect
