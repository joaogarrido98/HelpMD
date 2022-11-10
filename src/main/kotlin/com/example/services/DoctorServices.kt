package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.ActiveDoctorTable
import com.example.entities.DoctorTable
import com.example.models.Doctor
import com.example.models.DoctorRegisterRequest
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class DoctorServices {
    val db = DatabaseManager

    /**
     * find doctor where id is the same as given
     * @param doctorId holds the id to find the user
     * @return Doctor object or null
     */
    suspend fun findDoctorById(doctorId: Int): Doctor? {
        return db.query {
            DoctorTable.select { DoctorTable.doctor_id eq doctorId }
                .map { rowToDoctor(it) }
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
                .map { rowToDoctor(it) }
                .singleOrNull()
        }
    }

    /**
     * This method transforms a database row into a Doctor object
     * @param row has the row that was retrieved from the database
     * @return a Doctor object
     */
    private fun rowToDoctor(row: ResultRow): Doctor {
        return Doctor(
            doctor_id = row[DoctorTable.doctor_id],
            doctor_email = row[DoctorTable.doctor_email],
            doctor_name = row[DoctorTable.doctor_name],
            doctor_password = row[DoctorTable.doctor_password],
            doctor_patients_count = row[DoctorTable.doctor_patients_count],
            doctor_sign_language = row[DoctorTable.doctor_sign_language]
        )
    }
}