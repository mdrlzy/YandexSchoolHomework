package com.mdrlzy.budgetwise.core.domain

import java.time.OffsetDateTime

interface Prefs {
    fun saveLastSync(date: OffsetDateTime)
    fun getLastSync(): OffsetDateTime?

    fun savePinCode(pin: String)
    fun getPinCode(): String?
    fun clearPinCode()
}