package com.mdrlzy.budgetwise.feature.settings.presentation.screen.settings

import java.time.OffsetDateTime

data class SettingsScreenState(
    val isDarkTheme: Boolean,
    val lastSync: OffsetDateTime?,
    val syncFrequency: Float,
)

sealed class SettingsScreenEffect {
    data class LaunchSyncWorker(val interval: Float): SettingsScreenEffect()
}
