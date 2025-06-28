package com.mdrlzy.budgetwise.feature.categories.di

import com.mdrlzy.budgetwise.feature.categories.data.repo.CategoryRepoImpl
import com.mdrlzy.budgetwise.feature.categories.domain.repo.CategoryRepo
import dagger.Binds
import dagger.Module

@Module
abstract class CategoriesModule {
    @CategoriesScope
    @Binds
    abstract fun categoriesRepo(impl: CategoryRepoImpl): CategoryRepo
}
