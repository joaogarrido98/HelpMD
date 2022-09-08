package com.example.entities

import org.jetbrains.exposed.sql.Table

/**
 * ORM object for activation table on DB
 */
object ActivationTable : Table("Activation"){
    val activation_code = varchar("activation_code", 100).uniqueIndex("idx_activation_code")
    val patient_email = varchar("patient_email", 500).uniqueIndex()
}