package com.example

import com.example.database.DatabaseManager
import com.example.routes.doctorRoutes
import com.example.routes.patientHistoryRoutes
import com.example.routes.patientRoutes
import com.example.services.DoctorServices
import com.example.services.PatientServices
import com.example.tools.JwtManager
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    DatabaseManager.init()
    val patientServices = PatientServices()
    val doctorServices = DoctorServices()

    install(DefaultHeaders)

    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
            setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
                indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
                indentObjectsWith(DefaultIndenter("  ", "\n"))
            })
        }
    }

    install(Authentication) {
        /**
         * create patient authentication path with patient email validation
         */
        jwt("patient-interaction") {
            realm = System.getenv("JWT_REALM")
            verifier(JwtManager.verifier)
            validate {
                val payload = it.payload
                val patientEmail = payload.getClaim("patient_email").asString()
                val patient = patientServices.findPatientByEmail(patientEmail)
                patient
            }
        }

        /**
         * create patient authentication path with patient email validation
         */
        jwt("doctor-interaction") {
            realm = System.getenv("JWT_REALM")
            verifier(JwtManager.verifier)
            validate {
                val payload = it.payload
                val doctorId = payload.getClaim("doctor_id").asInt()
                val doctor = doctorServices.findDoctorById(doctorId)
                doctor
            }
        }
    }

    routing {
        patientRoutes(patientServices)
        patientHistoryRoutes(patientServices)
        doctorRoutes(doctorServices)
    }
}
