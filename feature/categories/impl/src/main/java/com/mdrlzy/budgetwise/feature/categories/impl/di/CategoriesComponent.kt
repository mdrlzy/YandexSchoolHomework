package com.mdrlzy.budgetwise.feature.categories.impl.di

import com.mdrlzy.budgetwise.core.di.CoreComponent
import com.mdrlzy.budgetwise.feature.categories.impl.presentation.CategoriesViewModelFactory
import dagger.Component

@CategoriesScope
@Component(dependencies = [CoreComponent::class], modules = [CategoriesModule::class])
interface CategoriesComponent {
    fun categoriesViewModelFactory(): CategoriesViewModelFactory
}
