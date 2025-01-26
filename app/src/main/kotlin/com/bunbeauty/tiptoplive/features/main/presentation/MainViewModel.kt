package com.bunbeauty.tiptoplive.features.main.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.features.main.domain.Timer
import com.bunbeauty.tiptoplive.features.main.domain.UpdateUsedDaysUseCase
import com.bunbeauty.tiptoplive.shared.domain.SaveImageUriUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SECONDS_30 = 30_000L

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveImageUriUseCase: SaveImageUriUseCase,
    private val updateUsedDaysUseCase: UpdateUsedDaysUseCase,
    private val timer: Timer
) : BaseViewModel<Main.State, Main.Action, Main.Event>(
    initState = {
        Main.State(showNoCameraPermission = false)
    }
) {

    override fun onAction(action: Main.Action) {
        when (action) {
            Main.Action.AppStart -> {
                viewModelScope.launch {
                    timer.start(SECONDS_30)
                    updateUsedDaysUseCase()
                }
            }

            Main.Action.AppStop -> {
                viewModelScope.launch {
                    timer.stop()
                }
            }

            Main.Action.CameraPermissionDeny -> {
                setState {
                    copy(showNoCameraPermission = true)
                }
            }

            Main.Action.CameraPermissionAccept -> {
                sendEvent(Main.Event.OpenStream)
            }

            Main.Action.CloseCameraRequiredDialogClick -> {
                setState {
                    copy(showNoCameraPermission = false)
                }
            }

            is Main.Action.AvatarSelected -> {
                viewModelScope.launch {
                    action.uri?.let { uri ->
                        saveImageUriUseCase(uri.toString())
                    }
                }
            }
        }
    }

}