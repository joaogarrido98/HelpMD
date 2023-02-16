package com.example.models

data class Schedule(
    val schedule_id: Int,
    val schedule_doctor: Int,
    val schedule_day_of_week: Int,
    val schedule_start: String,
    val schedule_end: String,
)


data class AddScheduleRequest(
    var schedule_doctor: Int,
    val schedule_day_of_week: Int,
    val schedule_start: String,
    val schedule_end: String,
) {
    fun isValid(): Boolean {
        return !(schedule_start.isEmpty() || schedule_end.isEmpty() || schedule_day_of_week < 1 ||
                schedule_day_of_week > 7)
    }
}
