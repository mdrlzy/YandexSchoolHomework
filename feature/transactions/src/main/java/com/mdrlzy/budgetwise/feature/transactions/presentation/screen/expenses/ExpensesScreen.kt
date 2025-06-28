package com.mdrlzy.budgetwise.feature.transactions.presentation.screen.expenses

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mdrlzy.budgetwise.core.ui.CoreRDrawable
import com.mdrlzy.budgetwise.core.ui.CoreRString
import com.mdrlzy.budgetwise.core.ui.composable.BWAddFab
import com.mdrlzy.budgetwise.core.ui.composable.BWErrorRetryScreen
import com.mdrlzy.budgetwise.core.ui.composable.BWHorDiv
import com.mdrlzy.budgetwise.core.ui.composable.BWListItem
import com.mdrlzy.budgetwise.core.ui.composable.BWListItemEmoji
import com.mdrlzy.budgetwise.core.ui.composable.BWLoadingScreen
import com.mdrlzy.budgetwise.core.ui.composable.BWTopBar
import com.mdrlzy.budgetwise.core.ui.composable.ListenActiveScreenEffect
import com.mdrlzy.budgetwise.core.ui.utils.CurrencyUtils
import com.mdrlzy.budgetwise.feature.transactions.di.TransactionsComponentHolder
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.generated.transactions.destinations.TransactionHistoryScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectAsState

@Destination<ExternalModuleGraph>()
@Composable
fun ExpensesScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
) {
    val context = LocalContext.current
    val component =
        remember {
            TransactionsComponentHolder.provide(context)
        }
    val viewModel: ExpensesViewModel =
        viewModel(factory = component.expensesTodayViewModelFactory())

    val state by viewModel.collectAsState()

    ListenActiveScreenEffect(
        onActive = viewModel::onActive,
        onInactive = viewModel::onInactive,
    )

    Scaffold(
        topBar = {
            BWTopBar(
                title = stringResource(CoreRString.expenses_today),
                trailingIcon = painterResource(CoreRDrawable.ic_history),
                onTrailingIconClick = {
                    navigator.navigate(
                        TransactionHistoryScreenDestination(
                            isIncomeMode = false,
                        ),
                    )
                },
            )
        },
        floatingActionButton = {
            if (state is ExpensesScreenState.Success) {
                BWAddFab { }
            }
        },
    ) {
        Box(Modifier.padding(it)) {
            when (state) {
                is ExpensesScreenState.Error ->
                    BWErrorRetryScreen(
                        error = (state as ExpensesScreenState.Error).error,
                    ) { viewModel.onRetry() }

                ExpensesScreenState.Loading -> BWLoadingScreen()

                is ExpensesScreenState.Success -> Content(state as ExpensesScreenState.Success)
            }
        }
    }
}

@Composable
private fun Content(state: ExpensesScreenState.Success) {
    LazyColumn {
        item {
            BWListItem(
                leadingText = stringResource(CoreRString.all),
                trailingText = "${state.sum} ${CurrencyUtils.getSymbolOrCode(state.currency)}",
                background = MaterialTheme.colorScheme.secondary,
                height = 56.dp,
            )
            BWHorDiv()
        }
        items(state.transactions) {
            BWListItemEmoji(
                leadingText = it.categoryName,
                trailingText = it.amount,
                leadDescText = it.comment,
                emoji = it.emoji,
                height = 70.dp,
                trailingIcon = painterResource(CoreRDrawable.ic_more),
                onClick = {},
            )
            BWHorDiv()
        }
        item {
            Spacer(Modifier.height(70.dp))
        }
    }
}
