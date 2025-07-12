package com.mdrlzy.budgetwise.feature.account.api

/**
 * A lightweight representation of an account, typically used for lists or previews.
 *
 * Contains only essential information without timestamps or user reference.
 *
 * @property id Unique identifier of the account.
 * @property name Display name of the account.
 * @property balance Current balance, represented as a string to preserve precision.
 * @property currency ISO 4217 currency code (e.g., "USD", "EUR").
 */
data class AccountBrief(
    val id: Long,
    val name: String,
    val balance: String,
    val currency: String,
)
