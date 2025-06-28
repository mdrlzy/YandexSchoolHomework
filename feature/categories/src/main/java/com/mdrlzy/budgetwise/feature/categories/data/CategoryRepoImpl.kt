package com.mdrlzy.budgetwise.feature.categories.data

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Category
import com.mdrlzy.budgetwise.feature.categories.domain.repo.CategoryRepo
import javax.inject.Inject

class CategoryRepoImpl @Inject constructor(
    private val remote: CategoryRemoteDataSource
) : CategoryRepo {
    override suspend fun getAll(): EitherT<List<Category>> {
        return remote.getAll()
    }
}
