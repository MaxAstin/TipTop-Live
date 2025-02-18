package com.bunbeauty.tiptoplive.shared.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import com.bunbeauty.tiptoplive.features.billing.domain.IsPremiumAvailableUseCase
import com.bunbeauty.tiptoplive.shared.domain.model.ViewerCount
import javax.inject.Inject

class GetViewerCountUseCase @Inject constructor(
    private val isPremiumAvailableUseCase: IsPremiumAvailableUseCase,
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(): ViewerCount {
        val isPremium = isPremiumAvailableUseCase()

        return if (isPremium) {
            val viewerCountIndex = keyValueStorage.getViewerCountIndex(defaultValue = 0)
            ViewerCount.entries.getOrNull(viewerCountIndex) ?: ViewerCount.V_100_200
        } else {
            ViewerCount.V_100_200
        }
    }

}