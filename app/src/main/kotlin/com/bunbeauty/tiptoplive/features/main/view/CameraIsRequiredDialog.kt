package com.bunbeauty.tiptoplive.features.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLiveSecondaryButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.features.main.presentation.Main

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraIsRequiredDialog(
    onAction: (Main.Action) -> Unit,
    onSettingsClick: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = {
            onAction(Main.Action.CloseCameraRequiredDialogClick)
        }
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(FakeLiveTheme.colors.background)
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.required_camera_permisseon_title,
                    stringResource(R.string.app_name)
                ),
                color = FakeLiveTheme.colors.onBackground,
                style = FakeLiveTheme.typography.titleMedium,
            )
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.required_camera_permisseon_body),
                color = FakeLiveTheme.colors.onBackground,
                style = FakeLiveTheme.typography.bodyMedium,
            )
            FakeLiveSecondaryButton(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.End),
                text = stringResource(R.string.settings),
                onClick = onSettingsClick,
            )
        }
    }
}

@LocalePreview
@Composable
private fun CameraIsRequiredDialogPreview() {
    FakeLiveTheme {
        CameraIsRequiredDialog(
            onAction = {},
            onSettingsClick = {}
        )
    }
}