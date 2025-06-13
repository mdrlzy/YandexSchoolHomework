package com.mdrlzy.budgetwise.presentation.screen.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.mdrlzy.budgetwise.presentation.ui.theme.BudgetwiseTheme
import com.mdrlzy.budgetwise.presentation.ui.theme.Primary
import com.mdrlzy.budgetwise.presentation.ui.theme.Surface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Primary.toArgb(), Primary.toArgb()),
            navigationBarStyle = SystemBarStyle.light(Surface.toArgb(), Surface.toArgb()),
        )
        setContent {
            BudgetwiseTheme {
                MainScreen()
            }
        }
    }
}