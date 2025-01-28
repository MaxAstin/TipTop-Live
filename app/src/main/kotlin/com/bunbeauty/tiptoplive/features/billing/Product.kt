package com.bunbeauty.tiptoplive.features.billing

data class Product(
    val id: String,
    val name: String,
    val currentPrice: String,
    val previousPrice: String?,
    val discountPercent: Int?
)