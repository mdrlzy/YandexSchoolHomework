package com.mdrlzy.budgetwise.core.db.prefs

import android.content.Context
import com.mdrlzy.budgetwise.core.domain.Prefs
import java.time.OffsetDateTime

class PrefsImpl(context: Context): Prefs {

    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LAST_SYNC = "last_sync"
        private const val KEY_PIN_CODE = "pin_code"
    }

    override fun saveLastSync(date: OffsetDateTime) {
        prefs.edit()
            .putString(KEY_LAST_SYNC, date.toString())
            .apply()
    }

    override fun getLastSync(): OffsetDateTime? {
        return prefs.getString(KEY_LAST_SYNC, null)?.let {
            try {
                OffsetDateTime.parse(it)
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun savePinCode(pin: String) {
        prefs.edit()
            .putString(KEY_PIN_CODE, pin)
            .apply()
    }

    override fun getPinCode(): String? {
        return prefs.getString(KEY_PIN_CODE, null)
    }

    override fun clearPinCode() {
        prefs.edit()
            .remove(KEY_PIN_CODE)
            .apply()
    }

}