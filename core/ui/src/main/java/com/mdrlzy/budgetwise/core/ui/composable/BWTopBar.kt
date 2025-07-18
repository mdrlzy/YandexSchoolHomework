@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.budgetwise.core.ui.composable

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun BWTopBar(
    title: String,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    onLeadingIconClick: (() -> Unit)? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.primary
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            leadingIcon?.let {
                IconButton(
                    onClick = {
                        onLeadingIconClick?.invoke()
                    },
                ) {
                    Icon(
                        painter = leadingIcon,
                        contentDescription = null,
                    )
                }
            }
        },
        actions = {
            trailingIcon?.let {
                IconButton(
                    onClick = {
                        onTrailingIconClick?.invoke()
                    },
                ) {
                    Icon(
                        painter = trailingIcon,
                        contentDescription = null,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor),
    )
}
