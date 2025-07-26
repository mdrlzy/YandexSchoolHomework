package com.mdrlzy.budgetwise.feature.settings.presentation.screen.setpincode

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.core.di.CoreComponentProvider
import com.mdrlzy.budgetwise.core.ui.CoreRDrawable
import com.mdrlzy.budgetwise.core.ui.CoreRString
import com.mdrlzy.budgetwise.core.ui.composable.BWTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<ExternalModuleGraph>
@Composable
fun SetPinCodeScreen(
    navigator: DestinationsNavigator,
) {
    val context = LocalContext.current
    val viewModel: SetPinCodeViewModel = viewModel(
        factory = SetPinCodeViewModelFactory(
            (context.applicationContext as CoreComponentProvider).provideCoreComponent().prefs()
        )
    )
    val state by viewModel.collectAsState()
    val focusRequester = remember { FocusRequester() }

    viewModel.collectSideEffect { effect ->
        when (effect) {
            SetPinCodeScreenEffect.NavigateBack -> navigator.popBackStack()
            SetPinCodeScreenEffect.ToastCodeSaved -> Toast.makeText(
                context,
                CoreRString.pin_toast_saved,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            BWTopBar(
                title = stringResource(CoreRString.pin_set_title),
                leadingIcon = painterResource(CoreRDrawable.ic_back),
                onLeadingIconClick = { navigator.popBackStack() }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = when (state) {
                    is SetPinCodeScreenState.Confirm -> stringResource(CoreRString.pin_set_confirm)
                    is SetPinCodeScreenState.Input -> stringResource(CoreRString.pin_set_input)
                    SetPinCodeScreenState.WrongConfirm -> stringResource(CoreRString.pin_set_wrong)
                },
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (state is SetPinCodeScreenState.Input || state is SetPinCodeScreenState.Confirm) {
                    val length = when (val s = state) {
                        is SetPinCodeScreenState.Confirm -> s.input.length
                        is SetPinCodeScreenState.Input -> s.input.length
                        SetPinCodeScreenState.WrongConfirm -> 4
                    }
                    repeat(4) { index ->
                        val filled = index < length
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(
                                    color = when {
                                        filled -> Color.Black
                                        else -> Color.Gray
                                    },
                                    shape = CircleShape
                                )
                        )
                    }
                } else {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(
                                    color = Color.Red,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            val input = when (val s = state) {
                is SetPinCodeScreenState.Confirm -> s.input
                is SetPinCodeScreenState.Input -> s.input
                SetPinCodeScreenState.WrongConfirm -> ""
            }

            BasicTextField(
                value = input,
                onValueChange = { newValue ->
                    viewModel.onInputChanged(newValue)
                },
                modifier = Modifier
                    .size(1.dp)
                    .alpha(0f)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }
}