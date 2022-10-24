package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.ActiveDoctorTable
import com.example.entities.DoctorTable
import com.example.models.Doctor
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ActiveDoctorServices {
    val db = DatabaseManager

    /**
     * activate or deactivate a doctor on active doctor table
     */
    suspend fun updateDoctorStatus(doctor_id: Int) {
        db.query {
            ActiveDoctorTable.update(where = { ActiveDoctorTable.doctor_id eq doctor_id }) {
                it[doctor_active] = not(doctor_active)
            }
        }
    }

    /**
     * @return list of doctors that are active for on call
     */
    suspend fun getActiveDoctors(): List<Doctor> {
        val doctorList = mutableListOf<Doctor>()
        db.query {
            (ActiveDoctorTable innerJoin DoctorTable).select { ActiveDoctorTable.doctor_active.eq(true) }.limit(20)
                .map {
                    doctorList.add(rowToDoctor(it))
                }
        }
        return doctorList
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
