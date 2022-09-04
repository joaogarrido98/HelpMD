package com.example.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.time

object TimetableTable : Table("Timetable") {
    val timetable_id = integer("timetable_id").autoIncrement()
    val timetable_date = date("timetable_date")
    val timetable_start = time("timetable_time")
    val timetable_end = time("timetable_end")

    override val primaryKey = PrimaryKey(timetable_id, name = "PK_TIMETABLE")
}
