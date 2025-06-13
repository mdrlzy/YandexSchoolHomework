package com.mdrlzy.budgetwise.presentation.screen.expensestoday

import androidx.lifecycle.ViewModel
import com.mdrlzy.budgetwise.presentation.model.TransactionUiModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.OffsetDateTime

data class ExpensesTodayState(
    val all: String,
    val currency: String,
    val transactions: List<TransactionUiModel>,
)

sealed class ExpensesTodayEffect {

}

class ExpensesTodayViewModel : ViewModel(), ContainerHost<ExpensesTodayState, ExpensesTodayEffect> {
    override val container: Container<ExpensesTodayState, ExpensesTodayEffect> =
        container(
            ExpensesTodayState(
                all = "436 558",
                currency = "₽",
                transactions = listOf(
                    mockTransaction(
                        name = "Аренда квартиры",
                        amount = "100 000 ₽",
                        emoji = "\uD83C\uDFE0"
                    ),
                    mockTransaction(
                        name = "Одежда",
                        amount = "50 000 ₽",
                        emoji = "\uD83D\uDC57"
                    ),
                    mockTransaction(
                        name = "На собачку",
                        comment = "Джек",
                        amount = "50 000 ₽",
                        emoji = "\uD83D\uDC36"
                    ),
                    mockTransaction(
                        name = "На собачку",
                        comment = "Энни",
                        amount = "50 000 ₽",
                        emoji = "\uD83D\uDC36"
                    ),
                    mockTransaction(
                        name = "Ремонт квартиры",
                        amount = "50 000 ₽",
                        emoji = "\uD83D\uDEE0\uFE0F"
                    ),
                    mockTransaction(
                        name = "Продукты",
                        amount = "100 000 ₽",
                        emoji = "\uD83C\uDF6D"
                    ),
                    mockTransaction(
                        name = "Спортзал",
                        amount = "100 000 ₽",
                        emoji = "\uD83C\uDFCB\uFE0F"
                    ),
                    mockTransaction(
                        name = "Медицина",
                        amount = "100 000 ₽",
                        emoji = "\uD83D\uDC8A",
                    )
                )
            )
        )
}

private fun mockTransaction(
    name: String,
    amount: String,
    comment: String? = null,
    emoji: String,
) = TransactionUiModel(
    id = 0L,
    accountId = 0L,
    categoryId = 0L,
    categoryName = name,
    amount = amount,
    transactionDate = OffsetDateTime.now(),
    comment = comment,
    emoji = emoji,
    createdAt = OffsetDateTime.now(),
    updatedAt = OffsetDateTime.now(),
)