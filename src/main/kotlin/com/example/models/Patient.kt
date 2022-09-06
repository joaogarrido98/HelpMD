package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Patient(
    val patient_id: Int,
    val patient_email: String,
    val patient_name: String,
    val patient_dob: String,
    val patient_weight: Int,
    val patient_height: Int,
    val patient_gender: String,
    val patient_password: String,
    val patient_active: Boolean,
    val patient_deaf: Boolean
)

@Serializable
data class PatientLoginRequest(
    val patient_email: String,
    val patient_password: String
) {
    /**
     * Validates the login request
     * @return true or false depending on if all data needed is present
     */
    fun isValid(): Boolean {
        return !(this.patient_email.isEmpty() || this.patient_password.isEmpty())
    }
}



