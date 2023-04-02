package com.example.entities

import org.jetbrains.exposed.sql.Table

object DocumentsTable : Table("Documents") {
    val documents_id = integer("documents_id").autoIncrement()
    val document_patient = integer("document_patient").references(PatientTable.patient_id)
    val document_path = varchar("document_path", 500)
    val document_name = varchar("document_name", 500)
}