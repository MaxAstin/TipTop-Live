package com.bunbeauty.tiptoplive.features.subscription.mapper

import com.bunbeauty.tiptoplive.features.billing.Product
import com.bunbeauty.tiptoplive.features.subscription.view.SubscriptionItem

fun List<Product>.toSubscriptions(): List<SubscriptionItem> {
    return mapIndexed { index, product ->
        SubscriptionItem(
            id = product.id,
            name = product.name,
            currentPrice = product.currentPrice,
            previousPrice = product.previousPrice,
            discountPercent = product.discountPercent?.let { discountPercent ->
                "$discountPercent%"
            },
            isLifetime = product.id == "lifetime",
            isSelected = index == 0
        )
    }
}