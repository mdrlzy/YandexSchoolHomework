package com.mdrlzy.budgetwise.feature.transactions.impl.domain.repo

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Account
import com.mdrlzy.budgetwise.feature.categories.api.Category
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.TransactionRequest
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime

interface TransactionRepo {
    val changesFlow: Flow<Unit>

    suspend fun getByPeriod(
        accountId: Long,
        start: OffsetDateTime,
        end: OffsetDateTime,
    ): EitherT<List<Transaction>>

    suspend fun getById(id: Long): EitherT<Transaction>

    suspend fun create(
        account: Account,
        category: Category,
        transactionRequest: TransactionRequest
    ): EitherT<Transaction>

    suspend fun update(
        id: Long,
        transactionRequest: TransactionRequest
    ): EitherT<Transaction>

    suspend fun delete(id: Long): EitherT<Unit>
}
