package com.mdrlzy.budgetwise.feature.categories.impl.di

import com.mdrlzy.budgetwise.core.di.CoreComponent
import com.mdrlzy.budgetwise.feature.categories.api.di.CategoriesFeatureApi
import com.mdrlzy.budgetwise.feature.categories.impl.presentation.main.CategoriesViewModelFactory
import com.mdrlzy.budgetwise.feature.categories.impl.presentation.search.SearchCategoryViewModelFactory
import dagger.Component

@CategoriesScope
@Component(dependencies = [CoreComponent::class], modules = [CategoriesModule::class])
interface CategoriesComponent: CategoriesFeatureApi {
    fun categoriesViewModelFactory(): CategoriesViewModelFactory
    fun searchCategoryViewModelFactory(): SearchCategoryViewModelFactory
}
