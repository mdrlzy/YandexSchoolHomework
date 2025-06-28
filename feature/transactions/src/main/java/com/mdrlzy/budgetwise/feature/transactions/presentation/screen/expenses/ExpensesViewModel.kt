package com.mdrlzy.budgetwise.feature.transactions.presentation.screen.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.feature.transactions.domain.usecase.GetExpenseTransactionsUseCase
import com.mdrlzy.budgetwise.feature.transactions.presentation.model.TransactionUiModel
import com.mdrlzy.budgetwise.feature.transactions.presentation.model.toUiModel
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.math.BigDecimal
import java.time.OffsetDateTime
import javax.inject.Inject

sealed class ExpensesScreenState {
    data object Loading : ExpensesScreenState()

    data class Success(
        val sum: BigDecimal = BigDecimal.ZERO,
        val currency: String = "",
        val transactions: List<TransactionUiModel> = emptyList(),
    ) : ExpensesScreenState()

    data class Error(val error: Throwable?) : ExpensesScreenState()
}

sealed class ExpensesScreenEffect

class ExpensesViewModel(
    private val accountRepo: AccountRepo,
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
) : ViewModel(), ContainerHost<ExpensesScreenState, ExpensesScreenEffect> {
    override val container: Container<ExpensesScreenState, ExpensesScreenEffect> =
        container(ExpensesScreenState.Loading)

    private var initJob: Job? = null

    fun onActive() = init()

    fun onInactive() {
        initJob?.cancel()
    }

    fun onRetry() = init()

    private fun init() {
        initJob =
            intent {
                if (state is ExpensesScreenState.Success) return@intent

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
}

class ExpensesViewModelFactory
    @Inject
    constructor(
        private val accountRepo: AccountRepo,
        private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ExpensesViewModel(accountRepo, getExpenseTransactionsUseCase) as T
        }
    }
