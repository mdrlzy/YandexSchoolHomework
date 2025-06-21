package com.mdrlzy.budgetwise.domain.model

data class AccountBrief(
    val id: Long,
    val name: String,
    val balance: String,
    val currency: String
)