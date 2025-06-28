package com.mdrlzy.budgetwise.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.core.ui.CoreRDrawable
import com.mdrlzy.budgetwise.core.ui.CoreRString
import com.mdrlzy.budgetwise.core.ui.composable.BWHorDiv
import com.mdrlzy.budgetwise.core.ui.composable.BWListItem
import com.mdrlzy.budgetwise.core.ui.composable.BWListItemIcon
import com.mdrlzy.budgetwise.core.ui.composable.BWTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import org.orbitmvi.orbit.compose.collectAsState

@Destination<ExternalModuleGraph>
@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = viewModel()
    val state by viewModel.collectAsState()

    Scaffold(
        topBar = {
            BWTopBar(
                title = stringResource(CoreRString.settings),
            )
        },
    ) {
        Column(Modifier.padding(it)) {
            BWListItem(
                leadingText = stringResource(CoreRString.dark_theme),
                height = 56.dp,
                trailingContent = {
                    Switch(
                        checked = state.isDarkTheme,
                        onCheckedChange = viewModel::onToggleDarkTheme,
                    )
                },
            )
            BWHorDiv()
            SettingsListItem(stringResource(CoreRString.sounds)) { }
            BWHorDiv()
            SettingsListItem(stringResource(CoreRString.code_password)) { }
            BWHorDiv()
            SettingsListItem(stringResource(CoreRString.language)) { }
            BWHorDiv()
            SettingsListItem(stringResource(CoreRString.about_app)) { }
            BWHorDiv()
        }
    }
}

@Composable
private fun SettingsListItem(
    text: String,
    onClick: () -> Unit,
) {
    BWListItemIcon(
        leadingText = text,
        height = 56.dp,
        trailingIcon = painterResource(CoreRDrawable.ic_arrow_right),
        onClick = onClick,
    )
}
