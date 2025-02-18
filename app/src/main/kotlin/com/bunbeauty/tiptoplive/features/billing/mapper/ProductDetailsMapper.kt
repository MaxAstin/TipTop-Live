package com.bunbeauty.tiptoplive.features.billing.mapper

import com.android.billingclient.api.ProductDetails
import com.bunbeauty.tiptoplive.features.billing.model.Product
import java.math.BigDecimal
import java.math.RoundingMode

fun ProductDetails.inAppProductToProduct(percent: Int): Product? {
    val details = oneTimePurchaseOfferDetails ?: return null

    return Product(
        id = productId,
        offerToken = null,
        name = name,
        currentPrice = details.formattedPrice,
        previousPrice = details.formattedPrice.calculatePreviousPrice(percent = percent),
        discountPercent = details.formattedPrice.calculateDiscountPercent(percent = percent)
    )
}

fun ProductDetails.subscriptionToProduct(percent: Int): Product? {
    val subscriptionOfferDetail = subscriptionOfferDetails?.firstOrNull() ?: return null
    val pricingPhase = subscriptionOfferDetail.pricingPhases.pricingPhaseList.firstOrNull() ?: return null

    return Product(
        id = productId,
        offerToken = subscriptionOfferDetail.offerToken,
        name = name,
        currentPrice = pricingPhase.formattedPrice,
        previousPrice = pricingPhase.formattedPrice.calculatePreviousPrice(percent = percent),
        discountPercent = pricingPhase.formattedPrice.calculateDiscountPercent(percent = percent)
    )
}

private fun String.calculatePreviousPrice(percent: Int): String {
    val currency = currency()
    val decimalPart = decimalPart()
    var price = toBigBigDecimal()
    if (price.toInt() == 0) {
        price = BigDecimal(1)
    }
    val previousPrice = price
        .multiply(BigDecimal(100))
        .divide(
            BigDecimal(100 - percent),
            0,
            RoundingMode.HALF_UP
        )
        .toInt()

    return "$currency$previousPrice$decimalPart"
}

private fun String.calculateDiscountPercent(percent: Int): Int {
    val currentPrice = toBigBigDecimal()
    val decimalPart = decimalPart()
    val previousPrice = currentPrice.multiply(BigDecimal(100))
        .divide(
            BigDecimal(100 - percent),
            0,
            RoundingMode.HALF_UP
        )
        .toInt()
    return 100 - currentPrice
        .multiply(BigDecimal(100))
        .divide(
            BigDecimal("$previousPrice$decimalPart"),
            0,
            RoundingMode.HALF_UP
        )
        .toInt()
}

private fun String.toBigBigDecimal(): BigDecimal {
    val number = replace(currency(), "")
        .replace(",", "")
    return BigDecimal(number)
}

private fun String.currency(): String {
    return takeWhile { !it.isDigit() }
}

private fun String.decimalPart(): String {
    return split('.')
        .getOrNull(1)
        ?.let { decimal ->
            ".$decimal"
        }.orEmpty()
}