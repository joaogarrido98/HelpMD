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
    suspend fun deactivateDoctorStatus(doctor_id: Int) {
        db.query {
            ActiveDoctorTable.deleteWhere { ActiveDoctorTable.doctor_id eq doctor_id }
        }
    }

    /**
     * activate or deactivate a doctor on active doctor table
     */
    suspend fun activateDoctorStatus(doctorID: Int) {
        db.query {
            ActiveDoctorTable.insert {
                it[doctor_id] = doctorID
            }
        }
    }

    /**
     * check if doctor is active in db, query for resilience of the db
     */
    suspend fun isDoctorActive(doctorID: Int): ResultRow? {
        return db.query {
            ActiveDoctorTable.select { ActiveDoctorTable.doctor_id.eq(doctorID) }.singleOrNull()
        }
    }

    /**
     * @return list of doctors that are active for on call
     */
    suspend fun getActiveDoctors(): List<Doctor> {
        val doctorList : MutableList<Doctor> = mutableListOf()
        db.query {
            (ActiveDoctorTable innerJoin DoctorTable).selectAll().limit(20)
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
