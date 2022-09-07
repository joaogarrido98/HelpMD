package com.example

import com.example.database.DatabaseManager
import com.example.routes.doctorRoutes
import com.example.routes.patientRoutes
import com.example.services.DoctorServices
import com.example.services.PatientServices
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(Authentication) {
    }
    install(DefaultHeaders)

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
        })
    }
    DatabaseManager.init()
    val patientServices = PatientServices()
    val doctorServices = DoctorServices()

    routing {
        patientRoutes(patientServices)
        doctorRoutes(doctorServices)
    }
}
