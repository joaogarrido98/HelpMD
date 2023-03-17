package com.example.services

import com.example.entities.*
import com.example.models.*
import org.jetbrains.exposed.sql.ResultRow

object ResultRows {

    /**
     * @param row holds a result row of Activation table
     * @return Activation object
     */
    fun rowToActivation(row: ResultRow): Activation {
        return Activation(
            activation_code = row[ActivationTable.activation_code],
            patient_email = row[ActivationTable.patient_email]
        )
    }

    /**
     * This method transforms a database row into a Patient object
     * @param row has the row that was retrieved from the database
     * @return a Patient object
     */
    fun rowToPatient(row: ResultRow): Patient {
        return Patient(
            patient_id = row[PatientTable.patient_id].toInt(),
            patient_name = row[PatientTable.patient_name],
            patient_email = row[PatientTable.patient_email],
            patient_dob = row[PatientTable.patient_dob].toString(),
            patient_weight = row[PatientTable.patient_weight],
            patient_password = row[PatientTable.patient_password],
            patient_gender = row[PatientTable.patient_gender],
            patient_height = row[PatientTable.patient_height],
            patient_active = row[PatientTable.patient_active],
            patient_deaf = row[PatientTable.patient_deaf],
            patient_doctor = row[PatientTable.patient_doctor]
        )
    }

    /**
     * This method transforms a database row into a Patient object
     * @param row has the row that was retrieved from the database
     * @return a Patient object
     */
    fun rowToPatientDoctor(row: ResultRow): PatientDoctor {
        return PatientDoctor(
            patient_id = row[PatientTable.patient_id].toInt(),
            patient_name = row[PatientTable.patient_name],
            patient_email = row[PatientTable.patient_email],
            patient_dob = row[PatientTable.patient_dob].toString(),
            patient_weight = row[PatientTable.patient_weight],
            patient_gender = row[PatientTable.patient_gender],
            patient_height = row[PatientTable.patient_height],
            patient_deaf = row[PatientTable.patient_deaf],
        )
    }



    /**
     * This method transforms a database row into a Doctor object
     * @param row has the row that was retrieved from the database
     * @return a Doctor object
     */
    fun rowToDoctor(row: ResultRow): Doctor {
        return Doctor(
            doctor_id = row[DoctorTable.doctor_id],
            doctor_email = row[DoctorTable.doctor_email],
            doctor_name = row[DoctorTable.doctor_name],
            doctor_password = row[DoctorTable.doctor_password],
            doctor_patients_count = row[DoctorTable.doctor_patients_count],
            doctor_sign_language = row[DoctorTable.doctor_sign_language]
        )
    }


    /**
     * @param row holds the result row from the query
     * @return AppointmentResult object
     */
    fun rowToAppointmentResult(row: ResultRow): AppointmentResult {
        return AppointmentResult(
            result = row[AppointmentResultTable.result],
            booking_id = row[AppointmentResultTable.booking_id],
            result_id = row[AppointmentResultTable.result_id]
        )
    }

    /**
     * @param row holds the result row from the query
     * @return Bookings object
     */
    fun rowToBookings(row: ResultRow): Bookings {
        return Bookings(
            booking_date_start = row[BookingsTable.booking_date_start].toString(),
            booking_date_end = row[BookingsTable.booking_date_end].toString(),
            booking_doctor = row[DoctorTable.doctor_name],
            booking_id = row[BookingsTable.booking_id],
            booking_patient = row[BookingsTable.booking_patient]
        )
    }

    /**
     * 
     */
    fun rowToBookingsDoctor(row:ResultRow): BookingsDoctor {
        return BookingsDoctor(
            booking_date_start = row[BookingsTable.booking_date_start].toString(),
            booking_date_end = row[BookingsTable.booking_date_end].toString(),
            booking_doctor = row[BookingsTable.booking_doctor],
            booking_id = row[BookingsTable.booking_id],
            booking_patient = row[PatientTable.patient_name],
            booking_patient_id = row[PatientTable.patient_id]
        )
    }



    /**
     * This method transforms a database row into a PatientHistory object
     * @param row has the row that was retrieved from the database
     * @return a PatientHistory object
     */
    fun rowToPatientHistory(row: ResultRow): PatientHistory {
        return PatientHistory(
            patient_id = row[PatientHistoryTable.patient_id],
            history_id = row[PatientHistoryTable.history_id],
            patient_family = row[PatientHistoryTable.patient_family],
            patient_allergies = row[PatientHistoryTable.patient_allergies],
            patient_blood = row[PatientHistoryTable.patient_blood],
            patient_diseases = row[PatientHistoryTable.patient_diseases],
            patient_lifestyle = row[PatientHistoryTable.patient_lifestyle],
            patient_vaccines = row[PatientHistoryTable.patient_vaccines]
        )
    }

    /**
     * Turn the result row from sql into a Prescriptions object
     * @param row holds a result row
     * @return an object of Prescription type
     */
    fun rowToPrescriptions(row: ResultRow): Prescriptions {
        return Prescriptions(
            patient_id = row[PrescriptionsTable.prescription_id],
            prescription_date = row[PrescriptionsTable.prescription_date].toString(),
            prescription_doctor = row[DoctorTable.doctor_name],
            prescription_id = row[PrescriptionsTable.prescription_id],
            prescription_dosage = row[PrescriptionsTable.prescription_dosage],
            prescription_medicine = row[PrescriptionsTable.prescription_medicine],
            prescription_regular = row[PrescriptionsTable.prescription_regular],
            prescription_type = row[PrescriptionsTable.prescription_type],
            prescription_used = row[PrescriptionsTable.prescription_used]
        )
    }


    /**
     * this function turns a sql result into an object of type Schedule
     * @param row holds the sql result from query
     * @return object of type Schedule
     */
    fun rowToSchedule(row: ResultRow): Schedule {
        return Schedule(
            schedule_id = row[SchedulesTable.schedule_id],
            schedule_doctor = row[SchedulesTable.schedule_doctor],
            schedule_day_of_week = row[SchedulesTable.schedule_day_of_week],
            schedule_end = row[SchedulesTable.schedule_end].toString(),
            schedule_start = row[SchedulesTable.schedule_start].toString()
        )
    }
}