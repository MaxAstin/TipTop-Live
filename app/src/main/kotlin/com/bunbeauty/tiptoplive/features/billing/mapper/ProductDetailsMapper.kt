package com.bunbeauty.tiptoplive.features.billing.mapper

import com.android.billingclient.api.ProductDetails
import com.bunbeauty.tiptoplive.features.billing.model.Product
import java.math.BigDecimal
import java.math.RoundingMode

enum class PriceFormat {
    DOTS_COMMA,
    COMMAS_DOT
}

private val dotsCommaRegex = Regex("^\\d{1,3}(\\.\\d{3})*(,\\d{1,2})?\$")
private val commasDotRegex = Regex("^\\d{1,3}(,\\d{3})*(\\.\\d{1,2})?\$")

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

fun String.calculatePreviousPrice(percent: Int): String? {
    return try {
        val format = getPriceFormat()
        var price = toBigBigDecimal(format = format)
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
        val formattedPreviousPrice = previousPrice.formatNumber(format = format)
        val decimalPart = decimalPart(format = format)

        "${getPrefix()}$formattedPreviousPrice$decimalPart${getPostfix()}"
    } catch (exception: Exception) {
        null
    }
}

fun String.calculateDiscountPercent(percent: Int): Int? {
    return try {
        val format = getPriceFormat()
        val currentPrice = toBigBigDecimal(format = format)
        val previousPrice = currentPrice.multiply(BigDecimal(100))
            .divide(
                BigDecimal(100 - percent),
                0,
                RoundingMode.HALF_UP
            )
            .toInt()
        val decimalPart = decimalPart(format = format)
            .replace(',', '.')

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

private fun String.getPriceFormat(): PriceFormat {
    val number = getNumber()
    return when {
        number.matches(dotsCommaRegex) -> {
            PriceFormat.DOTS_COMMA
        }

        number.matches(commasDotRegex) -> {
            PriceFormat.COMMAS_DOT
        }

        else -> error("Unknown format: $number")
    }
}

private fun String.toBigBigDecimal(format: PriceFormat): BigDecimal {
    var number = getNumber()
    number = when (format) {
        PriceFormat.DOTS_COMMA -> {
            number.replace(".", "")
                .replace(',', '.')
        }

        PriceFormat.COMMAS_DOT -> {
            number.replace(",", "")
        }
    }

    return BigDecimal(number)
}

private fun String.decimalPart(format: PriceFormat): String {
    val regex = when (format) {
        PriceFormat.DOTS_COMMA -> {
            dotsCommaRegex
        }

        PriceFormat.COMMAS_DOT -> {
            commasDotRegex
        }
    }

    val number = getNumber()
    return regex.matchEntire(number)
        ?.groups
        ?.get(2)
        ?.value ?: ""
}

private fun Int.formatNumber(format: PriceFormat): String {
    val thousandsDivider = when (format) {
        PriceFormat.DOTS_COMMA -> '.'
        PriceFormat.COMMAS_DOT -> ','
    }
    return "%,d".format(this)
        .replace(' ', thousandsDivider)
}

private fun String.getNumber(): String {
    return dropWhile {
        !it.isDigit()
    }.dropLastWhile {
        !it.isDigit()
    }
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