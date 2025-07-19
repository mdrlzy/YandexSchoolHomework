package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.analyze

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.core.ui.CoreRDrawable
import com.mdrlzy.budgetwise.core.ui.CoreRString
import com.mdrlzy.budgetwise.core.ui.composable.BWDatePicker
import com.mdrlzy.budgetwise.core.ui.composable.BWErrorRetryScreen
import com.mdrlzy.budgetwise.core.ui.composable.BWHorDiv
import com.mdrlzy.budgetwise.core.ui.composable.BWListItem
import com.mdrlzy.budgetwise.core.ui.composable.BWListItemEmoji
import com.mdrlzy.budgetwise.core.ui.composable.BWLoadingScreen
import com.mdrlzy.budgetwise.core.ui.composable.BWTopBar
import com.mdrlzy.budgetwise.core.ui.utils.DateTimeHelper
import com.mdrlzy.budgetwise.feature.transactions.impl.di.TransactionsComponentHolder
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectAsState

@Destination<ExternalModuleGraph>
@Composable
fun AnalyzeTransactionsScreen(
    isIncomeMode: Boolean,
    startDate: Long,
    endDate: Long,
    navigator: DestinationsNavigator,
) {
    val context = LocalContext.current
    val component =
        remember {
            TransactionsComponentHolder.provide(context)
        }
    val viewModel: AnalyzeTransactionsViewModel =
        viewModel(
            factory = component.analyzeTransactionsViewModelFactory()
                .create(isIncomeMode, startDate, endDate),
        )
    val state by viewModel.collectAsState()

    Scaffold(
        topBar = {
            BWTopBar(
                title = stringResource(CoreRString.analyze),
                leadingIcon = painterResource(CoreRDrawable.ic_back),
                onLeadingIconClick = { navigator.popBackStack() },
                containerColor = MaterialTheme.colorScheme.surface,
            )
        }
    ) {
        Box(Modifier.padding(it)) {
            when (state) {
                is AnalyzeTransactionsScreenState.Error ->
                    BWErrorRetryScreen(
                        error = (state as AnalyzeTransactionsScreenState.Error).error,
                    ) { viewModel.onRetry() }

                AnalyzeTransactionsScreenState.Loading -> BWLoadingScreen()

                is AnalyzeTransactionsScreenState.Success -> Content(
                    state = state as AnalyzeTransactionsScreenState.Success,
                    onStartDateSelected = viewModel::onStartDateSelected,
                    onEndDateSelected = viewModel::onEndDateSelected,
                )
            }
        }

    }
}

@Composable
private fun Content(
    state: AnalyzeTransactionsScreenState.Success,
    onStartDateSelected: (Long?) -> Unit,
    onEndDateSelected: (Long?) -> Unit,
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    if (showStartDatePicker) {
        BWDatePicker(
            selectedDate = state.start.toInstant().toEpochMilli(),
            onDateSelected = {
                onStartDateSelected(it)
                showStartDatePicker = false
            },
            onDismiss = { showStartDatePicker = false },
        )
    }

    if (showEndDatePicker) {
        BWDatePicker(
            selectedDate = state.end.toInstant().toEpochMilli(),
            onDateSelected = {
                onEndDateSelected(it)
                showEndDatePicker = false
            },
            onDismiss = { showEndDatePicker = false },
        )
    }

    LazyColumn {
        item {
            BWListItem(
                leadingText = stringResource(CoreRString.period_start),
                trailingContent = {
                    AssistChip(
                        onClick = { showStartDatePicker = true },
                        label = {
                            Text(
                                text = DateTimeHelper.fullDate(state.start),
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = CircleShape,
                        border = null,
                    )
                },
                height = 56.dp,
            )
            BWHorDiv()
            BWListItem(
                leadingText = stringResource(CoreRString.period_end),
                trailingContent = {
                    AssistChip(
                        onClick = { showEndDatePicker = true },
                        label = {
                            Text(
                                text = DateTimeHelper.fullDate(state.end),
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = CircleShape,
                        border = null,
                    )
                },
                height = 56.dp,
            )
            BWHorDiv()
            BWListItem(
                leadingText = stringResource(CoreRString.sum),
                trailingText = "${state.sum} ${state.currency}",
                height = 56.dp,
            )
            BWHorDiv()
        }
        if (state.categories.isNotEmpty()) {
            items(state.categories) { categorySummary ->
                BWListItemEmoji(
                    leadingText = categorySummary.category.name,
                    trailingText = "${categorySummary.percentage}%",
                    trailDescText = "${categorySummary.categoryTotal} ${state.currency}",
                    emoji = categorySummary.category.emoji,
                    height = 70.dp,
                    trailingIcon = painterResource(CoreRDrawable.ic_more),
                )
                BWHorDiv()
            }
        } else {
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(CoreRString.no_data),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}