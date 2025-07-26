package com.mdrlzy.budgetwise.feature.settings.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mdrlzy.budgetwise.core.domain.Prefs
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class SettingsViewModel(
    private val prefs: Prefs,
) : ViewModel(), ContainerHost<SettingsScreenState, SettingsScreenEffect> {
    private var syncFrequencyDebounceJob: Job? = null

    override val container: Container<SettingsScreenState, SettingsScreenEffect> =
        container(
            SettingsScreenState(
                isDarkTheme = false,
                lastSync = null,
                syncFrequency = prefs.getSyncFrequencyHours()
            )
        )

    init {
        intent {
            val isDarkTheme = prefs.isDarkTheme.first()
            reduce {
                state.copy(
                    lastSync = prefs.getLastSync(),
                    isDarkTheme = isDarkTheme
                )
            }
        }
    }

    fun onToggleDarkTheme(new: Boolean) =
        intent {
            prefs.setDarkTheme(new)
            reduce {
                state.copy(isDarkTheme = new)
            }
        }

    fun onChangeSyncFrequency(new: Float) = intent {
        reduce {
            state.copy(syncFrequency = new)
        }

        syncFrequencyDebounceJob?.cancel()
        syncFrequencyDebounceJob = viewModelScope.launch {
            delay(300)
            prefs.setSyncFrequencyHours(new)
            postSideEffect(SettingsScreenEffect.LaunchSyncWorker(new))
        }
    }

    fun onResetPinCode() {
        prefs.clearPinCode()
    }
}

class SettingsViewModelFactory(private val prefs: Prefs) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(prefs) as T
    }
}
