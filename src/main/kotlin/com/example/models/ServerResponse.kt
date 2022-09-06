package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class ServerResponse(
    val success: Boolean,
    val message: String,
    val response: Unit? = null
)
