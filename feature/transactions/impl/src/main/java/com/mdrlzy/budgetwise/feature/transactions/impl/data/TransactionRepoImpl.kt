package com.mdrlzy.budgetwise.feature.transactions.impl.data

import arrow.core.left
import arrow.core.right
import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.PendingType
import com.mdrlzy.budgetwise.core.domain.model.Account
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.feature.account.api.AccountBrief
import com.mdrlzy.budgetwise.feature.categories.api.Category
import com.mdrlzy.budgetwise.feature.categories.api.CategoryRepo
import com.mdrlzy.budgetwise.feature.transactions.impl.data.local.PendingTransactionLocalDataSource
import com.mdrlzy.budgetwise.feature.transactions.impl.data.local.TransactionLocalDataSource
import com.mdrlzy.budgetwise.feature.transactions.impl.data.remote.TransactionRemoteDataSource
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.PendingTransaction
import com.mdrlzy.budgetwise.feature.transactions.api.TransactionRequest
import com.mdrlzy.budgetwise.feature.transactions.api.Transaction
import com.mdrlzy.budgetwise.feature.transactions.api.TransactionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.OffsetDateTime
import javax.inject.Inject

class TransactionRepoImpl @Inject constructor(
    private val accountRepo: AccountRepo,
    private val categoriesRepo: CategoryRepo,
    private val remote: TransactionRemoteDataSource,
    private val local: TransactionLocalDataSource,
    private val pending: PendingTransactionLocalDataSource,
) : TransactionRepo {
    private val _changesFlow = MutableSharedFlow<Unit>()
    override val changesFlow: Flow<Unit> get() = _changesFlow

    override suspend fun getByPeriod(
        accountId: Long,
        start: OffsetDateTime,
        end: OffsetDateTime,
    ): EitherT<List<Transaction>> {
        val account = accountRepo.getAccount().getOrNull()
            ?: return IllegalStateException("No account").left()
        val categories = categoriesRepo.getAll().getOrNull()
            ?: return IllegalStateException("No categories").left()

        return remote.getByPeriod(accountId, start, end).fold(
            ifLeft = {
                val localTransactions = local.getByPeriod(account, categories, start, end)
                val pendingTransactions = pending.getByPeriod(account, categories, start, end)
                mergeWithPending(localTransactions, pendingTransactions).right()
            },
            ifRight = { remoteTransactions ->
                local.save(remoteTransactions)
                val pendingTransactions = pending.getByPeriod(account, categories, start, end)
                val merged = mergeWithPending(remoteTransactions, pendingTransactions)
                merged.right()
            }
        )
    }

    override suspend fun getById(
        id: Long,
        account: Account,
    ): EitherT<Transaction> {
        return remote.getById(id).fold(
            ifLeft = {
                val categories = categoriesRepo.getAll().getOrNull()
                    ?: return IllegalStateException("No categories").left()

                val all = pending.getAll(account, categories)

                val pending = all.firstOrNull { it.remoteId == id || -it.localId == id }

                if (pending?.pendingType == PendingType.DELETE) {
                    return@fold IllegalStateException("Transaction has been deleted locally").left()
                }

                if (pending != null) {
                    return@fold pending.toTransaction().right()
                }

                val local = local.getById(id, account, categories)
                    ?: return@fold IllegalStateException("Transaction not found locally").left()

                local.right()
            },
            ifRight = {
                local.save(it)
                it.right()
            }
        )
    }

    override suspend fun create(
        account: Account,
        category: Category,
        transactionRequest: TransactionRequest
    ): EitherT<Transaction> {
        return remote.create(account, category, transactionRequest).fold(
            ifLeft = {
                val now = OffsetDateTime.now()
                val pendingTransaction = PendingTransaction(
                    localId = 0L,
                    remoteId = null,
                    account = AccountBrief(
                        account.id,
                        account.name,
                        account.balance,
                        account.currency
                    ),
                    category = category,
                    amount = transactionRequest.amount,
                    transactionDate = transactionRequest.transactionDate,
                    comment = transactionRequest.comment,
                    createdAt = now,
                    updatedAt = now,
                    pendingType = PendingType.CREATE
                )
                pending.save(pendingTransaction)
                _changesFlow.emit(Unit)

                val fakeTransaction = pendingTransaction.toTransaction()
                fakeTransaction.right()
            },
            ifRight = {
                local.save(it)
                _changesFlow.emit(Unit)
                it.right()
            }
        )
    }

    override suspend fun update(
        id: Long,
        account: Account,
        category: Category,
        transactionRequest: TransactionRequest
    ): EitherT<Transaction> {
        return remote.update(id, transactionRequest).fold(
            ifLeft = {
                val categories = categoriesRepo.getAll().getOrNull()
                    ?: return IllegalStateException("No categories").left()

                val pendingList = pending.getAll(account, categories)

                val pendingOriginal = pendingList.firstOrNull {
                    it.remoteId == id || -it.localId == id
                }
                val now = OffsetDateTime.now()

                if (pendingOriginal != null && pendingOriginal.pendingType != PendingType.DELETE) {
                    val updatedPending = pendingOriginal.copy(
                        category = category,
                        amount = transactionRequest.amount,
                        transactionDate = transactionRequest.transactionDate,
                        comment = transactionRequest.comment,
                        updatedAt = now
                    )
                    pending.save(updatedPending)
                    _changesFlow.emit(Unit)
                    return@fold updatedPending.toTransaction().right()
                }

                val original =  local.getById(id, account, categories)
                    ?: return@fold IllegalStateException("Transaction not found").left()

                val pendingTransaction = PendingTransaction(
                    localId = 0L,
                    remoteId = original.id,
                    account = original.account,
                    category = category,
                    amount = transactionRequest.amount,
                    transactionDate = transactionRequest.transactionDate,
                    comment = transactionRequest.comment,
                    createdAt = original.createdAt,
                    updatedAt = now,
                    pendingType = PendingType.EDIT
                )
                pending.save(pendingTransaction)
                _changesFlow.emit(Unit)
                pendingTransaction.toTransaction().right()
            },
            ifRight = {
                local.save(it)
                _changesFlow.emit(Unit)
                it.right()
            }
        )
    }

    override suspend fun delete(
        id: Long,
        account: Account,
        category: Category,
    ): EitherT<Unit> {
        return remote.delete(id).fold(
            ifLeft = {
                val categories = categoriesRepo.getAll().getOrNull()
                    ?: return@fold IllegalStateException("No categories").left()

                val allPending = pending.getAll(account, categories)

                allPending.filter {
                    it.remoteId == id || -it.localId == id
                }.forEach {
                    pending.delete(it.localId)
                }

                local.delete(id)

                val now = OffsetDateTime.now()

                // TODO: Refactor to avoid requiring full data for pending deletions.
                val pendingTransaction = PendingTransaction(
                    localId = 0L,
                    remoteId = id,
                    account = AccountBrief(
                        account.id,
                        account.name,
                        account.balance,
                        account.currency
                    ),
                    category = category,
                    amount = "",
                    transactionDate = now,
                    comment = "",
                    createdAt = now,
                    updatedAt = now,
                    pendingType = PendingType.DELETE
                )

                pending.save(pendingTransaction)
                _changesFlow.emit(Unit)
                Unit.right()
            },
            ifRight = {
                local.delete(id)
                _changesFlow.emit(Unit)
                Unit.right()
            }
        )
    }

    private fun mergeWithPending(
        base: List<Transaction>,
        pending: List<PendingTransaction>
    ): List<Transaction> {
        val pendingCreates = pending
            .filter { it.pendingType == PendingType.CREATE }
            .map { it.toTransaction() }
        val pendingEdits = pending
            .filter { it.pendingType == PendingType.EDIT }
            .map { it.toTransaction() }
        val pendingDeletes = pending
            .filter { it.pendingType == PendingType.DELETE }
            .map { it.remoteId }
            .toSet()

        return buildList {
            base
                .filterNot { it.id in pendingDeletes }
                .forEach { baseTx ->

                    val edited = pendingEdits.find { it.id == baseTx.id }
                    add(edited ?: baseTx)
                }

            addAll(pendingCreates)
        }
    }
}

fun PendingTransaction.toTransaction(): Transaction = Transaction(
    id = remoteId ?: -localId,
    account = account,
    category = category,
    amount = amount,
    transactionDate = transactionDate,
    comment = comment,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
