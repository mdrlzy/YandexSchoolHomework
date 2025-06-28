package com.mdrlzy.budgetwise.feature.settings

data class SettingsScreenState(
    val isDarkTheme: Boolean,
)

sealed class SettingsScreenEffect
