package com.example.model

data class DailyAyah(
    val arabicText: String,
    val translationText: String,
    val surahName: String,
    val ayahNumber: Int
)

data class DailyHadith(
    val text: String,
    val source: String
)

data class DailyDua(
    val arabicText: String,
    val translationText: String,
    val reference: String
)

data class DailyDhikr(
    val arabicText: String,
    val translationText: String,
    val count: Int = 0
)
