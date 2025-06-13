package com.mdrlzy.budgetwise.presentation.screen.expensestoday

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mdrlzy.budgetwise.R
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItemEmoji
import com.mdrlzy.budgetwise.presentation.ui.composable.AppTopBar
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.presentation.screen.main.MainNavGraph
import com.mdrlzy.budgetwise.presentation.ui.composable.AppFab
import com.mdrlzy.budgetwise.presentation.ui.composable.AppHorDiv
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.orbitmvi.orbit.compose.collectAsState

@Destination<MainNavGraph>()
@Composable
fun ExpensesTodayScreen() {
    val viewModel: ExpensesTodayViewModel = viewModel()

    val state by viewModel.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.expenses_today),
                trailingIcon = painterResource(R.drawable.ic_history)
            )
        },
        floatingActionButton = {
            AppFab {  }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                AppListItem(
                    leadingText = stringResource(R.string.all),
                    trailingText = "${state.all}${state.currency}",
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
}