package com.mdrlzy.budgetwise.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun MainScreen() {
    var currentRoute = remember { mutableStateOf("1") }

    Column(Modifier.safeDrawingPadding()) {
        Spacer(Modifier.weight(1f))
        BottomNavigation(currentRoute.value) { currentRoute.value = it }
    }
}