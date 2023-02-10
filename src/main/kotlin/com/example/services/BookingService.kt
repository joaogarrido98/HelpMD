package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.BookingsTable
import com.example.entities.DoctorTable
import com.example.models.Bookings
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

class BookingServices {
    val db = DatabaseManager

    /**
     * get bookings for a specific patient
     * @param patient_id holds patient id
     * @return a list of bookings
     */
    suspend fun getPatientBookings(patient_id: Int): List<Bookings> {
        val bookingsList = mutableListOf<Bookings>()
        db.query {
            (BookingsTable innerJoin DoctorTable).select { BookingsTable.booking_patient.eq(patient_id) }
                .map {
                    bookingsList.add(rowToBookings(it))
                }
        }
        return bookingsList
    }

    /**
     * @param row holds the result row from the query
     * @return Bookings object
     */
    private fun rowToBookings(row: ResultRow): Bookings {
        return Bookings(
            booking_date = row[BookingsTable.booking_date].toString(),
            booking_doctor = row[DoctorTable.doctor_name],
            booking_id = row[BookingsTable.booking_id],
            booking_start = row[BookingsTable.booking_start].toString(),
            booking_end = row[BookingsTable.booking_end].toString(),
            booking_patient = row[BookingsTable.booking_patient]
        )
    }
}