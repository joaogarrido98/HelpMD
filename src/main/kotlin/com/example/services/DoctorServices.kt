package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.DoctorTable
import com.example.models.Doctor
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

class DoctorServices {
    val db = DatabaseManager

    /**
     *
     */
    suspend fun findDoctorById(doctorId: Int): Doctor? {
        return db.query {
            DoctorTable.select { DoctorTable.doctor_id eq doctorId }
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
            doctor_id = row[DoctorTable.doctor_id]
        )
    }
}