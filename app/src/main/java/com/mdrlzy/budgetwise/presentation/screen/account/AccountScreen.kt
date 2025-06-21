package com.mdrlzy.budgetwise.presentation.screen.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.R
import com.mdrlzy.budgetwise.presentation.screen.main.MainNavGraph
import com.mdrlzy.budgetwise.presentation.ui.composable.BWAddFab
import com.mdrlzy.budgetwise.presentation.ui.composable.BWHorDiv
import com.mdrlzy.budgetwise.presentation.ui.composable.BWListItemEmoji
import com.mdrlzy.budgetwise.presentation.ui.composable.BWListItemIcon
import com.mdrlzy.budgetwise.presentation.ui.composable.BWTopBar
import com.mdrlzy.budgetwise.presentation.ui.composable.BWErrorRetryScreen
import com.mdrlzy.budgetwise.presentation.ui.composable.BWLoadingScreen
import com.mdrlzy.budgetwise.presentation.ui.composable.ListenActiveScreenEffect
import com.mdrlzy.budgetwise.presentation.ui.utils.CurrencyUtils
import com.mdrlzy.budgetwise.presentation.ui.utils.appComponent
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState

@Destination<MainNavGraph>
@Composable
fun AccountScreen() {
    val context = LocalContext.current
    val viewModel: AccountViewModel =
        viewModel(factory = context.appComponent.accountViewModelFactory())
    val state by viewModel.collectAsState()

    ListenActiveScreenEffect(
        onActive = viewModel::onActive,
        onInactive = viewModel::onInactive,
    )

    Scaffold(
        topBar = {
            BWTopBar(
                title = stringResource(R.string.my_account),
                trailingIcon = painterResource(R.drawable.ic_edit)
            )
        },
        floatingActionButton = {
            BWAddFab { }
        }
    ) {
        Box(Modifier.padding(it)) {
            when (state) {
                is AccountScreenState.Error -> BWErrorRetryScreen(
                    error = (state as AccountScreenState.Error).error
                ) { viewModel.onRetry() }

                AccountScreenState.Loading -> BWLoadingScreen()

                is AccountScreenState.Success -> Content(state as AccountScreenState.Success)
            }
        }
    }
}

@Composable
private fun Content(state: AccountScreenState.Success) {
    Column() {
        BWListItemEmoji(
            leadingText = stringResource(R.string.balance),
            trailingText = state.account.balance,
            background = MaterialTheme.colorScheme.secondary,
            height = 56.dp,
            emoji = "\uD83D\uDCB0",
            emojiBackground = Color.White,
            trailingIcon = painterResource(R.drawable.ic_more),
            onClick = {}
        )
        BWHorDiv()
        BWListItemIcon(
            leadingText = stringResource(R.string.currency),
            trailingText = CurrencyUtils.getSymbolOrCode(state.account.currency),
            background = MaterialTheme.colorScheme.secondary,
            height = 56.dp,
            trailingIcon = painterResource(R.drawable.ic_more),
            onClick = {}
        )
    }
}