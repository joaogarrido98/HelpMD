package com.example.models

import kotlinx.serialization.Serializable

/*This file contains all the models for the patient requests
* Patient model
* Login model
* */

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

@Serializable
data class PatientRegisterRequest(
    val patient_email: String,
    val patient_name: String,
    val patient_dob: String,
    val patient_weight: Int,
    val patient_height: Int,
    val patient_gender: String,
    var patient_password: String,
    val patient_deaf: Boolean
) {
    /**
     * Validates the register request
     * @return true or false depending on if all data needed is present
     */
    fun isValid(): Boolean {
        return !(this.patient_email.isEmpty() || this.patient_password.isEmpty() || this.patient_name.isEmpty() ||
                this.patient_dob.isEmpty() || this.patient_gender.isEmpty())
    }
}



