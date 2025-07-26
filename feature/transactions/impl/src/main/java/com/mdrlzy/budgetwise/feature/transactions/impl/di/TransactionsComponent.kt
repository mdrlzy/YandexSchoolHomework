package com.mdrlzy.budgetwise.feature.transactions.impl.di

import com.mdrlzy.budgetwise.core.di.CoreComponent
import com.mdrlzy.budgetwise.feature.categories.api.di.CategoriesFeatureApi
import com.mdrlzy.budgetwise.feature.transactions.api.di.TransactionFeatureApiProvider
import com.mdrlzy.budgetwise.feature.transactions.api.di.TransactionsFeatureApi
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.usecase.SyncTransactionsUseCase
import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.analyze.AnalyzeTransactionsViewModelFactory
import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.edit.EditTransactionViewModelFactory
import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.expenses.ExpensesViewModelFactory
import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.income.IncomeViewModelFactory
import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.transactionhistory.TransactionHistoryViewModelFactory
import dagger.Component

@TransactionsScope
@Component(
    dependencies = [CoreComponent::class, CategoriesFeatureApi::class],
    modules = [TransactionsModule::class]
)
interface TransactionsComponent: TransactionsFeatureApi {
    fun expensesTodayViewModelFactory(): ExpensesViewModelFactory

    fun incomeViewModelFactory(): IncomeViewModelFactory

    fun transactionHistoryViewModelFactory(): TransactionHistoryViewModelFactory.Factory

    fun editTransactionViewModelFactory(): EditTransactionViewModelFactory.Factory

    fun analyzeTransactionsViewModelFactory(): AnalyzeTransactionsViewModelFactory.Factory

    fun syncTransactionsUseCase(): SyncTransactionsUseCase
}
