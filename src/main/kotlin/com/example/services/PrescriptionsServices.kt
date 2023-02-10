package com.example.services

import com.example.database.DatabaseManager
import com.example.entities.ActiveDoctorTable
import com.example.entities.DoctorTable
import com.example.entities.PrescriptionsTable
import com.example.models.Doctor
import com.example.models.Prescriptions
import com.example.models.PrescriptionsAddRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import java.time.LocalDate

class PrescriptionsServices {
    private val db = DatabaseManager

    /**
     * Add a new prescription to the database
     * @param prescription holds a Prescription add object
     */
    suspend fun addPrescription(prescription: PrescriptionsAddRequest) {
        db.query {
            PrescriptionsTable.insert {
                it[patient_id] = prescription.patient_id
                it[prescription_date] = LocalDate.now()
                it[prescription_doctor] = prescription.prescription_doctor!!
                it[prescription_dosage] = prescription.prescription_dosage
                it[prescription_medicine] = prescription.prescription_medicine
                it[prescription_regular] = prescription.prescription_regular
                it[prescription_type] = prescription.prescription_type
            }
        }
    }

    /**
     * finds a given prescription through the prescription ID
     * @param prescriptionId holds an integer which is the prescription id in the db
     */
    suspend fun findPrescription(prescriptionId: Int): Prescriptions? {
        return db.query {
            (PrescriptionsTable innerJoin DoctorTable).select(where = PrescriptionsTable.prescription_id.eq(prescriptionId)).map {
                rowToPrescriptions(it)
            }.singleOrNull()
        }
    }

    /**
     * delete a given prescription through the prescription ID
     * @param prescriptionId holds an integer which is the prescription id in the db
     */
    suspend fun deletePrescription(prescriptionId: Int) {
        db.query {
            PrescriptionsTable.update ({ PrescriptionsTable.prescription_id.eq(prescriptionId) }){
                it[prescription_used] = true
            }
        }
    }

    /**
     * refill a prescription
     * used turns to false and date becomes the current time
     * @param prescriptionId holds the id of the prescription to be changed
     */
    suspend fun refillPrescription(prescriptionId: Int){
        db.query {
            PrescriptionsTable.update ({PrescriptionsTable.prescription_id.eq(prescriptionId)}){
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
        val prescriptionList = mutableListOf<Prescriptions>()
        db.query {
            (PrescriptionsTable innerJoin DoctorTable).select { PrescriptionsTable.patient_id.eq(patient) and
                    PrescriptionsTable.prescription_regular.eq(regular)}
                .map {
                    prescriptionList.add(rowToPrescriptions(it))
                }
        }
        return prescriptionList
    }

    /**
     * Turn the result row from sql into a Prescriptions object
     */
    private fun rowToPrescriptions(row: ResultRow): Prescriptions {
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
}