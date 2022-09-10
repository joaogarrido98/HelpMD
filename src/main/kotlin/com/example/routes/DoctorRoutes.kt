package com.example.routes

import com.example.models.*
import com.example.services.DoctorServices
import com.example.tools.HashingUtils
import com.example.tools.JwtManager
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.doctorRoutes(doctorServices: DoctorServices) {
    /**
     * login route logic
     * check if the request is valid
     * if yes check for the doctor and password and match them
     * if correct we return a jwt token and the doctor object
     */
    post("doctor/login") {
        val request = call.receive<DoctorLoginRequest>()
        if (!request.isValid()) {
            call.respond(ServerResponse(false, "Bad Request"))
            return@post
        }
        try {
            val doctor: Doctor? = doctorServices.findDoctorByEmail(request.doctor_email)
            if (doctor == null) {
                call.respond(ServerResponse(false, "User does not exist"))
                return@post
            }
            if (doctor.doctor_password != HashingUtils.hash(request.doctor_password)) {
                call.respond(ServerResponse(false, "User email or password incorrect"))
                return@post
            }
            call.respond(ServerResponse(true, JwtManager.generateTokenDoctor(doctor), doctor))
        } catch (e: Exception) {
            call.respond(ServerResponse(false, "Unable to login"))
        }
    }

}
