package com.mdrlzy.budgetwise.feature.categories.impl.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdrlzy.budgetwise.core.ui.CoreRString
import com.mdrlzy.budgetwise.core.ui.composable.BWErrorRetryScreen
import com.mdrlzy.budgetwise.core.ui.composable.BWHorDiv
import com.mdrlzy.budgetwise.core.ui.composable.BWListItemEmoji
import com.mdrlzy.budgetwise.core.ui.composable.BWLoadingScreen
import com.mdrlzy.budgetwise.core.ui.composable.BWTopBar
import com.mdrlzy.budgetwise.feature.categories.impl.di.CategoriesComponentHolder
import com.mdrlzy.budgetwise.feature.categories.impl.presentation.main.CategoriesScreenState
import com.mdrlzy.budgetwise.feature.categories.impl.presentation.main.FilterInput
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.orbitmvi.orbit.compose.collectAsState

@Destination<ExternalModuleGraph>
@Composable
fun SearchCategoryScreen(
    resultNavigator: ResultBackNavigator<Long>,
    isIncomeMode: Boolean,
) {
    val context = LocalContext.current
    val component =
        remember {
            CategoriesComponentHolder.provide(context)
        }

    val viewModel: SearchCategoryViewModel =
        viewModel(factory = component.searchCategoryViewModelFactory().create(isIncomeMode))
    val state by viewModel.collectAsState()

    Scaffold(
        topBar = {
            BWTopBar(
                title = stringResource(CoreRString.my_categories),
            )
        },
    ) {
        Box(Modifier.padding(it)) {
            when (state) {
                is CategoriesScreenState.Error ->
                    BWErrorRetryScreen(
                        error = (state as CategoriesScreenState.Error).error,
                    ) { viewModel.onRetry() }

                CategoriesScreenState.Loading -> BWLoadingScreen()

                is CategoriesScreenState.Success ->
                    Content(
                        state = state as CategoriesScreenState.Success,
                        onSearchQueryChange = viewModel::onFilterChange,
                        onItemClick = { id ->
                            resultNavigator.navigateBack(id)
                        }
                    )
            }
        }
    }
}

@Composable
private fun Content(
    state: CategoriesScreenState.Success,
    onSearchQueryChange: (String) -> Unit,
    onItemClick: (id: Long) -> Unit,
) {
    LazyColumn {
        stickyHeader {
            FilterInput(filter = state.filter, onSearchQueryChange = onSearchQueryChange)
        }
        items(state.filtered) {
            BWListItemEmoji(
                leadingText = it.name,
                emoji = it.emoji,
                height = 70.dp,
                onClick = { onItemClick(it.id) },
            )
            BWHorDiv()
        }
    }
}