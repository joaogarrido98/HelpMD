package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.DoctorTable
import com.example.entities.PatientHistoryTable
import com.example.entities.PatientTable
import com.example.models.*
import org.jetbrains.exposed.sql.*

class DoctorServices {
    private val db = DatabaseManager
    private val rows = ResultRows

    /**
     * find doctor where id is the same as given
     * @param doctorId holds the id to find the user
     * @return Doctor object or null
     */
    suspend fun findDoctorById(doctorId: Int): Doctor? {
        return db.query {
            DoctorTable.select { DoctorTable.doctor_id eq doctorId }
                .map { rows.rowToDoctor(it) }
                .singleOrNull()
        }
    }


    /**
     * find patient where id is the same as given
     * @param patientId holds the id to find the user
     * @return Patient object or null
     */
    suspend fun findPatientById(patientId: Int): PatientDoctor? {
        return db.query {
            (PatientTable innerJoin PatientHistoryTable).select { PatientTable.patient_id eq patientId }
                .map { rows.rowToPatientDoctor(it) }
                .singleOrNull()
        }
    }


    /**
     * register a doctor into the database and initialize patient count as 0
     * @param doctor holds a doctor register request object
     */
    suspend fun addDoctor(doctor: Doctor) {
        db.query {
            DoctorTable.insert {
                it[doctor_email] = doctor.doctor_email as String
                it[doctor_name] = doctor.doctor_name as String
                it[doctor_password] = doctor.doctor_password as String
                it[doctor_patients_count] = 0
                it[doctor_sign_language] = doctor.doctor_sign_language as Boolean
            }
        }
    }


    /**
     * find doctor where email is the same as given
     * @param doctorEmail holds the email to find the user
     * @return Doctor object or null
     */
    suspend fun findDoctorByEmail(doctorEmail: String): Doctor? {
        return db.query {
            DoctorTable.select { DoctorTable.doctor_email.eq(doctorEmail) }
                .map { rows.rowToDoctor(it) }
                .singleOrNull()
        }
    }

    /**
     * update the password of a doctor
     * @param email holds the email of the doctor that needs to be updated
     * @param newPassword holds the new password
     */
    suspend fun updatePassword(email: String, newPassword: String) {
        db.query {
            DoctorTable.update(where = { DoctorTable.doctor_email eq email }) {
                it[doctor_password] = newPassword
            }
        }
    }

    /**
     * get all patients for a specific doctor
     * @param doctor_id holds the id for the doctor we want to find the patients
     * @return a list of patients
     */
    suspend fun getDoctorPatients(doctor_id: Int): List<PatientDoctor> {
        val patientList = mutableListOf<PatientDoctor>()
        db.query {
            PatientTable.select { PatientTable.patient_doctor.eq(doctor_id) }.andWhere { PatientTable.patient_active
                .eq(true) }
                .map {
                    patientList.add(rows.rowToPatientDoctor(it))
                }
        }
        return patientList
    }


}