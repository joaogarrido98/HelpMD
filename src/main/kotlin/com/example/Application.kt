package com.example

import com.example.database.DatabaseManager
import com.example.routes.*
import com.example.services.*
import com.example.tools.JwtManager
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import java.text.DateFormat
import java.time.Duration

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    DatabaseManager.init()
    val patientServices = PatientServices()
    val doctorServices = DoctorServices()
    val historyServices = HistoryServices()
    val activeDoctorServices = ActiveDoctorServices()
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

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = JacksonWebsocketContentConverter()
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
        activeDoctorRoutes(activeDoctorServices)
        prescriptionsRoutes(prescriptionsServices)
        bookingRoutes(bookingServices)
        schedulesRoutes(scheduleServices)
    }
}
