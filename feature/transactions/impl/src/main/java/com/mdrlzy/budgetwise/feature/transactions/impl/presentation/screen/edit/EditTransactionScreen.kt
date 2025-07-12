@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.edit

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.core.ui.CoreRDrawable
import com.mdrlzy.budgetwise.core.ui.CoreRString
import com.mdrlzy.budgetwise.core.ui.composable.BWDatePicker
import com.mdrlzy.budgetwise.core.ui.composable.BWErrorRetryScreen
import com.mdrlzy.budgetwise.core.ui.composable.BWHorDiv
import com.mdrlzy.budgetwise.core.ui.composable.BWListItem
import com.mdrlzy.budgetwise.core.ui.composable.BWListItemIcon
import com.mdrlzy.budgetwise.core.ui.composable.BWLoadingScreen
import com.mdrlzy.budgetwise.core.ui.composable.BWTopBar
import com.mdrlzy.budgetwise.feature.transactions.impl.di.TransactionsComponentHolder
import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.navigation.TransactionsExternalNavigator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.OpenResultRecipient
import com.ramcosta.composedestinations.result.onResult
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.format.DateTimeFormatter

@Destination<ExternalModuleGraph>()
@Composable
fun EditTransactionScreen(
    navigator: DestinationsNavigator,
    isIncomeMode: Boolean,
    transactionId: Long?,
    externalNavigator: TransactionsExternalNavigator,
    resultRecipient: OpenResultRecipient<Long>,
) {
    val context = LocalContext.current
    val component =
        remember {
            TransactionsComponentHolder.provide(context)
        }
    val viewModel: EditTransactionViewModel =
        viewModel(
            factory = component.editTransactionViewModelFactory()
                .create(isIncomeMode, transactionId),
        )
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            EditTransactionScreenEffect.NavigateBack -> navigator.popBackStack()
        }
    }

    resultRecipient.onResult {
        viewModel.onCategorySelected(it)
    }

    val isSuccess = state is EditTransactionScreenState.Success

    Scaffold(
        topBar = {
            val title = stringResource(
                if (isIncomeMode) CoreRString.my_income else CoreRString.my_expenses
            )
            BWTopBar(
                title = title,
                leadingIcon = painterResource(CoreRDrawable.ic_close),
                trailingIcon = if (isSuccess) painterResource(CoreRDrawable.ic_done) else null,
                onLeadingIconClick = viewModel::onCancel,
                onTrailingIconClick = viewModel::onDone,
            )
        }
    ) {
        Box(Modifier.padding(it)) {
            when (val s = state) {
                is EditTransactionScreenState.Error -> BWErrorRetryScreen(
                    error = s.error,
                    onRetry = { viewModel.onRetry(s.success) }
                )

                EditTransactionScreenState.Loading -> BWLoadingScreen()

                is EditTransactionScreenState.Success ->
                    Content(
                        transactionId = transactionId,
                        state = s,
                        onCategoryClick = { externalNavigator.navigateToSearchCategory() },
                        onAmountChanged = viewModel::onAmountChanged,
                        onCommentChanged = viewModel::onCommentChanged,
                        onDateSelected = viewModel::onDateChange,
                        onDelete = viewModel::onDelete,
                    )
            }
        }
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
private fun Content(
    transactionId: Long?,
    state: EditTransactionScreenState.Success,
    onCategoryClick: () -> Unit,
    onAmountChanged: (String) -> Unit,
    onCommentChanged: (String) -> Unit,
    onDateSelected: (Long?) -> Unit,
    onDelete: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        BWDatePicker(
            state.date.toInstant().toEpochMilli(),
            onDateSelected = {
                onDateSelected(it)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
        )
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        EditListItem(
            leadText = stringResource(CoreRString.account),
            trailText = state.account.name,
            onClick = {},
        )
        BWHorDiv()
        EditListItem(
            leadText = stringResource(CoreRString.category),
            trailText = state.category.name,
            onClick = onCategoryClick,
        )
        BWHorDiv()
        BWListItem(
            leadingText = stringResource(CoreRString.amount),
            height = 70.dp,
            trailingContent = {
                AmountField(
                    value = state.amount,
                    onValueChanged = onAmountChanged,
                )
            }
        )
        BWHorDiv()
        BWListItem(
            leadingText = stringResource(CoreRString.date),
            trailingText = state.date.format(dateFormatter),
            onClick = { showDatePicker = true },
            height = 70.dp
        )
        BWHorDiv()
        BWListItem(
            leadingText = stringResource(CoreRString.time),
            trailingText = state.date.format(timeFormatter),
            onClick = {},
            height = 70.dp
        )
        BWHorDiv()
        CommentField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 23.dp),
            value = state.comment,
            onValueChanged = onCommentChanged
        )
        BWHorDiv()
        if (transactionId != null) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(
                    text = stringResource(CoreRString.delete_expense),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                )
            }
        }
    }
}

@Composable
private fun EditListItem(
    leadText: String,
    trailText: String,
    onClick: () -> Unit,
) {
    BWListItemIcon(
        leadingText = leadText,
        trailingText = trailText,
        trailingIcon = painterResource(CoreRDrawable.ic_more),
        height = 70.dp,
        onClick = onClick,
    )
}

