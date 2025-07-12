package com.mdrlzy.budgetwise.feature.categories.api

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Category

interface CategoryRepo {
    suspend fun getAll(): EitherT<List<Category>>
}
