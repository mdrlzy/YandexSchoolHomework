package com.mdrlzy.budgetwise.di

import android.app.Application
import android.content.Context
import com.mdrlzy.budgetwise.di.module.NetworkModule
import com.mdrlzy.budgetwise.di.module.RepoModule
import com.mdrlzy.budgetwise.domain.repo.NetworkStatus
import com.mdrlzy.budgetwise.presentation.screen.account.AccountViewModelFactory
import com.mdrlzy.budgetwise.presentation.screen.categories.CategoriesViewModelFactory
import com.mdrlzy.budgetwise.presentation.screen.expenses.ExpensesViewModelFactory
import com.mdrlzy.budgetwise.presentation.screen.income.IncomeViewModelFactory
import com.mdrlzy.budgetwise.presentation.screen.transactionhistory.TransactionHistoryViewModelFactory
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

    fun expensesTodayViewModelFactory(): ExpensesViewModelFactory

    fun incomeViewModelFactory(): IncomeViewModelFactory

    fun accountViewModelFactory(): AccountViewModelFactory

    fun categoriesViewModelFactory(): CategoriesViewModelFactory

    fun transactionHistoryViewModelFactory(): TransactionHistoryViewModelFactory.Factory


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance context: Context
        ): AppComponent
    }
}