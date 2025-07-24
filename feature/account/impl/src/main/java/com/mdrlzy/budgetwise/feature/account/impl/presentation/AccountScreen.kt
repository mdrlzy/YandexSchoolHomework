@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.budgetwise.feature.account.impl.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.core.di.CoreComponentProvider
import com.mdrlzy.budgetwise.core.domain.model.Currency
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
import com.mdrlzy.budgetwise.core.ui.composable.chart.BWBarChart
import com.mdrlzy.budgetwise.core.ui.composable.chart.ChartDataEntry
import com.mdrlzy.budgetwise.core.ui.utils.CurrencyUtils
import com.mdrlzy.budgetwise.feature.account.impl.domain.usecase.BuildMonthAccountStatsUseCase
import com.mdrlzy.budgetwise.feature.transactions.api.di.TransactionFeatureApiProvider
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import org.orbitmvi.orbit.compose.collectAsState
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

@Destination<ExternalModuleGraph>
@Composable
fun AccountScreen() {
    val context = LocalContext.current
    val coreComponent =
        remember {
            (context.applicationContext as CoreComponentProvider).provideCoreComponent()
        }
    val transactionsFeatureApi = remember {
        (context.applicationContext as TransactionFeatureApiProvider).provideTransactionFeatureApi()
    }
    val viewModel: AccountViewModel =
        viewModel(
            factory = AccountViewModelFactory(
                coreComponent.accountRepo(),
                BuildMonthAccountStatsUseCase(
                    transactionsFeatureApi.transactionRepo(),
                    coreComponent.accountRepo()
                )
            )
        )
    val state by viewModel.collectAsState()

    ListenActiveScreenEffect(
        onActive = viewModel::onActive,
        onInactive = viewModel::onInactive,
    )

    Scaffold(
        topBar = {
            val success = state
            if (success is AccountScreenState.Success) {
                if (success.isEditMode) {
                    EditTopBar(
                        name = success.accountName,
                        onNameChanged = viewModel::onNameChanged,
                        onCancel = viewModel::onCancelEdit,
                        onDone = viewModel::onDoneEdit,
                    )
                } else {
                    BWTopBar(
                        title = success.account.name,
                        trailingIcon = painterResource(CoreRDrawable.ic_edit),
                        onTrailingIconClick = viewModel::onEdit
                    )
                }
            }
        },
        floatingActionButton = {
            val success = state
            if (success is AccountScreenState.Success) {
                BWAddFab { }
            }
        },
    ) {
        Box(Modifier.padding(it)) {
            when (val st = state) {
                is AccountScreenState.Error ->
                    BWErrorRetryScreen(
                        error = st.error,
                    ) { viewModel.onRetry() }

                AccountScreenState.Loading -> BWLoadingScreen()

                is AccountScreenState.Success -> Content(
                    st,
                    onCurrencySymbolChange = viewModel::onChangeCurrency
                )
            }
        }
    }
}

@Composable
private fun Content(
    state: AccountScreenState.Success,
    onCurrencySymbolChange: (Currency) -> Unit
) {
    val currencySheetState = rememberModalBottomSheetState()
    var showCurrencySheet by remember { mutableStateOf(false) }

    if (showCurrencySheet) {
        CurrencyBottomSheet(
            sheetState = currencySheetState,
            onDismiss = {
                showCurrencySheet = false
            },
            onSelect = {
                onCurrencySymbolChange(it)
            }
        )
    }

    Column {
        BWListItemEmoji(
            leadingText = stringResource(CoreRString.balance),
            trailingText = state.account.balance,
            background = MaterialTheme.colorScheme.secondary,
            height = 56.dp,
            emoji = "\uD83D\uDCB0",
            emojiBackground = Color.White,
            trailingIcon = painterResource(CoreRDrawable.ic_more),
            onClick = {},
        )
        BWHorDiv()
        BWListItemIcon(
            leadingText = stringResource(CoreRString.currency),
            trailingText = CurrencyUtils.getSymbolOrCode(state.account.currency),
            background = MaterialTheme.colorScheme.secondary,
            height = 56.dp,
            trailingIcon = painterResource(CoreRDrawable.ic_more),
            onClick = { showCurrencySheet = true },
        )
        BWBarChart(
            modifier = Modifier.padding(top = 16.dp),
            height = 220.dp,
            data = state.chartData.mapToChartData(
                positive = MaterialTheme.colorScheme.primary,
                negative = MaterialTheme.colorScheme.errorContainer,
            )
        )
    }
}

private val chartFormatter = DateTimeFormatter.ofPattern("dd.MM")

private fun Map<OffsetDateTime, BigDecimal>.mapToChartData(
    positive: Color,
    negative: Color
) = map { (date, value) ->
    val color = if (value > BigDecimal.ZERO) positive else negative
    val label = if (date.dayOfMonth % 10 == 0) {
        date.format(chartFormatter)
    } else {
        null
    }
    ChartDataEntry(value.toFloat().absoluteValue, color, label)
}
