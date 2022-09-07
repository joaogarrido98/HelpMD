package com.example.tools

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.Doctor
import com.example.models.Patient

/**
 * class to manage the jwt tokens
 * creation and verifying tokens
 */
object JwtManager {
    private val issuer = System.getenv("JWT_ISSUER")
    private val jwtSecret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC256(jwtSecret)
    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    /**
     * @param patient contains Patient object which we use to generate a token
     * @return generated token
     */
    fun generateTokenPatient(patient: Patient): String {
        return JWT.create()
            .withSubject("helpmd")
            .withIssuer(issuer)
            .withClaim("patient_id", patient.patient_id)
            .withClaim("patient_email", patient.patient_email)
            .sign(algorithm)
    }


    /**
     * @param doctor contains Patient object which we use to generate a token
     * @return generated token
     */
    fun generateTokenDoctor(doctor: Doctor): String {
        return JWT.create()
            .withSubject("helpmd")
            .withIssuer(issuer)
            .withClaim("doctor_id", doctor.doctor_id)
            .sign(algorithm)
    }
}