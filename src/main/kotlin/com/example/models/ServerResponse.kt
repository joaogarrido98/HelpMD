package com.example.models

import kotlinx.serialization.Serializable

/**
 * class that will be the response for every route in the server
 * return a success, a message and a unit in case there's one
 */
@Serializable
data class ServerResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)
