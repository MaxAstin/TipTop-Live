package com.bunbeauty.tiptoplive.features.main.domain

import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import com.bunbeauty.tiptoplive.common.util.getCurrentTimeSeconds
import com.bunbeauty.tiptoplive.common.util.toDateString
import javax.inject.Inject

class UpdateUsedDaysUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage,
    private val analyticsManager: AnalyticsManager,
) {

    suspend operator fun invoke() {
        val currentDate = getCurrentTimeSeconds().toDateString()
        val lastUsedDate = keyValueStorage.getLastUsedDate()

        if (lastUsedDate != currentDate) {
            val usedDayCount = keyValueStorage.getUsedDayCount(defaultValue = 0)
            keyValueStorage.saveLastUsedDate(date = currentDate)
            val updatedCount = usedDayCount + 1
            keyValueStorage.saveUsedDayCount(count = updatedCount)
            when (updatedCount) {
                in 1..4 -> analyticsManager.trackUsedDays(days = updatedCount.toString())
                5 -> analyticsManager.trackUsedDays(days = "5+")
                else -> Unit // Do not send
            }
        }
    }

}