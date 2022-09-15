package com.example.routes

import com.example.models.Doctor
import com.example.models.ServerResponse
import com.example.services.ActiveDoctorServices
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.activeDoctorRoutes(activeDoctorServices: ActiveDoctorServices) {
    authenticate("patient-interaction") {
        /**
         * get all active doctors
         */
        get("active/doctor") {
            try {

            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get active doctors"))
            }
        }
    }


    authenticate("doctor-interaction") {
        /**
         * update from active to inactive and from inactive to active
         */
        post("active/doctor/change-status") {
            try {
                val doctorId = call.principal<Doctor>()!!.doctor_id
                activeDoctorServices.updateDoctorStatus(doctorId)
                call.respond(ServerResponse(true, "Doctor status updated"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to change doctor status"))
            }
        }
    }
}