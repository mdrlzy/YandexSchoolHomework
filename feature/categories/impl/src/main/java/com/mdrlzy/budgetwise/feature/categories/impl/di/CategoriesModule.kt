package com.mdrlzy.budgetwise.feature.categories.impl.di

import com.mdrlzy.budgetwise.feature.categories.impl.data.CategoryRepoImpl
import com.mdrlzy.budgetwise.feature.categories.impl.domain.repo.CategoryRepo
import dagger.Binds
import dagger.Module

@Module
abstract class CategoriesModule {
    @CategoriesScope
    @Binds
    abstract fun categoriesRepo(impl: CategoryRepoImpl): CategoryRepo
}
