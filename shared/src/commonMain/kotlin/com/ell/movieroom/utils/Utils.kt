package com.ell.movieroom.utils

fun String.toSeconds(): Long {
    return runCatching {
        val cleaned = trim()
        if (cleaned.isEmpty()) return 0L

        val parts = cleaned
            .split(":")
            .map { it.trim() }

        // Must be mm:ss or hh:mm:ss
        if (parts.size !in 2..3) return 0L
        if (parts.any { it.isEmpty() }) return 0L

        val numbers = parts.map {
            val value = it.toLong()
            if (value < 0) return 0L
            value
        }

        fun safeMultiply(a: Long, b: Long): Long {
            if (a != 0L && b > Long.MAX_VALUE / a) return 0L
            return a * b
        }

        fun safeAdd(a: Long, b: Long): Long {
            if (a > Long.MAX_VALUE - b) return 0L
            return a + b
        }

        when (numbers.size) {
            2 -> {
                val (min, sec) = numbers
                safeAdd(safeMultiply(min, 60), sec)
            }

            3 -> {
                val (hour, min, sec) = numbers
                safeAdd(
                    safeAdd(
                        safeMultiply(hour, 3600),
                        safeMultiply(min, 60)
                    ),
                    sec
                )
            }

            else -> 0L
        }
    }.getOrDefault(0L)
}

fun Long.toVideoTime(): String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    fun pad2(value: Long): String =
        if (value < 10) "0$value" else value.toString()

    return if (hours > 0) {
        "${pad2(hours)}:${pad2(minutes)}:${pad2(seconds)}"
    } else {
        "${pad2(minutes)}:${pad2(seconds)}"
    }
}


fun Long.toVideoTimeRounded(): String {
    // round to nearest second
    val totalSeconds = (this + 500) / 1000

    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    fun pad2(value: Long): String =
        if (value < 10) "0$value" else value.toString()

    return if (hours > 0) {
        "${pad2(hours)}:${pad2(minutes)}:${pad2(seconds)}"
    } else {
        "${pad2(minutes)}:${pad2(seconds)}"
    }
}


