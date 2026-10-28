package com.example

import com.example.model.*
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testSpecialDayEvaluation() {
        val eidFitr = SpecialDay.EidAlFitr(1)
        assertTrue(eidFitr.isSpecial)
        assertTrue(eidFitr.title.contains("عيد الفطر"))

        val eidAdha = SpecialDay.EidAlAdha(2)
        assertTrue(eidAdha.isSpecial)
        assertTrue(eidAdha.subtitle.contains("اليوم 2"))

        val ramadanAshra1 = SpecialDay.Ramadan(5, 1)
        assertTrue(ramadanAshra1.isSpecial)
        assertTrue(ramadanAshra1.subtitle.contains("عشر الرحمة"))

        val ramadanAshra2 = SpecialDay.Ramadan(15, 2)
        assertTrue(ramadanAshra2.subtitle.contains("عشر المغفرة"))

        val ramadanAshra3 = SpecialDay.Ramadan(25, 3)
        assertTrue(ramadanAshra3.subtitle.contains("عشر العتق من النار"))

        val normalDay = SpecialDay.None
        assertFalse(normalDay.isSpecial)
    }

    @Test
    fun testNumeralFormatting() {
        val arabicPrefs = AppPreferences(numeralSystem = NumeralSystem.ARABIC)
        assertEquals("12:45:09", arabicPrefs.formatNumber("12:45:09"))
        assertEquals("12:45:09", arabicPrefs.formatNumber("١٢:٤٥:٠٩"))

        val easternPrefs = AppPreferences(numeralSystem = NumeralSystem.EASTERN_ARABIC_INDIC)
        assertEquals("١٢:٤٥:٠٩", easternPrefs.formatNumber("12:45:09"))
        assertEquals("١٤٤٧", easternPrefs.formatNumber(1447))
    }

    @Test
    fun testPrayerOffsets() {
        val offsets = mapOf(PrayerType.FAJR to -5, PrayerType.MAGHRIB to 3)
        val prefs = AppPreferences(prayerOffsets = offsets)
        assertEquals(-5, prefs.getOffset(PrayerType.FAJR))
        assertEquals(3, prefs.getOffset(PrayerType.MAGHRIB))
        assertEquals(0, prefs.getOffset(PrayerType.DHUHR))
    }
}
