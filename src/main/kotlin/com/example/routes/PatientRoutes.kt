package com.example.routes

import com.example.models.PatientLoginRequest
import com.example.services.PatientServices
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.patientRoutes(patientServices: PatientServices) {
    post("/patient/login") {
        val patient: PatientLoginRequest
        try {
            patient = call.receive()
            if (!patient.isValid()) {
                call.respond("not valid")
            }
            call.respond("valid")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}
