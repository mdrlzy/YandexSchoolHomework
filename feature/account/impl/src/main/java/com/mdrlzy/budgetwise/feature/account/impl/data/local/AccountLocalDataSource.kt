package com.mdrlzy.budgetwise.feature.account.impl.data.local

import arrow.core.Either
import com.mdrlzy.budgetwise.core.db.dao.AccountDao
import com.mdrlzy.budgetwise.core.db.entity.AccountEntity
import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Account
import javax.inject.Inject

class AccountLocalDataSource @Inject constructor(private val dao: AccountDao) {
    suspend fun get(): EitherT<Account> {
        return Either.catch { dao.getAll().first().toAccount() }
    }

    suspend fun save(account: Account) {
        dao.insert(account.toEntity())
    }
}

private fun AccountEntity.toAccount() = Account(
    id,
    userId,
    name,
    balance,
    currency,
    createdAt,
    updatedAt
)

private fun Account.toEntity() = AccountEntity(
    id,
    userId,
    name,
    balance,
    currency,
    createdAt,
    updatedAt
)