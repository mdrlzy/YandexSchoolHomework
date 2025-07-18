package com.mdrlzy.budgetwise.feature.transactions.impl.data.remote

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Account
import com.mdrlzy.budgetwise.feature.account.api.AccountBrief
import com.mdrlzy.budgetwise.feature.categories.api.Category
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.Transaction
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.TransactionRequest
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TransactionRemoteDataSource @Inject constructor(
    private val api: BWTransactionsApi
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

    suspend fun create(
        account: Account,
        category: Category,
        transactionRequest: TransactionRequest
    ): EitherT<Transaction> {
        return api.createTransaction(transactionRequest.toDto())
            .map { it.toDomain(account, category) }
    }

    suspend fun update(
        id: Long,
        transactionRequest: TransactionRequest
    ): EitherT<Transaction> {
        return api.updateTransaction(id, transactionRequest.toDto()).map { it.toDomain() }
    }

    suspend fun delete(id: Long): EitherT<Unit> {
        return api.deleteTransaction(id)
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

private fun TransactionSimpleDto.toDomain(
    account: Account,
    category: Category,
) = Transaction(
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

private fun TransactionRequest.toDto(): TransactionRequestDto {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
    val utcDateTime = transactionDate.withOffsetSameInstant(ZoneOffset.UTC)
    val formatted = utcDateTime.format(formatter)

    return TransactionRequestDto(accountId, categoryId, amount, formatted, comment)
}