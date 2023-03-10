package com.example.routes

import com.example.models.*
import com.example.services.PatientServices
import com.example.tools.HashingUtils
import com.example.tools.JwtManager
import com.example.tools.MessageUtils
import com.example.tools.ProjectUtils
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.head
import kotlinx.html.*


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

    static("static-resources") { resources("css") }

    get("patient/terms") {
        call.respondHtml {
            head {
                title { +"Terms&Conditions" }
                styleLink("/static-resources/main.css")
                meta("viewport", content = "width=device-width, initial-scale=1", "utf-8")
            }
            body {
                h1 {
                    +"Terms&Conditions"
                }
                p {
                    +"Welcome to HelpMD!"
                    br
                    br
                    +"These terms and conditions outline the rules and regulations for the use of HelpMD's app."
                    br
                    br
                    +"By accessing this app we assume you accept these terms and conditions. Do not continue to use "
                    +"HelpMD if you do not agree to take all of the terms and conditions stated on this page."
                }
                h2{
                    +"License"
                }
                p{
                    +"Unless otherwise stated, HelpMD owns the intellectual property rights for all material on "
                    + "HelpMD. All intellectual property rights are reserved. You may access this from HelpMD "
                    + "for your own personal use subjected to restrictions set in these terms and "
                    + "conditions."
                }
                p{
                    +"You must not:"
                }
                ul {
                    li {
                        +"Republish material from HelpMD"
                    }
                    li {
                        +"Reproduce, duplicate or copy material from HelpMD"
                    }
                    li {
                        +"Redistribute content from HelpMD"
                    }
                }
                p{
                    +"This Agreement shall begin on the date hereof."
                }
                p{
                    +"HelpMD reserves the right to monitor all accounts and to remove any accounts which can be "
                    + "considered inappropriate, offensive or causes breach of these Terms and Conditions."
                }
            }
        }
    }

    get("patient/privacy") {
        call.respondHtml {
            head {
                title { +"Privacy Policy" }
                styleLink("/static-resources/main.css")
                meta("viewport", content = "width=device-width, initial-scale=1", "utf-8")
                link("./icon.png", "icon", "image/svg+xml")
            }
            body {
                div("main") {
                    div {
                        h1 {
                            +"Privacy Policy"
                        }
                        p {
                            +"Welcome to HelpMD!\n"
                            +"These terms and conditions outline the rules and regulations for the use of HelpMD's "
                            +"app.\n"
                            +"By accessing this website we assume you accept these terms and conditions. Do not continue to use HelpMD"
                            +" if you do not agree to take all of the terms and conditions stated on this page.\n"
                        }
                    }
                }
            }
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
                val patient = call.principal<Patient>()
                if (patient != null) {
                    if (patient.patient_password == HashingUtils.hash(request.patient_old_password)) {
                        val password = HashingUtils.hash(request.patient_password)
                        patientServices.updatePassword(patient.patient_email, password)
                        call.respond(ServerResponse(true, "Password Updated"))
                        return@post
                    }
                    call.respond(ServerResponse(false, "Old password does not match"))
                    return@post
                }
                call.respond(ServerResponse(false, "Unable to update password"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to update password"))
            }
        }


        /**
         * This method deactivates the user
         */
        post("patient/deactivate") {
            try {
                val patient = call.principal<Patient>()
                if (patient != null) {
                    patientServices.deactivatePatient(patient.patient_email)
                    call.respond(ServerResponse(true, "Account Deactivated"))
                    return@post
                }
                call.respond(ServerResponse(false, "Unable to deactivate account"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to deactivate account"))
            }
        }


        /**
         * get all the data associated with the user
         */
        get("patient/data"){
            try {
                val patient = call.principal<Patient>()
                if (patient != null) {
                    val data = patientServices.getAllData(patient.patient_id)
                    println(data)
                    call.respond(ServerResponse(true, "My Data", data))
                }
                call.respond(ServerResponse(false, "Unable to get data"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get data"))
            }
        }
    }
}

/**
 * when done add an if message not sent then delete user ON REGISTER
 */
