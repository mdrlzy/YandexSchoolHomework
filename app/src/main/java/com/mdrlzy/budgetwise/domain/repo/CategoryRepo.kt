package com.mdrlzy.budgetwise.domain.repo

import com.mdrlzy.budgetwise.domain.model.Category

interface CategoryRepo {
    fun getAll(): List<Category>
}