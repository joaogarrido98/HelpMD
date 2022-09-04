package com.example.entities

import org.jetbrains.exposed.sql.Table

object ActivationTable : Table("Activation"){
    val activation_code = varchar("activation_code", 100).uniqueIndex("idx_activation_code")
    val patient_id = integer("patient_id").uniqueIndex().references(PatientTable.patient_id)
}