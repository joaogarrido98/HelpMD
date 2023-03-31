package com.example.entities

import org.jetbrains.exposed.sql.Table

object CallsTable : Table("CallsTable"){
    val call_id = integer("call_id")
    val call_booking = integer("call_booking").references(BookingsTable.booking_id)
    val call_room = varchar("call_room", 1000)
}