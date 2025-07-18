package com.mdrlzy.budgetwise.feature.transactions.impl.domain.usecase

import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.PendingType
import com.mdrlzy.budgetwise.core.domain.model.Account
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.feature.account.api.AccountBrief
import com.mdrlzy.budgetwise.feature.categories.api.CategoryRepo
import com.mdrlzy.budgetwise.feature.transactions.impl.data.local.PendingTransactionLocalDataSource
import com.mdrlzy.budgetwise.feature.transactions.impl.data.local.TransactionLocalDataSource
import com.mdrlzy.budgetwise.feature.transactions.impl.data.remote.TransactionRemoteDataSource
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.PendingTransaction
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.TransactionRequest
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.repo.TransactionRepo
import javax.inject.Inject

class SyncTransactionsUseCase @Inject constructor(
    private val accountRepo: AccountRepo,
    private val categoriesRepo: CategoryRepo,
    private val remoteDataSource: TransactionRemoteDataSource,
    private val pendingDataSource: PendingTransactionLocalDataSource,
    private val localDataSource: TransactionLocalDataSource,
) {
    suspend operator fun invoke(): EitherT<Unit> {
        val account = accountRepo.getAccount().getOrElse { return it.left() }
        val categories = categoriesRepo.getAll().getOrElse { return it.left() }

        val pending = pendingDataSource.getAll(account, categories)
        pending.forEach { pendingTransaction ->
            when (pendingTransaction.pendingType) {
                PendingType.CREATE -> create(account, pendingTransaction)
                PendingType.EDIT -> edit(account, pendingTransaction)
                PendingType.DELETE -> delete(pendingTransaction)
            }
        }

        return Unit.right()
    }

    private suspend fun create(account: Account, pendingTransaction: PendingTransaction) {
        remoteDataSource.create(
            account,
            pendingTransaction.category,
            TransactionRequest(
                pendingTransaction.account.id,
                pendingTransaction.category.id,
                pendingTransaction.amount,
                pendingTransaction.transactionDate,
                pendingTransaction.comment
            )
        ).onRight {
            pendingDataSource.delete(pendingTransaction.localId)
        }
    }

    private suspend fun edit(
        account: Account,
        pendingTransaction: PendingTransaction
    ) {
        pendingTransaction.remoteId?.let {
            remoteDataSource.update(
                pendingTransaction.remoteId,
                TransactionRequest(
                    pendingTransaction.account.id,
                    pendingTransaction.category.id,
                    pendingTransaction.amount,
                    pendingTransaction.transactionDate,
                    pendingTransaction.comment
                )
            ).onRight {
                pendingDataSource.delete(pendingTransaction.localId)
            }
        } ?: create(account, pendingTransaction)
    }

    private suspend fun delete(pendingTransaction: PendingTransaction) {
        if (pendingTransaction.remoteId == null) {
            pendingDataSource.delete(pendingTransaction.localId)
            return
        }

        pendingTransaction.remoteId.let {
            remoteDataSource.delete(it)
        }.onRight {
            pendingDataSource.delete(pendingTransaction.localId)
        }
    }
}