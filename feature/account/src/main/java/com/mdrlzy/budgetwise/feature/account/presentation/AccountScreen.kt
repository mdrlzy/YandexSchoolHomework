package com.mdrlzy.budgetwise.feature.account.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.core.di.CoreComponentProvider
import com.mdrlzy.budgetwise.core.ui.CoreRDrawable
import com.mdrlzy.budgetwise.core.ui.CoreRString
import com.mdrlzy.budgetwise.core.ui.composable.BWAddFab
import com.mdrlzy.budgetwise.core.ui.composable.BWErrorRetryScreen
import com.mdrlzy.budgetwise.core.ui.composable.BWHorDiv
import com.mdrlzy.budgetwise.core.ui.composable.BWListItemEmoji
import com.mdrlzy.budgetwise.core.ui.composable.BWListItemIcon
import com.mdrlzy.budgetwise.core.ui.composable.BWLoadingScreen
import com.mdrlzy.budgetwise.core.ui.composable.BWTopBar
import com.mdrlzy.budgetwise.core.ui.composable.ListenActiveScreenEffect
import com.mdrlzy.budgetwise.core.ui.utils.CurrencyUtils
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import org.orbitmvi.orbit.compose.collectAsState

@Destination<ExternalModuleGraph>
@Composable
fun AccountScreen() {
    val context = LocalContext.current
    val coreComponent = remember {
        (context.applicationContext as CoreComponentProvider).provideCoreComponent()
    }
    val viewModel: AccountViewModel =
        viewModel(factory = AccountViewModelFactory(coreComponent.accountRepo()))
    val state by viewModel.collectAsState()

    ListenActiveScreenEffect(
        onActive = viewModel::onActive,
        onInactive = viewModel::onInactive,
    )

    Scaffold(
        topBar = {
            BWTopBar(
                title = stringResource(CoreRString.my_account),
                trailingIcon = painterResource(CoreRDrawable.ic_edit)
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
            leadingText = stringResource(CoreRString.balance),
            trailingText = state.account.balance,
            background = MaterialTheme.colorScheme.secondary,
            height = 56.dp,
            emoji = "\uD83D\uDCB0",
            emojiBackground = Color.White,
            trailingIcon = painterResource(CoreRDrawable.ic_more),
            onClick = {}
        )
        BWHorDiv()
        BWListItemIcon(
            leadingText = stringResource(CoreRString.currency),
            trailingText = CurrencyUtils.getSymbolOrCode(state.account.currency),
            background = MaterialTheme.colorScheme.secondary,
            height = 56.dp,
            trailingIcon = painterResource(CoreRDrawable.ic_more),
            onClick = {}
        )
    }
}