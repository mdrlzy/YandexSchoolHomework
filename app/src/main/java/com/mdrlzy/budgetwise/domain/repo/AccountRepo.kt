package com.mdrlzy.budgetwise.domain.repo

import arrow.core.Either
import com.mdrlzy.budgetwise.domain.EitherT
import com.mdrlzy.budgetwise.domain.model.Account

interface AccountRepo {
    suspend fun getAccountId(): EitherT<Long>
    suspend fun getAccount(): EitherT<Account>
}