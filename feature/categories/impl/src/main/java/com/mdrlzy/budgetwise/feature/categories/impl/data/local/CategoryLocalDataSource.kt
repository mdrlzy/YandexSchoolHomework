package com.mdrlzy.budgetwise.feature.categories.impl.data.local

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.mdrlzy.budgetwise.core.db.dao.CategoryDao
import com.mdrlzy.budgetwise.core.db.entity.CategoryEntity
import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.feature.categories.api.Category
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    private val dao: CategoryDao,
) {
    suspend fun getAll(): EitherT<List<Category>> {
        val categories = dao.getAll().map { it.toCategory() }
        categories.ifEmpty { return IllegalStateException("Categories are empty").left() }

        return categories.right()
    }

    suspend fun save(categories: List<Category>) {
        dao.insertAll(categories.map { it.toEntity() })
    }
}

private fun CategoryEntity.toCategory() = Category(
    id,
    name,
    emoji,
    isIncome
)

private fun Category.toEntity() = CategoryEntity(
    id,
    name,
    emoji,
    isIncome
)