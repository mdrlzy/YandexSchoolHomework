package com.mdrlzy.budgetwise.feature.categories.presentation

import com.mdrlzy.budgetwise.core.domain.model.Category

sealed class CategoriesScreenState {
    data object Loading : CategoriesScreenState()

    data class Success(
        val categories: List<Category>,
        val searchQuery: String = "",
    ) : CategoriesScreenState()

    data class Error(val error: Throwable?) : CategoriesScreenState()
}

sealed class CategoriesScreenEffect
