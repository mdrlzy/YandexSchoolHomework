package com.mdrlzy.budgetwise.core.network.response

data class StatItem(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: String
)