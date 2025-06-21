package com.mdrlzy.budgetwise.presentation.screen.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdrlzy.budgetwise.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.domain.usecase.GetIncomeTransactionsUseCase
import com.mdrlzy.budgetwise.presentation.model.TransactionUiModel
import com.mdrlzy.budgetwise.presentation.model.toUiModel
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.math.BigDecimal
import java.time.OffsetDateTime
import javax.inject.Inject

sealed class IncomeScreenState {
    data object Loading : IncomeScreenState()

    data class Success(
        val sum: BigDecimal = BigDecimal.ZERO,
        val currency: String = "",
        val transactions: List<TransactionUiModel> = emptyList(),
    ) : IncomeScreenState()

    data class Error(val error: Throwable?) : IncomeScreenState()
}

sealed class IncomeScreenEffect

class IncomeViewModel(
    private val accountRepo: AccountRepo,
    private val getIncomeTransactionsUseCase: GetIncomeTransactionsUseCase,
) : ViewModel(), ContainerHost<IncomeScreenState, IncomeScreenEffect> {
    override val container: Container<IncomeScreenState, IncomeScreenEffect> =
        container(IncomeScreenState.Loading)

    private var initJob: Job? = null

    fun onActive() = init()

    fun onInactive() {
        initJob?.cancel()
    }

    fun onRetry() = init()

    private fun init() {
        initJob = intent {
            if (state is IncomeScreenState.Success) return@intent

            reduce {
                IncomeScreenState.Loading
            }

            val today = OffsetDateTime.now()
            val transactionsResult = getIncomeTransactionsUseCase.invoke(
                today.withHour(0).withMinute(0),
                today.withHour(23).withMinute(59)
            )
            val accountResult = accountRepo.getAccount()

            if (transactionsResult.isRight() && accountResult.isRight()) {
                val transactions = transactionsResult.getOrNull()!!.map { it.toUiModel() }
                val account = accountResult.getOrNull()!!
                val sum = transactions.sumOf { BigDecimal(it.amount) }

                reduce {
                    IncomeScreenState.Success(
                        sum = sum,
                        currency = account.currency,
                        transactions = transactions,
                    )
                }

            } else {
                val left = transactionsResult.leftOrNull() ?: accountResult.leftOrNull()
                reduce {
                    IncomeScreenState.Error(left)
                }
            }
        }
    }
}

class IncomeViewModelFactory @Inject constructor(
    private val accountRepo: AccountRepo,
    private val getIncomeTransactionsUseCase: GetIncomeTransactionsUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IncomeViewModel(accountRepo, getIncomeTransactionsUseCase) as T
    }
}