package com.bunbeauty.tiptoplive.features.preparation.view

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bunbeauty.tiptoplive.shared.domain.model.ViewerCount
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

@Composable
fun ViewersDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onItemClick: (ViewerCount) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        modifier = modifier.background(FakeLiveTheme.colors.background),
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        ViewerCount.entries.onEach { viewerCount ->
            ViewersDropdownMenuItem(
                text = viewerCount.text,
                onClick = {
                    onItemClick(viewerCount)
                }
            )
        }
    }

}

@Composable
private fun ViewersDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        modifier = Modifier.background(FakeLiveTheme.colors.background),
        text = {
            Text(
                text = text,
                style = FakeLiveTheme.typography.bodyMedium,
            )
        },
        colors = MenuDefaults.itemColors(
            textColor = FakeLiveTheme.colors.onBackground,
        ),
        onClick = onClick
    )
}