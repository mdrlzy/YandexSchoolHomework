package com.mdrlzy.budgetwise.presentation.ui.utils

import android.icu.util.Currency

object CurrencyUtils {
    fun getSymbolOrCode(code: String): String {
        return try {
            Currency.getInstance(code).symbol
        } catch (e: Throwable) {
            code
        }
    }
}