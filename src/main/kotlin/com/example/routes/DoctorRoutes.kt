package com.example.routes

import com.example.models.ServerResponse
import com.example.services.DoctorServices
import io.ktor.server.routing.*


fun Route.doctorRoutes(doctorServices: DoctorServices) {
    val response = ServerResponse(false, "Something went wrong", "")

}
