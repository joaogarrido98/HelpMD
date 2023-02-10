package com.example.routes

import com.example.models.Patient
import com.example.models.ServerResponse
import com.example.services.BookingServices
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.bookingRoutes(bookingServices: BookingServices) {
    authenticate("patient-interaction") {

        //get bookings for user
        get("bookings/patient") {
            try {
                val patient = call.principal<Patient>()!!.patient_id
                val bookings = bookingServices.getPatientBookings(patient)
                call.respond(ServerResponse(true, "Bookings", bookings))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get bookings"))
            }
        }

        //post booking for user
        post("bookings"){
            try {

            }catch (e:Exception){
                call.respond(ServerResponse(false, "Unable to add booking"))
            }
        }
        //delete booking for user

        //
    }

    authenticate("doctor-interaction") {
        get("bookings/doctor") {
            try {
                val patient = call.principal<Patient>()!!.patient_id

            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get bookings"))
            }
        }
    }
}