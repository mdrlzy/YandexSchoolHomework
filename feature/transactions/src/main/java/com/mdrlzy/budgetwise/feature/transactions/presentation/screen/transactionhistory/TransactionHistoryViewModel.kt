package com.mdrlzy.budgetwise.feature.transactions.presentation.screen.transactionhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.core.ui.utils.DateTimeHelper
import com.mdrlzy.budgetwise.feature.transactions.domain.usecase.GetExpenseTransactionsUseCase
import com.mdrlzy.budgetwise.feature.transactions.domain.usecase.GetIncomeTransactionsUseCase
import com.mdrlzy.budgetwise.feature.transactions.presentation.model.toUiModel
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

class TransactionHistoryViewModel(
    private val isIncomeMode: Boolean,
    private val accountRepo: AccountRepo,
    private val getIncomeTransactionsUseCase: GetIncomeTransactionsUseCase,
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
) : ViewModel(), ContainerHost<TransactionHistoryScreenState, TransactionHistoryScreenEffect> {
    private val initialStartDate = OffsetDateTime.now().withDayOfMonth(1)
    private val initialEndDate = OffsetDateTime.now()

    override val container: Container<TransactionHistoryScreenState, TransactionHistoryScreenEffect> =
        container(TransactionHistoryScreenState.Loading)

    init {
        init()
    }

    fun onRetry() =
        intent {
            init()
        }

    fun onStartDateSelected(millis: Long?) =
        intent {
            millis?.let {
                val date =
                    DateTimeHelper.startOfDay(
                        Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toOffsetDateTime(),
                    )

                reduce {
                    (state as TransactionHistoryScreenState.Success).copy(startDate = date)
                }
                init()
            }
        }

    fun onEndDateSelected(millis: Long?) =
        intent {
            millis?.let {
                val date =
                    DateTimeHelper.endOfDay(
                        Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toOffsetDateTime(),
                    )

                reduce {
                    (state as TransactionHistoryScreenState.Success).copy(endDate = date)
                }
                init()
            }
        }

    private fun init() =
        intent {
            val startDate =
                if (state is TransactionHistoryScreenState.Success) {
                    (state as TransactionHistoryScreenState.Success).startDate
                } else {
                    initialStartDate
                }

            val endDate =
                if (state is TransactionHistoryScreenState.Success) {
                    (state as TransactionHistoryScreenState.Success).endDate
                } else {
                    initialEndDate
                }

            val transactionsResult =
                if (isIncomeMode) {
                    getIncomeTransactionsUseCase(startDate, endDate)
                } else {
                    getExpenseTransactionsUseCase(startDate, endDate)
                }

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

class TransactionHistoryViewModelFactory
    @AssistedInject
    constructor(
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
                getExpenseTransactionsUseCase,
            ) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted isIncomeMode: Boolean,
            ): TransactionHistoryViewModelFactory
        }
    }
