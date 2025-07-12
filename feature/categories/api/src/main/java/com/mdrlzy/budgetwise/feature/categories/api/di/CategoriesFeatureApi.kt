package com.mdrlzy.budgetwise.feature.categories.api.di

import com.mdrlzy.budgetwise.feature.categories.api.CategoryRepo

interface CategoriesFeatureApi {
    fun categoryRepo(): CategoryRepo
}