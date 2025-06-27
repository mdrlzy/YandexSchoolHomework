package com.mdrlzy.budgetwise.core.domain.model

data class AccountBrief(
    val id: Long,
    val name: String,
    val balance: String,
    val currency: String
)