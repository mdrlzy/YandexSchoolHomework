package com.mdrlzy.budgetwise.presentation.screen.expensestoday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdrlzy.budgetwise.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.domain.usecase.GetExpenseTransactionsUseCase
import com.mdrlzy.budgetwise.presentation.model.TransactionUiModel
import com.mdrlzy.budgetwise.presentation.model.toUiModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.math.BigDecimal
import java.time.OffsetDateTime
import javax.inject.Inject

data class ExpensesTodayState(
    val sum: BigDecimal = BigDecimal.ZERO,
    val currency: String = "",
    val transactions: List<TransactionUiModel> = emptyList(),
    val isError: Boolean = false,
    val initialized: Boolean = false,
)

sealed class ExpensesTodayEffect {

}

class ExpensesTodayViewModel(
    private val accountRepo: AccountRepo,
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
) : ViewModel(), ContainerHost<ExpensesTodayState, ExpensesTodayEffect> {
    override val container: Container<ExpensesTodayState, ExpensesTodayEffect> =
        container(ExpensesTodayState())

    init {
        intent {
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
                    state.copy(
                        sum = sum,
                        currency = account.currency,
                        transactions = transactions,
                        initialized = true,
                    )
                }
            } else {
                reduce {
                    state.copy(isError = true, initialized = true)
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