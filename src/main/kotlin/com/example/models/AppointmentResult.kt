package com.example.models

data class AppointmentResult(
    val result_id: Int,
    val booking_id: Int,
    val result: String
)

data class AddAppointmentResult(
    val booking_id: Int,
    val result: String
) {
    fun isValid(): Boolean {
        return !(result.isEmpty() || booking_id < 1)
    }
}