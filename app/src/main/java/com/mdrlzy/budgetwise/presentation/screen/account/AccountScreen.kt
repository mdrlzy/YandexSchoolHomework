package com.mdrlzy.budgetwise.presentation.screen.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.R
import com.mdrlzy.budgetwise.presentation.screen.main.MainNavGraph
import com.mdrlzy.budgetwise.presentation.ui.composable.AppFab
import com.mdrlzy.budgetwise.presentation.ui.composable.AppHorDiv
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItemEmoji
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItemIcon
import com.mdrlzy.budgetwise.presentation.ui.composable.AppTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.orbitmvi.orbit.compose.collectAsState

@Destination<MainNavGraph>
@Composable
fun AccountScreen() {
    val viewModel: AccountViewModel = viewModel()
    val state by viewModel.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = state.accountState.name,
                trailingIcon = painterResource(R.drawable.ic_edit)
            )
        },
        floatingActionButton = {
            AppFab {  }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            AppListItemEmoji(
                leadingText = stringResource(R.string.balance),
                trailingText = state.accountState.balance,
                background = MaterialTheme.colorScheme.secondary,
                height = 56.dp,
                emoji = "\uD83D\uDCB0",
                emojiBackground = Color.White,
                trailingIcon = painterResource(R.drawable.ic_more),
                onClick = {}
            )
            AppHorDiv()
            AppListItemIcon(
                leadingText = stringResource(R.string.currency),
                trailingText = state.accountState.currency,
                background = MaterialTheme.colorScheme.secondary,
                height = 56.dp,
                trailingIcon = painterResource(R.drawable.ic_more),
                onClick = {}
            )
        }
    }
}