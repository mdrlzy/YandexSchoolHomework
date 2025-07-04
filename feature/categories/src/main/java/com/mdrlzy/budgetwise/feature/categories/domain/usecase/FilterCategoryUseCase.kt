package com.mdrlzy.budgetwise.feature.categories.domain.usecase

import com.mdrlzy.budgetwise.core.domain.model.Category
import com.mdrlzy.budgetwise.feature.categories.di.CategoriesScope
import javax.inject.Inject

@CategoriesScope
class FilterCategoryUseCase @Inject constructor() {
    suspend operator fun invoke(all: List<Category>, filter: String): List<Category> {
        return all
            .filter { it.name.contains(filter) }
    }
}