package com.bunbeauty.tiptoplive.features.preparation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLiveDialogButton
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLiveSecondaryButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.features.preparation.presentation.Preparation
import com.bunbeauty.tiptoplive.features.stream.presentation.TIME_LIMIT_FOR_FREE_VERSION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreamDurationLimitsDialog(
    onAction: (Preparation.Action) -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = {
            onAction(Preparation.Action.CloseFeedbackDialogClick)
        }
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(FakeLiveTheme.colors.background)
                .padding(24.dp)
        ) {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.limits_live_has_ended),
                    color = FakeLiveTheme.colors.onBackground,
                    style = FakeLiveTheme.typography.titleMedium,
                )
                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(24.dp)
                        .clickableWithoutIndication {
                            onAction(Preparation.Action.CloseFeedbackDialogClick)
                        },
                    painter = painterResource(R.drawable.ic_close),
                    tint = FakeLiveTheme.colors.onSurfaceVariant,
                    contentDescription = "close"
                )
            }
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                text = stringResource(
                    R.string.limits_free_session_limited,
                    TIME_LIMIT_FOR_FREE_VERSION
                ),
                color = FakeLiveTheme.colors.onBackground,
                style = FakeLiveTheme.typography.bodyMedium,
            )
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.End),
                horizontalArrangement = spacedBy(16.dp)
            ) {
                FakeLiveSecondaryButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.limits_later),
                    onClick = {
                        onAction(Preparation.Action.PremiumLaterClick)
                    },
                )
                FakeLiveDialogButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.limits_upgrade),
                    background = FakeLiveTheme.colors.interactive,
                    onClick = {
                        onAction(Preparation.Action.PremiumClick)
                    },
                )
            }
        }
    }
}

@LocalePreview
@Composable
private fun FeedbackDialogPreview() {
    StreamDurationLimitsDialog(
        onAction = {}
    )
}