package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.analyze

import com.mdrlzy.budgetwise.feature.categories.api.Category
import java.math.BigDecimal

data class CategorySummary(
    val category: Category,
    val categoryTotal: BigDecimal,
    val percentage: Double,
)