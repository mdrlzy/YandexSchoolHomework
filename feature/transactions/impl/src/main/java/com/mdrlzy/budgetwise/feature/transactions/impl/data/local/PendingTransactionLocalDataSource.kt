package com.mdrlzy.budgetwise.feature.transactions.impl.data.local

import com.mdrlzy.budgetwise.core.db.dao.PendingTransactionDao
import com.mdrlzy.budgetwise.core.db.entity.PendingTransactionEntity
import com.mdrlzy.budgetwise.core.domain.PendingType
import com.mdrlzy.budgetwise.core.domain.model.Account
import com.mdrlzy.budgetwise.feature.account.api.AccountBrief
import com.mdrlzy.budgetwise.feature.categories.api.Category
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.PendingTransaction
import java.time.OffsetDateTime
import javax.inject.Inject

class PendingTransactionLocalDataSource @Inject constructor(
    private val dao: PendingTransactionDao,
) {
    suspend fun getAll(
        account: Account,
        categories: List<Category>,
    ) = dao.getAll().map { it.toTransaction(account, categories) }

    suspend fun getByPeriod(
        account: Account,
        categories: List<Category>,
        start: OffsetDateTime,
        end: OffsetDateTime,
    ): List<PendingTransaction> {
        return dao
            .getAll()
            .map { it.toTransaction(account, categories) }
            .filter { transaction ->
                transaction.transactionDate.isAfter(start) && transaction.transactionDate.isBefore(end)
            }
    }

    suspend fun save(transaction: PendingTransaction) {
        dao.insert(transaction.toEntity())
    }

    suspend fun delete(localId: Long) {
        dao.delete(localId)
    }
}

private fun PendingTransaction.toEntity() = PendingTransactionEntity(
    localId,
    remoteId,
    account.id,
    category.id,
    amount,
    transactionDate,
    comment,
    createdAt,
    updatedAt,
    pendingType,
)

private fun PendingTransactionEntity.toTransaction(
    account: Account, categories: List<Category>
) = PendingTransaction(
    localId,
    remoteId,
    AccountBrief(account.id, account.name, account.balance, account.currency),
    categories.find { it.id == categoryId }!!,
    amount,
    transactionDate,
    comment,
    createdAt,
    updatedAt,
    pendingType,
)