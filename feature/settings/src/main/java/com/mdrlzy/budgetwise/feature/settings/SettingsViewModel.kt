package com.mdrlzy.budgetwise.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdrlzy.budgetwise.core.domain.Prefs
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class SettingsViewModel(
    private val prefs: Prefs,
) : ViewModel(), ContainerHost<SettingsScreenState, SettingsScreenEffect> {
    override val container: Container<SettingsScreenState, SettingsScreenEffect> =
        container(SettingsScreenState(false, null))

    init {
        intent {
            reduce {
                state.copy(lastSync = prefs.getLastSync())
            }
        }
    }

    fun onToggleDarkTheme(new: Boolean) =
        intent {
            reduce {
                state.copy(isDarkTheme = new)
            }
        }
}

class SettingsViewModelFactory(private val prefs: Prefs): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(prefs) as T
    }
}
