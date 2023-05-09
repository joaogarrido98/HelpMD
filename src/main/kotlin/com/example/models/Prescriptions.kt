package com.example.models

data class Prescriptions(
    val prescription_id: Int? = null,
    val patient_id: Int? = null,
    val prescription_date: String? = null,
    val prescription_regular: Boolean? = null,
    var prescription_doctor: Any? = null,
    val prescription_medicine: String? = null,
    val prescription_dosage: String? = null,
    val prescription_type : String? = null,
    val prescription_used : Boolean? = null
){
    fun isValid(): Boolean {
        return !(this.prescription_medicine.isNullOrEmpty() || this.prescription_dosage.isNullOrEmpty()|| this.prescription_type
            .isNullOrEmpty())
    }
}
