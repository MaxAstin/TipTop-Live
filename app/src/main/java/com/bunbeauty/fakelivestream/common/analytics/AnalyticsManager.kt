package com.bunbeauty.fakelivestream.common.analytics

import com.bunbeauty.fakelivestream.common.util.toTimeString
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject
import javax.inject.Singleton

private const val STREAM_STARTED = "stream_started"
private const val USERNAME_PARAM = "username"
private const val STREAM_STOPPED = "stream_stopped"
private const val STREAM_FINISHED = "stream_finished"
private const val STREAM_DURATION_PARAM = "stream_duration"

@Singleton
class AnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    private var startStreamTimeMillis: Long? = null

    fun trackStreamStart(username: String) {
        startStreamTimeMillis = System.currentTimeMillis()

        firebaseAnalytics.logEvent(STREAM_STARTED) {
            param(USERNAME_PARAM, username)
        }
    }

    fun trackStreamResumption() {
        startStreamTimeMillis = System.currentTimeMillis()
    }

    fun trackStreamStop() {
        val duration = getStreamDuration() ?: return

        firebaseAnalytics.logEvent(STREAM_STOPPED) {
            param(STREAM_DURATION_PARAM, duration)
        }
    }

    fun trackStreamFinish() {
        val duration = getStreamDuration() ?: return

        firebaseAnalytics.logEvent(STREAM_FINISHED) {
            param(STREAM_DURATION_PARAM, duration)
        }
    }

    private fun getStreamDuration(): String? {
        val start = startStreamTimeMillis ?: return null
        startStreamTimeMillis = null
        val finish = System.currentTimeMillis()

        return (finish - start).toTimeString()
    }

}