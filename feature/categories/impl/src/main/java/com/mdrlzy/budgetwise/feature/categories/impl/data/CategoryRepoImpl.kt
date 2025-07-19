package com.mdrlzy.budgetwise.feature.categories.impl.data

import arrow.core.right
import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.feature.categories.api.Category
import com.mdrlzy.budgetwise.feature.categories.impl.data.remote.CategoryRemoteDataSource
import com.mdrlzy.budgetwise.feature.categories.api.CategoryRepo
import com.mdrlzy.budgetwise.feature.categories.impl.data.local.CategoryLocalDataSource
import javax.inject.Inject

class CategoryRepoImpl @Inject constructor(
    private val remote: CategoryRemoteDataSource,
    private val local: CategoryLocalDataSource,
) : CategoryRepo {
    override suspend fun getAll(): EitherT<List<Category>> {
        return remote.getAll().fold(
            ifLeft = {
                local.getAll()
            },
            ifRight = {
                local.save(it)
                it.right()
            }
        )
    }
}
