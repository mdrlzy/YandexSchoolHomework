package com.mdrlzy.budgetwise.feature.transactions.data

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.AccountBrief
import com.mdrlzy.budgetwise.core.domain.model.Category
import com.mdrlzy.budgetwise.core.network.BWApi
import com.mdrlzy.budgetwise.core.network.response.TransactionDto
import com.mdrlzy.budgetwise.feature.transactions.domain.model.Transaction
import com.mdrlzy.budgetwise.feature.transactions.domain.repo.TransactionRepo
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
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
