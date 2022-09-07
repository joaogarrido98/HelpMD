package com.example.routes

import com.example.models.Patient
import com.example.models.PatientLoginRequest
import com.example.models.ServerResponse
import com.example.services.PatientServices
import com.example.tools.JwtManager
import com.example.tools.ProjectUtils
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.patientRoutes(patientServices: PatientServices) {
    val response = ServerResponse(false, "Something went wrong", "")

    post("/patient/login") {
        val request: PatientLoginRequest
        try {
            request = call.receive()
            if (!request.isValid()) {
                call.respond(response)
                return@post
            }
            try {
                val patient: Patient? = patientServices.findPatientByEmail(request.patient_email)
                if (patient == null || patient.patient_password == ProjectUtils.hash(request.patient_password)) {
                    if (!patient!!.patient_active) {
                        call.respond(ServerResponse(false, "Account not active", null))
                        return@post
                    }
                    call.respond(ServerResponse(true, JwtManager.generateTokenPatient(patient), patient))
                } else {
                    call.respond(ServerResponse(false, "User email or password incorrect", null))
                }
            } catch (e: Exception) {
                call.respond(response)
            }
        } catch (e: Exception) {
            call.respond(response)
            return@post
        }
    }
}
