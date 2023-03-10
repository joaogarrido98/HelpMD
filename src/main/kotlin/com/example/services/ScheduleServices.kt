package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.SchedulesTable
import com.example.models.AddScheduleRequest
import com.example.models.Schedule
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

class ScheduleServices {
    private val db = DatabaseManager

    /**
     * add a new schedule
     * @param schedule object
     */
    suspend fun addSchedule(schedule: AddScheduleRequest) {
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
     * get all the schedules for a specific doctor and day of the week
     * @param doctorId holds the id of the doctor we want to get the schedules
     * @return List of Schedule objects
     */
    suspend fun getScheduleOfDoctor(doctorId: Int, dayOfWeek: Int, day: LocalDateTime): List<Schedule> {
        val schedule: MutableList<Schedule> = mutableListOf()
        print(day)
        db.query {
            SchedulesTable.select { SchedulesTable.schedule_doctor eq doctorId }.andWhere {
                SchedulesTable.schedule_day_of_week eq dayOfWeek
            }.orderBy(SchedulesTable.schedule_start).map {
                schedule.add(rowToSchedule(it))
            }
        }
        return schedule
    }

    /**
     * delete the schedule given by the id param
     * @param scheduleId holds the id of the schedule we want to delete
     */
    suspend fun deleteSchedule(scheduleId: Int) {
        db.query {
            SchedulesTable.deleteWhere { schedule_id eq scheduleId }
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