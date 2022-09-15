package com.example.routes

import com.example.models.*
import com.example.services.PatientServices
import com.example.tools.HashingUtils
import com.example.tools.JwtManager
import com.example.tools.MessageUtils
import com.example.tools.ProjectUtils
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.patientRoutes(patientServices: PatientServices) {
    /**
     * login route logic
     * check if the request is valid
     * if yes check for the patient and password and match them
     * if correct we return a jwt token and the patient object
     */
    post("/patient/login") {
        val request = call.receive<PatientLoginRequest>()
        if (!request.isValid()) {
            call.respond(ServerResponse(false, "Bad Request"))
            return@post
        }
        try {
            val patient: Patient? = patientServices.findPatientByEmail(request.patient_email)
            if (patient == null) {
                call.respond(ServerResponse(false, "User does not exist"))
                return@post
            }
            if (patient.patient_password != HashingUtils.hash(request.patient_password)) {
                call.respond(ServerResponse(false, "User email or password incorrect"))
                return@post
            }
            if (!patient.patient_active) {
                call.respond(ServerResponse(false, "Account not active"))
                return@post
            }
            call.respond(ServerResponse(true, JwtManager.generateTokenPatient(patient), patient))
        } catch (e: Exception) {
            call.respond(ServerResponse(false, "Unable to login"))
        }
    }


    /**
     * register route logic
     * if all data is valid then insert new patient into db and send email
     * for account activation to patient
     */
    post("patient/register") {
        val request = call.receive<PatientRegisterRequest>()
        if (!request.isValid()) {
            call.respond(ServerResponse(false, "Bad Request"))
            return@post
        }
        try {
            val patient = patientServices.findPatientByEmail(request.patient_email)
            if (patient != null) {
                call.respond(ServerResponse(false, "Email already in use"))
                return@post
            }
            request.patient_password = HashingUtils.hash(request.patient_password)
            val code = ProjectUtils.generateRandomCode()
            val doctor = patientServices.assignDoctor(request.patient_deaf)
            patientServices.addPatient(request, code, doctor)
            MessageUtils.sendRegistrationEmail(request.patient_email, code)
            call.respond(ServerResponse(true, "Account created, please check your email"))
        } catch (e: Exception) {
            call.respond(ServerResponse(false, "Unable to create account"))
        }
    }

    /**
     * This method checks the query parameter code and if is not empty
     * find the activation code in database, match it to the correct patient and
     * activate that specific patient
     */
    get("patient/activate") {
        val code = call.request.queryParameters["code"]
        if (code.isNullOrEmpty()) {
            call.respond("Bad Request")
            return@get
        }
        try {
            val activationRow = patientServices.findActivationCode(code)
            if (activationRow == null) {
                call.respond("Activation Code not valid")
                return@get
            }
            val patient = patientServices.findPatientByEmail(activationRow.patient_email)
            if (patient == null) {
                call.respond("User invalid")
                return@get
            }
            patientServices.activatePatient(patient.patient_email, code)
            call.respond("Account Activated")
        } catch (e: Exception) {
            call.respond("Unable to activate account")
        }
    }

    /**
     * This method checks if request is valid and if so,
     * find the patient that requested a new password and send it
     * to him through email, updating it and hashing it at the same time
     * in the database
     */
    post("patient/password/recover") {
        val request = call.receive<PatientRecoverPasswordRequest>()
        if (!request.isValid()) {
            call.respond(ServerResponse(false, "Bad Request"))
            return@post
        }
        try {
            val patient = patientServices.findPatientByEmail(request.patient_email)
            if (patient == null) {
                call.respond(ServerResponse(false, "User does not exist"))
                return@post
            }
            val password = ProjectUtils.generateRandomCode()
            val hashedPassword = HashingUtils.hash(password)
            MessageUtils.sendRecoverEmail(request.patient_email, password)
            patientServices.updatePassword(request.patient_email, hashedPassword)
            call.respond(ServerResponse(true, "New Password Sent"))
        } catch (e: Exception) {
            call.respond(ServerResponse(false, "Unable to send new password"))
        }
    }


    /**
     * This method deactivates the user=
     */
    get("patient/active") {
        val email = "joao.melo.garrido@gmail.com"
        try {
            patientServices.active(email)
            call.respond(ServerResponse(true, "Account Deactivated"))
        } catch (e: Exception) {
            call.respond(ServerResponse(false, "Unable to deactivate account"))
        }
    }

    /**
     * All routes inside this authenticate block only allow patient to
     * actually interact with server if they are authenticated
     */
    authenticate("patient-interaction") {
        /**
         * This method gets a new password in the request
         * and changes the current password to the new password in the
         * database where the jwt token is the same as the patient
         */
        post("patient/password/change") {
            val request = call.receive<PatientChangePasswordRequest>()
            if (!request.isValid()) {
                call.respond(ServerResponse(false, "Bad request"))
                return@post
            }
            try {
                val email = call.principal<Patient>()!!.patient_email
                val password = HashingUtils.hash(request.patient_password)
                patientServices.updatePassword(email, password)
                call.respond(ServerResponse(true, "Password Updated"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to update password"))
            }
        }


        /**
         * This method deactivates the user=
         */
        post("patient/deactivate") {
            try {
                val email = call.principal<Patient>()!!.patient_email
                patientServices.deactivatePatient(email)
                call.respond(ServerResponse(true, "Account Deactivated"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to deactivate account"))
            }
        }
    }
}

/**
 * when done add an if message not sent then delete user ON REGISTER
 */
