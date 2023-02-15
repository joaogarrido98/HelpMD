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
        post("bookings/add") {
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

        /**
         * get the single upcoming booking for the specified patient
         * if null return false else return the booking
         */
        get("bookings/upcoming") {
            try {
                val patient = call.principal<Patient>()!!.patient_id
                val bookings = bookingServices.getUpcomingBookings(patient)
                if (bookings == null) {
                    call.respond(ServerResponse(false, "No upcoming booking"))
                    return@get
                }
                call.respond(ServerResponse(true, "Upcoming Booking", bookings))
            } catch (e: Exception) {
                print(e.message.toString())
                call.respond(ServerResponse(false, "Unable to get booking"))
            }
        }


        /**
         * delete specific booking route
         */
        post("bookings/delete/{id}") {
            val bookingId = call.parameters["id"]?.toInt()
            try {
                bookingId?.let { it1 -> bookingServices.deleteBooking(it1) }
                call.respond(ServerResponse(true, "Booking deleted"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to delete booking"))

            }
        }
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