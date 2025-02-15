package com.bunbeauty.tiptoplive.features.main.domain

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

enum class TimerResult {
    FINISHED,
    CANCELED
}

@Singleton
class Timer @Inject constructor() {

    private var currentTimer: Deferred<TimerResult>? = null

    suspend fun start(millis: Long): TimerResult = coroutineScope {
        val deferred = async {
            try {
                delay(millis)
                TimerResult.FINISHED
            } catch (exception: Exception) {
                TimerResult.CANCELED
            }
        }
        currentTimer = deferred
        deferred.await()
    }

    fun stop() {
        currentTimer?.cancel()
    }

}