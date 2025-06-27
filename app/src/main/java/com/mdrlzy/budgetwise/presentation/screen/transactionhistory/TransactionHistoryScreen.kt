package com.mdrlzy.budgetwise.presentation.screen.transactionhistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.core.ui.composable.BWTopBar
import com.mdrlzy.budgetwise.R
import com.mdrlzy.budgetwise.presentation.screen.main.MainNavGraph
import com.mdrlzy.budgetwise.core.ui.composable.BWHorDiv
import com.mdrlzy.budgetwise.core.ui.composable.BWListItem
import com.mdrlzy.budgetwise.core.ui.composable.BWListItemEmoji
import com.mdrlzy.budgetwise.core.ui.composable.BWDatePicker
import com.mdrlzy.budgetwise.core.ui.composable.BWErrorRetryScreen
import com.mdrlzy.budgetwise.core.ui.composable.BWLoadingScreen
import com.mdrlzy.budgetwise.core.ui.utils.CurrencyUtils
import com.mdrlzy.budgetwise.core.ui.utils.DateTimeHelper
import com.mdrlzy.budgetwise.core.ui.utils.appComponent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectAsState

@Destination<MainNavGraph>
@Composable
fun TransactionHistoryScreen(
    isIncomeMode: Boolean,
    navigator: DestinationsNavigator,
) {
    val context = LocalContext.current
    val viewModel: TransactionHistoryViewModel = viewModel(
        factory = context.appComponent.transactionHistoryViewModelFactory().create(isIncomeMode)
    )
    val state by viewModel.collectAsState()

    Scaffold(
        topBar = {
            BWTopBar(
                title = stringResource(R.string.my_history),
                leadingIcon = painterResource(R.drawable.ic_back),
                trailingIcon = painterResource(R.drawable.ic_analyse),
                onLeadingIconClick = { navigator.popBackStack() },
                onTrailingIconClick = {},
            )
        }
    ) {
        Box(Modifier.padding(it)) {
            when (state) {
                is TransactionHistoryScreenState.Error -> BWErrorRetryScreen(
                    error = (state as TransactionHistoryScreenState.Error).error
                ) { viewModel.onRetry() }

                TransactionHistoryScreenState.Loading -> BWLoadingScreen()

                is TransactionHistoryScreenState.Success -> Content(
                    state = state as TransactionHistoryScreenState.Success,
                    onStartDateSelected = viewModel::onStartDateSelected,
                    onEndDateSelected = viewModel::onEndDateSelected,
                )
            }
        }
    }
}

@Composable
private fun Content(
    state: TransactionHistoryScreenState.Success,
    onStartDateSelected: (Long?) -> Unit,
    onEndDateSelected: (Long?) -> Unit,
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    if (showStartDatePicker) {
        BWDatePicker(
            state.startDate.toInstant().toEpochMilli(),
            onDateSelected = {
                onStartDateSelected(it)
                showStartDatePicker = false
            },
            onDismiss = { showStartDatePicker = false }
        )
    }

    if (showEndDatePicker) {
        BWDatePicker(
            state.endDate.toInstant().toEpochMilli(),
            onDateSelected = {
                onEndDateSelected(it)
                showEndDatePicker = false
            },
            onDismiss = { showEndDatePicker = false }
        )
    }

    LazyColumn {
        item {
            BWListItem(
                leadingText = stringResource(R.string.start),
                trailingText = DateTimeHelper.fullDate(state.startDate),
                background = MaterialTheme.colorScheme.secondary,
                height = 56.dp,
                onClick = { showStartDatePicker = true },
            )
            BWHorDiv()
            BWListItem(
                leadingText = stringResource(R.string.end),
                trailingText = DateTimeHelper.fullDate(state.endDate),
                background = MaterialTheme.colorScheme.secondary,
                height = 56.dp,
                onClick = { showEndDatePicker = true },
            )
            BWHorDiv()
            BWListItem(
                leadingText = stringResource(R.string.sum),
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
                trailDescText = DateTimeHelper.shortDate(it.transactionDate),
                emoji = it.emoji,
                height = 70.dp,
                trailingIcon = painterResource(R.drawable.ic_more),
                onClick = {}
            )
            BWHorDiv()
        }
    }
}