package com.mdrlzy.budgetwise.core.ui.composable.chart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun BWBarChart(
    modifier: Modifier = Modifier,
    height: Dp,
    data: List<ChartDataEntry>,
    barWidthDp: Dp = 6.dp,
    spaceDp: Dp = 8.dp,
    labelColor: Color = Color.Black
) {
    val maxValue = (data.maxOfOrNull { it.value } ?: 1f).takeIf { it > 0 } ?: 1f
    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    val barWidthPx = with(density) { barWidthDp.toPx() }
    val spacePx = with(density) { spaceDp.toPx() }
    val totalWidth = (barWidthPx + spacePx) * data.size

    val animatables = remember(data) {
        data.map { Animatable(0f) }
    }

    LaunchedEffect(data) {
        animatables.forEachIndexed { index, animatable ->
            launch {
                animatable.animateTo(
                    targetValue = data[index].value,
                    animationSpec = tween(durationMillis = 600, delayMillis = index * 50)
                )
            }
        }
    }

    Box(
        modifier = modifier
            .height(height)
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        Canvas(
            modifier = Modifier
                .width(with(density) { totalWidth.toDp() })
                .fillMaxHeight()
        ) {
            data.forEachIndexed { index, barData ->
                val animatedValue = animatables[index].value
                var barHeight = (animatedValue / maxValue) * size.height
                val left = index * (barWidthPx + spacePx)
                var top = size.height - barHeight
                val cornerRadiusPx = 100.dp.toPx()
                var rectColor = barData.color
                val zeroBarHeight = 4.dp.toPx()

                if (animatedValue == 0f) {
                    top = size.height - zeroBarHeight
                    barHeight = zeroBarHeight
                    rectColor = Color.Gray
                }

                drawRoundRect(
                    color = rectColor,
                    topLeft = Offset(left, top),
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                )

                val labelOffsetPx = 10.dp.toPx()
                val labelSizePx = 9.sp.toPx()
                barData.label?.let {
                    drawContext.canvas.nativeCanvas.drawText(
                        barData.label,
                        left + barWidthPx / 2,
                        size.height + labelOffsetPx,
                        android.graphics.Paint().apply {
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = labelSizePx
                            color = labelColor.toArgb()
                        }
                    )
                }
            }
        }
    }
}