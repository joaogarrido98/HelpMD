package com.example.routes

import com.example.models.*
import com.example.services.DoctorServices
import com.example.tools.HashingUtils
import com.example.tools.JwtManager
import com.example.tools.MessageUtils
import com.example.tools.ProjectUtils
import io.ktor.server.application.*
import io.ktor.server.auth.*
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
        val request = call.receive<Doctor>()
        if (!request.isLoginValid()) {
            call.respond(ServerResponse(false, "Bad Request"))
            return@post
        }
        try {
            val doctor: Doctor? = request.doctor_email?.let { it1 -> doctorServices.findDoctorByEmail(it1) }
            if (doctor == null) {
                call.respond(ServerResponse(false, "Doctor does not exist"))
                return@post
            }
            if (doctor.doctor_password != request.doctor_password?.let { it1 -> HashingUtils.hash(it1) }) {
                call.respond(ServerResponse(false, "Email or password incorrect"))
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
        val request = call.receive<Doctor>()
        if (!request.isRegisterValid()) {
            call.respond(ServerResponse(false, "Bad Request"))
            return@post
        }
        try {
            val doctor = request.doctor_email?.let { it1 -> doctorServices.findDoctorByEmail(it1) }
            if (doctor == null) {
                call.respond(ServerResponse(false, "Doctor does not exist"))
                return@post
            }
            val password = ProjectUtils.generateRandomCode()
            val hashedPassword = HashingUtils.hash(password)
            request.doctor_email.let { it1 -> MessageUtils.sendRecoverEmail(it1, password) }
            request.doctor_email.let { it1 -> doctorServices.updatePassword(it1, hashedPassword) }
            call.respond(ServerResponse(true, "New Password Sent"))
        } catch (e: Exception) {
            call.respond(ServerResponse(false, "Unable to get new password"))
        }
    }


    /**
     * register a new doctor
     */
    post("doctor/register") {
        val request = call.receive<Doctor>()
        if (!request.isRecoverValid()) {
            call.respond(ServerResponse(false, "Bad Request"))
            return@post
        }
        try {
            request.doctor_password = request.doctor_password?.let { it1 -> HashingUtils.hash(it1) }
            doctorServices.addDoctor(request)
            call.respond(ServerResponse(true, "Account created"))
        } catch (e: Exception) {
            call.respond(ServerResponse(false, "Unable to create account"))
        }
    }

    authenticate("doctor-interaction"){
        /**
         * get all patients for the specific doctor
         */
        get("doctor/patients"){
            try{
                val doctor = call.principal<Doctor>()!!.doctor_id
                val patients = doctor?.let { it1 -> doctorServices.getDoctorPatients(it1) }
                call.respond(ServerResponse(true, "Doctor Patients", patients))
            }catch (e:Exception){
                call.respond(ServerResponse(false,"Unable to get patients"))
            }
        }

        /**
         * get specific patient searched
         */
        get("doctor/patient/{id}"){
            val patientId = call.parameters["id"]?.toInt()
            try{
                val patient = patientId?.let { it1 -> doctorServices.findPatientById(it1) }
                call.respond(ServerResponse(true, "Patient found", patient))
            }catch (e:Exception){
                call.respond(ServerResponse(false,"Unable to get patient"))
            }
        }
    }
}
