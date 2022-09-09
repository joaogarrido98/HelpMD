package com.example.models


data class PatientHistory(
    val history_id: Int,
    val patient_id: Int,
    val patient_allergies: String,
    val patient_family_history: String,
    val patient_lifestyle: String,
    val patient_vaccines: String,
    val patient_diseases: String,
    val patient_hospitalized: Boolean,
    val patient_blood: String
)

data class AddPatientHistoryRequest(
    val patient_allergies: String,
    val patient_family: String,
    val patient_lifestyle: String,
    val patient_vaccines: String,
    val patient_diseases: String,
    val patient_hospitalized: Boolean,
    val patient_blood: String
) {
    fun isValid(): Boolean {
        return !(patient_family.isEmpty() || patient_vaccines.isEmpty() || patient_blood.isEmpty())
    }
}