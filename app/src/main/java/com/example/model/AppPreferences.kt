package com.example.model

enum class NumeralSystem(val labelAr: String, val labelEn: String) {
    ARABIC("أرقام عربية (1, 2, 3)", "Arabic (1, 2, 3)"),
    EASTERN_ARABIC_INDIC("أرقام مشرقية (١, ٢, ٣)", "Eastern Arabic-Indic (١, ٢, ٣)")
}

enum class ImsakVisibility(val labelAr: String, val labelEn: String) {
    ALWAYS("دائماً", "Always"),
    RAMADAN_ONLY("في رمضان فقط", "Ramadan Only"),
    NEVER("إخفاء دائماً", "Never")
}

enum class AppLanguage(val code: String, val labelAr: String, val labelEn: String) {
    ARABIC("ar", "العربية", "Arabic"),
    ENGLISH("en", "English", "English")
}

data class SavedMosque(
    val name: String,
    val url: String
)

data class AppPreferences(
    val mosqueUrl: String = "",
    val savedMosques: List<SavedMosque> = listOf(
        SavedMosque(name = "مسجد الخلفاء الراشدين", url = "https://mawaqit.net/ar/ain-tedeles")
    ),
    val language: AppLanguage = AppLanguage.ARABIC,
    val numeralSystem: NumeralSystem = NumeralSystem.ARABIC,
    val isDarkMode: Boolean = true,
    val athanVolume: Float = 0.8f,
    val athanEnabled: Boolean = true,
    val fajrAthanEnabled: Boolean = true,
    val preAthanEnabled: Boolean = true,
    val preAthanMinutes: Int = 10,
    val imsakVisibility: ImsakVisibility = ImsakVisibility.ALWAYS,
    val eidCelebrationDays: Int = 3,
    val hijriCorrection: Int = 0,
    val showSpecialDayBadge: Boolean = true,
    val prayerOffsets: Map<PrayerType, Int> = emptyMap()
) {
    fun getOffset(prayer: PrayerType): Int = prayerOffsets[prayer] ?: 0

    fun resetToDefaults(preserveMosqueUrl: Boolean = true): AppPreferences {
        return AppPreferences(
            mosqueUrl = if (preserveMosqueUrl) this.mosqueUrl else "",
            savedMosques = if (preserveMosqueUrl) this.savedMosques else listOf(
                SavedMosque(name = "مسجد الخلفاء الراشدين", url = "https://mawaqit.net/ar/ain-tedeles")
            ),
            language = this.language,
            numeralSystem = NumeralSystem.ARABIC,
            isDarkMode = true,
            athanVolume = 0.8f,
            athanEnabled = true,
            fajrAthanEnabled = true,
            preAthanEnabled = true,
            preAthanMinutes = 10,
            imsakVisibility = ImsakVisibility.ALWAYS,
            eidCelebrationDays = 3,
            hijriCorrection = 0,
            showSpecialDayBadge = true,
            prayerOffsets = emptyMap()
        )
    }

    // Format numbers according to user's selected NumeralSystem
    fun formatNumber(number: Int): String {
        return formatNumber(number.toString())
    }

    fun formatNumber(input: String): String {
        return if (numeralSystem == NumeralSystem.EASTERN_ARABIC_INDIC) {
            input.map { char ->
                when (char) {
                    '0' -> '٠'
                    '1' -> '١'
                    '2' -> '٢'
                    '3' -> '٣'
                    '4' -> '٤'
                    '5' -> '٥'
                    '6' -> '٦'
                    '7' -> '٧'
                    '8' -> '٨'
                    '9' -> '٩'
                    else -> char
                }
            }.joinToString("")
        } else {
            input.map { char ->
                when (char) {
                    '٠' -> '0'
                    '١' -> '1'
                    '٢' -> '2'
                    '٣' -> '3'
                    '٤' -> '4'
                    '٥' -> '5'
                    '٦' -> '6'
                    '٧' -> '7'
                    '٨' -> '8'
                    '٩' -> '9'
                    else -> char
                }
            }.joinToString("")
        }
    }
}
