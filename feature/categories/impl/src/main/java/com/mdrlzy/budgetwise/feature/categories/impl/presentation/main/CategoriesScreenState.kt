package com.mdrlzy.budgetwise.feature.categories.impl.presentation.main

import com.mdrlzy.budgetwise.core.domain.model.Category

sealed class CategoriesScreenState {
    data object Loading : CategoriesScreenState()

    data class Success(
        val all: List<Category>,
        val filter: String = "",
        val filtered: List<Category>,
    ) : CategoriesScreenState()

    data class Error(val error: Throwable?) : CategoriesScreenState()
}

sealed class CategoriesScreenEffect
