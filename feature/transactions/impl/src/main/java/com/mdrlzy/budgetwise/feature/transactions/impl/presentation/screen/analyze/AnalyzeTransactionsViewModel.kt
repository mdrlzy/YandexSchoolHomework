package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.analyze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.core.ui.utils.DateTimeHelper
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.Transaction
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.repo.TransactionRepo
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.usecase.GetExpenseTransactionsUseCase
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.usecase.GetIncomeTransactionsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class AnalyzeTransactionsViewModel(
    private val isIncomeMode: Boolean,
    private val initialStartDate: Long,
    private val initialEndDate: Long,
    private val accountRepo: AccountRepo,
    private val transactionRepo: TransactionRepo,
    private val getIncomeTransactionsUseCase: GetIncomeTransactionsUseCase,
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
) : ViewModel(), ContainerHost<AnalyzeTransactionsScreenState, AnalyzeTransactionsScreenEffect> {

    override val container: Container<AnalyzeTransactionsScreenState, AnalyzeTransactionsScreenEffect> =
        container(AnalyzeTransactionsScreenState.Loading)

    init {
        init()
        accountRepo.accountFlow().onEach { account ->
            intent {
                val success = state
                if (success is AnalyzeTransactionsScreenState.Success) {
                    reduce {
                        success.copy(currency = account.currency)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onRetry() {
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
                    (state as AnalyzeTransactionsScreenState.Success).copy(start = date)
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
                    (state as AnalyzeTransactionsScreenState.Success).copy(end = date)
                }
                init()
            }
        }

    private fun init() = intent {
        val start =
            if (state is AnalyzeTransactionsScreenState.Success) {
                (state as AnalyzeTransactionsScreenState.Success).start
            } else {
                OffsetDateTime.ofInstant(Instant.ofEpochMilli(initialStartDate), ZoneOffset.UTC)
            }

        val end =
            if (state is AnalyzeTransactionsScreenState.Success) {
                (state as AnalyzeTransactionsScreenState.Success).end
            } else {
                OffsetDateTime.ofInstant(Instant.ofEpochMilli(initialEndDate), ZoneOffset.UTC)
            }

        val account = accountRepo.getAccount().getOrElse { err ->
            reduce {
                AnalyzeTransactionsScreenState.Error(err)
            }
            return@intent
        }

        val transactionsResult = if (isIncomeMode)
            getIncomeTransactionsUseCase.invoke(start, end)
        else
            getExpenseTransactionsUseCase.invoke(start, end)

        val transactions = transactionsResult.getOrElse { err ->
            reduce {
                AnalyzeTransactionsScreenState.Error(err)
            }
            return@intent
        }

        val total = transactions.sumOf { BigDecimal(it.amount) }

        val categories = analyze(transactions)

        reduce {
            AnalyzeTransactionsScreenState.Success(
                sum = total,
                currency = account.currency,
                start = start,
                end = end,
                categories = categories,
            )
        }
    }

    private fun analyze(
        transactions: List<Transaction>
    ): List<CategorySummary> {
        val parsedTransactions = transactions.map {
            it.category to BigDecimal(it.amount)
        }

        val total = parsedTransactions.sumOf { it.second }

        return parsedTransactions
            .groupBy { it.first }
            .map { (category, entries) ->
                val categorySum = entries.sumOf { it.second }
                val percentage = categorySum.divide(total, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal(100))
                    .toDouble()

                CategorySummary(
                    category = category,
                    categoryTotal = categorySum,
                    percentage = percentage
                )
            }
            .sortedByDescending { it.categoryTotal }
    }
}

class AnalyzeTransactionsViewModelFactory @AssistedInject constructor(
    @Assisted private val isIncomeMode: Boolean,
    @Assisted("start") private val startDate: Long,
    @Assisted("end") private val endDate: Long,
    private val accountRepo: AccountRepo,
    private val transactionRepo: TransactionRepo,
    private val getIncomeTransactionsUseCase: GetIncomeTransactionsUseCase,
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AnalyzeTransactionsViewModel(
            isIncomeMode,
            startDate,
            endDate,
            accountRepo,
            transactionRepo,
            getIncomeTransactionsUseCase,
            getExpenseTransactionsUseCase
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted isIncomeMode: Boolean,
            @Assisted("start") startDate: Long,
            @Assisted("end") endDate: Long,
        ): AnalyzeTransactionsViewModelFactory
    }
}