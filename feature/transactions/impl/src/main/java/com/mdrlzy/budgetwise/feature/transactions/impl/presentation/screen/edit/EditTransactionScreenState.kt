package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.edit

import com.mdrlzy.budgetwise.core.domain.model.Account
import com.mdrlzy.budgetwise.core.domain.model.Category
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.TransactionRequest
import java.time.OffsetDateTime

sealed class EditTransactionScreenState {
    data object Loading : EditTransactionScreenState()

    data class Success(
        val account: Account,
        val date: OffsetDateTime,
        val category: Category,
        val amount: String,
        val comment: String,
    ) : EditTransactionScreenState()

    data class Error(val error: Throwable?) : EditTransactionScreenState()
}

sealed class EditTransactionScreenEffect {
    data object NavigateBack : EditTransactionScreenEffect()
}

fun EditTransactionScreenState.Success.toRequest() = TransactionRequest(
    accountId = account.id,
    categoryId = category.id,
    amount = amount.toString(),
    transactionDate = date,
    comment = comment.ifEmpty { null }
)