package com.example.models


data class Bookings(
    val booking_id : Int,
    val booking_doctor : String,
    val booking_patient : Int,
    val booking_date_start : String,
    val booking_date_end : String,
)

data class BookingsDoctor(
    val booking_id : Int,
    val booking_doctor : Int,
    val booking_patient : String,
    val booking_date_start : String,
    val booking_date_end : String,
    val booking_patient_id: Int
)

data class AddBookingsRequest(
    val booking_doctor: Int,
    val booking_patient : Int,
    val booking_date_start : String,
    val booking_date_end : String,
){
    fun isValid() : Boolean {
        return !(booking_date_start.isEmpty() || booking_date_end.isEmpty())
    }
}