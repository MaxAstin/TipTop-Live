package com.bunbeauty.tiptoplive.common.ui.components.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.util.rememberMultipleEventsCutter

@Composable
fun FakeLiveDialogButton(
    text: String,
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(6.dp),
    @DrawableRes iconId: Int? = null,
) {
    val multipleEventsCutter = rememberMultipleEventsCutter()

    Button(
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = background),
        onClick = {
            multipleEventsCutter.processEvent(onClick)
        },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                color = FakeLiveTheme.colors.onSurface,
                style = FakeLiveTheme.typography.titleSmall,
            )
            if (iconId != null) {
                Image(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(16.dp),
                    painter = painterResource(iconId),
                    contentDescription = "icon"
                )
            }
        }
    }
}

@Preview
@Composable
private fun FakeLiveDialogButtonPreview() {
    FakeLiveTheme {
        FakeLiveDialogButton(
            text = "Button",
            iconId = R.drawable.thumbs_up,
            background = FakeLiveTheme.colors.interactive,
            onClick = {}
        )
    }
}