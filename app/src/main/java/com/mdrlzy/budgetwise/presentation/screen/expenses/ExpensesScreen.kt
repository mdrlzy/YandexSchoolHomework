@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.budgetwise.presentation.screen.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.R
import com.mdrlzy.budgetwise.presentation.screen.main.MainNavGraph
import com.mdrlzy.budgetwise.presentation.ui.composable.AppHorDiv
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItem
import com.mdrlzy.budgetwise.presentation.ui.composable.AppListItemEmoji
import com.mdrlzy.budgetwise.presentation.ui.composable.AppTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.orbitmvi.orbit.compose.collectAsState

@Destination<MainNavGraph>
@Composable
fun ExpensesScreen() {
    val viewModel: ExpensesViewModel = viewModel()
    val state by viewModel.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.my_expenses)
            )
        }
    ) {
        LazyColumn(Modifier.padding(it)) {
            item {
                Row(
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                        value = state.searchQuery,
                        onValueChange = {
                            viewModel.onSearchQueryChange(it)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search,
                        ),
                    ) { innerTextField ->
                        val interactionSource = remember { MutableInteractionSource() }
                        TextFieldDefaults.DecorationBox(
                            value = state.searchQuery,
                            visualTransformation = VisualTransformation.None,
                            innerTextField = innerTextField,
                            singleLine = true,
                            enabled = true,
                            interactionSource = interactionSource,
                            contentPadding = PaddingValues(0.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.find_expense),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                )
                            },
                        )
                    }
                    Icon(
                        modifier = Modifier.padding(end = 16.dp),
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                }
                AppHorDiv()
            }
            items(state.categories) {
                AppListItemEmoji(
                    leadingText = it.name,
                    emoji = it.emoji,
                    height = 70.dp,
                    onClick = {}
                )
                AppHorDiv()
            }
        }
    }
}