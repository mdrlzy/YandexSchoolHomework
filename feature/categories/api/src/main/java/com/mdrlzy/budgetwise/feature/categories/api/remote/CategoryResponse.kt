package com.mdrlzy.budgetwise.feature.categories.api.remote

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    val id: Long,
    val name: String,
    val emoji: String,
    val isIncome: Boolean,
)
