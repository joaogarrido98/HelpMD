package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.PatientHistoryTable
import com.example.models.PatientHistory
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class HistoryServices {
    private val db = DatabaseManager
    private val rows = ResultRows

    /**
     * Add medical history to a specific patient
     * @param patientId holds patient id to be inserted to
     * @param patientHistory holds the data to be inserted
     */
    suspend fun addPatientHistory(patientHistory: PatientHistory, patientId: Int) {
        db.query {
            PatientHistoryTable.insert {
                it[patient_id] = patientId
                it[patient_allergies] = patientHistory.patient_allergies as String
                it[patient_diseases] = patientHistory.patient_diseases as String
                it[patient_family] = patientHistory.patient_family as String
                it[patient_lifestyle] = patientHistory.patient_lifestyle as String
                it[patient_vaccines] = patientHistory.patient_vaccines as String
                it[patient_blood] = patientHistory.patient_blood as String
            }
        }
    }

    /**
     * @param patientId holds the patient which we want to find the medical history
     * @return PatientHistory object
     */
    suspend fun findPatientHistory(patientId: Int): PatientHistory? {
        return db.query {
            PatientHistoryTable.select { PatientHistoryTable.patient_id.eq(patientId) }
                .map { rows.rowToPatientHistory(it) }
                .singleOrNull()
        }
    }

}