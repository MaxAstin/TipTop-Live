package com.bunbeauty.tiptoplive.features.billing.model

data class Product(
    val id: String,
    val offerToken: String?,
    val name: String,
    val currentPrice: String,
    val previousPrice: String?,
    val discountPercent: Int?
)