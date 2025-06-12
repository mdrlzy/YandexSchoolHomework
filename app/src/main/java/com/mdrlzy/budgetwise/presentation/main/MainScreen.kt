package com.mdrlzy.budgetwise.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.rememberNavHostEngine

@Composable
fun MainScreen() {
    val engine = rememberNavHostEngine()
    val navController = engine.rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: NavGraphs.root.startRoute.route


    Column(Modifier.safeDrawingPadding()) {
        DestinationsNavHost(
            modifier = Modifier.weight(1f),
            engine = engine,
            navController = navController,
            navGraph = NavGraphs.root,
        )
        BottomNavigation(currentRoute) {
            navController.navigate(it)  {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
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