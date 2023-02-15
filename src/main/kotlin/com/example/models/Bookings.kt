package com.example.models


data class Bookings(
    val booking_id : Int,
    val booking_doctor : String,
    val booking_patient : Int,
    val booking_date : String,
)

data class AddBookingsRequest(
    val booking_doctor: Int,
    val booking_patient : Int,
    val booking_date : String,
){
    fun isValid() : Boolean {
        return booking_date.isNotEmpty()
    }
}