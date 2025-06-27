@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.budgetwise.feature.categories.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.core.di.CoreComponentProvider
import com.mdrlzy.budgetwise.core.ui.CoreRDrawable
import com.mdrlzy.budgetwise.core.ui.CoreRString
import com.mdrlzy.budgetwise.core.ui.composable.BWHorDiv
import com.mdrlzy.budgetwise.core.ui.composable.BWListItemEmoji
import com.mdrlzy.budgetwise.core.ui.composable.BWTopBar
import com.mdrlzy.budgetwise.core.ui.composable.BWErrorRetryScreen
import com.mdrlzy.budgetwise.core.ui.composable.BWLoadingScreen
import com.mdrlzy.budgetwise.core.ui.composable.ListenActiveScreenEffect
import com.mdrlzy.budgetwise.feature.categories.di.DaggerCategoriesComponent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import org.orbitmvi.orbit.compose.collectAsState

@Destination<ExternalModuleGraph>
@Composable
fun CategoriesScreen() {
    val context = LocalContext.current
    val component = remember {
        val coreComponent =
            (context.applicationContext as CoreComponentProvider).provideCoreComponent()
        DaggerCategoriesComponent.builder().coreComponent(coreComponent).build()
    }
    val viewModel: CategoriesViewModel =
        viewModel(factory = component.categoriesViewModelFactory())
    val state by viewModel.collectAsState()

    ListenActiveScreenEffect(
        onActive = viewModel::onActive,
        onInactive = viewModel::onInactive,
    )

    Scaffold(
        topBar = {
            BWTopBar(
                title = stringResource(CoreRString.my_expenses)
            )
        }
    ) {
        Box(Modifier.padding(it)) {
            when (state) {
                is CategoriesScreenState.Error -> BWErrorRetryScreen(
                    error = (state as CategoriesScreenState.Error).error
                ) { viewModel.onRetry() }

                CategoriesScreenState.Loading -> BWLoadingScreen()

                is CategoriesScreenState.Success ->
                    Content(
                        state = state as CategoriesScreenState.Success,
                        onSearchQueryChange = viewModel::onSearchQueryChange
                    )
            }
        }
    }
}

@Composable
private fun Content(
    state: CategoriesScreenState.Success,
    onSearchQueryChange: (String) -> Unit,
) {
    LazyColumn {
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
                        onSearchQueryChange(it)
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
                                text = stringResource(CoreRString.find_expense),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                            )
                        },
                    )
                }
                Icon(
                    modifier = Modifier.padding(end = 16.dp),
                    painter = painterResource(CoreRDrawable.ic_search),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
            BWHorDiv()
        }
        items(state.categories) {
            BWListItemEmoji(
                leadingText = it.name,
                emoji = it.emoji,
                height = 70.dp,
                onClick = {}
            )
            BWHorDiv()
        }
    }
}