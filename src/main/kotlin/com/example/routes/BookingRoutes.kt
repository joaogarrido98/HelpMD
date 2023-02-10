package com.example.routes

import com.example.models.*
import com.example.services.BookingServices
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.bookingRoutes(bookingServices: BookingServices) {
    authenticate("patient-interaction") {

        /**
         * get all the bookings for the user that requests
         */
        get("bookings/patient") {
            try {
                val patient = call.principal<Patient>()!!.patient_id
                val bookings = bookingServices.getPatientBookings(patient)
                call.respond(ServerResponse(true, "Bookings", bookings))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get bookings"))
            }
        }

        /**
         * add booking route
         * check if request is valid.
         * Add booking to db
         */
        post("bookings"){
            val request = call.receive<AddBookingsRequest>()
            if (!request.isValid()) {
                call.respond(ServerResponse(false, "Bad Request"))
                return@post
            }
            try {
                bookingServices.addBookings(request)
                call.respond(ServerResponse(true, "Booking added"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to add prescription"))
            }
        }


        //delete booking for user
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