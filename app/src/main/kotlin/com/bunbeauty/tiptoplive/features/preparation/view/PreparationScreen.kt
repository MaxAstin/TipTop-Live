package com.bunbeauty.tiptoplive.features.preparation.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.navigation.NavigationRote
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.components.CachedImage
import com.bunbeauty.tiptoplive.common.ui.components.FakeLiveTextField
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLiveGradientButton
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLiveIconButton
import com.bunbeauty.tiptoplive.common.ui.noEffectClickable
import com.bunbeauty.tiptoplive.common.ui.rippleClickable
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveStreamTheme
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.shared.domain.model.ViewerCount
import com.bunbeauty.tiptoplive.features.main.view.FeedbackDialog
import com.bunbeauty.tiptoplive.features.preparation.presentation.Preparation
import com.bunbeauty.tiptoplive.features.preparation.presentation.PreparationViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val IMAGE = "image/*"

@Composable
fun PreparationScreen(
    navController: NavHostController,
    streamDurationInSeconds: Int?,
    croppedImageUri: Uri?,
    onStartStreamClick: () -> Unit,
    onPositiveFeedbackClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    val viewModel: PreparationViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: Preparation.Action ->
            viewModel.onAction(action)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        val uriParam = uri?.toString() ?: return@rememberLauncherForActivityResult
        navController.navigate(
            NavigationRote.CropImage(uri = uriParam)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                Preparation.Event.OpenStream -> {
                    onStartStreamClick()
                }

                Preparation.Event.HandlePositiveFeedbackClick -> {
                    onPositiveFeedbackClick()
                }

                Preparation.Event.HandleAvatarClick -> {
                    galleryLauncher.launch(IMAGE)
                }

                Preparation.Event.HandleShareClick -> {
                    onShareClick()
                }

                Preparation.Event.HandleDonateClick -> {
                    navController.navigate(NavigationRote.Donation)
                }
            }
        }.launchIn(this)
    }

    LaunchedEffect(Unit) {
        if (streamDurationInSeconds != null) {
            viewModel.onAction(Preparation.Action.StreamFinished(durationInSeconds = streamDurationInSeconds))
        }
    }

    LaunchedEffect(Unit) {
        if (croppedImageUri != null) {
            viewModel.onAction(Preparation.Action.ImageSelect(uri = croppedImageUri))
        }
    }

    PreparationContent(
        state = state,
        onAction = onAction,
    )

    if (state.showFeedbackDialog) {
        FeedbackDialog(onAction = onAction)
    }
}

@Composable
private fun PreparationContent(
    state: Preparation.State,
    onAction: (Preparation.Action) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FakeLiveStreamTheme.colors.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.TopEnd),
            horizontalArrangement = spacedBy(8.dp)
        ) {
            FakeLiveIconButton(
                iconId = R.drawable.ic_share,
                contentDescription = "share",
                onClick = {
                    onAction(Preparation.Action.ShareClick)
                }
            )
//          TODO uncomment after adding In-App products
//          FakeLiveIconButton(
//              iconId = R.drawable.ic_donate,
//              contentDescription = "donate",
//              hasMarker = state.highlightDonate,
//              onClick = {
//                  onAction(Preparation.Action.DonateClick)
//              }
//          )
        }

        Column(modifier = Modifier.align(Alignment.Center)) {
            CachedImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(80.dp)
                    .clip(CircleShape)
                    .noEffectClickable {
                        onAction(Preparation.Action.AvatarClick)
                    },
                imageSource = state.image,
                cacheKey = state.image.data.toString(),
                contentDescription = "Avatar",
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .noEffectClickable {
                        onAction(Preparation.Action.AvatarClick)
                    }
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.preparation_edit_photo),
                    color = FakeLiveStreamTheme.colors.interactive,
                    style = FakeLiveStreamTheme.typography.titleSmall,
                )
            }

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.preparation_username),
                color = FakeLiveStreamTheme.colors.onSurfaceVariant,
                style = FakeLiveStreamTheme.typography.bodyMedium,
            )
            FakeLiveTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.username,
                hint = stringResource(R.string.preparation_username_hint),
                readOnly = false,
                onValueChange = { username ->
                    onAction(Preparation.Action.UsernameUpdate(username = username))
                },
            )

            var menuExpanded by remember {
                mutableStateOf(false)
            }
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.preparation_viewer_count),
                color = FakeLiveStreamTheme.colors.onSurfaceVariant,
                style = FakeLiveStreamTheme.typography.bodyMedium,
            )
            Box {
                FakeLiveTextField(
                    modifier = Modifier.rippleClickable {
                        menuExpanded = true
                    },
                    value = state.viewerCount.text,
                    hint = "",
                    readOnly = true,
                    onValueChange = {},
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                            contentDescription = "Arrow",
                            tint = FakeLiveStreamTheme.colors.iconVariant,
                        )
                    }
                )
                ViewersDropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = {
                        menuExpanded = false
                    },
                    onItemClick = { viewerCount ->
                        onAction(Preparation.Action.ViewerCountSelect(viewerCount = viewerCount))
                        menuExpanded = false
                    }
                )
            }
        }

        FakeLiveGradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            brush = Brush.linearGradient(
                colors = listOf(
                    FakeLiveStreamTheme.colors.instagram.logo1,
                    FakeLiveStreamTheme.colors.instagram.logo2,
                    FakeLiveStreamTheme.colors.instagram.logo3,
                ),
                start = Offset(Float.POSITIVE_INFINITY, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY),
            ),
            shape = RoundedCornerShape(6.dp),
            onClick = {
                onAction(Preparation.Action.StartStreamClick)
            }
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(12.dp),
                text = stringResource(R.string.preparation_start_live),
                color = FakeLiveStreamTheme.colors.onSurface,
                style = FakeLiveStreamTheme.typography.titleSmall,
            )
        }
    }
}

@LocalePreview
@Composable
private fun PreparationContentPreview() {
    FakeLiveTheme {
        PreparationContent(
            state = Preparation.State(
                image = ImageSource.ResId(R.drawable.img_default_avatar),
                username = "",
                viewerCount = ViewerCount.V_100_200,
                highlightDonate = true,
                showFeedbackDialog = false,
            ),
            onAction = {}
        )
    }
}