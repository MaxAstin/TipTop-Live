package com.bunbeauty.tiptoplive.features.stream.presentation

import android.net.Uri
import com.bunbeauty.tiptoplive.common.presentation.Base
import com.bunbeauty.tiptoplive.common.util.Seconds
import com.bunbeauty.tiptoplive.features.stream.domain.model.Comment
import com.bunbeauty.tiptoplive.features.stream.domain.model.Question

interface Stream {

    data class State(
        val imageUri: Uri?,
        val username: String,
        val viewersCount: Int,
        val comments: List<Comment>,
        val reactionCount: Int,
        val questionState: QuestionState,
        val startStreamTimeSeconds: Seconds,
        val isCameraEnabled: Boolean,
        val isCameraFront: Boolean,
        val showJoinRequests: Boolean,
        val showInvite: Boolean,
        val showDirect: Boolean,
    ) : Base.State

    data class QuestionState(
        val show: Boolean,
        val questions: List<SelectableQuestion>,
        val unreadQuestionCount: Int?,
        val currentQuestionToAnswer: SelectableQuestion?,
    ) {
        val isEmpty: Boolean = questions.isEmpty()
        val notAnsweredQuestions = questions.filterNot { question ->
            question.isAnswered
        }
        val answeredQuestions = questions.filter { question ->
            question.isAnswered
        }
        val selectedQuestion: SelectableQuestion? = questions.find { question ->
            question.isSelected
        }
    }

    data class SelectableQuestion(
        val question: Question,
        val isAnswered: Boolean,
        val isSelected: Boolean,
    )

    sealed interface Action : Base.Action {
        data object CameraClick : Action
        data object SwitchCameraClick : Action
        data object ShowJoinRequests : Action
        data object HideJoinRequests : Action
        data object ShowInvite : Action
        data object HideInvite : Action
        data object ShowQuestions : Action
        data object HideQuestions : Action
        data class FilterSelected(val index: Int) : Action
        data class ClickQuestion(val uuid: String) : Action
        data class DeleteQuestion(val uuid: String) : Action
        data object CloseCurrentQuestion : Action
        data object ShowDirect : Action
        data object HideDirect : Action
        data object Start : Action
        data object Stop : Action
        data object FinishStreamClick : Action
        data class CameraError(val exception: Exception) : Action
    }

    sealed interface Event : Base.Event {
        data class GoBack(val duration: Seconds) : Event
        data object ShowFilterNotAvailable : Event
    }
}