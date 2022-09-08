package com.example.routes

import com.example.models.Patient
import com.example.models.PatientLoginRequest
import com.example.models.PatientRegisterRequest
import com.example.models.ServerResponse
import com.example.services.PatientServices
import com.example.tools.HashingUtils
import com.example.tools.JwtManager
import com.example.tools.MessageUtils
import com.example.tools.ProjectUtils
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.patientRoutes(patientServices: PatientServices) {
    /**
     * login route logic
     * check if the request is valid
     * if yes check for the patient and password and match them if correct we return a jwt token and the patient object
     */
    post("/patient/login") {
        val request = call.receive<PatientLoginRequest>()
        if (!request.isValid()) {
            call.respond(ServerResponse(false, "Bad Request", ""))
            return@post
        }
        try {
            val patient: Patient? = patientServices.findPatientByEmail(request.patient_email)
            if (patient == null) {
                call.respond(ServerResponse(false, "No such user", ""))
                return@post
            }
            if (patient.patient_password != HashingUtils.hash(request.patient_password)) {
                call.respond(ServerResponse(false, "User email or password incorrect", ""))
                return@post
            }
            if (!patient.patient_active) {
                call.respond(ServerResponse(false, "Account not active", ""))
                return@post
            }
            call.respond(ServerResponse(true, JwtManager.generateTokenPatient(patient), patient))
        } catch (e: Exception) {
            call.respond(ServerResponse(false, "Unable to login", ""))
        }
    }

    /**
     * register route logic
     * if all data is valid then insert new patient into db and send email for account activation to patient
     */
    post("patient/register") {
        val request = call.receive<PatientRegisterRequest>()
        if (!request.isValid()) {
            call.respond(ServerResponse(false, "Bad Request", ""))
            return@post
        }
        try {
            val patient = patientServices.findPatientByEmail(request.patient_email)
            if (patient != null) {
                call.respond(ServerResponse(false, "Email already in use", ""))
                return@post
            }
            request.patient_password = HashingUtils.hash(request.patient_password)
            val code = ProjectUtils.generateRandomCode()
            patientServices.addPatient(request, code)
            MessageUtils.sendRegistrationEmail(request.patient_email, code)
            call.respond(ServerResponse(true, "Account created", ""))
        } catch (e: Exception) {
            call.respond(ServerResponse(false, "Unable to create account", ""))
        }
    }

    /**
     * This method checks the query parameter code and if is not empty
     * find the activation code in database, match it to the correct user and
     * activate that specific user
     */
    get("patient/activate") {
        val code = call.request.queryParameters["code"]
        if (code.isNullOrEmpty()) {
            call.respond("Sorry! Something went wrong")
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
}

/**
 * when done add an if message not sent then delete user
 * change from email
 * change email template
 */
