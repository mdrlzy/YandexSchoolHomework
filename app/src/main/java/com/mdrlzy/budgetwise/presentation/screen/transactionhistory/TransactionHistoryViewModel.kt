package com.mdrlzy.budgetwise.presentation.screen.transactionhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdrlzy.budgetwise.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.domain.usecase.GetExpenseTransactionsUseCase
import com.mdrlzy.budgetwise.domain.usecase.GetIncomeTransactionsUseCase
import com.mdrlzy.budgetwise.presentation.model.TransactionUiModel
import com.mdrlzy.budgetwise.presentation.model.toUiModel
import com.mdrlzy.budgetwise.presentation.ui.utils.DateTimeHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.math.BigDecimal
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

sealed class TransactionHistoryScreenState {

    data object Loading : TransactionHistoryScreenState()

    data class Success(
        val sum: BigDecimal = BigDecimal.ZERO,
        val currency: String = "",
        val transactions: List<TransactionUiModel> = emptyList(),
        val startDate: OffsetDateTime,
        val endDate: OffsetDateTime,
    ) : TransactionHistoryScreenState()

    data class Error(val error: Throwable?) : TransactionHistoryScreenState()
}

sealed class TransactionHistoryScreenEffect

class TransactionHistoryViewModel(
    private val isIncomeMode: Boolean,
    private val accountRepo: AccountRepo,
    private val getIncomeTransactionsUseCase: GetIncomeTransactionsUseCase,
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
) : ViewModel(), ContainerHost<TransactionHistoryScreenState, TransactionHistoryScreenEffect> {

    private val initialStartDate = DateTimeHelper.startOfDay(
        OffsetDateTime.now().minusDays(30)
    )
    private val initialEndDate = DateTimeHelper.endOfDay(OffsetDateTime.now())

    override val container: Container<TransactionHistoryScreenState, TransactionHistoryScreenEffect> =
        container(TransactionHistoryScreenState.Loading)


    init {
        init()
    }

    fun onRetry() = intent {
        init()
    }

    fun onStartDateSelected(millis: Long?) = intent {
        millis?.let {
            val date = DateTimeHelper.startOfDay(
                Instant.ofEpochMilli(millis)
                    .atZone(ZoneId.systemDefault())
                    .toOffsetDateTime()
            )

            reduce {
                (state as TransactionHistoryScreenState.Success).copy(startDate = date)
            }
            init()
        }
    }

    fun onEndDateSelected(millis: Long?) = intent {
        millis?.let {
            val date = DateTimeHelper.endOfDay(
                Instant.ofEpochMilli(millis)
                    .atZone(ZoneId.systemDefault())
                    .toOffsetDateTime()
            )

            reduce {
                (state as TransactionHistoryScreenState.Success).copy(endDate = date)
            }
            init()
        }
    }

    private fun init() = intent {
        val startDate = if (state is TransactionHistoryScreenState.Success)
            (state as TransactionHistoryScreenState.Success).startDate
        else
            initialStartDate

        val endDate = if (state is TransactionHistoryScreenState.Success)
            (state as TransactionHistoryScreenState.Success).endDate
        else
            initialEndDate

        val transactionsResult =
            if (isIncomeMode) getIncomeTransactionsUseCase(startDate, endDate)
            else getExpenseTransactionsUseCase(startDate, endDate)

        val accountResult = accountRepo.getAccount()

        if (transactionsResult.isRight() && accountResult.isRight()) {
            val account = accountResult.getOrNull()!!
            val transactions = transactionsResult.getOrNull()!!.map { it.toUiModel() }
            val sum = transactions.sumOf { BigDecimal(it.amount) }

            reduce {
                TransactionHistoryScreenState.Success(
                    sum = sum,
                    currency = account.currency,
                    transactions = transactions,
                    startDate = startDate,
                    endDate = endDate,
                )
            }
        } else {
            val left = transactionsResult.leftOrNull() ?: accountResult.leftOrNull()
            reduce {
                TransactionHistoryScreenState.Error(left)
            }
        }
    }
}

class TransactionHistoryViewModelFactory @AssistedInject constructor(
    @Assisted private val isIncomeMode: Boolean,
    private val accountRepo: AccountRepo,
    private val getIncomeTransactionsUseCase: GetIncomeTransactionsUseCase,
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransactionHistoryViewModel(
            isIncomeMode,
            accountRepo,
            getIncomeTransactionsUseCase,
            getExpenseTransactionsUseCase
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted isIncomeMode: Boolean,
        ): TransactionHistoryViewModelFactory
    }
}