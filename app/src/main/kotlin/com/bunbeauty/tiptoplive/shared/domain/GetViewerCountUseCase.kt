package com.bunbeauty.tiptoplive.shared.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import com.bunbeauty.tiptoplive.shared.domain.model.ViewerCount
import javax.inject.Inject

class GetViewerCountUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(): ViewerCount {
        val viewerCountIndex = keyValueStorage.getViewerCountIndex(defaultValue = 0)
        return ViewerCount.entries.getOrNull(viewerCountIndex) ?: ViewerCount.V_100_200
    }

}