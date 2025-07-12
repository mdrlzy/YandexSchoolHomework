package com.mdrlzy.budgetwise.feature.transactions.impl.data

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.feature.transactions.impl.data.remote.TransactionRemoteDataSource
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.TransactionRequest
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.TransactionResponse
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
    ): EitherT<List<TransactionResponse>> {
        return remote.getByPeriod(accountId, start, end)
    }

    override suspend fun getById(id: Long): EitherT<TransactionResponse> {
        return remote.getById(id)
    }

    override suspend fun create(transactionRequest: TransactionRequest): EitherT<TransactionResponse> {
        return remote.create(transactionRequest)
    }

    override suspend fun update(
        id: Long,
        transactionRequest: TransactionRequest
    ): EitherT<TransactionResponse> {
        return remote.update(id, transactionRequest)
    }

    override suspend fun delete(id: Long) {
        return remote.delete(id)
    }
}
