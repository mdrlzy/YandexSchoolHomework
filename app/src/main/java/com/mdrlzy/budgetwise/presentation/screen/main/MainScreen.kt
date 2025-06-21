package com.mdrlzy.budgetwise.presentation.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mdrlzy.budgetwise.presentation.ui.utils.appComponent
import com.mdrlzy.budgetwise.presentation.ui.utils.keyboardAsState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.AccountScreenDestination
import com.ramcosta.composedestinations.generated.destinations.CategoriesScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ExpensesTodayScreenDestination
import com.ramcosta.composedestinations.generated.destinations.IncomeScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SplashScreenDestination
import com.ramcosta.composedestinations.rememberNavHostEngine
import kotlinx.coroutines.flow.drop

private val noBottomBarRoutes = listOf(
    SplashScreenDestination.route,
)

@Composable
fun MainScreen() {
    val engine = rememberNavHostEngine()
    val navController = engine.rememberNavController()
    val snackState = remember { SnackbarHostState() }
    val ctx = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        ctx.appComponent.networkStatus().onlineStatus
            .drop(1)
            .collect { online ->
                val visuals =
                    if (online)
                        ConnectivityOnlineSnackbarVisuals
                    else
                        ConnectivityOfflineSnackbarVisuals
                snackState.showSnackbar(visuals)
            }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: NavGraphs.main.startRoute.route


    val isKeyboardOpen by keyboardAsState()
    val bottomBarVisible = remember { mutableStateOf(false) }

    bottomBarVisible.value = currentRoute !in noBottomBarRoutes

    if (isKeyboardOpen)
        bottomBarVisible.value = false

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackState,
            ) { data ->
                val visuals = data.visuals
                when (visuals) {
                    is ConnectivityOnlineSnackbarVisuals ->
                        ConnectivityOnlineSnackbar()

                    is ConnectivityOfflineSnackbarVisuals ->
                        ConnectivityOfflineSnackbar()
                }
            }
        },
        bottomBar = {
            AnimatedBottomNavigation(navBackStackEntry, currentRoute, bottomBarVisible) {
                navController.navigate(it) {
                    popUpTo(ExpensesTodayScreenDestination.route) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    ) {
        DestinationsNavHost(
            modifier = Modifier.padding(it),
            engine = engine,
            navController = navController,
            navGraph = NavGraphs.main,
        )
    }
}