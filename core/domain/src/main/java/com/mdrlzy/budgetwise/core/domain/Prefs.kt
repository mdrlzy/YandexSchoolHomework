package com.mdrlzy.budgetwise.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.time.OffsetDateTime

interface Prefs {
    val isDarkTheme: StateFlow<Boolean>

    fun setDarkTheme(isDarkTheme: Boolean)

    fun saveLastSync(date: OffsetDateTime)
    fun getLastSync(): OffsetDateTime?

    fun savePinCode(pin: String)
    fun getPinCode(): String?
    fun clearPinCode()
}