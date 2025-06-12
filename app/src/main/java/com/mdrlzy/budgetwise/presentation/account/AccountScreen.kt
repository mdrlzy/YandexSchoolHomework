package com.mdrlzy.budgetwise.presentation.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mdrlzy.budgetwise.R
import com.mdrlzy.budgetwise.presentation.ui.composable.AppHorDiv
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItemEmoji
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItemMore
import com.mdrlzy.budgetwise.presentation.ui.composable.AppTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>
@Composable
fun AccountScreen() {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.my_account),
                trailingIcon = painterResource(R.drawable.ic_edit)
            )
        },
    ) {
        Column(modifier = Modifier.padding(it)) {
            AppListItemEmoji(
                leadingText = stringResource(R.string.balance),
                trailingText = "-670 000 ₽",
                background = MaterialTheme.colorScheme.secondary,
                height = 56.dp,
                emoji = "\uD83D\uDCB0",
                emojiBackground = Color.White,
            )
            AppHorDiv()
            AppListItemMore(
                leadingText = stringResource(R.string.currency),
                trailingText = "₽",
                background = MaterialTheme.colorScheme.secondary,
                height = 56.dp,
            )
        }
    }
}