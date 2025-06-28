package com.mdrlzy.budgetwise.core.domain.model

/**
 * Represents a transaction category used for organizing expenses or income.
 *
 * @property id Unique identifier of the category.
 * @property name Name of the category (e.g., "Groceries", "Salary").
 * @property emoji Emoji icon representing the category (e.g., "🍕", "💼").
 * @property isIncome Indicates whether this category is for income (`true`) or expense (`false`).
 */
data class Category(
    val id: Long,
    val name: String,
    val emoji: String,
    val isIncome: Boolean,
)
