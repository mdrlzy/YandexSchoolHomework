package com.mdrlzy.budgetwise.data.repo

import arrow.core.right
import com.mdrlzy.budgetwise.data.network.BWApi
import com.mdrlzy.budgetwise.data.network.response.AccountDto
import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.domain.model.Account
import com.mdrlzy.budgetwise.domain.repo.AccountRepo
import java.time.OffsetDateTime
import javax.inject.Inject

class AccountRepoImpl @Inject constructor(
    private val api: BWApi
): AccountRepo {
    private var cacheAccount: Account? = null

    override suspend fun getAccount(): EitherT<Account> {
        return api.getAccounts().map {
            it.first().toDomain().also {
                cacheAccount = it
            }
        }
    }

    override suspend fun getAccountId(): EitherT<Long> {
        cacheAccount?.let {
            return it.id.right()
        } ?: let {
            return getAccount().map { it.id }
        }
    }
}

private fun AccountDto.toDomain() = Account(
    id,
    userId,
    name,
    balance,
    currency,
    OffsetDateTime.parse(createdAt),
    OffsetDateTime.parse(updatedAt),
)