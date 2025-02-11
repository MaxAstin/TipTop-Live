package com.bunbeauty.tiptoplive.common.navigation

import kotlinx.serialization.Serializable

interface NavigationRote {

    @Serializable
    data object Intro

    @Serializable
    data class Preparation(
        val uri: String? = null,
        val durationInSeconds: Int? = null,
        val showStreamDurationLimits: Boolean = false
    )

    @Serializable
    data class CropImage(val uri: String)

    @Serializable
    data object Stream

    @Serializable
    data object Subscription

    @Serializable
    data class SuccessfullyPurchased(val subscriptionName: String)

    @Serializable
    data object PurchaseFailed

}