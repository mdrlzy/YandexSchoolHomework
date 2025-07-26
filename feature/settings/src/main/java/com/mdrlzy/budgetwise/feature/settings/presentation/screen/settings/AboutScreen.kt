package com.mdrlzy.budgetwise.feature.settings.presentation.screen.settings

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mdrlzy.budgetwise.core.di.CoreComponentProvider
import com.mdrlzy.budgetwise.core.ui.CoreRDrawable
import com.mdrlzy.budgetwise.core.ui.CoreRString
import com.mdrlzy.budgetwise.core.ui.composable.BWHorDiv
import com.mdrlzy.budgetwise.core.ui.composable.BWListItem
import com.mdrlzy.budgetwise.core.ui.composable.BWTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

@Destination<ExternalModuleGraph>
@Composable
fun AboutScreen(
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val coreComponent = remember {
        (context.applicationContext as CoreComponentProvider).provideCoreComponent()
    }

    Scaffold(
        topBar = {
            BWTopBar(
                title = stringResource(CoreRString.about_app),
                leadingIcon = painterResource(CoreRDrawable.ic_back),
                onLeadingIconClick = {
                    navigator.popBackStack()
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            BWListItem(
                leadingText = stringResource(CoreRString.version),
                trailingText = coreComponent.buildConfigFields().versionName,
                height = 56.dp
            )
            BWHorDiv()
            BWListItem(
                leadingText = stringResource(CoreRString.last_update),
                trailingText = "Суббота 6:00",
                height = 56.dp
            )
        }
    }
}

private fun getLastUpdateTimeOrNull(context: Context): OffsetDateTime? {
    return try {
        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        OffsetDateTime.ofInstant(
            Instant.ofEpochMilli(info.lastUpdateTime),
            ZoneId.systemDefault()
        )
    } catch (e: Exception) {
        null
    }
}