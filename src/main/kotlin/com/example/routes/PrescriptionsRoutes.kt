package com.example.routes

import com.example.models.*
import com.example.services.PrescriptionsServices
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*

fun Route.prescriptionsRoutes(prescriptionsServices: PrescriptionsServices) {
    static("static-resources") { resources("css") }


    authenticate("doctor-interaction") {
        /**
         * add prescription route
         * check if request is valid then gets the doctor that it's adding.
         * Add prescription to db
         */
        post("prescriptions/add") {
            val request = call.receive<PrescriptionsAddRequest>()
            if (!request.isValid()) {
                call.respond(ServerResponse(false, "Bad Request"))
                return@post
            }
            try {
                val doctor = call.principal<Doctor>()!!.doctor_id
                request.prescription_doctor = doctor
                prescriptionsServices.addPrescription(request)
                call.respond(ServerResponse(true, "Prescription added"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to add prescription"))
            }
        }
    }

    authenticate("patient-interaction") {
        /**
         * get all the prescriptions for a specific patient
         */
        get("prescriptions/{type}") {
            val prescriptionType = call.parameters["type"]
            try {
                val patient = call.principal<Patient>()!!.patient_id
                if (prescriptionType == "regular") {
                    val prescriptions = prescriptionsServices.getPrescriptions(patient, true)
                    call.respond(ServerResponse(true, "Prescriptions", prescriptions))
                    return@get
                }
                if (prescriptionType == "once") {
                    val prescriptions = prescriptionsServices.getPrescriptions(patient,false)
                    call.respond(ServerResponse(true, "Prescriptions", prescriptions))
                    return@get
                }
                call.respond(ServerResponse(false, "No prescriptions of this type"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to get prescriptions"))
            }
        }

        /**
         * get the most recent prescription
         */
        get("prescriptions/recent"){
            try{
                val patient = call.principal<Patient>()!!.patient_id
                val prescription = prescriptionsServices.getMostRecentPrescription(patient)
                print(prescription)
                call.respond(ServerResponse(true, "Prescription", prescription))
            }catch (e:Exception){
                call.respond(ServerResponse(false, "Unable to get most recent prescription"))
            }
        }

        /**
         * refill specific prescription
         */
        post("prescriptions/refill/{id}"){
            val prescriptionId = call.parameters["id"]?.toInt()
            try{
                prescriptionId?.let { it1 -> prescriptionsServices.refillPrescription(it1) }
                call.respond(ServerResponse(true, "Prescription Refilled"))
            }catch (e: Exception){
                call.respond(ServerResponse(false, "Unable to refill prescription"))
            }
        }
    }


    /**
     * view prescription
     */
    get("prescriptions/view/{prescription}") {
        try {
            val prescriptionId = call.parameters["prescription"]?.toInt()
            val prescription: Prescriptions? =
                prescriptionId?.let { it1 -> prescriptionsServices.findPrescription(it1) }
            if (prescription == null) {
                call.respond("Prescription does not exist")
                return@get
            }
            prescriptionsServices.deletePrescription(prescriptionId)
            call.respondHtml {
                head {
                    title { +"Prescription" }
                    styleLink("/static-resources/main.css")
                    meta("viewport", content = "width=device-width, initial-scale=1", "utf-8")
                }
                body {
                    h1{
                        +"Prescription"
                    }
                    if(prescription.prescription_used && !prescription.prescription_regular){
                        h2("handed"){
                            +"This prescription has been handled already"
                        }
                    }
                    if(prescription.prescription_regular){
                        h2("regular") {
                            +"This prescription is a regular prescription"
                        }
                    }
                    h2 {
                        +"Medicine:"
                    }
                    p {
                        +prescription.prescription_medicine
                    }
                    h2 {
                        +"Date of Prescriptions:"
                    }
                    p {
                        +prescription.prescription_date
                    }
                    h2 {
                        +"Doctor:"
                    }
                    p {
                        +prescription.prescription_doctor
                    }
                    h2 {
                        +"Medicine Type:"
                    }
                    p {
                        +prescription.prescription_type
                    }
                    h2 {
                        +"How to take:"
                    }
                    p {
                        +prescription.prescription_dosage
                    }
                }
            }
        } catch (e: Exception) {
            call.respond(ServerResponse(false, "Unable to get prescription"))
        }
    }
}