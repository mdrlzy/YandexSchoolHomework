package com.mdrlzy.budgetwise.presentation.screen.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mdrlzy.budgetwise.core.di.CoreComponentProvider
import com.mdrlzy.budgetwise.core.ui.theme.BudgetwiseTheme
import com.mdrlzy.budgetwise.core.ui.theme.Primary
import com.mdrlzy.budgetwise.core.ui.theme.Surface
import com.mdrlzy.budgetwise.presentation.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Primary.toArgb(), Primary.toArgb()),
            navigationBarStyle = SystemBarStyle.light(Surface.toArgb(), Surface.toArgb()),
        )
        val prefs = (applicationContext as CoreComponentProvider).provideCoreComponent().prefs()
        setContent {
            val isDarkTheme by prefs.isDarkTheme.collectAsStateWithLifecycle()
            BudgetwiseTheme(darkTheme = isDarkTheme) {
                MainScreen()
            }
        }
    }
}
