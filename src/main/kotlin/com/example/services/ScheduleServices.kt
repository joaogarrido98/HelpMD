package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.SchedulesTable
import com.example.models.AddScheduleRequest
import com.example.models.Schedule
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.time.LocalTime

class ScheduleServices {
    private val db = DatabaseManager

    /**
     * add a new schedule
     * @param schedule object
     */
    suspend fun addSchedule(schedule: AddScheduleRequest){
        db.query {
            SchedulesTable.insert {
                it[schedule_doctor] = schedule.schedule_doctor
                it[schedule_end] = LocalTime.parse(schedule.schedule_end)
                it[schedule_start] = LocalTime.parse(schedule.schedule_start)
                it[schedule_day_of_week] = schedule.schedule_day_of_week
            }
        }
    }


    /**
     * get all the schedules for a specific doctor
     * @param doctor_id holds the id of the doctor we want to get the schedules
     * @return List of Schedule objects
     */
    suspend fun getScheduleOfDoctor(doctor_id: Int): List<Schedule> {
        val schedule: MutableList<Schedule> = mutableListOf()
        db.query {
            SchedulesTable.select { SchedulesTable.schedule_doctor eq doctor_id }.map {
                schedule.add(rowToSchedule(it))
            }
        }
        return schedule
    }

    /**
     * delete the schedule given by the id param
     * @param
     */
    suspend fun deleteSchedule(schedule_id: Int) {
        db.query {
            SchedulesTable.deleteWhere { SchedulesTable.schedule_id eq schedule_id }
        }
    }

    /**
     * this function turns a sql result into an object of type Schedule
     * @param row holds the sql result from query
     * @return object of type Schedule
     */
    private fun rowToSchedule(row: ResultRow): Schedule {
        return Schedule(
            schedule_id = row[SchedulesTable.schedule_id],
            schedule_doctor = row[SchedulesTable.schedule_doctor],
            schedule_day_of_week = row[SchedulesTable.schedule_day_of_week],
            schedule_end = row[SchedulesTable.schedule_end].toString(),
            schedule_start = row[SchedulesTable.schedule_start].toString()
        )
    }
}