package com.example.routes

import com.example.models.AddScheduleRequest
import com.example.models.Doctor
import com.example.models.ServerResponse
import com.example.services.ScheduleServices
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.schedulesRoutes(scheduleServices: ScheduleServices) {
    authenticate("doctor-interaction") {
        /**
         * add a new schedule to the database route
         */
        post("schedules/add"){
            val request = call.receive<AddScheduleRequest>()
            if (!request.isValid()) {
                call.respond(ServerResponse(false, "Bad Request"))
                return@post
            }
            try {
                val doctor = call.principal<Doctor>()!!.doctor_id
                request.schedule_doctor = doctor
                scheduleServices.addSchedule(request)
                call.respond(ServerResponse(true, "Schedule added"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to add schedules"))
            }
        }

        /**
         * delete a schedule from the database route
         */
        post("schedules/delete/{schedule_id}"){
            val schedule_id : Int = call.parameters["schedule_id"]!!.toInt()
            try{
                scheduleServices.deleteSchedule(schedule_id)
                call.respond(ServerResponse(true, "Schedule deleted"))
            }catch (e:Exception){
                call.respond(ServerResponse(false, "Unable to delete schedule"))
            }
        }
    }


    authenticate ("patient-interaction", "doctor-interaction"){
        /**
         * get all the schedules for a specific doctor
         */
        get("schedules/{doctor_id}"){
            val doctor_id : Int = call.parameters["doctor_id"]!!.toInt()
            try{
                val schedule = scheduleServices.getScheduleOfDoctor(doctor_id)
                call.respond(ServerResponse(true, "Schedule", schedule))
            }catch (e:Exception){
                call.respond(ServerResponse(false, "Unable to get schedule"))
            }
        }
    }
}