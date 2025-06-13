package com.mdrlzy.budgetwise.presentation.screen.expenses

import androidx.lifecycle.ViewModel
import com.mdrlzy.budgetwise.domain.model.Category
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

data class ExpensesScreenState(
    val categories: List<Category>,
    val searchQuery: String = "",
)

sealed class ExpensesScreenEffect

class ExpensesViewModel: ViewModel(), ContainerHost<ExpensesScreenState, ExpensesScreenEffect> {
    override val container: Container<ExpensesScreenState, ExpensesScreenEffect> =
        container(
            ExpensesScreenState(
            listOf(
                mockCategory("Аренда квартиры", "\uD83C\uDFE0"),
                mockCategory("Одежда", "\uD83D\uDC57"),
                mockCategory("На собачку", "\uD83D\uDC36"),
                mockCategory("На собачку", "\uD83D\uDC36"),
                mockCategory("Ремонт квартиры", "\uD83D\uDEE0\uFE0F"),
                mockCategory("Продукты", "\uD83C\uDF6D"),
                mockCategory("Спортзал", "\uD83C\uDFCB\uFE0F"),
                mockCategory("Медицина", "\uD83D\uDC8A")
            )
        )
        )

    fun onSearchQueryChange(query: String) = blockingIntent {
        reduce {
            state.copy(searchQuery = query)
        }
    }
}

private fun mockCategory(name: String, emoji: String) = Category(0L, name, emoji, false)