package com.mdrlzy.budgetwise.feature.account.impl.domain.usecase

import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.feature.transactions.api.TransactionRepo
import java.math.BigDecimal
import java.time.OffsetDateTime
import javax.inject.Inject

class BuildMonthAccountStatsUseCase @Inject constructor(
    private val transactionRepo: TransactionRepo,
    private val accountRepo: AccountRepo,
) {
    private val startDate = OffsetDateTime.now().withDayOfMonth(1)
    private val endDate = OffsetDateTime.now()

    suspend operator fun invoke(): EitherT<Map<OffsetDateTime, BigDecimal>> {
        val accountId = accountRepo.getAccountId().getOrElse { return it.left() }
        val transactions = transactionRepo.getByPeriod(accountId, startDate, endDate)
            .getOrElse { return it.left() }

        val transactionsByDay = transactions.groupBy { transaction ->
            val date = transaction.transactionDate
            OffsetDateTime.of(
                date.year,
                date.monthValue,
                date.dayOfMonth,
                0,
                0,
                0,
                0,
                date.offset,
            )
        }.mapValues { (_, dayTransactions) ->
            dayTransactions.fold(BigDecimal.ZERO) { acc, transaction ->
                val amount = BigDecimal(transaction.amount)
                if (transaction.category.isIncome)
                    acc + amount
                else
                    acc - amount
            }
        }

        val allDays = mutableListOf<OffsetDateTime>()
        var current = OffsetDateTime.of(
            startDate.year,
            startDate.monthValue,
            startDate.dayOfMonth,
            0, 0, 0, 0,
            startDate.offset
        )

        val end = OffsetDateTime.of(
            endDate.year,
            endDate.monthValue,
            endDate.dayOfMonth,
            0, 0, 0, 0,
            endDate.offset
        )

        while (!current.isAfter(end)) {
            allDays.add(current)
            current = current.plusDays(1)
        }

        val result = allDays.associateWith { date ->
            transactionsByDay[date] ?: BigDecimal.ZERO
        }

        return result.right()
    }
}