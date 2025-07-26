package com.mdrlzy.budgetwise.feature.transactions.api

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Account
import com.mdrlzy.budgetwise.feature.categories.api.Category
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime

interface TransactionRepo {
    val changesFlow: Flow<Unit>

    suspend fun getByPeriod(
        accountId: Long,
        start: OffsetDateTime,
        end: OffsetDateTime,
    ): EitherT<List<Transaction>>

    suspend fun getById(
        id: Long,
        account: Account,
    ): EitherT<Transaction>

    suspend fun create(
        account: Account,
        category: Category,
        transactionRequest: TransactionRequest
    ): EitherT<Transaction>

    suspend fun update(
        id: Long,
        account: Account,
        category: Category,
        transactionRequest: TransactionRequest
    ): EitherT<Transaction>

    suspend fun delete(
        id: Long,
        account: Account,
        category: Category,
    ): EitherT<Unit>
}
