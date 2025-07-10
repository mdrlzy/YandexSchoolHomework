package com.mdrlzy.budgetwise.feature.transactions.impl.data

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.AccountBrief
import com.mdrlzy.budgetwise.core.domain.model.Category
import com.mdrlzy.budgetwise.core.network.BWApi
import com.mdrlzy.budgetwise.core.network.response.TransactionDto
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.Transaction
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TransactionRemoteDataSource @Inject constructor(
    private val api: BWApi
) {
    suspend fun getByPeriod(
        accountId: Long,
        start: OffsetDateTime,
        end: OffsetDateTime,
    ): EitherT<List<Transaction>> {
        return api.getTransactionsByPeriod(
            accountId,
            start.format(DateTimeFormatter.ISO_LOCAL_DATE),
            end.format(DateTimeFormatter.ISO_LOCAL_DATE),
        ).map { transactions -> transactions.map { it.toDomain() } }
    }

    suspend fun getById(id: Long): EitherT<Transaction> {
        return api.getTransactionById(id).map { it.toDomain() }
    }
}

private fun TransactionDto.toDomain() =
    Transaction(
        id = id,
        account = AccountBrief(
            account.id,
            account.name,
            account.balance,
            account.currency
        ),
        category = Category(category.id, category.name, category.emoji, category.isIncome),
        amount = amount,
        transactionDate = OffsetDateTime.parse(transactionDate),
        comment = comment,
        createdAt = OffsetDateTime.parse(createdAt),
        updatedAt = OffsetDateTime.parse(updatedAt),
    )