package com.mdrlzy.budgetwise.presentation.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mdrlzy.budgetwise.presentation.ui.utils.keyboardAsState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.AccountScreenDestination
import com.ramcosta.composedestinations.generated.destinations.CategoriesScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ExpensesTodayScreenDestination
import com.ramcosta.composedestinations.generated.destinations.IncomeScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.rememberNavHostEngine

private val bottomBarVisibleRoutes = listOf(
    ExpensesTodayScreenDestination.route,
    IncomeScreenDestination.route,
    AccountScreenDestination.route,
    CategoriesScreenDestination.route,
    SettingsScreenDestination.route,
)

@Composable
fun MainScreen() {
    val engine = rememberNavHostEngine()
    val navController = engine.rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: NavGraphs.main.startRoute.route

    val isKeyboardOpen by keyboardAsState()
    val bottomBarVisible = remember { mutableStateOf(false) }

    bottomBarVisible.value = currentRoute in bottomBarVisibleRoutes

    if (isKeyboardOpen)
        bottomBarVisible.value = false

    Column(Modifier.safeDrawingPadding()) {
        DestinationsNavHost(
            modifier = Modifier.weight(1f),
            engine = engine,
            navController = navController,
            navGraph = NavGraphs.main,
        )
        AnimatedBottomNavigation(currentRoute, bottomBarVisible) {
            navController.navigate(it)  {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(ExpensesTodayScreenDestination.route) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    }
}