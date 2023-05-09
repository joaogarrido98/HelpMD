package com.example.entities

import org.jetbrains.exposed.sql.Table

object AppointmentResultTable : Table("AppointResults"){
    val result_id = integer("result_id").autoIncrement()
    val booking_id = integer("booking_id").references(BookingsTable.booking_id).uniqueIndex("idx_booking_result")
    val result = varchar("result", 5000)
}