package com.mdrlzy.budgetwise.core.domain

object CurrUtils {
    fun validateInput(
        oldInput: String,
        newInput: String,
    ): String {
        val containsDigitsAndDot = Regex("[0-9]*\\.?[0-9]*")
        if (!containsDigitsAndDot.matches(newInput))
            return oldInput

        val leadingZeros = "^0+(?=\\d)".toRegex()

        return newInput.replace(leadingZeros, "")
    }
}