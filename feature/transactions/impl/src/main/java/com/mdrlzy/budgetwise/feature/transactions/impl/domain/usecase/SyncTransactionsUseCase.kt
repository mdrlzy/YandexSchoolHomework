package com.mdrlzy.budgetwise.feature.transactions.impl.domain.usecase

import android.util.Log
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.PendingType
import com.mdrlzy.budgetwise.core.domain.Prefs
import com.mdrlzy.budgetwise.core.domain.expection.NoInternetException
import com.mdrlzy.budgetwise.core.domain.model.Account
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.core.domain.repo.NetworkStatus
import com.mdrlzy.budgetwise.feature.account.api.AccountBrief
import com.mdrlzy.budgetwise.feature.categories.api.CategoryRepo
import com.mdrlzy.budgetwise.feature.transactions.impl.data.local.PendingTransactionLocalDataSource
import com.mdrlzy.budgetwise.feature.transactions.impl.data.local.TransactionLocalDataSource
import com.mdrlzy.budgetwise.feature.transactions.impl.data.remote.TransactionRemoteDataSource
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.PendingTransaction
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.TransactionRequest
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.repo.TransactionRepo
import java.time.OffsetDateTime
import javax.inject.Inject

class SyncTransactionsUseCase @Inject constructor(
    private val accountRepo: AccountRepo,
    private val categoriesRepo: CategoryRepo,
    private val remoteDataSource: TransactionRemoteDataSource,
    private val pendingDataSource: PendingTransactionLocalDataSource,
    private val localDataSource: TransactionLocalDataSource,
    private val networkStatus: NetworkStatus,
    private val prefs: Prefs,
) {
    suspend operator fun invoke(): EitherT<Unit> {
        val account = accountRepo.getAccount().getOrElse { return it.left() }
        val categories = categoriesRepo.getAll().getOrElse { return it.left() }

        if (networkStatus.isOnline().not())
            return NoInternetException().left()

        val pending = pendingDataSource.getAll(account, categories)
        Log.i(LOG_TAG, "Starting sync, pending count: ${pending.size}")
        pending.forEach { pendingTransaction ->
            when (pendingTransaction.pendingType) {
                PendingType.CREATE -> create(account, pendingTransaction)
                PendingType.EDIT -> edit(account, pendingTransaction)
                PendingType.DELETE -> delete(pendingTransaction)
            }
        }

        prefs.saveLastSync(OffsetDateTime.now())
        Log.i(LOG_TAG, "Sync finished")
        return Unit.right()
    }

    private suspend fun create(account: Account, pendingTransaction: PendingTransaction) {
        Log.i(LOG_TAG, "[Sync] Creating transaction: $pendingTransaction")
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
            Log.i(LOG_TAG, "[Sync] Created $pendingTransaction")
        }.onLeft {
            Log.i(LOG_TAG, "[Sync] Failed to create: err[${it.message}] $pendingTransaction")
        }
    }

    private suspend fun edit(
        account: Account,
        pendingTransaction: PendingTransaction
    ) {
        Log.i(LOG_TAG, "[Sync] Editing transaction: $pendingTransaction")
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
                Log.i(LOG_TAG, "[Sync] Edited $pendingTransaction")
            }.onLeft {
                Log.i(LOG_TAG, "[Sync] Failed to edit: err[${it.message}] $pendingTransaction")
            }
        } ?: create(account, pendingTransaction)
    }

    private suspend fun delete(pendingTransaction: PendingTransaction) {
        Log.i(LOG_TAG, "[Sync] Deleting transaction: $pendingTransaction")
        if (pendingTransaction.remoteId == null) {
            pendingDataSource.delete(pendingTransaction.localId)
            return
        }

        pendingTransaction.remoteId.let {
            remoteDataSource.delete(it)
        }.onRight {
            pendingDataSource.delete(pendingTransaction.localId)
            Log.i(LOG_TAG, "[Sync] Deleted $pendingTransaction")
        }.onLeft {
            Log.i(LOG_TAG, "[Sync] Failed to delete: err[${it.message}] $pendingTransaction")
        }
    }
    companion object {
        private val LOG_TAG = "SyncTransactionsUseCase"
    }
}