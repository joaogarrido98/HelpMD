package com.example.models

import io.ktor.server.auth.*

/**
 * This file contains all the models for the patient requests
 * Patient model
 * Login model
 */

data class Patient(
    val patient_id: Int? = null,
    val patient_email: String? = null,
    val patient_name: String? = null,
    val patient_dob: String? = null,
    val patient_weight: Int? = null,
    val patient_height: Int? = null,
    val patient_gender: String? = null,
    var patient_password: String? = null,
    val patient_active: Boolean? = null,
    val patient_deaf: Boolean? = null,
    val patient_doctor: Int? = null,
    var patient_old_password: String? = null,
) : Principal{
    /**
     * Validates the recover password request
     * @return true or false depending on if all data needed is present
     */
    fun isRecoverValid(): Boolean {
        return this.patient_email.isNullOrEmpty()
    }


    /**
     * Validates the login request
     * @return true or false depending on if all data needed is present
     */
    fun isLoginValid(): Boolean {
        return !(this.patient_email.isNullOrEmpty() || this.patient_password.isNullOrEmpty())
    }

    /**
     * Validates the register request
     * @return true or false depending on if all data needed is present
     */
    fun isRegisterValid(): Boolean {
        return !(this.patient_email.isNullOrEmpty() || this.patient_password.isNullOrEmpty() || this.patient_name.isNullOrEmpty() ||
                this.patient_dob.isNullOrEmpty() || this.patient_gender.isNullOrEmpty())
    }

    /**
     * Validates the change password request
     * @return true or false depending on if all data needed is present
     */
    fun isChangeValid(): Boolean {
        return !(this.patient_password.isNullOrEmpty() || this.patient_old_password.isNullOrEmpty())
    }
}

data class PatientDoctor(
    val patient_id: Int,
    val patient_email: String,
    val patient_name: String,
    val patient_dob: String,
    val patient_weight: Int,
    val patient_height: Int,
    val patient_gender: String,
    val patient_deaf: Boolean,
    val patient_allergies: String,
    val patient_family: String,
    val patient_lifestyle: String,
    val patient_vaccines: String,
    val patient_diseases: String,
    val patient_blood: String
)
