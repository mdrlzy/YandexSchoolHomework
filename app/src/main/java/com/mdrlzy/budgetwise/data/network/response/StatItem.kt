package com.mdrlzy.budgetwise.data.network.response

data class StatItem(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: String
)