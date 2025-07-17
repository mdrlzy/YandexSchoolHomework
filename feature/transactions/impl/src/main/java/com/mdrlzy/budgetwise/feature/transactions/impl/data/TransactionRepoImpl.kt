package com.mdrlzy.budgetwise.feature.transactions.impl.data

import arrow.core.right
import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.feature.categories.api.CategoryRepo
import com.mdrlzy.budgetwise.feature.transactions.impl.data.local.TransactionLocalDataSource
import com.mdrlzy.budgetwise.feature.transactions.impl.data.remote.TransactionRemoteDataSource
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.TransactionRequest
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.TransactionResponse
import com.mdrlzy.budgetwise.feature.transactions.impl.domain.repo.TransactionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.OffsetDateTime
import javax.inject.Inject

class TransactionRepoImpl @Inject constructor(
    private val accountRepo: AccountRepo,
    private val categoriesRepo: CategoryRepo,
    private val remote: TransactionRemoteDataSource,
    private val local: TransactionLocalDataSource,
) : TransactionRepo {
    private val _changesFlow = MutableSharedFlow<Unit>()
    override val changesFlow: Flow<Unit> get() = _changesFlow

    override suspend fun getByPeriod(
        accountId: Long,
        start: OffsetDateTime,
        end: OffsetDateTime,
    ): EitherT<List<TransactionResponse>> {
        return remote.getByPeriod(accountId, start, end).fold(
            ifLeft = {
                //TODO!!!!
                local.getByPeriod(
                    accountRepo.getAccount().getOrNull()!!,
                    categoriesRepo.getAll().getOrNull()!!,
                    start,
                    end
                ).right()
            },
            ifRight = {
                local.save(it)
                it.right()
            }
        )
    }

    override suspend fun getById(id: Long): EitherT<TransactionResponse> {
        return remote.getById(id)
    }

    override suspend fun create(transactionRequest: TransactionRequest): EitherT<TransactionResponse> {
        return remote.create(transactionRequest).onRight {
            _changesFlow.emit(Unit)
            local.save(it)
        }
    }

    override suspend fun update(
        id: Long,
        transactionRequest: TransactionRequest
    ): EitherT<TransactionResponse> {
        return remote.update(id, transactionRequest).onRight {
            _changesFlow.emit(Unit)
        }
    }

    override suspend fun delete(id: Long): EitherT<Unit> {
        return remote.delete(id).onRight {
            _changesFlow.emit(Unit)
        }
    }
}
