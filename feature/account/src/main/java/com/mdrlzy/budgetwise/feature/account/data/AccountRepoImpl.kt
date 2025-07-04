package com.mdrlzy.budgetwise.feature.account.data

import arrow.core.right
import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Account
import com.mdrlzy.budgetwise.core.domain.model.Currency
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class AccountRepoImpl(
    private val remote: AccountRemoteDataSource
) : AccountRepo {
    private var flow: MutableStateFlow<Account?> = MutableStateFlow(null)

    override suspend fun getAccount(): EitherT<Account> {
        return remote.getAccount().map {
            flow.emit(it)
            it
        }
    }

    override fun accountFlow(): Flow<Account> = flow.filterNotNull()

    override suspend fun getAccountId(): EitherT<Long> {
        flow.first()?.let {
            return it.id.right()
        } ?: let {
            return getAccount().map { it.id }
        }
    }

    override suspend fun updateCurrency(newCurrency: Currency): EitherT<Account> {
        val account = flow.first() ?: error("Account must be cached before updating currency")
        val newAccount = account.copy(currency = newCurrency.code)
        return remote.updateAccount(account.id, newAccount).map {
            flow.emit(it)
            it
        }
    }

    override suspend fun updateName(newName: String): EitherT<Account> {
        val account = flow.first() ?: error("Account must be cached before updating currency")
        val newAccount = account.copy(name = newName)
        return remote.updateAccount(account.id, newAccount).map {
            flow.emit(it)
            it
        }
    }
}
