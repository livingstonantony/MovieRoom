package com.ell.movieroom.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(val from: String, val message: String)