@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.edit

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mdrlzy.budgetwise.core.ui.CoreRString

@Composable
fun CommentField(
    modifier: Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            onValueChanged(it)
        },
        keyboardOptions =
            KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
            ),
        textStyle = MaterialTheme.typography.bodyLarge,
    ) { innerTextField ->
        val interactionSource = remember { MutableInteractionSource() }
        TextFieldDefaults.DecorationBox(
            value = value,
            visualTransformation = VisualTransformation.None,
            innerTextField = innerTextField,
            singleLine = false,
            enabled = true,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(0.dp),
            placeholder = {
                if (value.isEmpty()) {
                    Text(
                        text = stringResource(CoreRString.comment),
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
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