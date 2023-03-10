package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.AppointmentResultTable
import com.example.entities.BookingsTable
import com.example.entities.DoctorTable
import com.example.entities.PrescriptionsTable
import com.example.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class BookingServices {
    private val db = DatabaseManager
    private val rows = ResultRows

    /**
     * get bookings for a specific patient
     * @param patient_id holds patient id
     * @return a list of bookings
     */
    suspend fun getPatientBookings(patient_id: Int): List<Bookings> {
        val currentTime = LocalDateTime.now()
        val bookingsList = mutableListOf<Bookings>()
        db.query {
            (BookingsTable innerJoin DoctorTable).select { BookingsTable.booking_patient.eq(patient_id) }.andWhere {
                BookingsTable.booking_date_end.greaterEq(currentTime)
            }.orderBy(BookingsTable.booking_date_start)
                .map {
                    bookingsList.add(rows.rowToBookings(it))
                }
        }
        return bookingsList
    }

    /**
     * get previous bookings for a specific patient
     * @param patient_id holds patient id
     * @return a list of bookings
     */
    suspend fun getPreviousPatientBookings(patient_id: Int): List<Bookings> {
        val currentTime = LocalDateTime.now()
        val bookingsList = mutableListOf<Bookings>()
        db.query {
            (BookingsTable innerJoin DoctorTable).select { BookingsTable.booking_patient.eq(patient_id) }.andWhere {
                BookingsTable.booking_date_end.less(currentTime)
            }.orderBy(BookingsTable.booking_date_start to SortOrder.DESC)
                .map {
                    bookingsList.add(rows.rowToBookings(it))
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
                it[booking_date_start] = LocalDateTime.parse(booking.booking_date_start)
                it[booking_date_end] = LocalDateTime.parse(booking.booking_date_end)
            }
        }
    }

    /**
     * get the closest booking to the current date
     * @param patient_id holds the id for the patient we want to find the upcoming booking
     * @return a booking
     */
    suspend fun getUpcomingBookings(patient_id: Int): Bookings? {
        val date: LocalDateTime = LocalDateTime.now()
        return db.query {
            (BookingsTable innerJoin DoctorTable).select { BookingsTable.booking_patient eq patient_id }.andWhere {
                BookingsTable.booking_date_end.greater(date)
            }.orderBy(
                BookingsTable.booking_date_start to SortOrder
                    .ASC
            )
                .map {
                    rows.rowToBookings(it)
                }.firstOrNull()
        }
    }

    /**
     * get the closest booking to the current date
     * @param doctor_id holds the id for the doctor we want to find the upcoming booking
     * @return a booking
     */
    suspend fun getUpcomingBookingsDoctor(doctor_id: Int): Bookings? {
        val date: LocalDateTime = LocalDateTime.now()
        return db.query {
            (BookingsTable innerJoin DoctorTable).select { BookingsTable.booking_doctor eq doctor_id }.andWhere {
                BookingsTable.booking_date_end.greater(date)
            }.orderBy(
                BookingsTable.booking_date_start to SortOrder
                    .ASC
            )
                .map {
                    rows.rowToBookings(it)
                }.firstOrNull()
        }
    }

    /**
     * delete a booking given with booking_id
     * @param booking_id holds the id of the booking to be deleted of type Int
     */
    suspend fun deleteBooking(booking_id: Int) {
        db.query {
            BookingsTable.deleteWhere { BookingsTable.booking_id eq booking_id }
        }
    }

    /**
     * get the appointment results from the previous appointments
     * @param patient_id holds the id of the patient we want to find
     * @return list of AppointmentResult objects
     */
    suspend fun getAppointmentResults(patient_id: Int): List<AppointmentResult> {
        val appointmentList = mutableListOf<AppointmentResult>()
        db.query {
            (AppointmentResultTable leftJoin BookingsTable).select { BookingsTable.booking_patient eq patient_id }
                .map {
                    appointmentList.add(rows.rowToAppointmentResult(it))
                }
        }
        return appointmentList
    }

    /**
     * get the latest appointment result for a given patient
     * @param patient holds an integer which is the id of the patient
     * @return an object of AppointmentResult type
     */
    suspend fun getLatestResult(patient: Int): AppointmentResult {
        return db.query {
            (AppointmentResultTable leftJoin BookingsTable).select { BookingsTable.booking_patient.eq(patient) }
                .orderBy(AppointmentResultTable.result_id)
                .map {
                    rows.rowToAppointmentResult(it)
                }.last()
        }
    }


    /**
     * add a result for appointment results
     * @param appointmentResult holds AddAppointmentResult object
     */
    suspend fun addAppointmentResult(appointmentResult: AddAppointmentResult) {
        db.query {
            AppointmentResultTable.insert {
                it[result] = appointmentResult.result
                it[booking_id] = appointmentResult.booking_id
            }
        }
    }


}