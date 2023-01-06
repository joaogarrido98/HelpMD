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
                val doctorList = activeDoctorServices.getActiveDoctors()
                call.respond(ServerResponse(true, "On call Doctors", doctorList))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get active doctors"))
            }
        }

        /**
         * check if doctor is active, if not send error message
         * if active remove doctor from active and send success message
         */
        post("active/doctor/join/{doctor}") {
            try {
                val doctor = call.parameters["doctor"]?.toInt()
                if (doctor != null) {
                    if (activeDoctorServices.isDoctorActive(doctor) == null) {
                        call.respond(ServerResponse(false, "Doctor is currently in-call"))
                        return@post
                    }
                    activeDoctorServices.deactivateDoctorStatus(doctor)
                    call.respond(ServerResponse(true, "Success"))
                } else {
                    call.respond(ServerResponse(false, "No doctor"))
                }
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to join this doctor"))
            }
        }
    }

    /**
     * update from inactive to active
     */
    authenticate("doctor-interaction") {
        post("active/doctor/activate") {
            try {
                val doctorId = call.principal<Doctor>()!!.doctor_id
                activeDoctorServices.activateDoctorStatus(doctorId)
                call.respond(ServerResponse(true, "Status updated"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to change doctor status"))
            }
        }

        /**
         * update from active to inactive and from inactive to active
         */
        post("active/doctor/deactivate") {
            try {
                val doctorId = call.principal<Doctor>()!!.doctor_id
                activeDoctorServices.deactivateDoctorStatus(doctorId)
                call.respond(ServerResponse(true, "Doctor status updated"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to change doctor status"))
            }
        }

        /**
         * check if specific doctor is active
         */
        get("is-active/doctor"){
            try {
                val doctorId = call.principal<Doctor>()!!.doctor_id
                val isDoctorActive = activeDoctorServices.isDoctorActive(doctorId)
                if(isDoctorActive != null){
                    call.respond(ServerResponse(true, "Active", true))
                }else{
                    call.respond(ServerResponse(true, "Not active", false))
                }
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to change doctor status"))
            }
        }
    }
}