package com.mdrlzy.budgetwise.feature.categories.impl.data.remote

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Category
import com.mdrlzy.budgetwise.feature.categories.api.remote.CategoryResponse
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(
    private val api: BWCategoriesApi
) {
    suspend fun getAll(): EitherT<List<Category>> {
        return api.getCategories().map { categories -> categories.map { it.toDomain() } }
    }
}

private fun CategoryResponse.toDomain() = Category(id, name, emoji, isIncome)