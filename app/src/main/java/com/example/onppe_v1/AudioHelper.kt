package com.example.onppe_v1

import java.util.*
import java.util.concurrent.TimeUnit

fun formatTimeUnit(timeInMilliseconds: Long): String {
    return try {
        String.format(
            Locale.FRANCE,
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds),
            TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds)
            )
        )
    } catch (e: Exception) {
        "00:00"
    }
}