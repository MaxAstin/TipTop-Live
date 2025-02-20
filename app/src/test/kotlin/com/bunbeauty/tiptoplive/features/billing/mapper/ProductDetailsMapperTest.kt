package com.bunbeauty.tiptoplive.features.billing.mapper

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.stream.Stream

class ProductDetailsMapperTest {

    companion object {
        @JvmStatic
        fun mappingData(): Stream<Pair<String, String?>> = Stream.of(
            "1 EUR" to "2 EUR",
            "1.00 EUR" to "2.00 EUR",
            "100 EUR" to "167 EUR",
            "100.00 EUR" to "167.00 EUR",
            "1,234.00 EUR" to "2,057.00 EUR",
            "100,234.00 EUR" to "167,057.00 EUR",
            "1,123,123 EUR" to "1,871,872 EUR",
            "1,123,123.00 EUR" to "1,871,872.00 EUR",
            "1,00 EUR" to "2,00 EUR",
            "100,00 EUR" to "167,00 EUR",
            "1.234,00 EUR" to "2.057,00 EUR",
            "100.234,00 EUR" to "167.057,00 EUR",
            "1.123.123 EUR" to "1.871.872 EUR",
            "1.123.123,00 EUR" to "1.871.872,00 EUR",
            "" to null,
        )
    }

    @ParameterizedTest
    @MethodSource("mappingData")
    fun `test previous price calculation`(data: Pair<String, String?>) {
        val (input, expected) = data
        val previousPrice = input.calculatePreviousPrice(percent = 40)
        assertEquals(expected, previousPrice)
    }

}