package com.mdrlzy.budgetwise.presentation.screen.account

import androidx.lifecycle.ViewModel
import com.mdrlzy.budgetwise.domain.model.AccountState
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

data class AccountScreenState(
    val accountState: AccountState,
)

sealed class AccountScreenEffect

class AccountViewModel : ViewModel(), ContainerHost<AccountScreenState, AccountScreenEffect> {
    override val container: Container<AccountScreenState, AccountScreenEffect> =
        container(
            AccountScreenState(
                AccountState(
                    id = 0L,
                    name = "Мой счет",
                    balance = "-670 000 ₽",
                    currency = "₽",
                )
            )
        )
}