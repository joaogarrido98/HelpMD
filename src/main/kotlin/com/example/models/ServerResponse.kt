package com.example.models

/**
 * class that will be the response for every route in the server
 * return a success, a message and a unit in case there's one
 */

data class ServerResponse(
    val success: Boolean,
    val message: String,
    val data: Any? = null
)
