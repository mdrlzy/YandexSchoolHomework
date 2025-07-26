package com.mdrlzy.budgetwise.feature.settings.presentation.screen.setpincode

sealed class SetPinCodeScreenState {
    data class Input(val input: String): SetPinCodeScreenState()
    data class Confirm(val input: String, val right: String): SetPinCodeScreenState()
    data object WrongConfirm : SetPinCodeScreenState()
}

sealed class SetPinCodeScreenEffect {
    data object ToastCodeSaved : SetPinCodeScreenEffect()
    data object NavigateBack : SetPinCodeScreenEffect()
}