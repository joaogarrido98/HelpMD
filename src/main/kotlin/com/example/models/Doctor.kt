package com.example.models

import io.ktor.server.auth.*
data class Doctor(
    val doctor_id: Int? = null,
    val doctor_name: String? = null,
    val doctor_email: String? = null,
    var doctor_password: String? = null,
    val doctor_sign_language: Boolean? = null,
    val doctor_patients_count: Int? = null
) : Principal{
    /**
     * Validates the login request
     * @return true or false depending on if all data needed is present
     */
    fun isLoginValid(): Boolean {
        return !(this.doctor_email.isNullOrEmpty() || this.doctor_password.isNullOrEmpty())
    }

    /**
     * Validates the register request
     * @return true or false depending on if all data needed is present
     */
    fun isRegisterValid(): Boolean {
        return !(this.doctor_email.isNullOrEmpty() || this.doctor_password.isNullOrEmpty() || this.doctor_name.isNullOrEmpty())
    }

    /**
     * Validates the register request
     * @return true or false depending on if all data needed is present
     */
    fun isRecoverValid(): Boolean {
        return this.doctor_email.isNullOrEmpty()
    }
}

