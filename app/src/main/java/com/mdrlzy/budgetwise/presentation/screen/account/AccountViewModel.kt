package com.mdrlzy.budgetwise.presentation.screen.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdrlzy.budgetwise.domain.model.Account
import com.mdrlzy.budgetwise.domain.model.AccountBrief
import com.mdrlzy.budgetwise.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.presentation.model.TransactionUiModel
import com.mdrlzy.budgetwise.presentation.model.toUiModel
import com.mdrlzy.budgetwise.presentation.screen.income.IncomeScreenState
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.math.BigDecimal
import java.time.OffsetDateTime
import javax.inject.Inject

sealed class AccountScreenState {
    data object Loading : AccountScreenState()

    data class Success(
        val account: Account
    ) : AccountScreenState()

    data class Error(val error: Throwable?) : AccountScreenState()
}

sealed class AccountScreenEffect

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

    private fun init() {
        initJob = intent {
            if (state is AccountScreenState.Success) return@intent

            accountRepo.getAccount().fold(
                ifLeft = {
                    reduce {
                        AccountScreenState.Error(it)
                    }
                },
                ifRight = {
                    reduce {
                        AccountScreenState.Success(it)
                    }
                },
            )
        }
    }
}

class AccountViewModelFactory @Inject constructor(
    private val accountRepo: AccountRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountViewModel(accountRepo) as T
    }
}