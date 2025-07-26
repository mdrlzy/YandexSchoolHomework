package com.mdrlzy.budgetwise.feature.settings.presentation.screen.enterpincode

sealed class EnterPinCodeScreenState {
    data class Input(val input: String): EnterPinCodeScreenState()
    data object Wrong : EnterPinCodeScreenState()
}

sealed class EnterPinCodeScreenEffect {
    data object NavigateNext : EnterPinCodeScreenEffect()
}