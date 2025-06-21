package com.mdrlzy.budgetwise.presentation.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mdrlzy.budgetwise.R
import com.mdrlzy.budgetwise.domain.exception.NoInternetException

@Composable
fun BWErrorRetryScreen(error: Throwable? = null, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val info = if (error is NoInternetException) {
                stringResource(R.string.no_internet)
            } else {
                stringResource(R.string.something_went_wrong)
            }
            Text(
                text = info,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(6.dp))
            Button(onClick = onRetry) {
                Text(
                    text = stringResource(R.string.retry),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}