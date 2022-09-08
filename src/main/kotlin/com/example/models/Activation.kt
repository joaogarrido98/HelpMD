package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Activation(
    val patient_email: String,
    val activation_code: String
)
