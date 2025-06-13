package com.mdrlzy.budgetwise.presentation.expensestoday

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.mdrlzy.budgetwise.R
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItemEmoji
import com.mdrlzy.budgetwise.presentation.ui.composable.AppTopBar
import androidx.compose.ui.unit.dp
import com.mdrlzy.budgetwise.presentation.ui.composable.AppHorDiv
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>(start = true)
@Composable
fun ExpensesTodayScreen() {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            AppTopBar(
                title = "Расходы сегодня",
                trailingIcon = painterResource(R.drawable.ic_history)
            )

            AppListItem(
                leadingText = "Всего",
                trailingText = "436 558 ₽",
                background = MaterialTheme.colorScheme.secondary,
                height = 56.dp,
            )
            AppHorDiv()
            AppListItemEmoji(
                leadingText = "Аренда квартиры",
                trailingText = "1000 ₽",
                descText = "Дженни",
                emoji = "\uD83D\uDE0A",
                height = 70.dp,
                onClick = {}
            )
            AppHorDiv()
        }
    }
}