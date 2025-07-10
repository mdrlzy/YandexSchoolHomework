package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.expenses

import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.model.TransactionUiModel
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
