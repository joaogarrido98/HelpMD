package com.example.routes

import com.example.models.AddPatientHistoryRequest
import com.example.models.Patient
import com.example.models.ServerResponse
import com.example.services.PatientServices
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.patientHistoryRoutes(patientServices: PatientServices) {
    authenticate("patient-interaction") {
        /**
         * this method gets a request for adding a medical history
         *  where the jwt token verifies to be the same as the patient
         */
        post("patient/history") {
            val request = call.receive<AddPatientHistoryRequest>()
            if (!request.isValid()) {
                call.respond(ServerResponse(false, "Bad request"))
                return@post
            }
            try {
                //if already exists dont let it put there
                val patientId = call.principal<Patient>()!!.patient_id
                patientServices.addPatientHistory(request, patientId)
                call.respond(ServerResponse(true, "Medical history added"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to add patient history"))
            }
        }
    }

    authenticate("doctor-interaction") {
        get("patient/history/{patient_id}") {
            try {

            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get patient history"))
            }
        }
    }
}