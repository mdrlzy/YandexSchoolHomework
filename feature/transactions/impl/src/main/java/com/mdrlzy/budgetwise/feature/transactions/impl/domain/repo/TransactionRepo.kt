package com.mdrlzy.budgetwise.feature.transactions.impl.domain.repo

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Account
import com.mdrlzy.budgetwise.feature.categories.api.Category
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.TransactionRequest
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.TransactionResponse
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime

interface TransactionRepo {
    val changesFlow: Flow<Unit>

    suspend fun getByPeriod(
        accountId: Long,
        start: OffsetDateTime,
        end: OffsetDateTime,
    ): EitherT<List<TransactionResponse>>

    suspend fun getById(id: Long): EitherT<TransactionResponse>

    suspend fun create(
        account: Account,
        category: Category,
        transactionRequest: TransactionRequest
    ): EitherT<TransactionResponse>

    suspend fun update(
        id: Long,
        transactionRequest: TransactionRequest
    ): EitherT<TransactionResponse>

    suspend fun delete(id: Long): EitherT<Unit>
}
