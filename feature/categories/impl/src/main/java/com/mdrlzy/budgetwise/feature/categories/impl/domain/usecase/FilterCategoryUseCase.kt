package com.mdrlzy.budgetwise.feature.categories.impl.domain.usecase

import com.mdrlzy.budgetwise.feature.categories.api.Category
import com.mdrlzy.budgetwise.feature.categories.impl.di.CategoriesScope
import javax.inject.Inject

@CategoriesScope
class FilterCategoryUseCase @Inject constructor() {
    suspend operator fun invoke(all: List<Category>, filter: String): List<Category> {
        return all
            .filter { it.name.contains(filter, ignoreCase = true) }
    }
}