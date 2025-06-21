package com.mdrlzy.budgetwise.presentation.screen.expensestoday

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mdrlzy.budgetwise.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.domain.usecase.GetExpenseTransactionsUseCase
import com.mdrlzy.budgetwise.presentation.model.TransactionUiModel
import com.mdrlzy.budgetwise.presentation.model.toUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.math.BigDecimal
import java.time.OffsetDateTime
import javax.inject.Inject

sealed class ExpensesTodayState {
    data object Loading : ExpensesTodayState()

    data class Success(
        val sum: BigDecimal = BigDecimal.ZERO,
        val currency: String = "",
        val transactions: List<TransactionUiModel> = emptyList(),
    ) : ExpensesTodayState()

    data class Error(val error: Throwable?) : ExpensesTodayState()
}

sealed class ExpensesTodayEffect {

}

class ExpensesTodayViewModel(
    private val accountRepo: AccountRepo,
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
) : ViewModel(), ContainerHost<ExpensesTodayState, ExpensesTodayEffect> {
    override val container: Container<ExpensesTodayState, ExpensesTodayEffect> =
        container(ExpensesTodayState.Loading)

    private var initJob: Job? = null

    fun onActive() = init()

    fun onInactive() {
        initJob?.cancel()
    }

    fun onRetry() = init()

    private fun init() {
        initJob = intent {
            if (state is ExpensesTodayState.Success) return@intent

            reduce {
                ExpensesTodayState.Loading
            }

            val today = OffsetDateTime.now()
            val transactionsResult = getExpenseTransactionsUseCase.invoke(
                today.withHour(0).withMinute(0),
                today.withHour(23).withMinute(59)
            )
            val accountResult = accountRepo.getAccount()

            if (transactionsResult.isRight() && accountResult.isRight()) {
                val transactions = transactionsResult.getOrNull()!!.map { it.toUiModel() }
                val account = accountResult.getOrNull()!!
                val sum = transactions.sumOf { BigDecimal(it.amount) }

                reduce {
                    ExpensesTodayState.Success(
                        sum = sum,
                        currency = account.currency,
                        transactions = transactions,
                    )
                }

            } else {
                val left = transactionsResult.leftOrNull() ?: accountResult.leftOrNull()
                reduce {
                    ExpensesTodayState.Error(left)
                }
            }
        }
    }
}

class ExpensesTodayViewModelFactory @Inject constructor(
    private val accountRepo: AccountRepo,
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExpensesTodayViewModel(accountRepo, getExpenseTransactionsUseCase) as T
    }
}