package com.bunbeauty.tiptoplive.features.stream.view.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.components.CachedImage
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource
import com.bunbeauty.tiptoplive.common.ui.components.bottomsheet.FakeLiveBottomSheet
import com.bunbeauty.tiptoplive.common.ui.components.bottomsheet.FakeLiveBottomSheetContent
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold
import com.bunbeauty.tiptoplive.features.stream.presentation.Stream
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
sealed interface QuestionState {
    @Immutable
    data object Hidden : QuestionState

    @Immutable
    data object Empty : QuestionState

    @Immutable
    data class NotEmpty(
        val notAnsweredQuestions: ImmutableList<QuestionUi>,
        val answeredQuestions: ImmutableList<QuestionUi>,
    ) : QuestionState
}

@Immutable
data class QuestionUi(
    val uuid: String,
    val picture: ImageSource<*>,
    val username: String,
    val text: String,
    val isSelected: Boolean,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsBottomSheet(
    show: Boolean,
    questionState: QuestionState,
    onAction: (Stream.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (show) {
        FakeLiveBottomSheet(
            modifier = modifier,
            onDismissRequest = {
                onAction(Stream.Action.HideQuestions)
            }
        ) {
            QuestionsContent(
                questionState = questionState,
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun ColumnScope.QuestionsContent(
    questionState: QuestionState,
    onAction: (Stream.Action) -> Unit
) {
    FakeLiveBottomSheetContent(
        title = stringResource(id = R.string.stream_questions_title),
    ) {
        when (questionState) {
            QuestionState.Empty -> {
                EmptyBottomSheetContent(
                    bodyResId = R.string.stream_questions_body,
                    descriptionResId = R.string.stream_questions_description,
                )
            }

            is QuestionState.NotEmpty -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    verticalArrangement = spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    if (questionState.notAnsweredQuestions.isNotEmpty()) {
                        item(key = "tapToAnswerText") {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.stream_questions_tap_to_answer),
                                color = FakeLiveTheme.colors.onSurface,
                                style = FakeLiveTheme.typography.titleMedium,
                            )
                        }
                        item(key = "everyoneWatchingText") {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.stream_questions_everyone_watching),
                                color = FakeLiveTheme.colors.onSurfaceVariant,
                                style = FakeLiveTheme.typography.bodyMedium,
                            )
                        }
                        items(
                            items = questionState.notAnsweredQuestions,
                            key = { question -> question.uuid }
                        ) { question ->
                            QuestionItem(
                                question = question,
                                onAction = onAction,
                            )
                        }
                    }
                    if (questionState.answeredQuestions.isNotEmpty()) {
                        item(key = "answeredQuestionsText") {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.stream_questions_answered),
                                color = FakeLiveTheme.colors.onSurface,
                                style = FakeLiveTheme.typography.titleMedium,
                            )
                        }
                        items(
                            items = questionState.answeredQuestions,
                            key = { question -> question.uuid }
                        ) { question ->
                            QuestionItem(
                                question = question,
                                onAction = onAction,
                            )
                        }
                    }
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun QuestionItem(
    question: QuestionUi,
    onAction: (Stream.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (question.isSelected) {
        FakeLiveTheme.colors.interactive
    } else {
        FakeLiveTheme.colors.border
    }
    val backgroundColor = if (question.isSelected) {
        FakeLiveTheme.colors.selectedSurface
    } else {
        Color.Transparent
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(2.dp)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(2.dp)
            )
            .padding(horizontal = 8.dp)
            .padding(top = 12.dp, bottom = 16.dp)
            .clickableWithoutIndication {
                onAction(
                    Stream.Action.ClickQuestion(
                        uuid = question.uuid
                    )
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CachedImage(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            imageSource = question.picture,
            cacheKey = question.username,
            contentDescription = "Comment avatar",
        )
        Column(
            modifier = Modifier.padding(start = 12.dp),
            verticalArrangement = spacedBy(4.dp)
        ) {
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
                modifier = Modifier.padding(top = 4.dp),
                text = annotatedString,
                color = FakeLiveTheme.colors.onSurface,
                style = FakeLiveTheme.typography.bodySmall,
            )
            Row(horizontalArrangement = spacedBy(16.dp)) {
                Text(
                    modifier = Modifier.clickableWithoutIndication {
                        onAction(Stream.Action.DeleteQuestion(question.uuid))
                    },
                    text = stringResource(R.string.stream_questions_delete),
                    color = FakeLiveTheme.colors.onSurfaceVariant,
                    style = FakeLiveTheme.typography.bodySmall.bold,
                )
                Text(
                    modifier = Modifier.clickableWithoutIndication {
                        onAction(Stream.Action.DeleteQuestion(question.uuid))
                    },
                    text = stringResource(R.string.stream_questions_report),
                    color = FakeLiveTheme.colors.onSurfaceVariant,
                    style = FakeLiveTheme.typography.bodySmall.bold,
                )
            }
        }
    }
}

@LocalePreview
@Composable
private fun EmptyQuestionsBottomSheetPreview() {
    FakeLiveTheme {
        Column {
            QuestionsContent(
                questionState = QuestionState.Empty,
                onAction = {}
            )
        }
    }
}

@LocalePreview
@Composable
private fun NotEmptyQuestionsBottomSheetPreview() {
    FakeLiveTheme {
        Column {
            QuestionsContent(
                questionState = QuestionState.NotEmpty(
                    notAnsweredQuestions = persistentListOf(
                        QuestionUi(
                            uuid = "1",
                            picture = ImageSource.ResId(R.drawable.a1),
                            username = "user.name",
                            text = "short question",
                            isSelected = true,
                        ),
                        QuestionUi(
                            uuid = "2",
                            picture = ImageSource.ResId(R.drawable.a2),
                            username = "user.name2",
                            text = "Looooooooooooooooooooooooooooooooooooo ooooo ooo ooong question",

                            isSelected = false,
                        ),
                    ),
                    answeredQuestions = persistentListOf(
                        QuestionUi(
                            uuid = "3",
                            picture = ImageSource.ResId(R.drawable.a3),
                            username = "user.name3",
                            text = "short question",
                            isSelected = false,
                        ),
                    )
                ),
                onAction = {},
            )
        }
    }
}