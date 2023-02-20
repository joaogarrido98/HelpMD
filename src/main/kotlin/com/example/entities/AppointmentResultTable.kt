package com.example.entities

import com.example.entities.ActivationTable.integer
import com.example.entities.ActivationTable.varchar
import com.example.entities.ActiveDoctorTable.references
import com.example.entities.ActiveDoctorTable.uniqueIndex
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Table.Dual.autoIncrement

object AppointmentResultTable : Table("AppointResults"){
    val result_id = integer("result_id").autoIncrement()
    val booking_id = integer("booking_id").references(BookingsTable.booking_id).uniqueIndex("idx_booking_result")
    val result = varchar("result", 5000)
}