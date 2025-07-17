package com.mdrlzy.budgetwise.core.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity
class TransactionEntity(
    @PrimaryKey
    val id: Long,
    val accountId: Long,
    val categoryId: Long,
    val amount: String,
    val transactionDate: OffsetDateTime,
    val comment: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)