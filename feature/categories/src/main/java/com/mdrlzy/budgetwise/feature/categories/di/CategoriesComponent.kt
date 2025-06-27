package com.mdrlzy.budgetwise.feature.categories.di

import com.mdrlzy.budgetwise.core.di.CoreComponent
import com.mdrlzy.budgetwise.feature.categories.presentation.CategoriesViewModelFactory
import dagger.Component

@CategoriesScope
@Component(dependencies = [CoreComponent::class], modules = [CategoriesModule::class])
interface CategoriesComponent {
    fun categoriesViewModelFactory(): CategoriesViewModelFactory
}