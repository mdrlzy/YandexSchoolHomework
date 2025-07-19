package com.mdrlzy.budgetwise.feature.settings

import java.time.OffsetDateTime

data class SettingsScreenState(
    val isDarkTheme: Boolean,
    val lastSync: OffsetDateTime?,
)

sealed class SettingsScreenEffect
