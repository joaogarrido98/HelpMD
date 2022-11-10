package com.example.routes

import com.example.models.*
import com.example.services.PrescriptionsServices
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.p

fun Route.prescriptionsRoutes(prescriptionsServices: PrescriptionsServices) {

    authenticate ("doctor-interaction"){
        /**
         * add prescription route
         * check if request is valid then gets the doctor that it's adding.
         * Add prescription to db
         */
        post("prescriptions/add"){
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
            }catch (e: Exception){
                call.respond(ServerResponse(false, "Unable to add prescription"))
            }
        }
    }

    authenticate ("patient-interaction"){
        /**
         * get all the prescriptions for a specific patient
         */
        get("prescriptions"){
            try {
                val patient = call.principal<Patient>()!!.patient_id
                val prescriptions = prescriptionsServices.getPrescriptions(patient)
                call.respond(ServerResponse(true,"Prescriptions", prescriptions))
            }catch (e:Exception){
                call.respond(ServerResponse(false, "Unable to get prescriptions"))
            }
        }
    }

    /**
     * view prescription
     * if prescription is regular dont delete else delete
     */
    get("prescriptions/view/{prescription}"){
        try {
            val prescriptionId = call.parameters["prescription"]?.toInt()
            val prescription : Prescriptions? = prescriptionId?.let { it1 -> prescriptionsServices.findPrescription(it1) }
            if(prescription == null){
                call.respond("Prescription does not exist")
                return@get
            }
            if(!prescription.prescription_regular){
                prescriptionsServices.deletePrescription(prescriptionId)
            }
            call.respondHtml {
                body {
                    div {
                        p {
                            +prescription.prescription_medicine
                        }
                        p {
                            +prescription.prescription_date
                        }
                    }
                }
            }
        }catch (e: Exception){
            call.respond(ServerResponse(false, "Unable to get prescription"))
        }
    }
}