package com.mdrlzy.budgetwise.core.ui.composable.chart

import androidx.compose.ui.graphics.Color

data class ChartDataEntry(
    val value: Float,
    val color: Color,
    val label: String? = null,
)