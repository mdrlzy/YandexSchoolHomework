package com.mdrlzy.budgetwise.feature.categories.api

import com.mdrlzy.budgetwise.core.domain.EitherT

interface CategoryRepo {
    suspend fun getAll(): EitherT<List<Category>>
}
