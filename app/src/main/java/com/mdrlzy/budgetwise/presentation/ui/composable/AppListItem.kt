@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.budgetwise.presentation.ui.composable

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mdrlzy.budgetwise.R

@Composable
fun AppListItem(
    leadingText: String,
    trailingText: String? = null,
    descText: String? = null,
    background: Color = Color.Transparent,
    height: Dp,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier
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
            descText?.let {
                Text(
                    text = descText,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        Spacer(Modifier.weight(1f))
        trailingText?.let {
            Text(
                text = trailingText,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        trailingContent?.let {
            trailingContent()
        }
    }
}

@Composable
fun AppListItemEmoji(
    leadingText: String,
    trailingText: String? = null,
    descText: String? = null,
    height: Dp,
    emoji: String = ":)",
    background: Color = Color.Transparent,
    emojiBackground: Color = Color(0xFFD4FAE6),
) {
    AppListItem(
        leadingText = leadingText,
        trailingText = trailingText,
        descText = descText,
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
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp),
                painter = painterResource(R.drawable.ic_more),
                contentDescription = null,
            )
        },
        background = background,
        height = height,
    )
}

@Composable
fun AppListItemMore(
    leadingText: String,
    trailingText: String? = null,
    descText: String? = null,
    background: Color = Color.Transparent,
    height: Dp,
) {
    AppListItem(
        leadingText = leadingText,
        trailingText = trailingText,
        descText = descText,
        trailingContent = {
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp),
                painter = painterResource(R.drawable.ic_more),
                contentDescription = null,
            )
        },
        background = background,
        height = height,
    )
}