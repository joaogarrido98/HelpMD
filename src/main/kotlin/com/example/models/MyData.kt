package com.example.models

import java.awt.print.Book

data class MyData(
    val patientId: Patient,
    val result: List<AppointmentResult>,
    val bookings: List<Book>,
    val history: List<PatientHistory>,
    val prescriptions: List<Prescriptions>
)
