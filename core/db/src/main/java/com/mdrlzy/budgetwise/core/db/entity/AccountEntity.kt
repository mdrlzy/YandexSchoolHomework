package com.mdrlzy.budgetwise.core.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity
class AccountEntity(
    @PrimaryKey
    val id: Long,
    val userId: Long,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)