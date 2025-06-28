package com.mdrlzy.budgetwise.feature.categories.data.repo

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Category
import com.mdrlzy.budgetwise.core.network.BWApi
import com.mdrlzy.budgetwise.core.network.response.CategoryResponse
import com.mdrlzy.budgetwise.feature.categories.domain.repo.CategoryRepo
import javax.inject.Inject

class CategoryRepoImpl
    @Inject
    constructor(private val api: BWApi) : CategoryRepo {
        override suspend fun getAll(): EitherT<List<Category>> {
            return api.getCategories().map { categories -> categories.map { it.toDomain() } }
        }
    }

private fun CategoryResponse.toDomain() = Category(id, name, emoji, isIncome)
