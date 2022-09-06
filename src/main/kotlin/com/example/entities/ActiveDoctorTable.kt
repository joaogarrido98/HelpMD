package com.example.entities

import org.jetbrains.exposed.sql.Table

object ActiveDoctorTable : Table("ActiveDoctor") {
    val active_doctor_id = integer("active_doctor_id").autoIncrement()
    val doctor_id = integer("doctor_id").references(DoctorTable.doctor_id)

    override val primaryKey = PrimaryKey(active_doctor_id, name = "PK_ACTIVE_DOCTOR")
}