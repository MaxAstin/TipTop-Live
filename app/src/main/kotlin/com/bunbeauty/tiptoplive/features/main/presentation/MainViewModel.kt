package com.bunbeauty.tiptoplive.features.main.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.features.main.domain.UpdateUsedDaysUseCase
import com.bunbeauty.tiptoplive.shared.domain.SaveImageUriUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveImageUriUseCase: SaveImageUriUseCase,
    private val updateUsedDaysUseCase: UpdateUsedDaysUseCase
) : BaseViewModel<Main.State, Main.Action, Main.Event>(
    initState = {
        Main.State(showNoCameraPermission = false)
    }
) {

    init {
        viewModelScope.launch {
            updateUsedDaysUseCase()
        }
    }

    override fun onAction(action: Main.Action) {
        when (action) {
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