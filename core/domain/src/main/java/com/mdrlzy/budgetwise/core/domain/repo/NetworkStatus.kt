package com.mdrlzy.budgetwise.core.domain.repo

import kotlinx.coroutines.flow.StateFlow

interface NetworkStatus {
    fun isOnline() = onlineStatus.value

    val onlineStatus: StateFlow<Boolean>
}