package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.PatientHistoryTable
import com.example.models.AddPatientHistoryRequest
import com.example.models.PatientHistory
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class HistoryServices {
    val db = DatabaseManager

    /**
     * Add medical history to a specific patient
     * @param patientId holds patient id to be inserted to
     * @param patientHistory holds the data to be inserted
     */
    suspend fun addPatientHistory(patientHistory: AddPatientHistoryRequest, patientId: Int) {
        db.query {
            PatientHistoryTable.insert {
                it[patient_id] = patientId
                it[patient_allergies] = patientHistory.patient_allergies
                it[patient_diseases] = patientHistory.patient_diseases
                it[patient_family] = patientHistory.patient_family
                it[patient_lifestyle] = patientHistory.patient_lifestyle
                it[patient_vaccines] = patientHistory.patient_vaccines
                it[patient_blood] = patientHistory.patient_blood
                it[patient_hospitalized] = patientHistory.patient_hospitalized
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
                .map { rowToPatientHistory(it) }
                .singleOrNull()
        }
    }

    /**
     * This method transforms a database row into a PatientHistory object
     * @param row has the row that was retrieved from the database
     * @return a PatientHistory object
     */
    private fun rowToPatientHistory(row: ResultRow): PatientHistory {
        return PatientHistory(
            patient_id = row[PatientHistoryTable.patient_id],
            history_id = row[PatientHistoryTable.history_id],
            patient_family_history = row[PatientHistoryTable.patient_family],
            patient_allergies = row[PatientHistoryTable.patient_allergies],
            patient_blood = row[PatientHistoryTable.patient_blood],
            patient_diseases = row[PatientHistoryTable.patient_diseases],
            patient_hospitalized = row[PatientHistoryTable.patient_hospitalized],
            patient_lifestyle = row[PatientHistoryTable.patient_lifestyle],
            patient_vaccines = row[PatientHistoryTable.patient_vaccines]
        )
    }
}