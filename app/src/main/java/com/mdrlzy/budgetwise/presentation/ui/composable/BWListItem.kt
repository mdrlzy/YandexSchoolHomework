@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.budgetwise.presentation.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BWListItem(
    leadingText: String,
    trailingText: String? = null,
    leadDescText: String? = null,
    trailDescText: String? = null,
    background: Color = Color.Transparent,
    height: Dp,
    onClick: (() -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .let { mod ->
                onClick?.let {
                    mod.clickable { onClick() }
                } ?: mod
            }
            .height(height)
            .background(background)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingContent?.let {
            leadingContent()
        }
        Column {
            Text(
                text = leadingText,
                style = MaterialTheme.typography.bodyLarge,
            )
            leadDescText?.let {
                Text(
                    text = leadDescText,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        Spacer(Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End) {
            trailingText?.let {
                Text(
                    text = trailingText,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            trailDescText?.let {
                Text(
                    text = trailDescText,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        trailingContent?.let {
            trailingContent()
        }
    }
}

@Composable
fun BWListItemEmoji(
    leadingText: String,
    trailingText: String? = null,
    leadDescText: String? = null,
    trailDescText: String? = null,
    background: Color = Color.Transparent,
    height: Dp,
    onClick: (() -> Unit)? = null,
    emoji: String = ":)",
    emojiBackground: Color = Color(0xFFD4FAE6),
    trailingIcon: Painter? = null,
) {
    BWListItem(
        leadingText = leadingText,
        trailingText = trailingText,
        leadDescText = leadDescText,
        trailDescText = trailDescText,
        leadingContent = {
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
                    .background(emojiBackground, shape = CircleShape)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = emoji,
                    fontSize = 13.5.sp,
                )
            }
        },
        trailingContent = {
            trailingIcon?.let {
                Icon(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp),
                    painter = trailingIcon,
                    contentDescription = null,
                )
            }
        },
        background = background,
        height = height,
        onClick = onClick,
    )
}

@Composable
fun BWListItemIcon(
    leadingText: String,
    trailingText: String? = null,
    descText: String? = null,
    background: Color = Color.Transparent,
    trailingIcon: Painter,
    height: Dp,
    onClick: (() -> Unit)? = null,
) {
    BWListItem(
        leadingText = leadingText,
        trailingText = trailingText,
        leadDescText = descText,
        trailingContent = {
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp),
                painter = trailingIcon,
                contentDescription = null,
            )
        },
        background = background,
        height = height,
        onClick = onClick,
    )
}