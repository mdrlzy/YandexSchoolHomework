package com.mdrlzy.budgetwise.feature.account.data

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Account
import com.mdrlzy.budgetwise.core.domain.model.Currency
import com.mdrlzy.budgetwise.core.network.BWApi
import com.mdrlzy.budgetwise.core.network.response.AccountDto
import com.mdrlzy.budgetwise.core.network.response.AccountUpdateRequest
import java.time.OffsetDateTime

class AccountRemoteDataSource(
    private val api: BWApi
) {
    suspend fun getAccount(): EitherT<Account> {
        return api.getAccounts().map {
            it.first().toDomain()
        }
    }

    suspend fun updateAccountCurrency(
        id: Long,
        account: Account,
        currency: Currency,
    ): EitherT<Account> {
        return api.updateAccountCurrency(
            id = id,
            body = account.toUpdateRequest().copy(currency = currency.code)
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
        OffsetDateTime.parse(createdAt),
        OffsetDateTime.parse(updatedAt),
    )

private fun Account.toUpdateRequest() = AccountUpdateRequest(
    name,
    balance,
    currency
)