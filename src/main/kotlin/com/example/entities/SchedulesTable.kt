package com.example.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.time

/**
 * ORM object for SCHEDULE table on DB
 */
object SchedulesTable : Table("Schedule") {
    val schedule_id = integer("schedule_id").autoIncrement()
    val schedule_doctor = integer("schedule_doctor").references(DoctorTable.doctor_id)
    val schedule_day_of_week = integer("day_of_week")
    val schedule_start = time("schedule_start")
    val schedule_end = time("schedule_end")

    override val primaryKey = PrimaryKey(schedule_id, name="PK_SCHEDULE")
}