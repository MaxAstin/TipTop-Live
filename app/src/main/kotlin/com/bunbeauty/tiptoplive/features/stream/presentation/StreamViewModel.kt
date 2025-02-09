package com.bunbeauty.tiptoplive.features.stream.presentation

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.common.util.Seconds
import com.bunbeauty.tiptoplive.common.util.getCurrentTimeSeconds
import com.bunbeauty.tiptoplive.features.billing.domain.IsPremiumAvailableUseCase
import com.bunbeauty.tiptoplive.features.stream.CameraUtil
import com.bunbeauty.tiptoplive.features.stream.domain.GetCommentsDelayUseCase
import com.bunbeauty.tiptoplive.features.stream.domain.GetCommentsUseCase
import com.bunbeauty.tiptoplive.features.stream.domain.GetQuestionUseCase
import com.bunbeauty.tiptoplive.features.stream.domain.model.Question
import com.bunbeauty.tiptoplive.shared.domain.GetImageUriFlowUseCase
import com.bunbeauty.tiptoplive.shared.domain.GetUsernameUseCase
import com.bunbeauty.tiptoplive.shared.domain.GetViewerCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min
import kotlin.random.Random

private const val TIME_LIMIT_FOR_FREE_VERSION = 60_000L // 60 sec

@HiltViewModel
class StreamViewModel @Inject constructor(
    private val getImageUriFlowUseCase: GetImageUriFlowUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val getViewerCountUseCase: GetViewerCountUseCase,
    private val isPremiumAvailableUseCase: IsPremiumAvailableUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val getCommentsDelayUseCase: GetCommentsDelayUseCase,
    private val getQuestionUseCase: GetQuestionUseCase,
    private val analyticsManager: AnalyticsManager,
    private val cameraUtil: CameraUtil,
) : BaseViewModel<Stream.State, Stream.Action, Stream.Event>(
    initState = {
        Stream.State(
            imageUri = null,
            username = "",
            viewersCount = 0,
            comments = emptyList(),
            reactionCount = 0,
            questionState = Stream.QuestionState(
                show = false,
                questions = emptyList(),
                unreadQuestionCount = null,
                currentQuestionToAnswer = null,
            ),
            startStreamTimeSeconds = getCurrentTimeSeconds(),
            isCameraEnabled = cameraUtil.hasCamera(),
            isCameraFront = cameraUtil.hasFrontCamera(),
            showJoinRequests = false,
            showInvite = false,
            showDirect = false,
        )
    }
) {

    init {
        setupAvatar()
        setupUsername()
        setupViewerCount()
        checkPremiumStatus()

        startGenerateReactions()
        startGenerateViewersCount()
        startGenerateComments()
        startGenerateQuestions()
    }

    override fun onAction(action: Stream.Action) {
        when (action) {
            Stream.Action.SwitchCameraClick -> {
                if (mutableState.value.isCameraFront) {
                    if (cameraUtil.hasBackCamera()) {
                        setState {
                            copy(isCameraFront = false)
                        }
                    }
                } else {
                    if (cameraUtil.hasFrontCamera()) {
                        setState {
                            copy(isCameraFront = true)
                        }
                    }
                }
            }

            Stream.Action.CameraClick -> {
                if (cameraUtil.hasCamera()) {
                    setState {
                        copy(isCameraEnabled = !isCameraEnabled)
                    }
                }
            }

            Stream.Action.ShowJoinRequests -> {
                setState {
                    copy(showJoinRequests = true)
                }
            }

            Stream.Action.HideJoinRequests -> {
                setState {
                    copy(showJoinRequests = false)
                }
            }

            Stream.Action.ShowInvite -> {
                setState {
                    copy(showInvite = true)
                }
            }

            Stream.Action.HideInvite -> {
                setState {
                    copy(showInvite = false)
                }
            }

            Stream.Action.ShowQuestions -> {
                setState {
                    copy(
                        questionState = questionState.copy(
                            show = true,
                            unreadQuestionCount = null
                        )
                    )
                }
                analyticsManager.trackOpenQuestions()
            }

            Stream.Action.HideQuestions -> {
                setState {
                    copy(
                        questionState = questionState.copy(
                            show = false
                        )
                    )
                }
            }

            is Stream.Action.FilterSelected -> {
                if (action.index > 0) {
                    sendEvent(event = Stream.Event.ShowFilterNotAvailable)
                }
            }

            is Stream.Action.ClickQuestion -> {
                setState {
                    copy(
                        questionState = questionState.copy(
                            questions = questionState.questions.map { question ->
                                question.copy(
                                    isSelected = if (question.question.uuid == action.uuid) {
                                        !question.isSelected
                                    } else {
                                        false
                                    },
                                    isAnswered = question.isAnswered ||
                                        (question.question.uuid == questionState.currentQuestionToAnswer?.question?.uuid)
                                )
                            },
                        )
                    )
                }
                if (currentState.questionState.selectedQuestion != null) {
                    setState {
                        copy(
                            questionState = questionState.copy(
                                show = false,
                                currentQuestionToAnswer = questionState.selectedQuestion
                            )
                        )
                    }
                    analyticsManager.trackSelectQuestion()
                }
            }

            is Stream.Action.DeleteQuestion -> {
                setState {
                    copy(
                        questionState = questionState.copy(
                            questions = questionState.questions.filter { question ->
                                question.question.uuid != action.uuid
                            }
                        )
                    )
                }
            }

            Stream.Action.CloseCurrentQuestion -> {
                setState {
                    copy(
                        questionState = questionState.copy(
                            questions = questionState.questions.map { question ->
                                question.copy(
                                    isSelected = false,
                                    isAnswered = question.isAnswered || question.isSelected
                                )
                            }
                        )
                    )
                }
            }

            Stream.Action.ShowDirect -> {
                setState {
                    copy(showDirect = true)
                }
            }

            Stream.Action.HideDirect -> {
                setState {
                    copy(showDirect = false)
                }
            }

            Stream.Action.Start -> {
                setState {
                    copy(startStreamTimeSeconds = getCurrentTimeSeconds())
                }
            }

            Stream.Action.Stop -> {
                val duration = getStreamDuration()
                analyticsManager.trackStreamStop(duration = duration)
            }

            Stream.Action.FinishStreamClick -> {
                val duration = getStreamDuration()
                analyticsManager.trackStreamFinish(duration = duration)
                sendEvent(Stream.Event.NavigateBack(duration = duration))
            }

            is Stream.Action.CameraError -> {
                analyticsManager.trackCameraError()
            }
        }
    }

    private fun setupAvatar() {
        setState {
            copy(imageUri = getImageUriFlowUseCase().firstOrNull()?.toUri())
        }
    }

    private fun setupUsername() {
        setState {
            copy(username = getUsernameUseCase())
        }
    }

    private fun setupViewerCount() {
        viewModelScope.launch {
            setState {
                copy(viewersCount = getViewerCountUseCase().min)
            }
        }
    }

    private fun checkPremiumStatus() {
        viewModelScope.launch {
            if (!isPremiumAvailableUseCase()) {
                delay(TIME_LIMIT_FOR_FREE_VERSION)
                analyticsManager.trackStreamAutoFinish()
                sendEvent(Stream.Event.NavigateBack(duration = null))
            }
        }
    }

    private fun startGenerateReactions() {
        viewModelScope.launch {
            delay(5_000)
            val viewerCount = getViewerCountUseCase()
            setState {
                copy(
                    reactionCount = min(10, viewerCount.min / 100 + 1)
                )
            }
        }
    }

    private fun startGenerateViewersCount() {
        viewModelScope.launch {
            delay(1_000)

            val viewerCount = getViewerCountUseCase()
            val onePercent = (viewerCount.max - viewerCount.min) / 100
            val step = min(onePercent, Random.nextInt(200, 800))
            val median = viewerCount.min + (viewerCount.max - viewerCount.min) / 2
            while (true) {
                val delayMillis = Random.nextLong(2_000, 4_000)
                delay(delayMillis)

                val current = mutableState.value.viewersCount
                val random = Random.nextInt(10)
                val isPositive = if (current < median) {
                    random < 6 // 0-5 vs 6-9 - 60%
                } else {
                    random < 3 // 0-2 vs 3-9 - 30%
                }
                val change = Random.nextInt(step, step * 5)
                val newCount = if (isPositive) {
                    current + change
                } else {
                    current - change
                }
                setState {
                    copy(viewersCount = newCount)
                }
            }
        }
    }

    private fun startGenerateComments() {
        viewModelScope.launch {
            while (true) {
                val newComments = getCommentsUseCase(viewersCount = currentState.viewersCount)
                val delayMillis = getCommentsDelayUseCase(viewersCount = currentState.viewersCount)
                delay(delayMillis)

                setState {
                    copy(
                        comments = newComments + comments.take(100)
                    )
                }
            }
        }
    }

    private fun startGenerateQuestions() {
        viewModelScope.launch {
            delay(5_000)
            while (true) {
                val questionCount = currentState.questionState.notAnsweredQuestions.size
                val newQuestion = getQuestionUseCase(questionCount = questionCount)
                if (newQuestion != null) {
                    handleNewQuestion(question = newQuestion)
                }

                delay(Random.nextLong(20_000, 50_000))
            }
        }
    }

    private fun handleNewQuestion(question: Question) {
        val newQuestion = Stream.SelectableQuestion(
            question = question,
            isSelected = false,
            isAnswered = false,
        )
        setState {
            copy(
                questionState = questionState.copy(
                    questions = questionState.questions + newQuestion,
                    unreadQuestionCount = if (questionState.show) {
                        null
                    } else {
                        (questionState.unreadQuestionCount ?: 0) + 1
                    }
                ),
            )
        }
    }

    private fun getStreamDuration(): Seconds {
        val start = mutableState.value.startStreamTimeSeconds
        val finish = getCurrentTimeSeconds()

        return finish - start
    }
}