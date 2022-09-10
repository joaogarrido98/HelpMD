package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.ActiveDoctorTable
import org.jetbrains.exposed.sql.not
import org.jetbrains.exposed.sql.update

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
}
