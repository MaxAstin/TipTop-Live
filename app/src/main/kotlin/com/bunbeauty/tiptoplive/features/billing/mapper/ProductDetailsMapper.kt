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

private fun String.calculatePreviousPrice(percent: Int): String? {
    return try {
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

        "${getPrefix()}$previousPrice$decimalPart${getPostfix()}"
    } catch (exception: Exception) {
        null
    }
}

private fun String.calculateDiscountPercent(percent: Int): Int? {
    return try {
        val currentPrice = toBigBigDecimal()
        val decimalPart = decimalPart()
            .replace(',', '.')
        val previousPrice = currentPrice.multiply(BigDecimal(100))
            .divide(
                BigDecimal(100 - percent),
                0,
                RoundingMode.HALF_UP
            )
            .toInt()
        100 - currentPrice
            .multiply(BigDecimal(100))
            .divide(
                BigDecimal("$previousPrice$decimalPart"),
                0,
                RoundingMode.HALF_UP
            )
            .toInt()
    } catch (exception: Exception) {
        null
    }
}

private fun String.toBigBigDecimal(): BigDecimal {
    var number = replace(getPrefix(), "")
        .replace(getPostfix(), "")
    number = if (number.contains('.') || number.count { it == ',' } > 1) {
        number.replace(",", "")
    } else {
        number.replace(',', '.')
    }
    return BigDecimal(number)
}

private fun String.getPrefix(): String {
    return takeWhile { char ->
        !char.isDigit()
    }
}

private fun String.getPostfix(): String {
    return takeLastWhile { char ->
        !char.isDigit()
    }
}

private fun String.decimalPart(): String {
    val lastDotIndex = lastIndexOf('.')
    val lastCommaIndex = lastIndexOf(',')
    val (index, divider) = if (lastDotIndex > lastCommaIndex) {
        lastDotIndex to '.'
    } else {
        lastCommaIndex to ','
    }

    val digits = substring(index + 1, length)
        .filter { it.isDigit() }
    return "$divider$digits"
}