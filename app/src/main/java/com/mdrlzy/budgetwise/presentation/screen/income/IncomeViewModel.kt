package com.mdrlzy.budgetwise.presentation.screen.income

import androidx.lifecycle.ViewModel
import com.mdrlzy.budgetwise.presentation.model.TransactionUiModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.OffsetDateTime

data class IncomeScreenState (
    val sumAmount: String,
    val incomeItems: List<TransactionUiModel>,
)

sealed class IncomeScreenEffect

class IncomeViewModel: ViewModel(), ContainerHost<IncomeScreenState, IncomeScreenEffect> {
    override val container: Container<IncomeScreenState, IncomeScreenEffect> =
        container(
            IncomeScreenState(
            sumAmount = "600 000 ₽",
            incomeItems = listOf(
                mockTransaction("Зарплата", "500 000 ₽"),
                mockTransaction("Подработка", "100 000 ₽"),
            )
        )
        )
}

private fun mockTransaction(
    name: String,
    amount: String,
) = TransactionUiModel(
    id = 0L,
    accountId = 0L,
    categoryId = 0L,
    categoryName = name,
    amount = amount,
    transactionDate = OffsetDateTime.now(),
    comment = null,
    emoji = "",
    createdAt = OffsetDateTime.now(),
    updatedAt = OffsetDateTime.now(),
)