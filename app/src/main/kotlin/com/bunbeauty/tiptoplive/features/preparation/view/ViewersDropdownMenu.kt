package com.bunbeauty.tiptoplive.features.preparation.view

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bunbeauty.tiptoplive.shared.domain.model.ViewerCount
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveStreamTheme

@Composable
fun ViewersDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onItemClick: (ViewerCount) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        modifier = modifier.background(FakeLiveStreamTheme.colors.background),
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
        modifier = Modifier.background(FakeLiveStreamTheme.colors.background),
        text = {
            Text(
                text = text,
                style = FakeLiveStreamTheme.typography.bodyMedium,
            )
        },
        colors = MenuDefaults.itemColors(
            textColor = FakeLiveStreamTheme.colors.onBackground,
        ),
        onClick = onClick
    )
}