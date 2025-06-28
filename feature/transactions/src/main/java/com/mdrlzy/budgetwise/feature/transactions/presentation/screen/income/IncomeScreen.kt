package com.mdrlzy.budgetwise.feature.transactions.presentation.screen.income

import androidx.compose.foundation.layout.Box
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
import com.mdrlzy.budgetwise.core.ui.CoreRDrawable
import com.mdrlzy.budgetwise.core.ui.CoreRString
import com.mdrlzy.budgetwise.core.ui.composable.BWAddFab
import com.mdrlzy.budgetwise.core.ui.composable.BWErrorRetryScreen
import com.mdrlzy.budgetwise.core.ui.composable.BWHorDiv
import com.mdrlzy.budgetwise.core.ui.composable.BWListItem
import com.mdrlzy.budgetwise.core.ui.composable.BWListItemIcon
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

@Destination<ExternalModuleGraph>
@Composable
fun IncomeScreen(
    navigator: DestinationsNavigator,
) {
    val context = LocalContext.current
    val component = remember {
        TransactionsComponentHolder.provide(context)
    }
    val viewModel: IncomeViewModel =
        viewModel(factory = component.incomeViewModelFactory())
    val state by viewModel.collectAsState()

    ListenActiveScreenEffect(
        onActive = viewModel::onActive,
        onInactive = viewModel::onInactive,
    )

    Scaffold(
        topBar = {
            BWTopBar(
                title = stringResource(CoreRString.income_today),
                trailingIcon = painterResource(CoreRDrawable.ic_history),
                onTrailingIconClick = {
                    navigator.navigate(
                        TransactionHistoryScreenDestination(
                            isIncomeMode = true
                        )
                    )
                }
            )
        },
        floatingActionButton = {
            if (state is IncomeScreenState.Success)
                BWAddFab { }
        }
    ) {
        Box(Modifier.padding(it)) {
            when (state) {
                is IncomeScreenState.Error -> BWErrorRetryScreen(
                    error = (state as IncomeScreenState.Error).error
                ) { viewModel.onRetry() }

                IncomeScreenState.Loading -> BWLoadingScreen()

                is IncomeScreenState.Success -> Content(state as IncomeScreenState.Success)
            }
        }
    }
}

@Composable
private fun Content(state: IncomeScreenState.Success) {
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
            BWListItemIcon(
                leadingText = it.categoryName,
                trailingText = it.amount,
                height = 70.dp,
                trailingIcon = painterResource(CoreRDrawable.ic_more),
                onClick = {},
            )
            BWHorDiv()
        }
    }
}