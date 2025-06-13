package com.mdrlzy.budgetwise.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mdrlzy.budgetwise.R
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItem
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItemIcon
import com.mdrlzy.budgetwise.presentation.ui.composable.AppTopBar
import androidx.compose.ui.unit.dp
import com.mdrlzy.budgetwise.presentation.ui.composable.AppHorDiv
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>
@Composable
fun SettingsScreen() {
    var isDarkTheme by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.settings)
            )
        }
    ) {
        Column(Modifier.padding(it)) {
            AppListItem(
                leadingText = stringResource(R.string.dark_theme),
                height = 56.dp,
                trailingContent = {
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { isDarkTheme = it }
                    )
                }
            )
            AppHorDiv()
            SettingsListItem(stringResource(R.string.sounds)) { }
            AppHorDiv()
            SettingsListItem(stringResource(R.string.code_password)) { }
            AppHorDiv()
            SettingsListItem(stringResource(R.string.language)) { }
            AppHorDiv()
            SettingsListItem(stringResource(R.string.about_app)) { }
            AppHorDiv()
        }
    }
}

@Composable
private fun SettingsListItem(
    text: String,
    onClick: () -> Unit
) {
    AppListItemIcon(
        leadingText = text,
        height = 56.dp,
        trailingIcon = painterResource(R.drawable.ic_arrow_right),
    )
}