package com.mdrlzy.budgetwise.core.domain.model

import java.time.OffsetDateTime

/**
 * Represents a user's financial account.
 *
 * @property id Unique identifier of the account.
 * @property userId Identifier of the user who owns the account.
 * @property name Name of the account (e.g., "Savings", "Cash", etc.).
 * @property balance Current balance of the account, represented as a string to preserve precision.
 * @property currency ISO 4217 currency code (e.g., "USD", "EUR").
 * @property createdAt Timestamp when the account was created.
 * @property updatedAt Timestamp of the last update to the account.
 */
data class Account(
    val id: Long,
    val userId: Long,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
