package com.mdrlzy.budgetwise.feature.account.presentation

import com.mdrlzy.budgetwise.core.domain.model.Account

sealed class AccountScreenState {
    data object Loading : AccountScreenState()

    data class Success(
        val account: Account,
        val isEditMode: Boolean = false,
        val accountName: String = account.name,
    ) : AccountScreenState()

    data class Error(val error: Throwable?) : AccountScreenState()
}

sealed class AccountScreenEffect
