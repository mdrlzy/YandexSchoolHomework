package com.mdrlzy.budgetwise.domain.repo

import com.mdrlzy.budgetwise.domain.EitherT
import com.mdrlzy.budgetwise.domain.model.Category

interface CategoryRepo {
    suspend fun getAll(): EitherT<List<Category>>
}