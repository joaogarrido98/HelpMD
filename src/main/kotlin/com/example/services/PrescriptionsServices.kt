package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.DoctorTable
import com.example.entities.PrescriptionsTable
import com.example.models.Prescriptions
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDate

class PrescriptionsServices {
    private val db = DatabaseManager
    private val rows = ResultRows

    /**
     * Add a new prescription to the database
     * @param prescription holds a Prescription add object
     */
    suspend fun addPrescription(prescription: Prescriptions) {
        db.query {
            PrescriptionsTable.insert {
                it[patient_id] = prescription.patient_id as Int
                it[prescription_date] = LocalDate.now()
                it[prescription_doctor] = prescription.prescription_doctor as Int
                it[prescription_dosage] = prescription.prescription_dosage as String
                it[prescription_medicine] = prescription.prescription_medicine as String
                it[prescription_regular] = prescription.prescription_regular as Boolean
                it[prescription_type] = prescription.prescription_type as String
            }
        }
    }

    /**
     * get the latest prescription for a given patient
     * @param patient holds an integer which is the id of the patient
     * @return an object of Prescription type
     */
    suspend fun getMostRecentPrescription(patient: Int): Prescriptions {
        return db.query {
            (PrescriptionsTable innerJoin DoctorTable).select(where = PrescriptionsTable.patient_id.eq(patient))
                .orderBy(PrescriptionsTable.prescription_date)
                .map {
                    rows.rowToPrescriptions(it)
                }.last()
        }
    }

    /**
     * finds a given prescription through the prescription ID
     * @param prescriptionId holds an integer which is the prescription id in the db
     * @return an object of type Prescription
     */
    suspend fun findPrescription(prescriptionId: Int): Prescriptions? {
        return db.query {
            (PrescriptionsTable innerJoin DoctorTable).select(
                where = PrescriptionsTable.prescription_id.eq(
                    prescriptionId
                )
            ).map {
                rows.rowToPrescriptions(it)
            }.singleOrNull()
        }
    }

    /**
     * delete a given prescription through the prescription ID
     * @param prescriptionId holds an integer which is the prescription id in the db
     */
    suspend fun deletePrescription(prescriptionId: Int) {
        db.query {
            PrescriptionsTable.update({ PrescriptionsTable.prescription_id.eq(prescriptionId) }) {
                it[prescription_used] = true
            }
        }
    }

    /**
     * refill a prescription
     * used turns to false and date becomes the current time
     * @param prescriptionId holds the id of the prescription to be changed
     */
    suspend fun refillPrescription(prescriptionId: Int) {
        db.query {
            PrescriptionsTable.update({ PrescriptionsTable.prescription_id.eq(prescriptionId) }) {
                it[prescription_used] = false
                it[prescription_date] = LocalDate.now()
            }
        }
    }

    /**
     * get list of prescriptions for a specific patient
     * @param patient holds the id of the patient to which the prescriptions belong to
     * @return a list of Prescriptions
     */
    suspend fun getPrescriptions(patient: Int, regular: Boolean): List<Prescriptions> {
        val prescriptionList : MutableList<Prescriptions> = mutableListOf()
        db.query {
            (PrescriptionsTable innerJoin DoctorTable).select {
                PrescriptionsTable.patient_id.eq(patient) and
                        PrescriptionsTable.prescription_regular.eq(regular)
            }.orderBy(PrescriptionsTable.prescription_id to SortOrder.DESC).map {
                prescriptionList.add(rows.rowToPrescriptions(it))
            }
        }
        return prescriptionList
    }

}