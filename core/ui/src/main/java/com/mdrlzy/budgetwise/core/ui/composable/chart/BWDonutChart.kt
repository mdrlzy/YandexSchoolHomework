package com.mdrlzy.budgetwise.core.ui.composable.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BWDonutChart(
    modifier: Modifier = Modifier,
    data: List<ChartDataEntry>,
    strokeWidth: Dp = 40.dp,
    circleRadius: Dp = 100.dp,
) {
    val total = data.map { it.value }.sum()
    if (total == 0f) return

    val angles = data.map { it.value / total * 360f }

    val colors = data.map { it.color }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val radiusPx = with(LocalDensity.current) { circleRadius.toPx() }
        val strokePx = with(LocalDensity.current) { strokeWidth.toPx() }

        Canvas(
            modifier = Modifier
                .size((circleRadius + strokeWidth) * 2)
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val rect = Rect(
                center = center,
                radius = radiusPx
            )
            val stroke = Stroke(width = strokePx, cap = StrokeCap.Butt)
            var startAngle = -90f

            angles.forEachIndexed { index, sweepAngle ->
                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = stroke,
                    topLeft = rect.topLeft,
                    size = rect.size
                )
                startAngle += sweepAngle
            }
        }

        Column(
            modifier = Modifier
                .size(circleRadius * 2)
                .padding(strokeWidth / 2),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            data.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(5.dp).background(it.color).clip(CircleShape))
                    Spacer(Modifier.width(3.dp))
                    Text(text = it.label ?: "", fontSize = 7.sp)
                }
            }
        }
    }
}