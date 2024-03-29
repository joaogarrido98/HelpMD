package com.example.routes

import com.example.models.Patient
import com.example.models.PatientHistory
import com.example.models.ServerResponse
import com.example.services.HistoryServices
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.patientHistoryRoutes(historyServices: HistoryServices) {
    authenticate("patient-interaction") {
        /**
         * this method gets a request for adding a medical history
         *  where the jwt token verifies to be the same as the patient
         */
        post("patient/history") {
            val request = call.receive<PatientHistory>()
            if (!request.isValid()) {
                call.respond(ServerResponse(false, "Bad request"))
                return@post
            }
            try {
                val patientId = call.principal<Patient>()!!.patient_id
                if (patientId != null) {
                    historyServices.addPatientHistory(request, patientId)
                }
                call.respond(ServerResponse(true, "Medical history added"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to add patient history"))
            }
        }

        /**
         * this method finds a returns the patient history for the patient that its asking
         */
        get("patient/history") {
            try {
                val patientId = call.principal<Patient>()!!.patient_id
                val history = patientId?.let { it1 -> historyServices.findPatientHistory(it1) }
                if (history == null) {
                    call.respond(ServerResponse(true, "No medical history", null))
                    return@get
                }
                call.respond(ServerResponse(true, "Medical History", history))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get patient history"))
            }
        }
    }

    authenticate("doctor-interaction") {
        /**
         * get patient medical history
         */
        get("patient/history/{patient_id}") {
            val patientId = call.parameters["patient_id"]!!.toInt()
            try {
                val patientHistory = historyServices.findPatientHistory(patientId)
                call.respond(ServerResponse(true, "Patient History", patientHistory))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get patient history"))
            }
        }
    }
}