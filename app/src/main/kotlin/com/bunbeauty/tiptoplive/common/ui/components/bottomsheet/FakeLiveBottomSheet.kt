package com.bunbeauty.tiptoplive.common.ui.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FakeLiveBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    shape: Shape = FakeLiveBottomSheetDefaults.shape,
    containerColor: Color = FakeLiveTheme.colors.surfaceVariant,
    contentColor: Color = contentColorFor(containerColor),
    dragHandle: @Composable (() -> Unit)? = { FakeLiveBottomSheetDefaults.DragHandle() },
    contentWindowInsets: WindowInsets = BottomSheetDefaults.windowInsets,
    content: @Composable ColumnScope.() -> Unit,
) {
    val systemBottomBarHeight = getSystemBottomBarHeight()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        dragHandle = dragHandle,
        contentWindowInsets = { contentWindowInsets },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = systemBottomBarHeight)
        ) {
            content()
        }
    }
}

@Composable
fun getSystemBottomBarHeight(): Dp {
    val view = LocalView.current
    val density = LocalDensity.current

    return remember {
        val insets = ViewCompat.getRootWindowInsets(view)
        if (insets != null) {
            val bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            with(density) {
                bottomInset.toDp().value.dp
            }
        } else {
            0.dp
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FakeLiveBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState()
    LaunchedEffect(Unit) {
        sheetState.show()
    }
    FakeLiveTheme {
        FakeLiveBottomSheet(
            onDismissRequest = {},
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Absolute.spacedBy(8.dp)
            ) {
                repeat(4) {
                    Spacer(
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth()
                            .background(FakeLiveTheme.colors.surface)
                    )
                }
            }
        }
    }
}