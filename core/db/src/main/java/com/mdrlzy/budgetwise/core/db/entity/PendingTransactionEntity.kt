package com.mdrlzy.budgetwise.core.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mdrlzy.budgetwise.core.domain.PendingType
import java.time.OffsetDateTime

@Entity
class PendingTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long,
    val remoteId: Long?,
    val accountId: Long,
    val categoryId: Long,
    val amount: String,
    val transactionDate: OffsetDateTime,
    val comment: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val pendingType: PendingType,
)