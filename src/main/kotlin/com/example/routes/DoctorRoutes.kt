package com.example.routes

import com.example.models.*
import com.example.services.DoctorServices
import com.example.tools.HashingUtils
import com.example.tools.JwtManager
import com.example.tools.MessageUtils
import com.example.tools.ProjectUtils
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

    /**
     * This method checks if request is valid and if so,
     * find the doctor that requested a new password and send it
     * to him through email, updating it and hashing it at the same time
     * in the database
     */
    post("doctor/password/recover") {
        val request = call.receive<DoctorRecoverPasswordRequest>()
        if (!request.isValid()) {
            call.respond(ServerResponse(false, "Bad Request"))
            return@post
        }
        try {
            val doctor = doctorServices.findDoctorByEmail(request.doctor_email)
            if (doctor == null) {
                call.respond(ServerResponse(false, "Doctor does not exist"))
                return@post
            }
            val password = ProjectUtils.generateRandomCode()
            val hashedPassword = HashingUtils.hash(password)
            MessageUtils.sendRecoverEmail(request.doctor_email, password)
            doctorServices.updatePassword(request.doctor_email, hashedPassword)
            call.respond(ServerResponse(true, "New Password Sent"))
        } catch (e: Exception) {
            call.respond(ServerResponse(false, "Unable to get new password"))
        }
    }


    /**
     * register a new doctor
     */
    post("doctor/register") {
        val request = call.receive<DoctorRegisterRequest>()
        if (!request.isValid()) {
            call.respond(ServerResponse(false, "Bad Request"))
            return@post
        }
        try {
            request.doctor_password = HashingUtils.hash(request.doctor_password)
            doctorServices.addDoctor(request)
            call.respond(ServerResponse(true, "Account created"))
        } catch (e: Exception) {
            call.respond(ServerResponse(false, "Unable to create account"))
        }
    }
}
