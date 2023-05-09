package com.example.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

/**
 * ORM object for PRESCRIPTIONS table on DB
 */
object PrescriptionsTable : Table("Prescriptions") {
    val prescription_id = integer("prescription_id").autoIncrement()
    val patient_id = integer("patient_id").references(PatientTable.patient_id)
    val prescription_date = date("prescription_date")
    val prescription_regular = bool("prescription_regular").default(false)
    var prescription_doctor = integer("prescription_doctor").references(DoctorTable.doctor_id)
    val prescription_medicine = varchar("prescription_medicine", 500)
    val prescription_type = varchar("prescription_type", 100)
    val prescription_dosage = varchar("prescription_dosage", 500)
    val prescription_used = bool("prescription_used").default(false)

    override val primaryKey = PrimaryKey(prescription_id, name = "PK_PRESCRIPTIONS")
}