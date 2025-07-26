package com.mdrlzy.budgetwise.feature.settings.presentation.screen.enterpincode

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdrlzy.budgetwise.core.domain.Prefs
import com.mdrlzy.budgetwise.feature.settings.presentation.screen.setpincode.SetPinCodeScreenEffect
import com.mdrlzy.budgetwise.feature.settings.presentation.screen.setpincode.SetPinCodeScreenState
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class EnterPinCodeViewModel(
    private val prefs: Prefs,
) : ViewModel(), ContainerHost<EnterPinCodeScreenState, EnterPinCodeScreenEffect> {
    override val container: Container<EnterPinCodeScreenState, EnterPinCodeScreenEffect> =
        container(EnterPinCodeScreenState.Input(""))

    private val right = prefs.getPinCode()

    fun onInputChanged(new: String) = intent {
        val filtered = new.filter { it.isDigit() }.take(4)
        when (val s = state) {
            is EnterPinCodeScreenState.Input -> {
                if (filtered.length == 4) {
                    if (filtered == right) {
                        postSideEffect(EnterPinCodeScreenEffect.NavigateNext)
                    } else {
                        launchResetTimer()
                        Log.d("RRRR", "Wrong")
                        reduce {
                            EnterPinCodeScreenState.Wrong
                        }
                        return@intent
                    }
                }

                reduce {
                    s.copy(input = filtered)
                }
            }

            else -> {}
        }
    }

    private fun launchResetTimer() = intent {
        delay(2000L)
        reduce {
            EnterPinCodeScreenState.Input("")
        }
    }
}

class EnterPinCodeViewModelFactory(
    private val prefs: Prefs,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EnterPinCodeViewModel(prefs) as T
    }
}