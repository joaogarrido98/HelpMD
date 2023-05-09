package com.example

import com.example.database.DatabaseManager
import com.example.routes.*
import com.example.services.*
import com.example.tools.JwtManager
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import java.text.DateFormat

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    DatabaseManager.init()
    val patientServices = PatientServices()
    val doctorServices = DoctorServices()
    val historyServices = HistoryServices()
    val prescriptionsServices = PrescriptionsServices()
    val bookingServices = BookingServices()
    val scheduleServices = ScheduleServices()

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
    }

    install(DefaultHeaders)

    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
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
        patientHistoryRoutes(historyServices)
        doctorRoutes(doctorServices)
        prescriptionsRoutes(prescriptionsServices)
        bookingRoutes(bookingServices)
        schedulesRoutes(scheduleServices)
    }
}
