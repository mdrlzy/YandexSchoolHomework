package com.mdrlzy.budgetwise.presentation.screen.splash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mdrlzy.budgetwise.app.R
import com.mdrlzy.budgetwise.presentation.navigation.MainNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.SplashScreenDestination
import com.ramcosta.composedestinations.generated.transactions.destinations.ExpensesScreenDestination
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<MainNavGraph>(start = true)
@Composable
fun SplashScreen(navController: NavController) {
    val viewModel: SplashViewModel = viewModel()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
    val progress by animateLottieCompositionAsState(composition, speed = 2f)

    viewModel.collectSideEffect { effect ->
        when (effect) {
            SplashScreenEffect.NavigateNext -> {
                val route = navController.currentBackStackEntry?.destination?.route ?: ""
                if (route == SplashScreenDestination.route) {
                    navController.navigate(ExpensesScreenDestination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    LottieAnimation(
        modifier = Modifier.fillMaxSize(),
        composition = composition,
        progress = { progress },
    )
    LaunchedEffect(progress) {
        if (progress == 1f) {
            viewModel.onAnimationFinish()
        }
    }
}
