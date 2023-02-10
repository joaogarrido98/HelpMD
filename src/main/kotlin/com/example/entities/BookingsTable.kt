package com.example.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.time

/**
 * ORM object for BOOKINGS table on DB
 */
object BookingsTable : Table("Bookings") {
    val booking_id = integer("booking_id").autoIncrement()
    val booking_doctor = integer("booking_doctor").references(DoctorTable.doctor_id)
    val booking_patient = integer("booking_patient").references(PatientTable.patient_id)
    val booking_date = date("timetable_date")
    val booking_start = time("timetable_time")
    val booking_end = time("timetable_end")

    override val primaryKey = PrimaryKey(booking_id, name = "PK_BOOKINGS")
}