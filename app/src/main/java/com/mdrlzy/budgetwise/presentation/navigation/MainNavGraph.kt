package com.mdrlzy.budgetwise.presentation.navigation

import com.ramcosta.composedestinations.animations.defaults.DefaultFadingTransitions
import com.ramcosta.composedestinations.annotation.ExternalDestination
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.generated.account.destinations.AccountScreenDestination
import com.ramcosta.composedestinations.generated.categories.destinations.CategoriesScreenDestination
import com.ramcosta.composedestinations.generated.settings.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.generated.transactions.destinations.EditTransactionScreenDestination
import com.ramcosta.composedestinations.generated.transactions.destinations.ExpensesScreenDestination
import com.ramcosta.composedestinations.generated.transactions.destinations.IncomeScreenDestination
import com.ramcosta.composedestinations.generated.transactions.destinations.TransactionHistoryScreenDestination

@NavHostGraph(
    defaultTransitions = DefaultFadingTransitions::class,
)
annotation class MainNavGraph {
    @ExternalDestination<EditTransactionScreenDestination>
    @ExternalDestination<ExpensesScreenDestination>
    @ExternalDestination<IncomeScreenDestination>
    @ExternalDestination<TransactionHistoryScreenDestination>
    @ExternalDestination<AccountScreenDestination>
    @ExternalDestination<CategoriesScreenDestination>
    @ExternalDestination<SettingsScreenDestination>
    companion object Includes
}
