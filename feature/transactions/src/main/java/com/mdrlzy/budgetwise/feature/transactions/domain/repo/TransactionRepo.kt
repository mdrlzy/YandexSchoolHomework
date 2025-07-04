package com.mdrlzy.budgetwise.feature.transactions.domain.repo

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.feature.transactions.domain.model.Transaction
import java.time.OffsetDateTime

interface TransactionRepo {
    suspend fun getByPeriod(
        accountId: Long,
        start: OffsetDateTime,
        end: OffsetDateTime,
    ): EitherT<List<Transaction>>

    suspend fun getById(id: Long): EitherT<Transaction>
}
