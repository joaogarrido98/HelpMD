package com.example.models

data class Bookings(
    val booking_id : Int,
    val booking_doctor : String,
    val booking_patient : Int,
    val booking_date : String,
    val booking_start : String,
    val booking_end: String
)