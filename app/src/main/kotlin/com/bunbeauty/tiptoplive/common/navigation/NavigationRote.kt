package com.bunbeauty.tiptoplive.common.navigation

import kotlinx.serialization.Serializable

interface NavigationRote {

    @Serializable
    object Intro

    @Serializable
    data class Preparation(
        val uri: String? = null,
        val durationInSeconds: Int? = null,
    )

    @Serializable
    data class CropImage(val uri: String)

    @Serializable
    object Stream

    @Serializable
    object Subscription

    @Serializable
    data class SuccessfullyPurchased(val subscriptionName: String)

}