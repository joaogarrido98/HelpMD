package com.example.entities

import org.jetbrains.exposed.sql.Table

/**
 * ORM object for ACTIVATE_DOCTOR table on DB
 */
object ActiveDoctorTable : Table("ActiveDoctor") {
    val active_doctor_id = integer("active_doctor_id").autoIncrement()
    val doctor_id = integer("doctor_id").references(DoctorTable.doctor_id).uniqueIndex("idx_doctor_id_active")
    val doctor_active = bool("doctor_active")

    override val primaryKey = PrimaryKey(active_doctor_id, name = "PK_ACTIVE_DOCTOR")
}