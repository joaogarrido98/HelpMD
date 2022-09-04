package com.example.entities

import org.jetbrains.exposed.sql.Table

object ScheduleDoctorTable : Table("ScheduleDoctor") {
    val schedule_doctor_id = integer("schedule_id").autoIncrement()
    val schedule_timetable = integer("schedule_timetable").references(SchedulesTable.schedule_id)
    val schedule_doctor = integer("schedule_doctor").references(DoctorTable.doctor_id)

    override val primaryKey = PrimaryKey(schedule_doctor_id, name="PK_SCHEDULE_DOCTOR")
}