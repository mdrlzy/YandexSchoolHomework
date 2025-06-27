package com.mdrlzy.budgetwise.feature.transactions.di

import com.mdrlzy.budgetwise.core.di.CoreComponent
import com.mdrlzy.budgetwise.feature.transactions.presentation.screen.expenses.ExpensesViewModelFactory
import com.mdrlzy.budgetwise.feature.transactions.presentation.screen.income.IncomeViewModelFactory
import com.mdrlzy.budgetwise.feature.transactions.presentation.screen.transactionhistory.TransactionHistoryViewModelFactory
import dagger.Component

@TransactionsScope
@Component(dependencies = [CoreComponent::class], modules = [TransactionsModule::class])
interface TransactionsComponent {
    fun expensesTodayViewModelFactory(): ExpensesViewModelFactory

    fun incomeViewModelFactory(): IncomeViewModelFactory

    fun transactionHistoryViewModelFactory(): TransactionHistoryViewModelFactory.Factory
}