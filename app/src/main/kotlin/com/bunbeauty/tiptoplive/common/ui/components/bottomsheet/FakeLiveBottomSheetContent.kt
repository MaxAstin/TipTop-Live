package com.bunbeauty.tiptoplive.common.ui.components.bottomsheet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

@Composable
fun ColumnScope.FakeLiveBottomSheetContent(
    title: String,
    @DrawableRes topIconId: Int? = null,
    titleColor: Color = FakeLiveTheme.colors.onSurface,
    dividerColor: Color = FakeLiveTheme.colors.border,
    content: @Composable () -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))
    topIconId?.let {
        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 12.dp)
                .size(64.dp),
            painter = painterResource(topIconId),
            contentDescription = "Top icon"
        )
    }
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = title,
        color = titleColor,
        style = FakeLiveTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        thickness = 0.5.dp,
        color = dividerColor
    )
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        content()
    }
}