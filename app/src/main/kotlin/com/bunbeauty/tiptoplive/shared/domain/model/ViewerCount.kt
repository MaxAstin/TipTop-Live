package com.bunbeauty.tiptoplive.shared.domain.model

enum class ViewerCount(
    val text: String,
    val min: Int,
    val max: Int,
) {
    V_100_200(
        text = "100–200",
        min = 100,
        max = 200,
    ),
    V_400_500(
        text = "300–500",
        min = 300,
        max = 500,
    ),
    V_1K_2K(
        text = "1K–2K",
        min = 1_000,
        max = 2_000,
    ),
    V_5K_10K(
        text = "5K–10K",
        min = 5_000,
        max = 10_000,
    ),
    V_20K_50K(
        text = "20K–50K",
        min = 20_000,
        max = 50_000,
    ),
    V_100K_200K(
        text = "100K–200K",
        min = 100_000,
        max = 200_000,
    ),
    V_500K_1M(
        text = "500K–1M",
        min = 500_000,
        max = 1_000_000,
    )
}