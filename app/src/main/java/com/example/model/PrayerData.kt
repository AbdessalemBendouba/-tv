package com.example.model

data class PrayerTimeItem(
    val type: PrayerType,
    val baseTimeString: String, // HH:mm from calendar
    val adjustedTimeString: String, // HH:mm after offset applied
    val totalMinutes: Int, // Minutes from midnight
    val waitMinutes: Int, // Iqama wait
    val iqamaTotalMinutes: Int = totalMinutes + waitMinutes,
    val isPassed: Boolean = false,
    val isCurrent: Boolean = false,
    val isNext: Boolean = false,
    val timeUntilMinutes: Int = 0
)

data class DayPrayerSchedule(
    val mosqueName: String = "",
    val mosqueSlug: String = "",
    val timezone: String = "Africa/Algiers",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val gregorianDate: String = "",
    val hijriDate: String = "",
    val hijriDay: Int = 1,
    val hijriMonth: Int = 1,
    val hijriYear: Int = 1447,
    val prayers: List<PrayerTimeItem> = emptyList(),
    val imsakTime: String = "",
    val shuruqTime: String = "",
    val specialDay: SpecialDay = SpecialDay.None,
    val calendarRaw: List<Map<String, List<String>>> = emptyList()
)
