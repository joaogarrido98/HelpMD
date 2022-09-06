package com.example.services

import com.example.database.DatabaseManager
import com.example.models.Patient

class PatientServices {
    private val db = DatabaseManager

    suspend fun getAllUsers(): List<Patient> {
        db.query {
        }
        return listOf()
    }

    private fun toUser() {

    }
}