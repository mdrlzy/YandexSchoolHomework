package com.mdrlzy.budgetwise.domain.model

import java.time.OffsetDateTime

data class Account(
    val id: Long,
    val userId: Long,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)