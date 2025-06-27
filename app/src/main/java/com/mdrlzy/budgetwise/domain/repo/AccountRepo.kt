package com.mdrlzy.budgetwise.domain.repo

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.domain.model.Account

interface AccountRepo {
    suspend fun getAccountId(): EitherT<Long>
    suspend fun getAccount(): EitherT<Account>
}