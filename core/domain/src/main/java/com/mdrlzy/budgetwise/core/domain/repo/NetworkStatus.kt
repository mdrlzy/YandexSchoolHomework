package com.mdrlzy.budgetwise.core.domain.repo

import kotlinx.coroutines.flow.StateFlow

/**
 * Provides information about the current network connectivity status.
 */
interface NetworkStatus {

    /**
     * Returns the current online status.
     *
     * @return `true` if the device is considered online, `false` otherwise.
     */
    fun isOnline() = onlineStatus.value

    /**
     * A [StateFlow] representing the current online status.
     *
     * Emits `true` when the device is online and `false` when offline.
     * Can be observed to react to connectivity changes in real time.
     */
    val onlineStatus: StateFlow<Boolean>
}