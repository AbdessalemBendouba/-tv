package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.AppPreferences
import com.example.model.ImsakVisibility
import com.example.model.NumeralSystem
import com.example.model.PrayerType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import com.example.model.SavedMosque
import org.json.JSONArray
import org.json.JSONObject

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("hayya_al_salah_prefs", Context.MODE_PRIVATE)

    private val _preferencesFlow = MutableStateFlow(loadPreferences())
    val preferencesFlow: StateFlow<AppPreferences> = _preferencesFlow.asStateFlow()

    fun getPreferences(): AppPreferences = _preferencesFlow.value

    fun updatePreferences(newPrefs: AppPreferences) {
        prefs.edit().apply {
            putString(KEY_MOSQUE_URL, newPrefs.mosqueUrl)
            putString(KEY_LANGUAGE, newPrefs.language.name)
            putString(KEY_NUMERAL_SYSTEM, newPrefs.numeralSystem.name)
            putBoolean(KEY_IS_DARK_MODE, newPrefs.isDarkMode)
            putFloat(KEY_ATHAN_VOLUME, newPrefs.athanVolume)
            putBoolean(KEY_ATHAN_ENABLED, newPrefs.athanEnabled)
            putBoolean(KEY_FAJR_ATHAN_ENABLED, newPrefs.fajrAthanEnabled)
            putBoolean(KEY_PRE_ATHAN_ENABLED, newPrefs.preAthanEnabled)
            putInt(KEY_PRE_ATHAN_MINS, newPrefs.preAthanMinutes)
            putString(KEY_IMSAK_VISIBILITY, newPrefs.imsakVisibility.name)
            putInt(KEY_EID_DAYS, newPrefs.eidCelebrationDays)
            putInt(KEY_HIJRI_CORRECTION, newPrefs.hijriCorrection)
            putBoolean(KEY_SHOW_SPECIAL_DAY_BADGE, newPrefs.showSpecialDayBadge)

            // Save savedMosques
            val mosquesArray = JSONArray()
            newPrefs.savedMosques.forEach { mosque ->
                val obj = JSONObject().apply {
                    put("name", mosque.name)
                    put("url", mosque.url)
                }
                mosquesArray.put(obj)
            }
            putString(KEY_SAVED_MOSQUES, mosquesArray.toString())

            // Save offsets
            PrayerType.entries.forEach { p ->
                putInt("offset_${p.name}", newPrefs.getOffset(p))
            }
            apply()
        }
        _preferencesFlow.value = newPrefs
    }

    fun restoreDefaults(preserveMosqueUrl: Boolean = true) {
        val currentPrefs = _preferencesFlow.value
        val defaultPrefs = currentPrefs.resetToDefaults(preserveMosqueUrl = preserveMosqueUrl)
        updatePreferences(defaultPrefs)
    }

    fun setMosqueUrl(url: String) {
        val trimmed = url.trim()
        val current = _preferencesFlow.value
        val list = current.savedMosques.toMutableList()
        val existingIndex = list.indexOfFirst { it.url.trim().equals(trimmed, ignoreCase = true) }
        if (existingIndex < 0 && trimmed.isNotBlank()) {
            list.add(SavedMosque(name = "مسجد جديد", url = trimmed))
        }
        val updated = current.copy(mosqueUrl = trimmed, savedMosques = list)
        updatePreferences(updated)
    }

    fun addSavedMosque(name: String, url: String) {
        val trimmedUrl = url.trim()
        val trimmedName = name.trim().ifBlank { "مسجد جديد" }
        if (trimmedUrl.isBlank()) return

        val current = _preferencesFlow.value
        val list = current.savedMosques.toMutableList()
        val existingIndex = list.indexOfFirst { it.url.trim().equals(trimmedUrl, ignoreCase = true) }
        if (existingIndex >= 0) {
            list[existingIndex] = SavedMosque(name = trimmedName, url = trimmedUrl)
        } else {
            list.add(SavedMosque(name = trimmedName, url = trimmedUrl))
        }
        val updated = current.copy(mosqueUrl = trimmedUrl, savedMosques = list)
        updatePreferences(updated)
    }

    fun removeSavedMosque(url: String) {
        val current = _preferencesFlow.value
        val list = current.savedMosques.filterNot { it.url == url }
        val newActiveUrl = if (current.mosqueUrl == url) {
            list.firstOrNull()?.url ?: ""
        } else {
            current.mosqueUrl
        }
        val updated = current.copy(mosqueUrl = newActiveUrl, savedMosques = list)
        updatePreferences(updated)
    }

    fun setActiveMosque(url: String) {
        val current = _preferencesFlow.value
        val updated = current.copy(mosqueUrl = url.trim())
        updatePreferences(updated)
    }

    private fun loadPreferences(): AppPreferences {
        val mosqueUrl = prefs.getString(KEY_MOSQUE_URL, "") ?: ""
        val numeralSystemName = prefs.getString(KEY_NUMERAL_SYSTEM, NumeralSystem.ARABIC.name)
        val numeralSystem = try {
            NumeralSystem.valueOf(numeralSystemName ?: NumeralSystem.ARABIC.name)
        } catch (e: Exception) {
            NumeralSystem.ARABIC
        }

        val isDarkMode = prefs.getBoolean(KEY_IS_DARK_MODE, true)
        val volume = prefs.getFloat(KEY_ATHAN_VOLUME, 0.8f)
        val athanEnabled = prefs.getBoolean(KEY_ATHAN_ENABLED, true)
        val fajrAthanEnabled = prefs.getBoolean(KEY_FAJR_ATHAN_ENABLED, true)
        val preAthanEnabled = prefs.getBoolean(KEY_PRE_ATHAN_ENABLED, true)
        val preAthanMins = prefs.getInt(KEY_PRE_ATHAN_MINS, 10)

        val imsakVisibilityName = prefs.getString(KEY_IMSAK_VISIBILITY, ImsakVisibility.ALWAYS.name)
        val imsakVisibility = try {
            ImsakVisibility.valueOf(imsakVisibilityName ?: ImsakVisibility.ALWAYS.name)
        } catch (e: Exception) {
            ImsakVisibility.ALWAYS
        }

        val eidDays = prefs.getInt(KEY_EID_DAYS, 3)
        val hijriCorrection = prefs.getInt(KEY_HIJRI_CORRECTION, 0)
        val showSpecialDayBadge = prefs.getBoolean(KEY_SHOW_SPECIAL_DAY_BADGE, true)

        val offsets = mutableMapOf<PrayerType, Int>()
        PrayerType.entries.forEach { p ->
            val offset = prefs.getInt("offset_${p.name}", 0)
            offsets[p] = offset
        }

        val languageName = prefs.getString(KEY_LANGUAGE, com.example.model.AppLanguage.ARABIC.name)
        val language = try {
            com.example.model.AppLanguage.valueOf(languageName ?: com.example.model.AppLanguage.ARABIC.name)
        } catch (e: Exception) {
            com.example.model.AppLanguage.ARABIC
        }

        val savedMosques = loadSavedMosques(mosqueUrl)

        return AppPreferences(
            mosqueUrl = mosqueUrl,
            savedMosques = savedMosques,
            language = language,
            numeralSystem = numeralSystem,
            isDarkMode = isDarkMode,
            athanVolume = volume,
            athanEnabled = athanEnabled,
            fajrAthanEnabled = fajrAthanEnabled,
            preAthanEnabled = preAthanEnabled,
            preAthanMinutes = preAthanMins,
            imsakVisibility = imsakVisibility,
            eidCelebrationDays = eidDays,
            hijriCorrection = hijriCorrection,
            showSpecialDayBadge = showSpecialDayBadge,
            prayerOffsets = offsets
        )
    }

    private fun loadSavedMosques(activeUrl: String): List<SavedMosque> {
        val jsonString = prefs.getString(KEY_SAVED_MOSQUES, null)
        val list = mutableListOf<SavedMosque>()
        if (!jsonString.isNullOrBlank()) {
            try {
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val name = obj.optString("name", "مسجد")
                    val url = obj.optString("url", "")
                    if (url.isNotBlank()) {
                        list.add(SavedMosque(name = name, url = url))
                    }
                }
            } catch (e: Exception) {
                // Ignore parse errors
            }
        }
        if (list.isEmpty()) {
            if (activeUrl.isNotBlank()) {
                list.add(SavedMosque(name = "مسجدي", url = activeUrl))
            } else {
                list.add(SavedMosque(name = "مسجد الخلفاء الراشدين", url = "https://mawaqit.net/ar/ain-tedeles"))
            }
        } else if (activeUrl.isNotBlank() && list.none { it.url.trim().equals(activeUrl.trim(), ignoreCase = true) }) {
            list.add(0, SavedMosque(name = "المسجد الحالي", url = activeUrl))
        }
        return list
    }

    companion object {
        private const val KEY_MOSQUE_URL = "mosque_url"
        private const val KEY_SAVED_MOSQUES = "saved_mosques"
        private const val KEY_LANGUAGE = "app_language"
        private const val KEY_NUMERAL_SYSTEM = "numeral_system"
        private const val KEY_IS_DARK_MODE = "is_dark_mode"
        private const val KEY_ATHAN_VOLUME = "athan_volume"
        private const val KEY_ATHAN_ENABLED = "athan_enabled"
        private const val KEY_FAJR_ATHAN_ENABLED = "fajr_athan_enabled"
        private const val KEY_PRE_ATHAN_ENABLED = "pre_athan_enabled"
        private const val KEY_PRE_ATHAN_MINS = "pre_athan_mins"
        private const val KEY_IMSAK_VISIBILITY = "imsak_visibility"
        private const val KEY_EID_DAYS = "eid_days"
        private const val KEY_HIJRI_CORRECTION = "hijri_correction"
        private const val KEY_SHOW_SPECIAL_DAY_BADGE = "show_special_day_badge"
    }
}
