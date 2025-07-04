package com.mdrlzy.budgetwise.core.domain.repo

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Account
import com.mdrlzy.budgetwise.core.domain.model.Currency
import kotlinx.coroutines.flow.Flow

/**
 * Repository for accessing account-related data.
 */
interface AccountRepo {
    /**
     * Returns the account ID.
     *
     * Attempts to return the value from cache to avoid unnecessary network operations.
     * If the cached value is missing or expired, it may fetch the ID from api.
     *
     * @return [EitherT] containing the [Long] account ID or an error.
     */
    suspend fun getAccountId(): EitherT<Long>

    /**
     * Fetches the full account details from the network.
     *
     * This method always performs a remote request and does not rely on local cache.
     *
     * @return [EitherT] containing the [Account] or an error.
     */
    suspend fun getAccount(): EitherT<Account>

    suspend fun accountFlow(): Flow<Account>

    suspend fun updateCurrency(currency: Currency): EitherT<Account>
}
