package com.mdrlzy.budgetwise.core.db.prefs

import android.content.Context
import com.mdrlzy.budgetwise.core.domain.Prefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.OffsetDateTime

class PrefsImpl(context: Context): Prefs {

    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    private val isDarkThemeFlow = MutableStateFlow(getInitialDarkTheme())


    companion object {
        private const val KEY_IS_DARK_THEME = "is_dark_theme"
        private const val KEY_LAST_SYNC = "last_sync"
        private const val KEY_PIN_CODE = "pin_code"
        private const val KEY_SYNC_FREQUENCY_HOURS = "sync_frequency_hours"
    }

    override val isDarkTheme: StateFlow<Boolean> = isDarkThemeFlow

    override fun setDarkTheme(isDarkTheme: Boolean) {
        prefs.edit()
            .putBoolean(KEY_IS_DARK_THEME, isDarkTheme)
            .apply()
        isDarkThemeFlow.value = isDarkTheme
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

    override fun getSyncFrequencyHours(): Float =
        prefs.getFloat(KEY_SYNC_FREQUENCY_HOURS, 2f)

    override fun setSyncFrequencyHours(value: Float) {
        prefs.edit()
            .putFloat(KEY_SYNC_FREQUENCY_HOURS, value)
            .apply()
    }


    private fun getInitialDarkTheme(): Boolean {
        return prefs.getBoolean(KEY_IS_DARK_THEME, false)
    }

}