package com.bunbeauty.tiptoplive.features.stream.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.navigation.NavigationRote
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.blurTop
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.components.CachedImage
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold
import com.bunbeauty.tiptoplive.features.stream.presentation.Stream
import com.bunbeauty.tiptoplive.features.stream.presentation.StreamViewModel
import com.bunbeauty.tiptoplive.features.stream.view.ui.AnimatedReaction
import com.bunbeauty.tiptoplive.features.stream.view.ui.AvatarImage
import com.bunbeauty.tiptoplive.features.stream.view.ui.CameraComponent
import com.bunbeauty.tiptoplive.features.stream.view.ui.EmptyBottomSheet
import com.bunbeauty.tiptoplive.features.stream.view.ui.FiltersRow
import com.bunbeauty.tiptoplive.features.stream.view.ui.QuestionState
import com.bunbeauty.tiptoplive.features.stream.view.ui.QuestionUi
import com.bunbeauty.tiptoplive.features.stream.view.ui.QuestionsBottomSheet
import com.bunbeauty.tiptoplive.features.stream.view.ui.VideoComponent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@SuppressLint("DiscouragedApi")
@Composable
fun StreamScreen(navController: NavHostController) {
    val viewModel: StreamViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: Stream.Action ->
            viewModel.onAction(action)
        }
    }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                is Stream.Event.NavigateBack -> {
                    val route = when (event.type) {
                        Stream.Event.NavigateBack.Type.Auto -> {
                            NavigationRote.Preparation(showStreamDurationLimits = true)
                        }
                        is Stream.Event.NavigateBack.Type.User -> {
                            NavigationRote.Preparation(
                                durationInSeconds = event.type.duration.value
                            )
                        }
                    }
                    navController.navigate(route = route) {
                        popUpTo<NavigationRote.Preparation> {
                            inclusive = true
                        }
                    }
                }

                Stream.Event.ShowFilterNotAvailable -> {
                    context.showToast(
                        message = context.resources.getString(
                            R.string.stream_filter_not_available
                        )
                    )
                }
            }
        }.launchIn(this)
    }
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        onAction(Stream.Action.Start)
    }
    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        onAction(Stream.Action.Stop)
    }

    StreamContent(
        state = state.toViewState(),
        onAction = onAction,
    )

    val view = LocalView.current
    DisposableEffect(Unit) {
        val originalState = view.keepScreenOn
        view.keepScreenOn = true

        onDispose {
            view.keepScreenOn = originalState
        }
    }
}

@Composable
private fun StreamContent(
    state: ViewState,
    onAction: (Stream.Action) -> Unit,
) {
    BackHandler {
        onAction(Stream.Action.FinishStreamClick)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FakeLiveTheme.colors.surface,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(vertical = 24.dp)
        ) {
            var showFilters by remember {
                mutableStateOf(false)
            }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                val modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                when (state.mode) {
                    Mode.CAMERA -> {
                        CameraComponent(
                            modifier = modifier,
                            isFront = state.isCameraFront,
                            isEnabled = state.isCameraEnabled,
                            image = state.image,
                            onCameraError = { exception ->
                                onAction(Stream.Action.CameraError(exception = exception))
                            }
                        )
                    }

                    Mode.VIDEO -> {
                        VideoComponent(modifier = modifier)
                    }
                }

                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                            .padding(top = 12.dp, bottom = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.TopEnd),
                            horizontalAlignment = Alignment.End
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AvatarImage(
                                    image = state.image,
                                    modifier = Modifier.size(32.dp)
                                )
                                UsernameRow(
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                        .weight(1f),
                                    username = state.username
                                )

                                LiveCard(modifier = Modifier.padding(start = 12.dp))
                                ViewersCard(
                                    modifier = Modifier.padding(start = 8.dp),
                                    viewersCount = state.viewersCount
                                )
                                ActionIcon(
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .clickableWithoutIndication(
                                            onClick = {
                                                onAction(Stream.Action.FinishStreamClick)
                                            }
                                        ),
                                    iconResId = R.drawable.ic_close,
                                    contentDescription = "Close",
                                )
                            }
                            Actions(
                                modifier = Modifier.padding(top = 16.dp),
                                onSwitchClick = {
                                    onAction(Stream.Action.SwitchCameraClick)
                                },
                                onCameraClick = {
                                    onAction(Stream.Action.CameraClick)
                                },
                                onFiltersClick = {
                                    showFilters = !showFilters
                                }
                            )
                        }
                        Column(
                            modifier = Modifier.align(Alignment.BottomStart),
                        ) {
                            Comments(comments = state.comments)
                            CurrentQuestion(
                                modifier = Modifier.padding(top = 16.dp),
                                question = state.selectedQuestion,
                                onAction = onAction
                            )
                        }
                    }
                }

                repeat(state.reactionCount) { i ->
                    AnimatedReaction(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp),
                        order = i
                    )
                }
            }
            Column {
                BottomPanel(
                    unreadQuestionCount = state.unreadQuestionCount,
                    onAction = onAction
                )
                if (showFilters) {
                    FiltersRow(
                        onFilterChanged = { index ->
                            onAction(
                                Stream.Action.FilterSelected(index = index)
                            )
                        }
                    )
                }
            }
        }

        JoinRequestsBottomSheet(
            show = state.showJoinRequests,
            onDismiss = {
                onAction(Stream.Action.HideJoinRequests)
            }
        )

        InviteBottomSheet(
            show = state.showInvite,
            onDismiss = {
                onAction(Stream.Action.HideInvite)
            }
        )

        QuestionsBottomSheet(
            show = state.questionState != QuestionState.Hidden,
            questionState = state.questionState,
            onAction = onAction,
        )

        DirectBottomSheet(
            show = state.showDirect,
            onDismiss = {
                onAction(Stream.Action.HideDirect)
            }
        )
    }
}

@Composable
private fun UsernameRow(
    username: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f, false),
            text = username,
            color = FakeLiveTheme.colors.onSurface,
            style = FakeLiveTheme.typography.titleSmall,
            overflow = Ellipsis,
            maxLines = 1,
        )
        Icon(
            modifier = Modifier
                .padding(start = 4.dp)
                .size(16.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_drop_down),
            contentDescription = "Dropdown",
            tint = FakeLiveTheme.colors.icon,
        )
    }
}

@Composable
private fun LiveCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(FakeLiveTheme.colors.instagram.accent)
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.stream_live),
            style = FakeLiveTheme.typography.bodySmall,
            color = FakeLiveTheme.colors.onSurface
        )
    }
}

@Composable
private fun ViewersCard(
    viewersCount: ViewersCountUi,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(FakeLiveTheme.colors.surface.copy(alpha = 0.5f))
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_eye),
                contentDescription = "Viewers",
                tint = FakeLiveTheme.colors.icon,
            )
            val viewersCountText = when (viewersCount) {
                is ViewersCountUi.UpToThousand -> viewersCount.count
                is ViewersCountUi.Thousands -> stringResource(
                    R.string.stream_thousands_viewers_count,
                    viewersCount.thousands,
                    viewersCount.hundreds
                )
            }
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = viewersCountText,
                style = FakeLiveTheme.typography.bodySmall,
                color = FakeLiveTheme.colors.onSurface
            )
        }
    }
}

@Composable
private fun Actions(
    modifier: Modifier = Modifier,
    onSwitchClick: () -> Unit,
    onCameraClick: (Boolean) -> Unit,
    onFiltersClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(16.dp)
    ) {
        var isMicMuted by remember {
            mutableStateOf(false)
        }
        var isCameraEnabled by remember {
            mutableStateOf(true)
        }

        ActionIcon(
            modifier = Modifier.clickableWithoutIndication(
                onClick = {
                    isMicMuted = !isMicMuted
                }
            ),
            iconResId = if (isMicMuted) {
                R.drawable.ic_mic_crossed_out
            } else {
                R.drawable.ic_mic
            },
            contentDescription = "Mic",
        )
        ActionIcon(
            modifier = Modifier.clickableWithoutIndication(
                onClick = {
                    isCameraEnabled = !isCameraEnabled
                    onCameraClick(isCameraEnabled)
                }
            ),
            iconResId = if (isCameraEnabled) {
                R.drawable.ic_camera
            } else {
                R.drawable.ic_camera_crossed_out
            },
            contentDescription = "Camera",
        )
        if (isCameraEnabled) {
            ActionIcon(
                modifier = Modifier.clickableWithoutIndication(
                    onClick = onSwitchClick
                ),
                iconResId = R.drawable.ic_switch,
                contentDescription = "Switch camera",
            )
            ActionIcon(
                modifier = Modifier.clickableWithoutIndication(
                    onClick = {
                        onFiltersClick()
                    }
                ),
                iconResId = R.drawable.ic_effect,
                contentDescription = "Effect",
            )
        }
    }
}

@Composable
private fun Comments(
    modifier: Modifier = Modifier,
    comments: List<CommentUi>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(224.dp)
            .blurTop(),
        verticalArrangement = spacedBy(16.dp, Alignment.Bottom),
        reverseLayout = true
    ) {
        items(comments) { comment ->
            CommentItem(comment)
        }
    }
}

@Composable
private fun CurrentQuestion(
    question: QuestionUi?,
    onAction: (Stream.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    question ?: return

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(FakeLiveTheme.colors.surface.copy(alpha = 0.8f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = spacedBy(12.dp),
        ) {
            CachedImage(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                imageSource = question.picture,
                cacheKey = question.username,
                contentDescription = "Question avatar",
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.stream_question_title),
                    color = FakeLiveTheme.colors.onSurface,
                    style = FakeLiveTheme.typography.titleSmall,
                )

                val usernameStyle = FakeLiveTheme.typography.titleSmall
                val annotatedString = remember(question) {
                    buildAnnotatedString {
                        withStyle(style = usernameStyle.toSpanStyle()) {
                            append("${question.username}  ")
                        }
                        append(question.text)
                    }
                }
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = annotatedString,
                    color = FakeLiveTheme.colors.onSurface,
                    style = FakeLiveTheme.typography.bodySmall,
                )
            }
        }
        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .size(12.dp)
                .clickableWithoutIndication {
                    onAction(Stream.Action.CloseCurrentQuestion)
                },
            imageVector = ImageVector.vectorResource(R.drawable.ic_close),
            contentDescription = "Close",
            tint = FakeLiveTheme.colors.icon,
        )
    }
}

@Composable
private fun CommentItem(
    comment: CommentUi,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CachedImage(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            imageSource = comment.picture,
            cacheKey = comment.username,
            contentDescription = "Comment avatar",
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = comment.username,
                color = FakeLiveTheme.colors.onSurface,
                style = FakeLiveTheme.typography.titleSmall,
            )
            Text(
                text = comment.text,
                color = FakeLiveTheme.colors.onSurface,
                style = FakeLiveTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun BottomPanel(
    unreadQuestionCount: Int?,
    onAction: (Stream.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(FakeLiveTheme.colors.surface)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = FakeLiveTheme.colors.border,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.stream_add_comment),
                style = FakeLiveTheme.typography.bodyMedium,
                color = FakeLiveTheme.colors.onSurface,
                overflow = Ellipsis,
                maxLines = 1,
            )
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_options),
                contentDescription = "Options",
                tint = FakeLiveTheme.colors.icon,
            )
        }
        ActionIcon(
            modifier = Modifier.clickableWithoutIndication(
                onClick = {
                    onAction(Stream.Action.ShowJoinRequests)
                }
            ),
            iconResId = R.drawable.ic_camera_plus,
            contentDescription = "Add camera",
        )
        ActionIcon(
            modifier = Modifier.clickableWithoutIndication(
                onClick = {
                    onAction(Stream.Action.ShowInvite)
                }
            ),
            iconResId = R.drawable.ic_invite,
            contentDescription = "Invite",
        )
        Box {
            ActionIcon(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clickableWithoutIndication(
                        onClick = {
                            onAction(Stream.Action.ShowQuestions)
                        }
                    ),
                iconResId = R.drawable.ic_question,
                contentDescription = "Question",
            )
            unreadQuestionCount?.let {
                Badge(
                    modifier = Modifier.align(Alignment.TopEnd),
                    containerColor = FakeLiveTheme.colors.negative,
                    contentColor = FakeLiveTheme.colors.onSurface
                ) {
                    Text(
                        text = unreadQuestionCount.toString(),
                        color = FakeLiveTheme.colors.onSurface,
                        style = FakeLiveTheme.typography.bodySmall.bold
                    )
                }
            }
        }
        ActionIcon(
            modifier = Modifier.clickableWithoutIndication(
                onClick = {
                    onAction(Stream.Action.ShowDirect)
                }
            ),
            iconResId = R.drawable.ic_direct,
            contentDescription = "Direct",
        )
    }
}

@Composable
private fun ActionIcon(
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int,
    contentDescription: String,
) {
    Icon(
        modifier = modifier.size(28.dp),
        imageVector = ImageVector.vectorResource(iconResId),
        contentDescription = contentDescription,
        tint = FakeLiveTheme.colors.icon,
    )
}

@Composable
private fun JoinRequestsBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
) {
    if (show) {
        EmptyBottomSheet(
            onDismissRequest = onDismiss,
            titleResId = R.string.stream_join_requests_title,
            bodyResId = R.string.stream_join_requests_body,
            descriptionResId = R.string.stream_join_requests_description,
        )
    }
}

@Composable
private fun InviteBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
) {
    if (show) {
        EmptyBottomSheet(
            onDismissRequest = onDismiss,
            titleResId = R.string.stream_invite_title,
            bodyResId = R.string.stream_invite_body,
            descriptionResId = R.string.stream_invite_description,
        )
    }
}

@Composable
private fun DirectBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
) {
    if (show) {
        EmptyBottomSheet(
            onDismissRequest = onDismiss,
            titleResId = R.string.stream_direct_title,
            bodyResId = R.string.stream_direct_body,
            descriptionResId = R.string.stream_direct_description,
        )
    }
}

private fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT)
        .show()
}

@LocalePreview
@Composable
private fun StreamScreenPreview() {
    FakeLiveTheme {
        Box(modifier = Modifier.background(Color.White)) {
            StreamContent(
                state = ViewState(
                    image = ImageSource.ResId(R.drawable.img_default_avatar),
                    username = "long_user_name",
                    viewersCount = ViewersCountUi.Thousands(
                        thousands = "10",
                        hundreds = "4",
                    ),
                    comments = persistentListOf(
                        CommentUi(
                            picture = ImageSource.ResId(R.drawable.img_default_avatar),
                            username = "username1",
                            text = "Text 1",
                        ),
                        CommentUi(
                            picture = ImageSource.ResId(R.drawable.img_default_avatar),
                            username = "username2",
                            text = "Text 2",
                        ),
                        CommentUi(
                            picture = ImageSource.ResId(R.drawable.img_default_avatar),
                            username = "username3",
                            text = "Text 3",
                        ),
                    ),
                    reactionCount = 10,
                    mode = Mode.CAMERA,
                    isCameraEnabled = true,
                    isCameraFront = true,
                    showJoinRequests = false,
                    showInvite = false,
                    questionState = QuestionState.Hidden,
                    unreadQuestionCount = 1,
                    selectedQuestion = QuestionUi(
                        uuid = "",
                        picture = ImageSource.ResId(R.drawable.img_default_avatar),
                        username = "username",
                        text = "Question?",
                        isSelected = true,
                    ),
                    showDirect = false,
                ),
                onAction = {},
            )
        }
    }
}