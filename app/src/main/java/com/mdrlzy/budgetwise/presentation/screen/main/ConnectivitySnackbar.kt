package com.mdrlzy.budgetwise.presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mdrlzy.budgetwise.core.ui.CoreRString

object ConnectivityOnlineSnackbarVisuals : SnackbarVisuals {
    override val actionLabel = ""
    override val duration = SnackbarDuration.Short
    override val message = ""
    override val withDismissAction = true
}

object ConnectivityOfflineSnackbarVisuals : SnackbarVisuals {
    override val actionLabel = ""
    override val duration = SnackbarDuration.Short
    override val message = ""
    override val withDismissAction = true
}

@Composable
fun ConnectivityOnlineSnackbar() {
    ConnectivityContent(
        onlineOrOffline = stringResource(CoreRString.online),
        desc = stringResource(CoreRString.your_connection_is_back),
        backgroundColor = Color(0xFFECFDF3),
        dotColor = Color(0xFF17B26A),
        textColor = Color(0xFF067647),
        borderColor = Color(0xFFABEFC6),
    )
}

@Composable
fun ConnectivityOfflineSnackbar() {
    ConnectivityContent(
        onlineOrOffline = stringResource(CoreRString.offline),
        desc = stringResource(CoreRString.your_network_is_offline),
        backgroundColor = Color(0xFFFEF3F2),
        dotColor = Color(0xFFFECDCA),
        textColor = Color(0xFFB42318),
        borderColor = Color(0xFFFECDCA),
    )
}

@Composable
private fun ConnectivityContent(
    onlineOrOffline: String,
    desc: String,
    backgroundColor: Color,
    dotColor: Color,
    textColor: Color,
    borderColor: Color,
) {
    Row(
        modifier =
            Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 15.dp,
                )
                .fillMaxWidth()
                .background(backgroundColor, CircleShape)
                .border(1.dp, borderColor, CircleShape),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier =
                Modifier
                    .padding(start = 4.dp, top = 4.dp, bottom = 4.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, borderColor, CircleShape),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .padding(start = 7.dp, top = 8.dp, bottom = 8.dp)
                        .size(6.dp)
                        .background(dotColor, CircleShape),
            )
            Text(
                modifier =
                    Modifier.padding(
                        start = 4.dp,
                        end = 8.dp,
                        top = 2.dp,
                        bottom = 2.dp,
                    ),
                text = onlineOrOffline,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = textColor,
            )
        }
        Text(
            modifier =
                Modifier.padding(
                    start = 6.dp,
                    top = 2.dp,
                    bottom = 2.dp,
                    end = 7.dp,
                ),
            text = desc,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = textColor,
        )
    }
}
