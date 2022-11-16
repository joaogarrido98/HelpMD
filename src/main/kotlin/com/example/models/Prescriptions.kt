package com.example.models

data class Prescriptions(
    val prescription_id: Int,
    val patient_id: Int,
    val prescription_date: String,
    val prescription_regular: Boolean,
    val prescription_doctor: String,
    val prescription_medicine: String,
    val prescription_dosage: String,
    val prescription_type : String,
    val prescription_used : Boolean
)

data class PrescriptionsAddRequest(
    val patient_id: Int,
    val prescription_regular: Boolean,
    val prescription_medicine: String,
    val prescription_dosage: String,
    var prescription_doctor: Int?,
    val prescription_type : String
){
    fun isValid(): Boolean {
        return !(this.prescription_medicine.isEmpty() || this.prescription_dosage.isEmpty()|| this.prescription_type.isEmpty())
    }
}