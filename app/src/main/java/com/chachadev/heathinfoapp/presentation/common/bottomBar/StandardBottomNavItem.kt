package com.chachadev.heathinfoapp.presentation.common.bottomBar

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.google.android.material.bottomnavigation.BottomNavigationItemView


@Composable
@Throws(IllegalArgumentException::class)
fun RowScope.StandardBottomNavItem(
    modifier: Modifier = Modifier,
    icon: Int? = null,
    title: String? = null,
    contentDescription: String? = null,
    selected: Boolean = false,
    alertCount: Int? = null,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = MaterialTheme.colorScheme.onBackground,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    if (alertCount != null && alertCount < 0) {
        throw IllegalArgumentException("Alert count can't be negative")
    }

    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        icon = {
            if (icon != null) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = contentDescription,
                    modifier = Modifier.size(25.dp),
                    tint = if (selected) selectedColor else unselectedColor,
                )
            }
        },
        label = {
            if (title != null) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp,
                    color = if (selected) selectedColor else unselectedColor,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                )
            }
        },
        alwaysShowLabel = true
    )
}