package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.BookingsTable
import com.example.entities.DoctorTable
import com.example.entities.SchedulesTable
import com.example.models.AddScheduleRequest
import com.example.models.Bookings
import com.example.models.Schedule
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.day
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

class ScheduleServices {
    private val db = DatabaseManager
    private val rows = ResultRows

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
        val schedules: MutableList<Schedule> = mutableListOf()
        val bookings: MutableList<Bookings> = mutableListOf()
        val startTime = day.withHour(0).withMinute(0).withSecond(0)
        val endTime = startTime.plusDays(1)
        db.query {
            (BookingsTable innerJoin DoctorTable).select {
                BookingsTable.booking_doctor eq doctorId
            }.andWhere {
                BookingsTable.booking_date_start.between(startTime, endTime)
            }.map {
                bookings.add(rows.rowToBookings(it))
            }
            SchedulesTable.select { SchedulesTable.schedule_doctor eq doctorId }.andWhere {
                SchedulesTable.schedule_day_of_week eq dayOfWeek
            }.orderBy(SchedulesTable.schedule_start).map {
                schedules.add(rows.rowToSchedule(it))
            }
        }
        val availableSchedules: List<Schedule> = schedules.filter { schedule ->
            val date = day.plusHours(LocalDateTime.parse(schedule.schedule_start).hour.toLong())
            println(date)
            bookings.any {
                LocalDateTime.parse(it.booking_date_start) == date
            }
        }
        return availableSchedules
    }

    /**
     * get all the schedules for a specific doctor
     * @param doctorId holds the id of the doctor we want to get the schedules
     * @return List of Schedule objects
     */
    suspend fun getAllSchedules(doctorId: Int): List<Schedule> {
        val schedule: MutableList<Schedule> = mutableListOf()
        db.query {
            SchedulesTable.select { SchedulesTable.schedule_doctor eq doctorId }.orderBy(SchedulesTable.schedule_start)
                .map {
                    schedule.add(rows.rowToSchedule(it))
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


}