package com.bunbeauty.tiptoplive.features.preparation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.shared.domain.model.ViewerCount
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.features.preparation.presentation.Preparation.ViewerCountItem
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ViewersDropdownMenu(
    expanded: Boolean,
    viewerCountList: ImmutableList<ViewerCountItem>,
    onDismissRequest: () -> Unit,
    onItemClick: (ViewerCount) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        modifier = modifier.background(FakeLiveTheme.colors.background),
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        viewerCountList.onEach { viewerCountItem ->
            ViewersDropdownMenuItem(
                viewerCountItem = viewerCountItem,
                onClick = {
                    onItemClick(viewerCountItem.viewerCount)
                }
            )
        }
    }
}

@Composable
private fun ViewersDropdownMenuItem(
    viewerCountItem: ViewerCountItem,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        modifier = Modifier.background(FakeLiveTheme.colors.background),
        trailingIcon = {
            if (!viewerCountItem.isAvailable) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_lock),
                    contentDescription = "Lock"
                )
            }
        },
        text = {
            Text(
                text = viewerCountItem.viewerCount.text,
                style = FakeLiveTheme.typography.bodyMedium,
            )
        },
        enabled = viewerCountItem.isAvailable,
        colors = MenuDefaults.itemColors(
            textColor = FakeLiveTheme.colors.onBackground,
        ),
        onClick = onClick
    )
}