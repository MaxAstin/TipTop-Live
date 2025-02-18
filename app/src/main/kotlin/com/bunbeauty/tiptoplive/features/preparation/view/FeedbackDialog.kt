package com.bunbeauty.tiptoplive.features.preparation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLiveDialogButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.features.preparation.presentation.Preparation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackDialog(
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
                    text = stringResource(
                        R.string.do_you_like,
                        stringResource(R.string.app_name)
                    ),
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
                    R.string.help_us_improve,
                    stringResource(R.string.app_name)
                ),
                color = FakeLiveTheme.colors.onBackground,
                style = FakeLiveTheme.typography.bodyMedium,
            )
            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .width(160.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.shy_emoji),
                contentDescription = "feedback emoji"
            )
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.End),
                horizontalArrangement = spacedBy(16.dp)
            ) {
                FakeLiveDialogButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.feedback_yes),
                    background = Color(0xFF5BC589),
                    iconId = R.drawable.thumbs_up,
                    onClick = {
                        onAction(
                            Preparation.Action.FeedbackClick(isPositive = true)
                        )
                    },
                )
                FakeLiveDialogButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.feedback_no),
                    background = Color(0xFFDD6962),
                    iconId = R.drawable.thumbs_down,
                    onClick = {
                        onAction(
                            Preparation.Action.FeedbackClick(isPositive = false)
                        )
                    },
                )
            }
        }
    }
}

@LocalePreview
@Composable
private fun FeedbackDialogPreview() {
    FeedbackDialog(
        onAction = {}
    )
}