@file:OptIn(ExperimentalAnimationApi::class)

package com.mdrlzy.budgetwise.presentation.screen.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.mdrlzy.budgetwise.R
import com.ramcosta.composedestinations.generated.destinations.AccountScreenDestination
import com.ramcosta.composedestinations.generated.destinations.CategoriesScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ExpensesTodayScreenDestination
import com.ramcosta.composedestinations.generated.destinations.IncomeScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SettingsScreenDestination

sealed class BottomNavItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: String,
) {
    data object ExpensesToday : BottomNavItem(
        R.string.bottom_nav_expenses_today,
        R.drawable.bottom_nav_expenses_today,
        ExpensesTodayScreenDestination.route,
    )

    data object Income : BottomNavItem(
        R.string.bottom_nav_income,
        R.drawable.bottom_nav_income,
        IncomeScreenDestination.route,
    )

    data object Account : BottomNavItem(
        R.string.bottom_nav_account,
        R.drawable.bottom_nav_account,
        AccountScreenDestination.route,
    )

    data object Expenses : BottomNavItem(
        R.string.bottom_nav_expenses,
        R.drawable.bottom_nav_expenses,
        CategoriesScreenDestination.route,
    )

    data object Settings : BottomNavItem(
        R.string.bottom_nav_settings,
        R.drawable.bottom_nav_settings,
        SettingsScreenDestination.route,
    )
}

private val bottomBarItems = listOf(
    BottomNavItem.ExpensesToday,
    BottomNavItem.Income,
    BottomNavItem.Account,
    BottomNavItem.Expenses,
    BottomNavItem.Settings,
)

@Composable
fun AnimatedBottomNavigation(
    currentRoute: String,
    bottomBarVisible: State<Boolean>,
    onBottomBarItemClick: (String) -> Unit,
) {
    AnimatedContent(
        targetState = bottomBarVisible.value,
        transitionSpec = {
            slideInVertically { height -> height } with
                    slideOutVertically { height -> height }
        },
    ) { expanded ->
        if (expanded)
            BottomNavigation(currentRoute, onBottomBarItemClick)
    }
}

@Composable
fun BottomNavigation(
    currentRoute: String,
    onBottomBarItemClick: (String) -> Unit,
) {
    BottomAppBar {
        bottomBarItems.forEach { item ->
            val selected = currentRoute.contains(item.route)
            NavigationBarItem(
                selected = selected,
                onClick = { onBottomBarItemClick(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = stringResource(item.title),
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.title),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (selected) FontWeight.W600 else FontWeight.W500,
                            color = if (selected)
                                MaterialTheme.colorScheme.onSurface
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.onSurface,
                        indicatorColor = MaterialTheme.colorScheme.secondary,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
            )
        }
    }
}