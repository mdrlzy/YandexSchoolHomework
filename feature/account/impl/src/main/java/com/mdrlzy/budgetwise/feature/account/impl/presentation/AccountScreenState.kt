package com.mdrlzy.budgetwise.feature.account.impl.presentation

import com.mdrlzy.budgetwise.core.domain.model.Account
import java.math.BigDecimal
import java.time.OffsetDateTime

sealed class AccountScreenState {
    data object Loading : AccountScreenState()

    data class Success(
        val account: Account,
        val isEditMode: Boolean = false,
        val accountName: String = account.name,
        val chartData: Map<OffsetDateTime, BigDecimal>,
    ) : AccountScreenState()

    data class Error(val error: Throwable?) : AccountScreenState()
}

sealed class AccountScreenEffect
