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
                val bookings = patient?.let { it1 -> bookingServices.getPatientBookings(it1) }
                call.respond(ServerResponse(true, "Bookings", bookings))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get bookings"))
            }
        }

        /**
         * get all the previous bookings for the user that requests
         */
        get("bookings/previous") {
            try {
                val patient = call.principal<Patient>()!!.patient_id
                val bookings = patient?.let { it1 -> bookingServices.getPreviousPatientBookings(it1) }
                call.respond(ServerResponse(true, "Bookings", bookings))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get bookings"))
            }
        }

        /**
         * get the result from the appointment
         */
        get("appointment/results") {
            try {
                val patient = call.principal<Patient>()!!.patient_id
                val appointmentResults = patient?.let { it1 -> bookingServices.getAppointmentResults(it1) }
                call.respond(ServerResponse(true, "Appointment Results", appointmentResults))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get appointment results"))
            }
        }

        /**
         * get the latest result from the appointment
         */
        get("appointment/results/latest") {
            try {
                val patient = call.principal<Patient>()!!.patient_id
                val appointmentResult = patient?.let { it1 -> bookingServices.getLatestResult(it1) }
                call.respond(ServerResponse(true, "Appointment Results", appointmentResult))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get appointment results"))
            }
        }

        /**
         * add booking route
         * check if request is valid.
         * Add booking to db
         */
        post("bookings/add") {
            val request = call.receive<Bookings>()
            if (!request.isValid()) {
                call.respond(ServerResponse(false, "Bad Request"))
                return@post
            }
            try {
                val booking = bookingServices.findBooking(request)
                if(booking == null){
                    bookingServices.addBookings(request)
                    call.respond(ServerResponse(true, "Booking added"))
                    return@post
                }
                call.respond(ServerResponse(false, "Booking already exists"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to add booking"))
            }
        }

        /**
         * get the single upcoming booking for the specified patient
         * if null return false else return the booking
         */
        get("bookings/upcoming") {
            try {
                val patient = call.principal<Patient>()!!.patient_id
                val bookings = patient?.let { it1 -> bookingServices.getUpcomingBookings(it1) }
                if (bookings == null) {
                    call.respond(ServerResponse(false, "No upcoming booking"))
                    return@get
                }
                call.respond(ServerResponse(true, "Upcoming Booking", bookings))
            } catch (e: Exception) {
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
        /**
         * get the upcoming bookings for the doctors
         */
        get("bookings/upcoming/doctor") {
            try {
                val doctor = call.principal<Doctor>()!!.doctor_id
                val bookings = doctor?.let { it1 -> bookingServices.getUpcomingBookingsDoctor(it1) }
                call.respond(ServerResponse(true, "Upcoming Bookings", bookings))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get booking"))
            }
        }


        /**
         * add appointment results
         */
        post("appointment/results/add") {
            val request = call.receive<AppointmentResult>()
            if (!request.isValid()) {
                call.respond(ServerResponse(false, "Bad Request"))
                return@post
            }
            try {
                bookingServices.addAppointmentResult(request)
                call.respond(ServerResponse(true, "Appointment Result added"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to add appointment result"))
            }
        }
    }
}