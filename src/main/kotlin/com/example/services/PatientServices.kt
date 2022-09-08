package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.ActivationTable
import com.example.entities.PatientTable
import com.example.models.Activation
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
     */
    suspend fun addPatient(patient: PatientRegisterRequest, code: String) {
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
            }
            ActivationTable.insert {
                it[activation_code] = code
                it[patient_email] = patient.patient_email
            }
        }
    }


    /**
     * query to find a row in activation table from the activation code
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
            patient_deaf = row[PatientTable.patient_deaf]
        )
    }


}