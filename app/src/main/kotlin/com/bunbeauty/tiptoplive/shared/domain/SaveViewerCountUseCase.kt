package com.bunbeauty.tiptoplive.shared.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import com.bunbeauty.tiptoplive.shared.domain.model.ViewerCount
import javax.inject.Inject

class SaveViewerCountUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(viewerCount: ViewerCount) {
        keyValueStorage.saveViewerCountIndex(index = ViewerCount.entries.indexOf(viewerCount))
    }

}