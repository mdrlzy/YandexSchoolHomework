@file:OptIn(ExperimentalAnimationApi::class)

package com.mdrlzy.budgetwise.presentation.navigation

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
import androidx.navigation.NavBackStackEntry
import com.mdrlzy.budgetwise.core.ui.CoreRDrawable
import com.mdrlzy.budgetwise.core.ui.CoreRString
import com.ramcosta.composedestinations.generated.account.destinations.AccountScreenDestination
import com.ramcosta.composedestinations.generated.categories.destinations.CategoriesScreenDestination
import com.ramcosta.composedestinations.generated.settings.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.generated.transactions.destinations.AnalyzeTransactionsScreenDestination
import com.ramcosta.composedestinations.generated.transactions.destinations.EditTransactionScreenDestination
import com.ramcosta.composedestinations.generated.transactions.destinations.ExpensesScreenDestination
import com.ramcosta.composedestinations.generated.transactions.destinations.IncomeScreenDestination
import com.ramcosta.composedestinations.generated.transactions.destinations.TransactionHistoryScreenDestination

sealed class BottomNavItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: String,
) {
    data object Expenses : BottomNavItem(
        CoreRString.bottom_nav_expenses_today,
        CoreRDrawable.bottom_nav_expenses_today,
        ExpensesScreenDestination.route,
    )

    data object Income : BottomNavItem(
        CoreRString.bottom_nav_income,
        CoreRDrawable.bottom_nav_income,
        IncomeScreenDestination.route,
    )

    data object Account : BottomNavItem(
        CoreRString.bottom_nav_account,
        CoreRDrawable.bottom_nav_account,
        AccountScreenDestination.route,
    )

    data object Categories : BottomNavItem(
        CoreRString.bottom_nav_expenses,
        CoreRDrawable.bottom_nav_expenses,
        CategoriesScreenDestination.route,
    )

    data object Settings : BottomNavItem(
        CoreRString.bottom_nav_settings,
        CoreRDrawable.bottom_nav_settings,
        SettingsScreenDestination.route,
    )
}

private val bottomBarItems =
    listOf(
        BottomNavItem.Expenses,
        BottomNavItem.Income,
        BottomNavItem.Account,
        BottomNavItem.Categories,
        BottomNavItem.Settings,
    )

@Composable
fun AnimatedBottomNavigation(
    navBackStackEntry: NavBackStackEntry?,
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
        if (expanded) {
            BottomNavigation(navBackStackEntry, currentRoute, onBottomBarItemClick)
        }
    }
}

@Composable
fun BottomNavigation(
    navBackStackEntry: NavBackStackEntry?,
    currentRoute: String,
    onBottomBarItemClick: (String) -> Unit,
) {
    BottomAppBar {
        bottomBarItems.forEach { item ->
            // bottom bar is visible in TransactionHistoryScreen
            // and can be opened from two different screens: Income and Expenses
            val selected = when (currentRoute) {
                TransactionHistoryScreenDestination.route -> {
                    val isIncome = navBackStackEntry?.arguments?.getBoolean("isIncomeMode")
                    when (item) {
                        BottomNavItem.Expenses -> isIncome?.not() ?: false
                        BottomNavItem.Income -> isIncome ?: false
                        else -> false
                    }
                }
                EditTransactionScreenDestination.route -> {
                    val isIncome = navBackStackEntry?.arguments?.getBoolean("isIncomeMode")
                    when (item) {
                        BottomNavItem.Expenses -> isIncome?.not() ?: false
                        BottomNavItem.Income -> isIncome ?: false
                        else -> false
                    }
                }
                AnalyzeTransactionsScreenDestination.route -> {
                    val isIncome = navBackStackEntry?.arguments?.getBoolean("isIncomeMode")
                    when (item) {
                        BottomNavItem.Expenses -> isIncome?.not() ?: false
                        BottomNavItem.Income -> isIncome ?: false
                        else -> false
                    }
                }
                else -> currentRoute.contains(item.route)
            }
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
                        style =
                            MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (selected) FontWeight.W600 else FontWeight.W500,
                                color =
                                    if (selected) {
                                        MaterialTheme.colorScheme.onSurface
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                            ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
