package com.example.models

import io.ktor.server.auth.*

data class Doctor(
    val doctor_id: Int
) : Principal
