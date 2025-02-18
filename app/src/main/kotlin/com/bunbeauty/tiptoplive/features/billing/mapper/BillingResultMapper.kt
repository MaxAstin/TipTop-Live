package com.bunbeauty.tiptoplive.features.billing.mapper

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.bunbeauty.tiptoplive.features.billing.model.PurchaseResult

fun BillingResult.toPurchaseResultError(): PurchaseResult.Error? {
    return when (responseCode) {
        BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED -> {
            PurchaseResult.Error(reason = "FEATURE_NOT_SUPPORTED")
        }

        BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> {
            PurchaseResult.Error(reason = "SERVICE_DISCONNECTED")
        }

        BillingClient.BillingResponseCode.USER_CANCELED -> {
            PurchaseResult.Error(reason = "USER_CANCELED")
        }

        BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> {
            PurchaseResult.Error(reason = "SERVICE_UNAVAILABLE")
        }

        BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {
            PurchaseResult.Error(reason = "BILLING_UNAVAILABLE")
        }

        BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> {
            PurchaseResult.Error(reason = "ITEM_UNAVAILABLE")
        }

        BillingClient.BillingResponseCode.DEVELOPER_ERROR -> {
            PurchaseResult.Error(reason = "DEVELOPER_ERROR")
        }

        BillingClient.BillingResponseCode.ERROR -> {
            PurchaseResult.Error(reason = "ERROR")
        }

        BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
            PurchaseResult.Error(reason = "ITEM_ALREADY_OWNED")
        }

        BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {
            PurchaseResult.Error(reason = "ITEM_NOT_OWNED")
        }

        BillingClient.BillingResponseCode.NETWORK_ERROR -> {
            PurchaseResult.Error(reason = "NETWORK_ERROR")
        }

        else -> null
    }
}