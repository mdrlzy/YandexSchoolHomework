package com.mdrlzy.budgetwise.feature.transactions.impl.data.remote

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.AccountBrief
import com.mdrlzy.budgetwise.core.domain.model.Category
import com.mdrlzy.budgetwise.feature.transactions.api.remote.TransactionDto
import com.mdrlzy.budgetwise.feature.transactions.api.remote.TransactionRequestDto
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.TransactionResponse
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
    ): EitherT<List<TransactionResponse>> {
        return api.getTransactionsByPeriod(
            accountId,
            start.format(DateTimeFormatter.ISO_LOCAL_DATE),
            end.format(DateTimeFormatter.ISO_LOCAL_DATE),
        ).map { transactions -> transactions.map { it.toDomain() } }
    }

    suspend fun getById(id: Long): EitherT<TransactionResponse> {
        return api.getTransactionById(id).map { it.toDomain() }
    }

    suspend fun create(transactionRequest: TransactionRequest): EitherT<Unit> {
        return api.createTransaction(transactionRequest.toDto())
    }

    suspend fun update(
        id: Long,
        transactionRequest: TransactionRequest
    ): EitherT<TransactionResponse> {
        return api.updateTransaction(id, transactionRequest.toDto()).map { it.toDomain() }
    }

    suspend fun delete(id: Long) {
        api.deleteTransaction(id)
    }
}

private fun TransactionDto.toDomain() =
    TransactionResponse(
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