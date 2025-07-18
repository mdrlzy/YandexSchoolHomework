package com.mdrlzy.budgetwise.feature.transactions.impl.data.local

import com.mdrlzy.budgetwise.core.db.dao.TransactionDao
import com.mdrlzy.budgetwise.core.db.entity.TransactionEntity
import com.mdrlzy.budgetwise.core.domain.model.Account
import com.mdrlzy.budgetwise.feature.account.api.AccountBrief
import com.mdrlzy.budgetwise.feature.categories.api.Category
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.Transaction
import java.time.OffsetDateTime
import javax.inject.Inject

class TransactionLocalDataSource @Inject constructor(
    private val dao: TransactionDao,
) {
    suspend fun getByPeriod(
        account: Account,
        categories: List<Category>,
        start: OffsetDateTime,
        end: OffsetDateTime,
    ): List<Transaction> {
        return dao
            .getAll()
            .map { transaction ->
                transaction.toTransaction(account, categories)
            }
            //TODO: store date as long and filter by sql
            .filter { transaction ->
                transaction.transactionDate.isAfter(start) && transaction.transactionDate.isBefore(
                    end
                )
            }
    }

    suspend fun getById(
        id: Long,
        account: Account,
        categories: List<Category>,
    ) = dao.findById(id)?.toTransaction(account, categories)

    suspend fun save(transaction: Transaction) {
        dao.insert(transaction.toEntity())
    }

    suspend fun save(transactions: List<Transaction>) {
        dao.insertAll(transactions.map { it.toEntity() })
    }

    suspend fun delete(id: Long) = dao.delete(id)
}

private fun Transaction.toEntity() = TransactionEntity(
    id,
    account.id,
    category.id,
    amount,
    transactionDate,
    comment,
    createdAt,
    updatedAt,
)

private fun TransactionEntity.toTransaction(
    account: Account,
    categories: List<Category>,
) = Transaction(
    id,
    AccountBrief(account.id, account.name, account.balance, account.currency),
    categories.find { it.id == categoryId }!!,
    amount,
    transactionDate,
    comment,
    createdAt,
    updatedAt,
)