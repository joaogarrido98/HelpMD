package com.example.models

data class Schedule(
    val schedule_id: Int? = null,
    var schedule_doctor:  Int? = null,
    val schedule_day_of_week:  Int? = null,
    val schedule_start: String? = null,
    val schedule_end: String? = null,
){
    fun isValid(): Boolean {
        return !(schedule_start.isNullOrEmpty() || schedule_end.isNullOrEmpty() || schedule_day_of_week!! < 1 ||
                schedule_day_of_week > 7)
    }
}