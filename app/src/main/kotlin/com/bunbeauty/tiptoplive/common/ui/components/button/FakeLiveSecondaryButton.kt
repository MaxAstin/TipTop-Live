package com.bunbeauty.tiptoplive.common.ui.components.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.util.rememberMultipleEventsCutter

@Composable
fun FakeLiveSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val multipleEventsCutter = rememberMultipleEventsCutter()

    Button(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        onClick = {
            multipleEventsCutter.processEvent(onClick)
        },
    ) {
        Text(
            text = text,
            color = FakeLiveTheme.colors.onSurfaceVariant,
            style = FakeLiveTheme.typography.titleSmall,
        )
    }
}

@Preview
@Composable
private fun FakeLiveSecondaryButtonPreview() {
    FakeLiveTheme {
        FakeLiveSecondaryButton(
            text = "Button",
            onClick = {},
        )
    }
}