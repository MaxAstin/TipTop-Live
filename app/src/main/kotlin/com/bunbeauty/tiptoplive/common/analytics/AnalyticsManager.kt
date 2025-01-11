package com.bunbeauty.tiptoplive.common.analytics

import android.util.Log
import com.bunbeauty.tiptoplive.common.util.Seconds
import com.bunbeauty.tiptoplive.common.util.toTimeString
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject
import javax.inject.Singleton

private const val STREAM_STARTED_EVENT = "stream_started"
private const val USERNAME_PARAM = "username"
private const val VIEWER_COUNT_PARAM = "viewer_count"
private const val VIEWER_COUNT_EVENT = "viewer_count_"

private const val STREAM_STOPPED_EVENT = "stream_stopped"
private const val STREAM_FINISHED_EVENT = "stream_finished"
private const val STREAM_DURATION_PARAM = "stream_duration"
private const val CAMERA_ERROR_EVENT = "camera_error"

private const val FEEDBACK_POSITIVE_EVENT = "feedback_positive"
private const val FEEDBACK_NEGATIVE_EVENT = "feedback_negative"

private const val OPEN_QUESTIONS_EVENT = "open_questions"
private const val SELECT_QUESTION_EVENT = "select_question"

private const val SHARE_EVENT = "share"
private const val DONATE_EVENT = "donate"

private const val OPEN_DONATION_EVENT = "open_donation_"
private const val PRODUCT_NOT_FOUND_EVENT = "product_not_found_"
private const val START_BILLING_FLOW_EVENT = "start_billing_flow_"
private const val FAIL_BILLING_FLOW_EVENT = "fail_billing_flow_"
private const val PURCHASE_PRODUCT_EVENT = "purchase_product_"
private const val FEATURE_NOT_SUPPORTED_EVENT = "billing_not_supported"
private const val SERVICE_DISCONNECTED_EVENT = "billing_disconnected"
private const val USER_CANCELED_EVENT = "billing_user_canceled"
private const val SERVICE_UNAVAILABLE_EVENT = "billing_service_unavailable"
private const val BILLING_UNAVAILABLE_EVENT = "billing_unavailable"
private const val ITEM_UNAVAILABLE_EVENT = "billing_item_unavailable"
private const val DEVELOPER_ERROR_EVENT = "billing_developer_error"
private const val ERROR_EVENT = "billing_error"
private const val ITEM_ALREADY_OWNED_EVENT = "billing_item_already_owned"
private const val ITEM_NOT_OWNED_EVENT = "billing_item_not_owned"
private const val NETWORK_ERROR_EVENT = "billing_network_error"
private const val USED_DAYS_EVENT = "used_day_"

private const val ANALYTICS_TAG = "analytics"

@Singleton
class AnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    fun trackStreamStart(username: String, viewerCount: Int) {
        trackEvent("$VIEWER_COUNT_EVENT$viewerCount")
        trackEvent(
            event = STREAM_STARTED_EVENT,
            params = mapOf(
                USERNAME_PARAM to username,
                VIEWER_COUNT_PARAM to viewerCount
            )
        )
    }

    fun trackStreamStop(duration: Seconds) {
        trackEvent(
            event = STREAM_STOPPED_EVENT,
            params = mapOf(
                STREAM_DURATION_PARAM to duration.toTimeString()
            )
        )
    }

    fun trackStreamFinish(duration: Seconds) {
        trackEvent(
            event = STREAM_FINISHED_EVENT,
            params = mapOf(
                STREAM_DURATION_PARAM to duration.toTimeString()
            )
        )
    }

    fun trackCameraError() {
        trackEvent(event = CAMERA_ERROR_EVENT)
    }

    fun trackFeedback(isPositive: Boolean) {
        val event = if (isPositive) {
            FEEDBACK_POSITIVE_EVENT
        } else {
            FEEDBACK_NEGATIVE_EVENT
        }
        trackEvent(event = event)
    }

    fun trackOpenQuestions() {
        trackEvent(event = OPEN_QUESTIONS_EVENT)
    }

    fun trackSelectQuestion() {
        trackEvent(event = SELECT_QUESTION_EVENT)
    }

    fun trackShare() {
        trackEvent(event = SHARE_EVENT)
    }

    fun trackDonate() {
        trackEvent(event = DONATE_EVENT)
    }

    fun trackOpenDonation(productId: String) {
        trackEvent(event = "$OPEN_DONATION_EVENT$productId")
    }

    fun trackProductNotFound(productId: String) {
        trackEvent(event = "$PRODUCT_NOT_FOUND_EVENT$productId")
    }

    fun trackStartBillingFlow(productId: String) {
        trackEvent(event = "$START_BILLING_FLOW_EVENT$productId")
    }

    fun trackFailBillingFlow(productId: String) {
        trackEvent(event = "$FAIL_BILLING_FLOW_EVENT$productId")
    }

    fun trackPurchaseProduct(productId: String) {
        trackEvent(event = "$PURCHASE_PRODUCT_EVENT$productId")
    }

    fun trackFeatureNotSupported() {
        trackEvent(event = FEATURE_NOT_SUPPORTED_EVENT)
    }

    fun trackServiceDisconnected() {
        trackEvent(event = SERVICE_DISCONNECTED_EVENT)
    }

    fun trackUserCanceled() {
        trackEvent(event = USER_CANCELED_EVENT)
    }

    fun trackServiceUnavailable() {
        trackEvent(event = SERVICE_UNAVAILABLE_EVENT)
    }

    fun trackBillingUnavailable() {
        trackEvent(event = BILLING_UNAVAILABLE_EVENT)
    }

    fun trackItemUnavailable() {
        trackEvent(event = ITEM_UNAVAILABLE_EVENT)
    }

    fun trackDeveloperError() {
        trackEvent(event = DEVELOPER_ERROR_EVENT)
    }

    fun trackError() {
        trackEvent(event = ERROR_EVENT)
    }

    fun trackItemAlreadyOwned() {
        trackEvent(event = ITEM_ALREADY_OWNED_EVENT)
    }

    fun trackItemNotOwned() {
        trackEvent(event = ITEM_NOT_OWNED_EVENT)
    }

    fun trackNetworkError() {
        trackEvent(event = NETWORK_ERROR_EVENT)
    }

    fun trackUsedDays(days: String) {
        trackEvent(event = "$USED_DAYS_EVENT$days")
    }

    private fun trackEvent(event: String, params: Map<String, Any> = emptyMap()) {
        firebaseAnalytics.logEvent(event) {
            params.forEach { (key, value) ->
                when (value) {
                    is String -> param(key, value)
                    is Long -> param(key, value)
                    is Int -> param(key, value.toLong())
                    else -> {
                        // Not supported
                    }
                }
            }
        }
        Log.d(ANALYTICS_TAG, event)
    }

}