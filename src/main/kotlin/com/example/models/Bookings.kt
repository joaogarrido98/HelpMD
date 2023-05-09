package com.example.models


data class Bookings(
    val booking_id : Int? = null,
    val booking_doctor : Any? = null,
    val booking_patient : Any? = null,
    val booking_date_start : String,
    val booking_date_end : String,
    val booking_patient_id: Int? = null,
){
    fun isValid() : Boolean {
        return !(booking_date_start.isEmpty() || booking_date_end.isEmpty())
    }
}
