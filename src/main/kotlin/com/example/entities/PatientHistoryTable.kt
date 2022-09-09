package com.example.entities

import org.jetbrains.exposed.sql.Table

/**
 * ORM object for PATIENT_HISTORY table on DB
 */
object PatientHistoryTable: Table("PatientHistory") {
    val history_id = integer("history_id").autoIncrement()
    val patient_id = integer("patient_id").references(PatientTable.patient_id).uniqueIndex("idx_patient_history")
    val patient_allergies = varchar("patient_allergies", 1500)
    val patient_family = varchar("patient_family", 1500)
    val patient_lifestyle = varchar("patient_lifestyle", 1500)
    val patient_vaccines = varchar("patient_vaccines", 1500)
    val patient_diseases = varchar("patient_diseases", 1500)
    val patient_blood = varchar("patient_blood", 100)
    val patient_hospitalized = bool("patient_hospitalized")

    override val primaryKey = PrimaryKey(history_id, name = "PK_PATIENT_HISTORY")
}