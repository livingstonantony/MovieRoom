package com.ell.movieroom

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform