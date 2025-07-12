package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.expenses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.repo.TransactionRepo
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.usecase.GetExpenseTransactionsUseCase
import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.model.toUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.math.BigDecimal
import java.time.OffsetDateTime
import javax.inject.Inject

class ExpensesViewModel(
    private val accountRepo: AccountRepo,
    private val transactionRepo: TransactionRepo,
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
) : ViewModel(), ContainerHost<ExpensesScreenState, ExpensesScreenEffect> {
    override val container: Container<ExpensesScreenState, ExpensesScreenEffect> =
        container(ExpensesScreenState.Loading)

    private var initJob: Job? = null

    init {
        accountRepo.accountFlow().onEach { account ->
            intent {
                val success = state
                if (success is ExpensesScreenState.Success) {
                    reduce {
                        success.copy(currency = account.currency)
                    }
                }
            }
        }.launchIn(viewModelScope)

        transactionRepo.changesFlow.onEach {
            load()
        }.launchIn(viewModelScope)
    }

    fun onActive() = init()

    fun onInactive() {
        initJob?.cancel()
    }

    fun onRetry() = init()

    private fun init() {
        initJob =
            intent {
                if (state is ExpensesScreenState.Success) return@intent

                load()
            }
    }

    private fun load() = intent {
        reduce {
            ExpensesScreenState.Loading
        }

        val today = OffsetDateTime.now()
        val transactionsResult =
            getExpenseTransactionsUseCase.invoke(
                today.withHour(0).withMinute(0),
                today.withHour(23).withMinute(59),
            )
        val accountResult = accountRepo.getAccount()

        if (transactionsResult.isRight() && accountResult.isRight()) {
            val transactions = transactionsResult.getOrNull()!!.map { it.toUiModel() }
            val account = accountResult.getOrNull()!!
            val sum = transactions.sumOf { BigDecimal(it.amount) }

            reduce {
                ExpensesScreenState.Success(
                    sum = sum,
                    currency = account.currency,
                    transactions = transactions,
                )
            }
        } else {
            val left = transactionsResult.leftOrNull() ?: accountResult.leftOrNull()
            reduce {
                ExpensesScreenState.Error(left)
            }
        }
    }
}

class ExpensesViewModelFactory @Inject constructor(
    private val accountRepo: AccountRepo,
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
    private val transactionRepo: TransactionRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExpensesViewModel(accountRepo, transactionRepo, getExpenseTransactionsUseCase) as T
    }
}
