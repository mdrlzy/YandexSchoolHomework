package com.mdrlzy.budgetwise.presentation.screen.main

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
import com.mdrlzy.budgetwise.core.di.CoreComponentProvider
import com.mdrlzy.budgetwise.core.ui.utils.keyboardAsState
import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.navigation.TransactionsExternalNavigator
import com.mdrlzy.budgetwise.feature.transactions.impl.presentation.screen.edit.EditTransactionScreen
import com.mdrlzy.budgetwise.presentation.navigation.AnimatedBottomNavigation
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.categories.destinations.SearchCategoryScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SplashScreenDestination
import com.ramcosta.composedestinations.generated.transactions.destinations.EditTransactionScreenDestination
import com.ramcosta.composedestinations.generated.transactions.destinations.ExpensesScreenDestination
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navargs.primitives.longNavType
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.scope.resultRecipient
import kotlinx.coroutines.flow.drop

private val noBottomBarRoutes =
    listOf(
        SplashScreenDestination.route,
        SearchCategoryScreenDestination.route,
    )

@Composable
fun MainScreen() {
    val engine = rememberNavHostEngine()
    val navController = engine.rememberNavController()
    val snackState = remember { SnackbarHostState() }
    val ctx = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        (ctx.applicationContext as CoreComponentProvider)
            .provideCoreComponent()
            .networkStatus()
            .onlineStatus
            .drop(1)
            .collect { online ->
                val visuals =
                    if (online) {
                        ConnectivityOnlineSnackbarVisuals
                    } else {
                        ConnectivityOfflineSnackbarVisuals
                    }
                snackState.showSnackbar(visuals)
            }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: NavGraphs.main.startRoute.route

    val isKeyboardOpen by keyboardAsState()
    val bottomBarVisible = remember { mutableStateOf(false) }

    bottomBarVisible.value = currentRoute !in noBottomBarRoutes

    if (isKeyboardOpen) {
        bottomBarVisible.value = false
    }

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
                    popUpTo(ExpensesScreenDestination.route) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
    ) {
        DestinationsNavHost(
            modifier = Modifier.padding(it),
            engine = engine,
            navController = navController,
            navGraph = NavGraphs.main,
        ) {
            composable(EditTransactionScreenDestination) {
                val externalNavigator = remember {
                    object : TransactionsExternalNavigator {
                        override fun navigateToSearchCategory() {
                            destinationsNavigator.navigate(SearchCategoryScreenDestination)
                        }
                    }
                }

                EditTransactionScreen(
                    navigator = destinationsNavigator,
                    isIncomeMode = navArgs.isIncomeMode,
                    transactionId = navArgs.transactionId,
                    externalNavigator = externalNavigator,
                    resultRecipient = resultRecipient<SearchCategoryScreenDestination, Long>(longNavType),
                )
            }
        }
    }
}
