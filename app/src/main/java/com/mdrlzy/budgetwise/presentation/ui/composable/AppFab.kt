package com.mdrlzy.budgetwise.presentation.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mdrlzy.budgetwise.R

@Composable
fun AppFab(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onClick() }
    ) {
        Icon(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(R.drawable.ic_add),
            contentDescription = null,
            tint = Color.White,
        )
    }
//    FloatingActionButton(
//        shape = CircleShape,
//        containerColor = MaterialTheme.colorScheme.primary,
//        contentColor = Color.White,
//        elevation = FloatingActionButtonDefaults.elevation(
//            defaultElevation = 0.dp,
//            pressedElevation = 0.dp,
//            focusedElevation = 0.dp,
//            hoveredElevation = 0.dp,
//        ),
//        onClick = onClick,
//    ) {
//        Icon(
//            painter = painterResource(R.drawable.ic_add),
//            contentDescription = null,
//        )
//    }
}