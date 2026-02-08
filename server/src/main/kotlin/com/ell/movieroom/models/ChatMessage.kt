package com.ell.movieroom.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(val from: String, val message: String)

@Serializable
data class ClientMessage(val message: String) // message received from client