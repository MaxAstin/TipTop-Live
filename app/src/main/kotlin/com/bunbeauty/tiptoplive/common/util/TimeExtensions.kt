package com.bunbeauty.tiptoplive.common.util

import java.time.Instant.ofEpochSecond
import java.time.ZoneId.systemDefault
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter

data class Seconds(val value: Int) {

    operator fun minus(other: Seconds): Seconds {
        return Seconds(value = value - other.value)
    }
}

fun getCurrentTimeSeconds(): Seconds {
    val seconds = (System.currentTimeMillis() / 1000).toInt()
    return Seconds(value = seconds)
}

fun Seconds.toTimeString(): String {
    return ofEpochSecond(value.toLong())
        .atZone(UTC)
        .toLocalDateTime()
        .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}

fun Seconds.toDateString(): String {
    return ofEpochSecond(value.toLong())
        .atZone(systemDefault())
        .toLocalDate()
        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
}