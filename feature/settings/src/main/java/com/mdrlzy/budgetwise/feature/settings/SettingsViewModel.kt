package com.mdrlzy.budgetwise.feature.settings

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

data class SettingsScreenState(
    val isDarkTheme: Boolean,
)

sealed class SettingsScreenEffect

class SettingsViewModel: ViewModel(), ContainerHost<SettingsScreenState, SettingsScreenEffect> {
    override val container: Container<SettingsScreenState, SettingsScreenEffect> =
        container(SettingsScreenState(false))

    fun onToggleDarkTheme(new: Boolean) = intent {
        reduce {
            state.copy(isDarkTheme = new)
        }
    }
}