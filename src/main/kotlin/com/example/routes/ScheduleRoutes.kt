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
import java.time.LocalDateTime

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
        post("schedules/delete/{scheduleId}"){
            val scheduleId : Int = call.parameters["scheduleId"]!!.toInt()
            try{
                scheduleServices.deleteSchedule(scheduleId)
                call.respond(ServerResponse(true, "Schedule deleted"))
            }catch (e:Exception){
                call.respond(ServerResponse(false, "Unable to delete schedule"))
            }
        }

        /**
         * get all schedules for specific doctor
         */
        get("schedules/doctor"){
            try{
                val doctor = call.principal<Doctor>()!!.doctor_id
                val schedules = scheduleServices.getAllSchedules(doctor)
                call.respond(ServerResponse(true, "Doctor Schedules", schedules))
            }catch (e:Exception){
                call.respond(ServerResponse(false,"Unable to get schedules"))
            }
        }
    }


    authenticate ("patient-interaction", "doctor-interaction"){
        /**
         * get all the schedules for a specific doctor
         */
        get("schedules/{doctorId}/{week}/{day}"){
            val doctorId : Int = call.parameters["doctorId"]!!.toInt()
            val dayOfWeek : Int = call.parameters["week"]!!.toInt()
            val day : LocalDateTime = LocalDateTime.parse(call.parameters["day"])
            try{
                val schedule = scheduleServices.getScheduleOfDoctor(doctorId, dayOfWeek, day)
                call.respond(ServerResponse(true, "Schedule", schedule))
            }catch (e:Exception){
                print(e.toString())
                call.respond(ServerResponse(false, "Unable to get schedule"))
            }
        }
    }
}