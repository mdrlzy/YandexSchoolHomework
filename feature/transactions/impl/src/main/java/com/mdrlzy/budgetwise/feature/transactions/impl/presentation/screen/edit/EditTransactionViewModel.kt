package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import arrow.core.getOrElse
import com.mdrlzy.budgetwise.core.domain.CurrUtils
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.feature.categories.api.CategoryRepo
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.repo.TransactionRepo
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

class EditTransactionViewModel(
    private val isIncomeMode: Boolean,
    private val transactionId: Long?,
    private val transactionRepo: TransactionRepo,
    private val accountRepo: AccountRepo,
    private val categoryRepo: CategoryRepo,
) : ViewModel(), ContainerHost<EditTransactionScreenState, EditTransactionScreenEffect> {
    private val isCreateNotEditMode = transactionId == null

    override val container: Container<EditTransactionScreenState, EditTransactionScreenEffect> =
        container(EditTransactionScreenState.Loading)

    init {
        init()
    }

    private fun init() = intent {
        val account = accountRepo.getAccount().getOrElse { err ->
            reduce {
                EditTransactionScreenState.Error(err, null)
            }
            return@intent
        }
        val categories = categoryRepo.getAll().getOrElse { err ->
            reduce {
                EditTransactionScreenState.Error(err, null)
            }
            return@intent
        }
        if (isCreateNotEditMode) {
            reduce {
                EditTransactionScreenState.Success(
                    account = account,
                    date = OffsetDateTime.now(),
                    category = categories.first(),
                    amount = "0",
                    comment = "",
                )
            }
        } else {
            val transaction = transactionRepo.getById(transactionId!!).getOrElse { err ->
                reduce {
                    EditTransactionScreenState.Error(err, null)
                }
                return@intent
            }

            reduce {
                EditTransactionScreenState.Success(
                    account = account,
                    date = transaction.transactionDate,
                    category = transaction.category,
                    amount = transaction.amount,
                    comment = transaction.comment.orEmpty(),
                )
            }
        }
    }

    fun onRetry(prevSuccess: EditTransactionScreenState.Success?) = intent {
        prevSuccess?.let {
            reduce {
                it
            }
        } ?: init()
    }

    fun onAmountChanged(new: String) = blockingIntent {
        val success = state as? EditTransactionScreenState.Success ?: return@blockingIntent
        reduce {
            success.copy(amount = CurrUtils.validateInput(success.amount, new))
        }
    }

    fun onCommentChanged(input: String) = blockingIntent {
        val success = state as? EditTransactionScreenState.Success ?: return@blockingIntent
        reduce {
            success.copy(comment = input)
        }
    }

    fun onCategorySelected(id: Long) = intent {
        val success = state as? EditTransactionScreenState.Success ?: return@intent
        val category = categoryRepo.getAll().getOrNull()!!.find { it.id == id }!!
        reduce {
            success.copy(category = category)
        }
    }

    fun onDateChange(millis: Long?) = intent {
        val success = state as? EditTransactionScreenState.Success ?: return@intent
        millis?.let {
            val oldDate = success.date
            val date = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toOffsetDateTime()
                .withHour(oldDate.hour)
                .withMinute(oldDate.minute)
                .withSecond(oldDate.second)
                .withNano(oldDate.nano)

            reduce {
                success.copy(date = date)
            }
        }
    }

    fun onDelete() = intent {
        transactionRepo.delete(transactionId!!).getOrElse { err ->
            val success = state as? EditTransactionScreenState.Success
            reduce {
                EditTransactionScreenState.Error(err, success)
            }
            return@intent
        }
        postSideEffect(EditTransactionScreenEffect.NavigateBack)
    }

    fun onCancel() = intent {
        postSideEffect(EditTransactionScreenEffect.NavigateBack)
    }


    fun onDone() = intent {
        val success = state as? EditTransactionScreenState.Success ?: return@intent
        if (isCreateNotEditMode) {
            transactionRepo
                .create(success.account, success.category, success.toRequest())
                .getOrElse { err ->
                    reduce {
                        EditTransactionScreenState.Error(err, success)
                    }
                    return@intent
                }
        } else {
            transactionRepo.update(transactionId!!, success.toRequest()).getOrElse { err ->
                reduce {
                    EditTransactionScreenState.Error(err, success)
                }
                return@intent
            }
        }
        postSideEffect(EditTransactionScreenEffect.NavigateBack)
    }
}

class EditTransactionViewModelFactory @AssistedInject constructor(
    @Assisted private val isIncomeMode: Boolean,
    @Assisted private val transactionId: Long?,
    private val transactionRepo: TransactionRepo,
    private val accountRepo: AccountRepo,
    private val categoryRepo: CategoryRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditTransactionViewModel(
            isIncomeMode,
            transactionId,
            transactionRepo,
            accountRepo,
            categoryRepo,
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted isIncomeMode: Boolean,
            @Assisted transactionId: Long?,
        ): EditTransactionViewModelFactory
    }
}