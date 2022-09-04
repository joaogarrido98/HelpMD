package com.example.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object PatientTable : Table("Patient"){
    val patient_id = integer("patient_id").autoIncrement()
    val patient_name = varchar("patient_name", 100)
    val patient_email = varchar("patient_email", 500).uniqueIndex("idx_patient_email")
    val patient_dob = date("patient_dob")
    val patient_gender = varchar("patient_gender", 30)
    val patient_password = varchar("patient_password", 500)
    val patient_active = bool("patient_active").default(false)
    val patient_deaf = bool("patient_deaf").default(false)

   override val primaryKey = PrimaryKey(patient_id, name = "PK_PatientTable")
}
