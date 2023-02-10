package com.example.models

import java.util.*

data class Bookings(
    val booking_id : Int,
    val booking_doctor : String,
    val booking_patient : Int,
    val booking_date : String,
    val booking_start : String,
    val booking_end: String
)

data class AddBookingsRequest(
    val booking_doctor: Int,
    val booking_patient : Int,
    val booking_date : String,
    val booking_start : String,
    val booking_end: String
){
    fun isValid() : Boolean {
        return !(booking_date.isEmpty() || booking_end.isEmpty() || booking_start.isEmpty() )
    }
}