package com.mdrlzy.budgetwise.core.domain.repo

import com.mdrlzy.budgetwise.core.domain.EitherT
import com.mdrlzy.budgetwise.core.domain.model.Account

interface AccountRepo {
    suspend fun getAccountId(): EitherT<Long>
    suspend fun getAccount(): EitherT<Account>
}