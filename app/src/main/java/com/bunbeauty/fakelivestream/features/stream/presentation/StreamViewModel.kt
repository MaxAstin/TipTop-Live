package com.bunbeauty.fakelivestream.features.stream.presentation

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.features.domain.GetImageUriUseCase
import com.bunbeauty.fakelivestream.features.domain.GetUsernameUseCase
import com.bunbeauty.fakelivestream.features.domain.GetViewerCountUseCase
import com.bunbeauty.fakelivestream.features.stream.domain.Comment
import com.bunbeauty.fakelivestream.features.stream.domain.GetCommentsUseCase
import com.bunbeauty.fakelivestream.ui.components.ImageSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min
import kotlin.random.Random

@HiltViewModel
class StreamViewModel @Inject constructor(
    private val getImageUriUseCase: GetImageUriUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val getViewerCountUseCase: GetViewerCountUseCase,
    private val getComments: GetCommentsUseCase,
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {

    private val mutableState = MutableStateFlow(
        StreamDataState(
            imageUri = null,
            username = "",
            viewersCount = 0,
            comments = emptyList(),
        )
    )
    val state: StateFlow<StreamViewState> = mutableState.map(::mapState)
        .stateIn(
            scope = viewModelScope,
            started = Eagerly,
            initialValue = mapState(mutableState.value),
        )

    init {
        getAvatar()
        getUsername()
        getViewerCount()
    }

    private fun getAvatar() {
        viewModelScope.launch {
            mutableState.update { state ->
                state.copy(imageUri = getImageUriUseCase()?.toUri())
            }
        }
    }

    private fun getUsername() {
        viewModelScope.launch {
            mutableState.update { state ->
                state.copy(username = getUsernameUseCase())
            }
        }
    }

    private fun getViewerCount() {
        viewModelScope.launch {
            val viewerCount = getViewerCountUseCase()
            mutableState.update { state ->
                state.copy(viewersCount = viewerCount.min)
            }

            startGenerateViewersCount(
                min = viewerCount.min,
                max = viewerCount.max
            )
            startGenerateComments()
        }
    }

    private fun startGenerateViewersCount(min: Int, max: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            delay(1_000)

            val onePercent = (max - min) / 100
            val step = min(onePercent, Random.nextInt(200, 800))
            val median = min + (max - min) / 2
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
                mutableState.update { state ->
                    state.copy(viewersCount = newCount)
                }
            }
        }
    }

    private fun startGenerateComments() {
        viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                val commentCount = Random.nextInt(1, 5)
                val newComments = getComments(count = commentCount)

                newComments.forEach { comment ->
                    preloadUserAvatar(comment)
                }
                val needPreload = newComments.any { it.picture != null }
                if (needPreload) {
                    delay(2_000)
                }

                mutableState.update { state ->
                    state.copy(comments = newComments + state.comments.take(100))
                }
            }
        }
    }

    private fun preloadUserAvatar(comment: Comment) {
        if (comment.picture == null) {
            return
        }

        val request = ImageRequest.Builder(applicationContext)
            .data(comment.picture)
            .diskCachePolicy(CachePolicy.DISABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCacheKey(comment.username)
            .build()
        applicationContext.imageLoader.enqueue(request)
    }

    private fun mapState(dataState: StreamDataState): StreamViewState {
        return dataState.run {
            StreamViewState(
                image = if (dataState.imageUri == null) {
                    ImageSource.Res(R.drawable.img_default_avatar)
                } else {
                    ImageSource.Device(data = dataState.imageUri)
                },
                username = username,
                viewersCount = if (viewersCount < 1_000) {
                    ViewersCount.UpToThousand(count = viewersCount.toString())
                } else {
                    val thousands = viewersCount / 1_000
                    val hundreds = viewersCount % 1_000 / 100
                    ViewersCount.Thousands(
                        thousands = thousands.toString(),
                        hundreds = hundreds.toString(),
                    )
                },
                comments = comments.map { comment ->
                    CommentUi(
                        picture = if (comment.picture == null) {
                            ImageSource.Res(R.drawable.img_default_avatar)
                        } else {
                            ImageSource.Url(data = comment.picture)
                        },
                        username = comment.username,
                        text = comment.text,
                    )
                },
            )
        }
    }
}