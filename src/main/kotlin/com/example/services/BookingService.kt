package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.BookingsTable
import com.example.entities.DoctorTable
import com.example.models.AddBookingsRequest
import com.example.models.Bookings
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.awt.print.Book
import java.time.LocalDate
import java.time.LocalTime

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
     * @param booking holds a booking to add to database
     */
    suspend fun addBookings(booking: AddBookingsRequest) {
        db.query {
            BookingsTable.insert {
                it[booking_patient] = booking.booking_patient
                it[booking_doctor] = booking.booking_doctor
                it[booking_date] = LocalDate.parse(booking.booking_date)
                it[booking_start] = LocalTime.parse(booking.booking_start)
                it[booking_end] = LocalTime.parse(booking.booking_end)
            }
        }
    }

    suspend fun getUpcomingBookings(patient_id: Int): Bookings? {
        return db.query {
            BookingsTable.select { BookingsTable.booking_patient eq patient_id }.orderBy(
                BookingsTable
                    .booking_date to SortOrder.ASC
            ).orderBy(BookingsTable.booking_start to SortOrder.ASC).map {
                rowToBookings(it)
            }.firstOrNull()
        }
    }

    /**
     * delete a booking given with booking_id
     * @param booking_id holds the id of the booking to be deleted of type Int
     */
    suspend fun deleteBooking(booking_id : Int){
        db.query {
            BookingsTable.deleteWhere { BookingsTable.booking_id eq booking_id }
        }
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