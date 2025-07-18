package com.mdrlzy.budgetwise.feature.account.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdrlzy.budgetwise.core.domain.model.Currency
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

class AccountViewModel(
    private val accountRepo: AccountRepo,
) : ViewModel(), ContainerHost<AccountScreenState, AccountScreenEffect> {
    override val container: Container<AccountScreenState, AccountScreenEffect> =
        container(AccountScreenState.Loading)

    private var initJob: Job? = null

    fun onActive() = init()

    fun onInactive() {
        initJob?.cancel()
    }

    fun onRetry() = init()

    fun onEdit() = intent {
        val success = state
        if (success is AccountScreenState.Success) {
            reduce {
                success.copy(isEditMode = true)
            }
        }
    }

    fun onCancelEdit() = intent {
        val success = state
        if (success is AccountScreenState.Success) {
            reduce {
                success.copy(isEditMode = false)
            }
        }
    }

    fun onDoneEdit() = intent {
        val success = state as AccountScreenState.Success
        accountRepo.updateName(success.accountName).fold(
            ifLeft = {
                reduce {
                    AccountScreenState.Error(it)
                }
            },
            ifRight = {
                reduce {
                    AccountScreenState.Success(account = it)
                }
            },
        )
    }

    fun onNameChanged(name: String) = blockingIntent {
        val success = state
        if (success is AccountScreenState.Success) {
            reduce {
                success.copy(accountName = name)
            }
        }
    }

    private fun init() {
        initJob =
            intent {
                if (state is AccountScreenState.Success) return@intent

                accountRepo.getAccount().fold(
                    ifLeft = {
                        reduce {
                            AccountScreenState.Error(it)
                        }
                    },
                    ifRight = {
                        reduce {
                            AccountScreenState.Success(account = it)
                        }
                    },
                )
            }
    }

    fun onChangeCurrency(currency: Currency) = intent {
        accountRepo.updateCurrency(currency).fold(
            ifLeft = {
                reduce {
                    AccountScreenState.Error(it)
                }
            },
            ifRight = {
                reduce {
                    AccountScreenState.Success(account = it)
                }
            },
        )
    }
}

class AccountViewModelFactory @Inject constructor(
    private val accountRepo: AccountRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountViewModel(accountRepo) as T
    }
}
