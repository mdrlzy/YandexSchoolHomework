package com.mdrlzy.budgetwise.feature.categories.domain.repo

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Category

interface CategoryRepo {
    suspend fun getAll(): EitherT<List<Category>>
}