package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.DoctorTable
import com.example.models.Doctor
import com.example.models.DoctorRegisterRequest
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

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
     * register a doctor into the database and initialize patient count as 0
     * @param doctor holds a doctor register request object
     */
    suspend fun addDoctor(doctor: DoctorRegisterRequest) {
        db.query {
            DoctorTable.insert {
                it[doctor_email] = doctor.doctor_email
                it[doctor_name] = doctor.doctor_name
                it[doctor_password] = doctor.doctor_password
                it[doctor_patients_count] = 0
                it[doctor_sign_language] = doctor.doctor_sign_language
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



}