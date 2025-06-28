package com.mdrlzy.budgetwise.feature.account.presentation

import com.mdrlzy.budgetwise.core.domain.model.Account

sealed class AccountScreenState {
    data object Loading : AccountScreenState()

    data class Success(
        val account: Account,
    ) : AccountScreenState()

    data class Error(val error: Throwable?) : AccountScreenState()
}

sealed class AccountScreenEffect
