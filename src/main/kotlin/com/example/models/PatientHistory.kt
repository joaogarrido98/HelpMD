package com.example.models


data class PatientHistory(
    val history_id: Int? = null,
    val patient_id: Int? = null,
    val patient_allergies: String? = null,
    val patient_family: String? = null,
    val patient_lifestyle: String? = null,
    val patient_vaccines: String? = null,
    val patient_diseases: String? = null,
    val patient_blood: String? = null
){
    fun isValid(): Boolean {
        return !(patient_family.isNullOrEmpty() || patient_vaccines.isNullOrEmpty() || patient_blood.isNullOrEmpty())
    }
}
