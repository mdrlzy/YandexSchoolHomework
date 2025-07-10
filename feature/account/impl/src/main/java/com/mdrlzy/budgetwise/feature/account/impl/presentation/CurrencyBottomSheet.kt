@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.budgetwise.feature.account.impl.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mdrlzy.budgetwise.core.domain.model.Currency
import com.mdrlzy.budgetwise.core.ui.CoreRDrawable
import com.mdrlzy.budgetwise.core.ui.CoreRString
import com.mdrlzy.budgetwise.core.ui.composable.BWListItem

private val Background = Color(0xFFF7F2FA)

@Composable
fun CurrencyBottomSheet(
    sheetState: SheetState,
    onSelect: (Currency) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { DragHandle() },
        containerColor = Color.White,
    ) {
        Content(onSelect, onDismiss)
    }
}

private class Item(
    @DrawableRes
    val icon: Int,
    @StringRes
    val title: Int,
    val symbol: Currency,
)

private val items = listOf(
    Item(
        CoreRDrawable.ic_curr_ruble,
        CoreRString.account_symbol_title_rub,
        Currency.RUB
    ),
    Item(
        CoreRDrawable.ic_curr_dollar,
        CoreRString.account_symbol_title_usd,
        Currency.USD
    ),
    Item(
        CoreRDrawable.ic_curr_euro,
        CoreRString.account_symbol_title_eur,
        Currency.EUR
    )
)

@Composable
private fun DragHandle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(
                Background, RoundedCornerShape(
                    topStart = 28.dp,
                    topEnd = 28.dp
                )
            )
    ) {
        Box(
            Modifier
                .align(Alignment.Center)
                .size(32.dp, 4.dp)
                .background(MaterialTheme.colorScheme.outline, CircleShape)
        )
    }
}

@Composable
private fun Content(
    onSelect: (Currency) -> Unit,
    onDismiss: () -> Unit,
) {
    Column {
        items.forEach {
            BWListItem(
                leadingContent = {
                    Icon(
                        painter = painterResource(it.icon),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(16.dp))
                },
                background = Background,
                leadingText = stringResource(it.title),
                height = 70.dp,
                onClick = {
                    onSelect(it.symbol)
                    onDismiss()
                }
            )
        }
        BWListItem(
            leadingContent = {
                Icon(
                    painter = painterResource(CoreRDrawable.ic_cancel),
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(Modifier.width(16.dp))
            },
            leadingText = stringResource(CoreRString.cancel),
            leadingTextColor = Color.White,
            background = MaterialTheme.colorScheme.error,
            height = 70.dp,
            onClick = onDismiss
        )
    }
}