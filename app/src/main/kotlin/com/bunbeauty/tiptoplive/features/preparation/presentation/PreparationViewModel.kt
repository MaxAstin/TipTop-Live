package com.bunbeauty.tiptoplive.features.preparation.presentation

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource
import com.bunbeauty.tiptoplive.features.billing.domain.IsPremiumAvailableUseCase
import com.bunbeauty.tiptoplive.features.preparation.domain.SaveShouldAskFeedbackUseCase
import com.bunbeauty.tiptoplive.features.preparation.domain.ShouldAskFeedbackUseCase
import com.bunbeauty.tiptoplive.features.preparation.presentation.Preparation.ViewerCountItem
import com.bunbeauty.tiptoplive.shared.domain.GetImageUriFlowUseCase
import com.bunbeauty.tiptoplive.shared.domain.GetUsernameUseCase
import com.bunbeauty.tiptoplive.shared.domain.GetViewerCountUseCase
import com.bunbeauty.tiptoplive.shared.domain.SaveImageUriUseCase
import com.bunbeauty.tiptoplive.shared.domain.SaveUsernameUseCase
import com.bunbeauty.tiptoplive.shared.domain.SaveViewerCountUseCase
import com.bunbeauty.tiptoplive.shared.domain.model.ViewerCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreparationViewModel @Inject constructor(
    private val getImageUriFlowUseCase: GetImageUriFlowUseCase,
    private val saveImageUriUseCase: SaveImageUriUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val saveUsernameUseCase: SaveUsernameUseCase,
    private val getViewerCountUseCase: GetViewerCountUseCase,
    private val saveViewerCountUseCase: SaveViewerCountUseCase,
    private val isPremiumAvailableUseCase: IsPremiumAvailableUseCase,
    private val shouldAskFeedbackUseCase: ShouldAskFeedbackUseCase,
    private val saveShouldAskFeedbackUseCase: SaveShouldAskFeedbackUseCase,
    private val analyticsManager: AnalyticsManager,
) : BaseViewModel<Preparation.State, Preparation.Action, Preparation.Event>(
    initState = {
        Preparation.State(
            image = ImageSource.ResId(R.drawable.img_default_avatar),
            username = "",
            viewerCountList = persistentListOf(),
            viewerCount = ViewerCount.V_100_200,
            status = Preparation.Status.LOADING,
            showFeedbackDialog = false,
        )
    }
) {

    init {
        initState()
        observeAndSaveUsername()
    }

    override fun onAction(action: Preparation.Action) {
        when (action) {
            Preparation.Action.StartScreen -> {
                checkPremiumStatus()
            }

            is Preparation.Action.ImageSelect -> {
                action.uri?.let { imageUri ->
                    viewModelScope.launch {
                        saveImageUriUseCase(uri = imageUri.toString())
                    }
                    mutableState.update { state ->
                        state.copy(image = ImageSource.Device(imageUri))
                    }
                }
            }

            Preparation.Action.AvatarClick -> {
                sendEvent(Preparation.Event.HandleAvatarClick)
            }

            is Preparation.Action.UsernameUpdate -> {
                mutableState.update { state ->
                    state.copy(username = action.username)
                }
            }

            is Preparation.Action.ViewerCountSelect -> {
                if (action.item.isAvailable) {
                    viewModelScope.launch {
                        saveViewerCountUseCase(viewerCount = action.item.viewerCount)
                    }
                    mutableState.update { state ->
                        state.copy(viewerCount = action.item.viewerCount)
                    }
                } else {
                    sendEvent(Preparation.Event.NavigateToSubscription)
                }
            }

            Preparation.Action.StartStreamClick -> {
                viewModelScope.launch {
                    analyticsManager.trackStreamStart(
                        username = mutableState.value.username,
                        viewerCount = mutableState.value.viewerCount.min
                    )
                    saveUsernameUseCase(mutableState.value.username)
                    sendEvent(Preparation.Event.OpenStream)
                }
            }

            is Preparation.Action.StreamFinished -> {
                viewModelScope.launch {
                    if (shouldAskFeedbackUseCase() && (action.durationInSeconds > 30)) {
                        setState {
                            copy(showFeedbackDialog = true)
                        }
                    }
                }
            }

            Preparation.Action.CloseFeedbackDialogClick -> {
                setState {
                    copy(showFeedbackDialog = false)
                }
            }

            is Preparation.Action.FeedbackClick -> {
                setState {
                    copy(showFeedbackDialog = false)
                }
                viewModelScope.launch {
                    saveShouldAskFeedbackUseCase(shouldAsk = false)
                }
                analyticsManager.trackFeedback(action.isPositive)
                if (action.isPositive) {
                    sendEvent(Preparation.Event.HandlePositiveFeedbackClick)
                }
            }

            is Preparation.Action.NotShowFeedbackChecked -> {
                viewModelScope.launch {
                    saveShouldAskFeedbackUseCase(shouldAsk = !action.checked)
                }
            }

            Preparation.Action.ShareClick -> {
                analyticsManager.trackShare()
                sendEvent(Preparation.Event.HandleShareClick)
            }

            Preparation.Action.PremiumClick -> {
                analyticsManager.trackPremiumClick()
                sendEvent(Preparation.Event.NavigateToSubscription)
            }
        }
    }

    private fun initState() {
        viewModelScope.launch {
            val usernameDeferred = async { getUsernameUseCase() }
            val viewerCountDeferred = async { getViewerCountUseCase() }
            val username = usernameDeferred.await()
            val viewerCount = viewerCountDeferred.await()

            setState {
                copy(
                    username = username,
                    viewerCount = viewerCount
                )
            }
        }
        getImageUriFlowUseCase().onEach { imageUri ->
            setState {
                copy(
                    image = if (imageUri == null) {
                        ImageSource.ResId(data = R.drawable.img_default_avatar)
                    } else {
                        ImageSource.Device(data = imageUri.toUri())
                    }
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun checkPremiumStatus() {
        viewModelScope.launch {
            val status = if (isPremiumAvailableUseCase()) {
                Preparation.Status.PREMIUM
            } else {
                Preparation.Status.FREE
            }
            setState {
                copy(
                    status = status,
                    viewerCountList = ViewerCount.entries.map { viewerCount ->
                        ViewerCountItem(
                            viewerCount = viewerCount,
                            isAvailable = viewerCount == ViewerCount.V_100_200
                                || status == Preparation.Status.PREMIUM,
                        )
                    }.toImmutableList()
                )
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeAndSaveUsername() {
        mutableState.map { state ->
            state.username
        }.distinctUntilChanged()
            .debounce(1_000)
            .onEach { username ->
                saveUsernameUseCase(username)
            }.launchIn(viewModelScope)
    }

}