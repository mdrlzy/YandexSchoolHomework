package com.mdrlzy.budgetwise.feature.settings.presentation.screen.setpincode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdrlzy.budgetwise.core.domain.Prefs
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class SetPinCodeViewModel(
    private val prefs: Prefs,
) : ViewModel(), ContainerHost<SetPinCodeScreenState, SetPinCodeScreenEffect> {
    override val container: Container<SetPinCodeScreenState, SetPinCodeScreenEffect> =
        container(SetPinCodeScreenState.Input(""))

    fun onInputChanged(new: String) = intent {
        val filtered = new.filter { it.isDigit() }.take(4)
        when (val s = state) {
            is SetPinCodeScreenState.Confirm -> reduce {
                if (filtered.length == 4) {
                    if (filtered == s.right) {
                        prefs.savePinCode(filtered)
                        navBack()
                        return@reduce state
                    } else {
                        launchResetTimer()
                        return@reduce SetPinCodeScreenState.WrongConfirm
                    }
                }

                s.copy(input = filtered)
            }
            is SetPinCodeScreenState.Input -> reduce {
                if (filtered.length == 4) {
                    return@reduce SetPinCodeScreenState.Confirm(input = "", right = new)
                }

                s.copy(input = filtered)
            }

            else -> {}
        }
    }

    private fun launchResetTimer() = intent {
        delay(2000L)
        reduce {
            SetPinCodeScreenState.Input("")
        }
    }

    private fun navBack() = intent {
        postSideEffect(SetPinCodeScreenEffect.NavigateBack)
    }
}

class SetPinCodeViewModelFactory(private val prefs: Prefs): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SetPinCodeViewModel(prefs) as T
    }
}