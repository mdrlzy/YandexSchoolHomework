package com.mdrlzy.budgetwise.feature.transactions.impl.data

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.feature.transactions.impl.data.remote.TransactionRemoteDataSource
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.Transaction
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.repo.TransactionRepo
import java.time.OffsetDateTime
import javax.inject.Inject

class TransactionRepoImpl @Inject constructor(
    private val remote: TransactionRemoteDataSource,
) : TransactionRepo {
    override suspend fun getByPeriod(
        accountId: Long,
        start: OffsetDateTime,
        end: OffsetDateTime,
    ): EitherT<List<Transaction>> {
        return remote.getByPeriod(accountId, start, end)
    }

    override suspend fun getById(id: Long): EitherT<Transaction> {
        return remote.getById(id)
    }
}
