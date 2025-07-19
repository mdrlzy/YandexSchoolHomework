package com.mdrlzy.budgetwise.feature.account.impl.data.remote

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Account
import com.mdrlzy.budgetwise.core.network.NetworkUtils
import com.mdrlzy.budgetwise.feature.account.api.remote.AccountDto
import com.mdrlzy.budgetwise.feature.account.api.remote.AccountUpdateRequest
import java.time.OffsetDateTime
import javax.inject.Inject

class AccountRemoteDataSource @Inject constructor(
    private val api: BWAccountApi
) {
    suspend fun getAccount(): EitherT<Account> {
        return api.getAccounts().map {
            it.first().toDomain()
        }
    }

    suspend fun updateAccount(
        id: Long,
        newAccount: Account,
    ): EitherT<Account> {
        return api.updateAccount(
            id = id,
            body = newAccount.toUpdateRequest(),
        ).map {
            it.toDomain()
        }
    }
}

private fun AccountDto.toDomain() =
    Account(
        id,
        userId,
        name,
        balance,
        currency,
        NetworkUtils.fromUtcString(createdAt),
        NetworkUtils.fromUtcString(updatedAt),
    )

private fun Account.toUpdateRequest() = AccountUpdateRequest(
    name,
    balance,
    currency
)