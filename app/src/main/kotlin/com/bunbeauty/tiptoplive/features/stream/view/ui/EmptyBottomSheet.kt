package com.bunbeauty.tiptoplive.features.stream.view.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.components.bottomsheet.FakeLiveBottomSheet
import com.bunbeauty.tiptoplive.common.ui.components.bottomsheet.FakeLiveBottomSheetContent
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmptyBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    @StringRes titleResId: Int,
    @StringRes bodyResId: Int,
    @StringRes descriptionResId: Int,
) {
    FakeLiveBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) {
        EmptyBottomSheetContent(
            titleResId = titleResId,
            bodyResId = bodyResId,
            descriptionResId = descriptionResId,
        )
    }
}

@Composable
private fun ColumnScope.EmptyBottomSheetContent(
    @StringRes titleResId: Int,
    @StringRes bodyResId: Int,
    @StringRes descriptionResId: Int,
) {
    FakeLiveBottomSheetContent(
        title = stringResource(id = titleResId)
    ) {
        EmptyBottomSheetContent(
            modifier = Modifier.fillMaxWidth(),
            bodyResId = bodyResId,
            descriptionResId = descriptionResId,
        )
    }
}

@Composable
fun EmptyBottomSheetContent(
    @StringRes bodyResId: Int,
    @StringRes descriptionResId: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxWidth(),
            text = stringResource(bodyResId),
            color = FakeLiveTheme.colors.onSurface,
            style = FakeLiveTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 100.dp
                )
                .fillMaxWidth(),
            text = stringResource(descriptionResId),
            color = FakeLiveTheme.colors.onSurfaceVariant,
            style = FakeLiveTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@LocalePreview
@Composable
private fun EmptyBottomSheetPreview() {
    FakeLiveTheme {
        Column {
            EmptyBottomSheetContent(
                titleResId = R.string.stream_questions_title,
                bodyResId = R.string.stream_questions_body,
                descriptionResId = R.string.stream_questions_description,
            )
        }
    }
}