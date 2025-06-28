package com.mdrlzy.budgetwise.core.di

import com.mdrlzy.budgetwise.core.domain.BuildConfigFields
import com.mdrlzy.budgetwise.core.domain.repo.AccountRepo
import com.mdrlzy.budgetwise.core.domain.repo.NetworkStatus
import com.mdrlzy.budgetwise.core.network.BWApi

interface CoreComponent {
    fun bwApi(): BWApi

    fun networkStatus(): NetworkStatus

    fun buildConfigFields(): BuildConfigFields

    fun accountRepo(): AccountRepo
}
