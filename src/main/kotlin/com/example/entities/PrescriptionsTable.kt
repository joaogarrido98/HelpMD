package com.example.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object PrescriptionsTable : Table("Prescriptions") {
    val prescription_id = integer("prescription_id").autoIncrement()
    val patient_id = integer("patient_id").references(PatientTable.patient_id)
    val prescription_date = date("prescription_date")
    val prescription_regular = bool("prescription_regular").default(false)
    val prescription_controlled = bool("prescription_controlled").default(false)
    val prescription_doctor = integer("prescription_doctor").references(DoctorTable.doctor_id)
    val prescription_medicine = varchar("prescription_medicine", 500)
    val prescription_dosage = varchar("prescription_dosage", 500)

    override val primaryKey = PrimaryKey(prescription_id, name = "PK_PRESCRIPTIONS")
}