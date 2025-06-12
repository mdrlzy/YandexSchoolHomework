package com.mdrlzy.budgetwise.presentation.income

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mdrlzy.budgetwise.R
import com.mdrlzy.budgetwise.presentation.ui.composable.AppFab
import com.mdrlzy.budgetwise.presentation.ui.composable.AppHorDiv
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItem
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItemIcon
import com.mdrlzy.budgetwise.presentation.ui.composable.AppTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>
@Composable
fun IncomeScreen() {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.income_today),
                trailingIcon = painterResource(R.drawable.ic_history)
            )
        },
        floatingActionButton = {
            AppFab {  }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            AppListItem(
                leadingText = stringResource(R.string.all),
                trailingText = "600 000 ₽",
                background = MaterialTheme.colorScheme.secondary,
                height = 56.dp,
            )
            AppHorDiv()
            AppListItemIcon(
                leadingText = "Зарплата",
                trailingText = "500 000 ₽",
                height = 70.dp,
                trailingIcon = painterResource(R.drawable.ic_more),
            )
            AppHorDiv()
            AppListItemIcon(
                leadingText = "Подработка",
                trailingText = "100 000 ₽",
                height = 70.dp,
                trailingIcon = painterResource(R.drawable.ic_more),
            )
            AppHorDiv()
        }
    }
}