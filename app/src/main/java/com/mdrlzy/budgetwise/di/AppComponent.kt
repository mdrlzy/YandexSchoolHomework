package com.mdrlzy.budgetwise.di

import android.app.Application
import android.content.Context
import com.mdrlzy.budgetwise.di.module.NetworkModule
import com.mdrlzy.budgetwise.di.module.RepoModule
import com.mdrlzy.budgetwise.presentation.screen.expensestoday.ExpensesTodayViewModelFactory
import com.mdrlzy.budgetwise.presentation.screen.income.IncomeViewModelFactory
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

    fun expensesTodayViewModelFactory(): ExpensesTodayViewModelFactory

    fun incomeViewModelFactory(): IncomeViewModelFactory


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance context: Context
        ): AppComponent
    }
}