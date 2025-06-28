package com.mdrlzy.budgetwise.presentation.screen.splash

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

sealed class SplashScreenEffect {
    data object NavigateNext : SplashScreenEffect()
}

class SplashViewModel : ViewModel(), ContainerHost<Unit, SplashScreenEffect> {
    override val container: Container<Unit, SplashScreenEffect> =
        container(Unit)

    init {
        fallbackTimer()
    }

    fun onAnimationFinish() =
        intent {
            postSideEffect(SplashScreenEffect.NavigateNext)
        }

    private fun fallbackTimer() =
        intent {
            delay(4000)
            postSideEffect(SplashScreenEffect.NavigateNext)
        }
}
