package com.bunbeauty.tiptoplive.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FakeLiveTextField(
    value: String,
    hint: String,
    readOnly: Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit) = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isFocused by remember {
        mutableStateOf(false)
    }
    BasicTextField(
        modifier = modifier
            .background(color = Color.Transparent)
            .indicatorLine(
                enabled = true,
                isError = false,
                interactionSource = interactionSource,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = FakeLiveTheme.colors.instagram.accent,
                    unfocusedIndicatorColor = FakeLiveTheme.colors.borderVariant,
                ),
                focusedIndicatorLineThickness = 2.dp,
                unfocusedIndicatorLineThickness = 2.dp,
            )
            .onFocusChanged {
                isFocused = it.isFocused
            },
        singleLine = true,
        interactionSource = interactionSource,
        enabled = !readOnly,
        readOnly = readOnly,
        value = value,
        onValueChange = onValueChange,
        decorationBox = @Composable { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (isFocused || value.isNotEmpty()) {
                        innerTextField()
                    } else {
                        Text(
                            text = hint,
                            style = FakeLiveTheme.typography.bodyMedium,
                            color = FakeLiveTheme.colors.onSurfaceVariant,
                        )
                    }
                }

                trailingIcon()
            }
        },
    )
}