package com.mdrlzy.budgetwise.presentation.screen.income

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.R
import com.mdrlzy.budgetwise.presentation.screen.main.MainNavGraph
import com.mdrlzy.budgetwise.presentation.ui.composable.BWAddFab
import com.mdrlzy.budgetwise.presentation.ui.composable.BWHorDiv
import com.mdrlzy.budgetwise.presentation.ui.composable.BWListItem
import com.mdrlzy.budgetwise.presentation.ui.composable.BWListItemIcon
import com.mdrlzy.budgetwise.presentation.ui.composable.BWTopBar
import com.mdrlzy.budgetwise.presentation.ui.composable.BWErrorRetryScreen
import com.mdrlzy.budgetwise.presentation.ui.composable.BWLoadingScreen
import com.mdrlzy.budgetwise.presentation.ui.composable.ListenActiveScreenEffect
import com.mdrlzy.budgetwise.presentation.ui.utils.CurrencyUtils
import com.mdrlzy.budgetwise.presentation.ui.utils.appComponent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.TransactionHistoryScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectAsState

@Destination<MainNavGraph>
@Composable
fun IncomeScreen(
    navigator: DestinationsNavigator,
) {
    val context = LocalContext.current
    val viewModel: IncomeViewModel =
        viewModel(factory = context.appComponent.incomeViewModelFactory())
    val state by viewModel.collectAsState()

    ListenActiveScreenEffect(
        onActive = viewModel::onActive,
        onInactive = viewModel::onInactive,
    )

    Scaffold(
        topBar = {
            BWTopBar(
                title = stringResource(R.string.income_today),
                trailingIcon = painterResource(R.drawable.ic_history),
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
                IncomeScreenState.Error -> BWErrorRetryScreen { viewModel.onRetry() }
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
                leadingText = stringResource(R.string.all),
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
                trailingIcon = painterResource(R.drawable.ic_more),
                onClick = {},
            )
            BWHorDiv()
        }
    }
}