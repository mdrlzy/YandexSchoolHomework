package com.mdrlzy.budgetwise.domain.usecase

import arrow.core.flatMap
import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.domain.model.Transaction
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.domain.repo.TransactionRepo
import java.time.OffsetDateTime
import javax.inject.Inject

class GetIncomeTransactionsUseCase @Inject constructor(
    private val accountRepo: AccountRepo,
    private val transactionRepo: TransactionRepo
) {
    suspend operator fun invoke(
        start: OffsetDateTime,
        end: OffsetDateTime
    ): EitherT<List<Transaction>> {
        return accountRepo.getAccountId().flatMap {
            transactionRepo.getByPeriod(
                it,
                start,
                end,
            ).map { transactions ->
                transactions.filter { it.category.isIncome }
                    .sortedByDescending { it.transactionDate }
            }
        }
    }
}