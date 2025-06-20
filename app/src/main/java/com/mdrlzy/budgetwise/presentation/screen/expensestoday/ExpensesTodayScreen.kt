package com.mdrlzy.budgetwise.presentation.screen.expensestoday

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mdrlzy.budgetwise.R
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItemEmoji
import com.mdrlzy.budgetwise.presentation.ui.composable.AppTopBar
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.presentation.App
import com.mdrlzy.budgetwise.presentation.screen.main.MainNavGraph
import com.mdrlzy.budgetwise.presentation.ui.composable.AppFab
import com.mdrlzy.budgetwise.presentation.ui.composable.AppHorDiv
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItem
import com.mdrlzy.budgetwise.presentation.ui.composable.BWLoadingScreen
import com.mdrlzy.budgetwise.presentation.ui.utils.CurrencyUtils
import com.mdrlzy.budgetwise.presentation.ui.utils.appComponent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.TransactionEditScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectAsState

@Destination<MainNavGraph>()
@Composable
fun ExpensesTodayScreen(
    navigator: DestinationsNavigator,
) {
    val context = LocalContext.current
    val viewModel: ExpensesTodayViewModel =
        viewModel(factory = context.appComponent.expensesTodayViewModelFactory())

    val state by viewModel.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.expenses_today),
                trailingIcon = painterResource(R.drawable.ic_history)
            )
        },
        floatingActionButton = {
            if (state.initialized && state.isError.not())
                AppFab {  }
        }
    ) {
        Box(Modifier.padding(it)) {
            when {
                state.initialized.not() -> BWLoadingScreen()
                else -> Content(state)
            }
        }
    }
}

@Composable
private fun Content(state: ExpensesTodayState) {
    LazyColumn {
        item {
            AppListItem(
                leadingText = stringResource(R.string.all),
                trailingText = "${state.sum} ${CurrencyUtils.getSymbolOrCode(state.currency)}",
                background = MaterialTheme.colorScheme.secondary,
                height = 56.dp,
            )
            AppHorDiv()
        }
        items(state.transactions) {
            AppListItemEmoji(
                leadingText = it.categoryName,
                trailingText = it.amount,
                descText = it.comment,
                emoji = it.emoji,
                height = 70.dp,
                trailingIcon = painterResource(R.drawable.ic_more),
                onClick = {}
            )
            AppHorDiv()
        }
        item {
            Spacer(Modifier.height(70.dp))
        }
    }
}