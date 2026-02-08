package com.ell.movieroom.utils


object Utils {

    fun getSeconds(time: String): Long {
        return runCatching {
            val cleaned = time.trim()
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
}

