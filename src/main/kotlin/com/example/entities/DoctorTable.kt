package com.example.entities

import org.jetbrains.exposed.sql.Table

/**
 * ORM object for DOCTOR table on DB
 */
object DoctorTable : Table("Doctor") {
    val doctor_id = integer("doctor_id").autoIncrement()
    val doctor_name = varchar("doctor_name", 100)
    val doctor_email = varchar("doctor_email", 500).uniqueIndex("idx_doctor_email")
    val doctor_password = varchar("doctor_password", 500)
    val doctor_sign_language = bool("doctor_sign_language").default(false)
    // in case i can use this keep here, need to see how im gonna do, if i can use a sample and if I can use a "fake"
    // doctor
    // val doctor_signature = varchar("doctor_signature", 1000).uniqueIndex("idx_doctor_signature")

    override val primaryKey = PrimaryKey(doctor_id, name = "PK_DOCTOR")
}