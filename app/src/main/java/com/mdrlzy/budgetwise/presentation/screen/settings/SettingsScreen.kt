package com.mdrlzy.budgetwise.presentation.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mdrlzy.budgetwise.R
import com.mdrlzy.budgetwise.core.ui.composable.BWListItem
import com.mdrlzy.budgetwise.core.ui.composable.BWListItemIcon
import com.mdrlzy.budgetwise.core.ui.composable.BWTopBar
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.presentation.screen.main.MainNavGraph
import com.mdrlzy.budgetwise.core.ui.composable.BWHorDiv
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState

@Destination<MainNavGraph>
@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = viewModel()
    val state by viewModel.collectAsState()

    Scaffold(
        topBar = {
            BWTopBar(
                title = stringResource(R.string.settings)
            )
        }
    ) {
        Column(Modifier.padding(it)) {
            BWListItem(
                leadingText = stringResource(R.string.dark_theme),
                height = 56.dp,
                trailingContent = {
                    Switch(
                        checked = state.isDarkTheme,
                        onCheckedChange = viewModel::onToggleDarkTheme,
                    )
                }
            )
            BWHorDiv()
            SettingsListItem(stringResource(R.string.sounds)) { }
            BWHorDiv()
            SettingsListItem(stringResource(R.string.code_password)) { }
            BWHorDiv()
            SettingsListItem(stringResource(R.string.language)) { }
            BWHorDiv()
            SettingsListItem(stringResource(R.string.about_app)) { }
            BWHorDiv()
        }
    }
}

@Composable
private fun SettingsListItem(
    text: String,
    onClick: () -> Unit
) {
    BWListItemIcon(
        leadingText = text,
        height = 56.dp,
        trailingIcon = painterResource(R.drawable.ic_arrow_right),
        onClick = onClick,
    )
}