package com.bunbeauty.fakelivestream.features.preparation.presentation

import android.net.Uri
import com.bunbeauty.fakelivestream.features.domain.model.ViewerCount
import com.bunbeauty.fakelivestream.ui.components.ImageSource

interface Preparation {

    data class State(
        val image: ImageSource<*>,
        val username: String,
        val viewerCount: ViewerCount,
    )

    sealed interface Action {
        data class ViewerCountSelect(val viewerCount: ViewerCount): Action
        data class UsernameUpdate(val username: String): Action
        data class ImageSelect(val uri: Uri?): Action
        data object StartStreamClick: Action
    }

}