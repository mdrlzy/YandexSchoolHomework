package com.mdrlzy.budgetwise.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdrlzy.budgetwise.core.domain.Prefs
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

sealed class SplashScreenEffect {
    data object NavigateNext : SplashScreenEffect()
    data object NavigatePinCode : SplashScreenEffect()
}

class SplashViewModel(
    private val prefs: Prefs
) : ViewModel(), ContainerHost<Unit, SplashScreenEffect> {
    override val container: Container<Unit, SplashScreenEffect> =
        container(Unit)

    init {
        fallbackTimer()
    }

    fun onAnimationFinish() =
        intent {
            navigate()
        }

    private fun fallbackTimer() =
        intent {
            delay(4000)
            navigate()
        }

    private fun navigate() = intent {
        prefs.getPinCode()?.let {
            postSideEffect(SplashScreenEffect.NavigatePinCode)
        } ?: postSideEffect(SplashScreenEffect.NavigateNext)
    }
}

class SplashViewModelFactory(
    private val prefs: Prefs
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(prefs) as T
    }
}
