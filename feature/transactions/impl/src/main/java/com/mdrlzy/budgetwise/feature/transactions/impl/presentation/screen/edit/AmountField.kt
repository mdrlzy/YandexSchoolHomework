@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.edit

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AmountField(
    value: String,
    onValueChanged: (String) -> Unit
) {
    BasicTextField(
        modifier = Modifier,
        value = value,
        onValueChange = {
            onValueChanged(it)
        },
        keyboardOptions =
            KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number,
            ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            textAlign = TextAlign.End
        ),
        maxLines = 1,
    ) { innerTextField ->
        val interactionSource = remember { MutableInteractionSource() }
        TextFieldDefaults.DecorationBox(
            value = value,
            visualTransformation = VisualTransformation.None,
            innerTextField = innerTextField,
            singleLine = true,
            enabled = true,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(0.dp),
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
        )
    }
}