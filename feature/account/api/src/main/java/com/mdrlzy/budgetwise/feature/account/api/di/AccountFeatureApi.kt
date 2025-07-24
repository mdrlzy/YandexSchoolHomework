package com.mdrlzy.budgetwise.feature.account.api.di

import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo

interface AccountFeatureApi {
    fun accountRepo(): AccountRepo
}