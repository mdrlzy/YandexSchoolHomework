package com.mdrlzy.budgetwise.feature.settings.presentation.navigation

interface SettingsFeatureExternalDeps {
    fun launchSyncWorker(hoursPeriod: Float)
}