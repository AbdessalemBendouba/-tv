package com.example.model

enum class PrayerType(
    val englishName: String,
    val arabicName: String,
    val defaultWaitMinutes: Int
) {
    FAJR("Fajr", "الفجر", 15),
    SHURUQ("Sunrise", "الشروق", 0),
    DHUHR("Dhuhr", "الظهر", 10),
    ASR("Asr", "العصر", 10),
    MAGHRIB("Maghrib", "المغرب", 5),
    ISHA("Isha", "العشاء", 10);

    fun getDisplayName(isFriday: Boolean = false): String {
        return if (this == DHUHR && isFriday) "الجمعة" else arabicName
    }
}
