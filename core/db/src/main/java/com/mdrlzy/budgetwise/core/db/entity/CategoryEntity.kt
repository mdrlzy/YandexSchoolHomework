package com.mdrlzy.budgetwise.core.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CategoryEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val emoji: String,
    val isIncome: Boolean,
)