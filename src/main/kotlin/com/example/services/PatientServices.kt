package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.ActivationTable
import com.example.entities.PatientHistoryTable
import com.example.entities.PatientTable
import com.example.models.Activation
import com.example.models.AddPatientHistoryRequest
import com.example.models.Patient
import com.example.models.PatientRegisterRequest
import org.jetbrains.exposed.sql.*
import java.time.LocalDate

/**
 * This class holds all the database implementation for the patient
 */
class PatientServices {
    private val db = DatabaseManager

    /**
     * find patient where email is the same as given
     * @param email holds the email to find the user
     * @return Patient object or null
     */
    suspend fun findPatientByEmail(email: String): Patient? {
        return db.query {
            PatientTable.select { PatientTable.patient_email.eq(email) }
                .map { rowToPatient(it) }
                .singleOrNull()
        }
    }

    /**
     * Add a new patient to the database
     * @param patient holds a Patient register object
     * @param code holds the code to be inserted
     */
    suspend fun addPatient(patient: PatientRegisterRequest, code: String, doctor: Int) {
        db.query {
            PatientTable.insert {
                it[patient_name] = patient.patient_name
                it[patient_email] = patient.patient_email
                it[patient_dob] = LocalDate.parse(patient.patient_dob)
                it[patient_password] = patient.patient_password
                it[patient_gender] = patient.patient_gender
                it[patient_weight] = patient.patient_weight
                it[patient_height] = patient.patient_height
                it[patient_active] = false
                it[patient_deaf] = patient.patient_deaf
                it[patient_doctor] = doctor
            }
            ActivationTable.insert {
                it[activation_code] = code
                it[patient_email] = patient.patient_email
            }
        }
    }


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
     * query to find a row in activation table from the activation code
     * @param activation_code holds the code to be inserted
     * @return object Activation or null
     */
    suspend fun findActivationCode(activation_code: String): Activation? {
        return db.query {
            ActivationTable.select {
                ActivationTable.activation_code.eq(
                    activation_code
                )
            }
                .map { rowToActivation(it) }
                .singleOrNull()
        }
    }

    /**
     * query to update a row in patient table from the deactivated to activated
     * @param email holds the email to which account to be activated
     * @param code holds the code to which row should be deleted
     */
    suspend fun activatePatient(email: String, code: String) {
        db.query {
            PatientTable.update(where = { PatientTable.patient_email eq email }) {
                it[patient_active] = true
            }
            ActivationTable.deleteWhere {
                ActivationTable.activation_code.eq(code)
            }
        }
    }


    /**
     * update the password of a patient
     * @param email holds the email of the patient that needs to be updated
     * @param newPassword holds the new password
     */
    suspend fun updatePassword(email: String, newPassword: String) {
        db.query {
            PatientTable.update(where = { PatientTable.patient_email eq email }) {
                it[patient_password] = newPassword
            }
        }
    }

    /**
     * @param row holds a result row of Activation table
     * @return Activation object
     */
    private fun rowToActivation(row: ResultRow): Activation {
        return Activation(
            activation_code = row[ActivationTable.activation_code],
            patient_email = row[ActivationTable.patient_email]
        )
    }

    /**
     * This method transforms a database row into a Patient object
     * @param row has the row that was retrieved from the database
     * @return a Patient object
     */
    private fun rowToPatient(row: ResultRow): Patient {
        return Patient(
            patient_id = row[PatientTable.patient_id].toInt(),
            patient_name = row[PatientTable.patient_name],
            patient_email = row[PatientTable.patient_email],
            patient_dob = row[PatientTable.patient_dob].toString(),
            patient_weight = row[PatientTable.patient_weight],
            patient_password = row[PatientTable.patient_password],
            patient_gender = row[PatientTable.patient_gender],
            patient_height = row[PatientTable.patient_height],
            patient_active = row[PatientTable.patient_active],
            patient_deaf = row[PatientTable.patient_deaf],
            patient_doctor = row[PatientTable.patient_doctor]
        )
    }


}