package com.example.models

import io.ktor.server.auth.*

data class Doctor(
    val doctor_id: Int,
    val doctor_name: String,
    val doctor_email: String,
    val doctor_password: String,
    val doctor_sign_language: Boolean,
    val doctor_patients_count: Int
) : Principal


data class DoctorLoginRequest(
    val doctor_email: String,
    val doctor_password: String
) {
    /**
     * Validates the login request
     * @return true or false depending on if all data needed is present
     */
    fun isValid(): Boolean {
        return !(this.doctor_email.isEmpty() || this.doctor_password.isEmpty())
    }
}


data class DoctorRegisterRequest(
    val doctor_name: String,
    val doctor_email: String,
    var doctor_password: String,
    val doctor_sign_language: Boolean,
) {
    /**
     * Validates the register request
     * @return true or false depending on if all data needed is present
     */
    fun isValid(): Boolean {
        return !(this.doctor_email.isEmpty() || this.doctor_password.isEmpty() || this.doctor_name.isEmpty())
    }
}