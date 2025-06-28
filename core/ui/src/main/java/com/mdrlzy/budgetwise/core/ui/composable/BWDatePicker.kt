@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.budgetwise.core.ui.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mdrlzy.budgetwise.ui.R

@Composable
fun BWDatePicker(
    selectedDate: Long,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            DatePickerButtons(
                onDateSelected = onDateSelected,
                onDismiss = onDismiss,
                datePickerState = datePickerState,
            )
        },
        colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.secondary),
    ) {
        DatePicker(
            state = datePickerState,
            title = null,
            headline = null,
            showModeToggle = false,
            colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.secondary),
        )
    }
}

@Composable
private fun DatePickerButtons(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    datePickerState: DatePickerState,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 12.dp),
    ) {
        Spacer(Modifier.weight(1f))
        TextButton(
            onClick = { onDismiss() },
        ) {
            Text(
                text = stringResource(R.string.cancel),
                style = MaterialTheme.typography.labelLarge,
            )
        }
        TextButton(
            onClick = { onDateSelected(datePickerState.selectedDateMillis) },
        ) {
            Text(
                text = stringResource(R.string.ok),
                style =
                    MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.W700,
                    ),
            )
        }
    }
}
