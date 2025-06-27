package com.mdrlzy.budgetwise.di

import android.app.Application
import android.content.Context
import com.mdrlzy.budgetwise.di.module.NetworkModule
import com.mdrlzy.budgetwise.di.module.RepoModule
import com.mdrlzy.budgetwise.core.domain.repo.NetworkStatus
import com.mdrlzy.budgetwise.feature.account.presentation.AccountViewModelFactory
import com.mdrlzy.budgetwise.feature.categories.presentation.CategoriesViewModelFactory
import com.mdrlzy.budgetwise.feature.transactions.presentation.screen.expenses.ExpensesViewModelFactory
import com.mdrlzy.budgetwise.feature.transactions.presentation.screen.income.IncomeViewModelFactory
import com.mdrlzy.budgetwise.feature.transactions.presentation.screen.transactionhistory.TransactionHistoryViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RepoModule::class,
        NetworkModule::class,
    ]
)
interface AppComponent {

    fun networkStatus(): NetworkStatus

    fun expensesTodayViewModelFactory(): com.mdrlzy.budgetwise.feature.transactions.presentation.screen.expenses.ExpensesViewModelFactory

    fun incomeViewModelFactory(): com.mdrlzy.budgetwise.feature.transactions.presentation.screen.income.IncomeViewModelFactory

    fun accountViewModelFactory(): com.mdrlzy.budgetwise.feature.account.presentation.AccountViewModelFactory

    fun categoriesViewModelFactory(): com.mdrlzy.budgetwise.feature.categories.presentation.CategoriesViewModelFactory

    fun transactionHistoryViewModelFactory(): com.mdrlzy.budgetwise.feature.transactions.presentation.screen.transactionhistory.TransactionHistoryViewModelFactory.Factory


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance context: Context
        ): AppComponent
    }
}