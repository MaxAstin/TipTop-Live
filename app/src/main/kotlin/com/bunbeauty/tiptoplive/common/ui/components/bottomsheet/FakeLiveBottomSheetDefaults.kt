package com.bunbeauty.tiptoplive.common.ui.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

object FakeLiveBottomSheetDefaults {

    val shape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomEnd = 0.dp,
        bottomStart = 0.dp
    )

    @Composable
    fun DragHandle(
        color: Color = FakeLiveTheme.colors.border
    ) {
        Spacer(
            modifier = Modifier
                .padding(12.dp)
                .width(36.dp)
                .height(4.dp)
                .background(
                    color = color,
                    shape = RoundedCornerShape(size = 2.5.dp)
                )
        )
    }

}