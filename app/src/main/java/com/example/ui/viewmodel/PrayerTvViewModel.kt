package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.DailiesRepository
import com.example.data.MawaqitScraper
import com.example.data.PreferencesManager
import com.example.model.*
import com.example.service.AlarmScheduler
import com.example.service.PrayerNotificationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class TvScreenTab {
    MAIN,
    ASTRONOMY,
    DAILIES,
    SETTINGS
}

data class PrayerTvUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val schedule: DayPrayerSchedule? = null,
    val preferences: AppPreferences = AppPreferences(),
    val activeTab: TvScreenTab = TvScreenTab.MAIN,
    val isDrawerOpen: Boolean = false,
    val isOnboardingVisible: Boolean = false,
    // Live ticking values
    val currentTimeString: String = "--:--:--",
    val currentTotalSeconds: Int = 0,
    val nextPrayer: PrayerTimeItem? = null,
    val nextPrayerCountdownFormatted: String = "--:--:--",
    val nextPrayerSecondsLeft: Int = 0,
    val lastPrayer: PrayerTimeItem? = null,
    val iqamaActivePrayer: PrayerTimeItem? = null,
    val iqamaSecondsLeft: Int = -1,
    val elapsedProgressPercent: Float = 0f,
    val sunPositionAngleRad: Float = 0f,
    val isDaytime: Boolean = true,
    // Astronomy & Moon
    val moonPhaseName: String = "محاق (قمر جديد)",
    val moonDrawableRes: Int = R.drawable.moon_new,
    val qiblaAngleDegrees: Float = 0f,
    // Dailies
    val dailyAyah: DailyAyah? = null,
    val dailyHadith: DailyHadith? = null,
    val dailyDua: DailyDua? = null,
    val dailyDhikr: DailyDhikr? = null,
    val dhikrCount: Int = 0
)

class PrayerTvViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager = PreferencesManager(application)
    private val scraper = MawaqitScraper()
    private val dailiesRepository = DailiesRepository()

    private val _uiState = MutableStateFlow(PrayerTvUiState())
    val uiState: StateFlow<PrayerTvUiState> = _uiState.asStateFlow()

    init {
        // Collect preferences
        viewModelScope.launch {
            preferencesManager.preferencesFlow.collect { prefs ->
                _uiState.update { it.copy(preferences = prefs) }
                if (prefs.mosqueUrl.isBlank()) {
                    _uiState.update { it.copy(isOnboardingVisible = true, isLoading = false) }
                } else {
                    _uiState.update { it.copy(isOnboardingVisible = false) }
                    loadSchedule()
                }
            }
        }

        // Load Dailies
        loadDailies()

        // Start 1-second ticker loop
        startClockTicker()
    }

    fun selectTab(tab: TvScreenTab) {
        _uiState.update { it.copy(activeTab = tab, isDrawerOpen = false) }
    }

    fun toggleDrawer(open: Boolean? = null) {
        _uiState.update { current ->
            current.copy(isDrawerOpen = open ?: !current.isDrawerOpen)
        }
    }

    fun setMosqueUrl(url: String) {
        val trimmed = url.trim()
        if (trimmed.isNotBlank()) {
            preferencesManager.setMosqueUrl(trimmed)
            _uiState.update { it.copy(isOnboardingVisible = false, isLoading = true) }
            loadSchedule()
        }
    }

    fun addMosque(name: String, url: String) {
        val trimmed = url.trim()
        val trimmedName = name.trim().ifBlank { "مسجد جديد" }
        if (trimmed.isNotBlank()) {
            preferencesManager.addSavedMosque(trimmedName, trimmed)
            _uiState.update { it.copy(isLoading = true) }
            loadSchedule()
        }
    }

    fun removeMosque(url: String) {
        preferencesManager.removeSavedMosque(url)
        loadSchedule()
    }

    fun switchMosque(url: String) {
        val trimmed = url.trim()
        if (trimmed.isNotBlank() && trimmed != _uiState.value.preferences.mosqueUrl) {
            preferencesManager.setActiveMosque(trimmed)
            _uiState.update { it.copy(isLoading = true) }
            loadSchedule()
        }
    }

    fun validateAndSetMosqueUrl(url: String, onResult: (Boolean, String?) -> Unit) {
        val trimmed = url.trim()
        if (trimmed.isBlank()) {
            onResult(false, "يرجى إدخال رابط أو رمز المسجد")
            return
        }

        viewModelScope.launch {
            val result = scraper.fetchSchedule(trimmed, _uiState.value.preferences)
            result.onSuccess { schedule ->
                val mosqueName = schedule.mosqueName.ifBlank { "مسجدي" }
                preferencesManager.addSavedMosque(mosqueName, trimmed)
                _uiState.update {
                    it.copy(
                        isOnboardingVisible = false,
                        isLoading = false,
                        schedule = schedule,
                        errorMessage = null
                    )
                }
                updateOngoingNotification(schedule)
                onResult(true, null)
            }.onFailure { err ->
                val msg = err.localizedMessage ?: "تعذر العثور على المسجد"
                onResult(false, msg)
            }
        }
    }

    fun restoreDefaultSettings() {
        preferencesManager.restoreDefaults(preserveMosqueUrl = true)
        loadSchedule()
    }

    fun updatePreferences(newPrefs: AppPreferences) {
        preferencesManager.updatePreferences(newPrefs)
        loadSchedule()
    }

    fun incrementDhikr() {
        _uiState.update { it.copy(dhikrCount = it.dhikrCount + 1) }
    }

    fun resetDhikr() {
        _uiState.update { it.copy(dhikrCount = 0) }
    }

    fun testAthanSound() {
        val prefs = _uiState.value.preferences
        val intent = Intent(getApplication(), PrayerNotificationService::class.java).apply {
            action = PrayerNotificationService.ACTION_PLAY_ATHAN
            putExtra(PrayerNotificationService.EXTRA_PRAYER_NAME, "تجريبي")
            putExtra(PrayerNotificationService.EXTRA_IS_FAJR, false)
            putExtra(PrayerNotificationService.EXTRA_VOLUME, prefs.athanVolume)
        }
        getApplication<Application>().startService(intent)
    }

    fun stopAthanSound() {
        val intent = Intent(getApplication(), PrayerNotificationService::class.java).apply {
            action = PrayerNotificationService.ACTION_STOP_AUDIO
        }
        getApplication<Application>().startService(intent)
    }

    fun loadSchedule() {
        val prefs = _uiState.value.preferences
        if (prefs.mosqueUrl.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = scraper.fetchSchedule(prefs.mosqueUrl, prefs)

            result.onSuccess { schedule ->
                // Calculate moon phase & Qibla
                val moonInfo = calculateMoonPhase(schedule.hijriDay)
                val qiblaAngle = calculateQiblaAngle(schedule.latitude, schedule.longitude)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        schedule = schedule,
                        moonPhaseName = moonInfo.first,
                        moonDrawableRes = moonInfo.second,
                        qiblaAngleDegrees = qiblaAngle,
                        errorMessage = null
                    )
                }

                // Schedule background Athan alarms
                AlarmScheduler.schedulePrayerAlarms(
                    context = getApplication(),
                    schedule = schedule,
                    volume = prefs.athanVolume,
                    athanEnabled = prefs.athanEnabled,
                    fajrAthanEnabled = prefs.fajrAthanEnabled,
                    preAthanEnabled = prefs.preAthanEnabled,
                    preAthanMins = prefs.preAthanMinutes
                )

                // Update ongoing notification
                updateOngoingNotification(schedule)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "فشل تحميل البيانات من مواقيت"
                    )
                }
            }
        }
    }

    private fun loadDailies() {
        viewModelScope.launch {
            val hadith = dailiesRepository.fetchHadith()
            val ayah = dailiesRepository.fetchDailyAyah()
            val dua = dailiesRepository.getTodayDua()
            val dhikr = dailiesRepository.getTodayDhikr()

            _uiState.update {
                it.copy(
                    dailyHadith = hadith,
                    dailyAyah = ayah,
                    dailyDua = dua,
                    dailyDhikr = dhikr
                )
            }
        }
    }

    private fun startClockTicker() {
        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                val state = _uiState.value
                val schedule = state.schedule
                val tzString = schedule?.timezone ?: "Africa/Algiers"
                val tz = TimeZone.getTimeZone(tzString)
                val now = Calendar.getInstance(tz)

                val h = now.get(Calendar.HOUR_OF_DAY)
                val m = now.get(Calendar.MINUTE)
                val s = now.get(Calendar.SECOND)
                val currentTotalSecs = h * 3600 + m * 60 + s
                val currentTotalMins = h * 60 + m

                val timeStr = String.format(Locale.US, "%02d:%02d:%02d", h, m, s)
                val formattedTime = state.preferences.formatNumber(timeStr)

                if (schedule != null && schedule.prayers.isNotEmpty()) {
                    // Filter canonical 5 prayers + sunrise
                    val prayers = schedule.prayers.filter {
                        it.type in listOf(PrayerType.FAJR, PrayerType.DHUHR, PrayerType.ASR, PrayerType.MAGHRIB, PrayerType.ISHA)
                    }

                    // Next Prayer & Countdown
                    var nextP: PrayerTimeItem? = null
                    var nextDiffSecs = Int.MAX_VALUE
                    var lastP: PrayerTimeItem? = null
                    var activeIqamaPrayer: PrayerTimeItem? = null
                    var iqamaSecsLeft = -1

                    val fajrSecs = (prayers.firstOrNull()?.totalMinutes ?: 300) * 60

                    for (i in prayers.indices) {
                        val p = prayers[i]
                        val pSecs = p.totalMinutes * 60
                        val iqamaSecs = p.iqamaTotalMinutes * 60

                        // Active Iqama check
                        if (currentTotalSecs in pSecs until iqamaSecs) {
                            activeIqamaPrayer = p
                            iqamaSecsLeft = iqamaSecs - currentTotalSecs
                        }

                        // Last prayer
                        if (currentTotalSecs >= pSecs) {
                            lastP = p
                        }

                        // Next prayer
                        val diff = pSecs - currentTotalSecs
                        if (diff > 0 && diff < nextDiffSecs) {
                            nextDiffSecs = diff
                            nextP = p
                        }
                    }

                    // If after Isha, next prayer is tomorrow's Fajr
                    if (nextP == null && prayers.isNotEmpty()) {
                        nextP = prayers.first()
                        val tomorrowFajrSecs = (24 * 3600) + fajrSecs
                        nextDiffSecs = tomorrowFajrSecs - currentTotalSecs
                    }

                    // When Fajr is next (between midnight and Fajr), previous prayer was yesterday's Isha
                    if (lastP == null && prayers.isNotEmpty()) {
                        lastP = prayers.find { it.type == PrayerType.ISHA } ?: prayers.lastOrNull()
                    }

                    val countdownFormatted = formatSecondsToCountdown(nextDiffSecs, state.preferences)

                    // Interval progress
                    val ishaP = prayers.find { it.type == PrayerType.ISHA } ?: prayers.lastOrNull()
                    val ishaSecs = (ishaP?.totalMinutes ?: 1200) * 60

                    val prevSecs: Int
                    val nextSecs: Int
                    if (currentTotalSecs < fajrSecs) {
                        prevSecs = ishaSecs - 86400
                        nextSecs = fajrSecs
                    } else if (currentTotalSecs >= ishaSecs) {
                        prevSecs = ishaSecs
                        nextSecs = fajrSecs + 86400
                    } else {
                        prevSecs = (lastP?.totalMinutes ?: (fajrSecs / 60)) * 60
                        nextSecs = (nextP?.totalMinutes ?: ((fajrSecs / 60) + 1440)) * 60
                    }
                    val totalInterval = (nextSecs - prevSecs).coerceAtLeast(1)
                    val elapsed = (currentTotalSecs - prevSecs).coerceAtLeast(0)
                    val progressPct = ((elapsed.toFloat() / totalInterval.toFloat()) * 100f).coerceIn(0f, 100f)

                    // Sun Angle Calculation
                    val shuruqMins = schedule.prayers.find { it.type == PrayerType.SHURUQ }?.totalMinutes ?: 390
                    val maghribMins = schedule.prayers.find { it.type == PrayerType.MAGHRIB }?.totalMinutes ?: 1110
                    val dhuhrMins = schedule.prayers.find { it.type == PrayerType.DHUHR }?.totalMinutes ?: 750

                    val isDay = currentTotalMins in shuruqMins until maghribMins
                    val sunAngle = if (isDay) {
                        if (currentTotalMins < dhuhrMins) {
                            val ratio = (currentTotalMins - shuruqMins).toFloat() / (dhuhrMins - shuruqMins).coerceAtLeast(1)
                            Math.PI.toFloat() + ratio * (Math.PI.toFloat() / 2f)
                        } else {
                            val ratio = (currentTotalMins - dhuhrMins).toFloat() / (maghribMins - dhuhrMins).coerceAtLeast(1)
                            1.5f * Math.PI.toFloat() + ratio * (Math.PI.toFloat() / 2f)
                        }
                    } else {
                        // Night celestial moon tracking
                        Math.PI.toFloat() + 0.5f * Math.PI.toFloat()
                    }

                    // Mark updated prayer items
                    val updatedPrayers = schedule.prayers.map { p ->
                        val isPassed = currentTotalMins >= p.totalMinutes
                        val isNext = (p.type == nextP?.type)
                        val isCurrent = (p.type == activeIqamaPrayer?.type)
                        val timeUntil = ((p.totalMinutes - currentTotalMins + 1440) % 1440)
                        p.copy(
                            isPassed = isPassed,
                            isNext = isNext,
                            isCurrent = isCurrent,
                            timeUntilMinutes = timeUntil
                        )
                    }

                    _uiState.update {
                        it.copy(
                            currentTimeString = formattedTime,
                            currentTotalSeconds = currentTotalSecs,
                            nextPrayer = nextP,
                            nextPrayerCountdownFormatted = countdownFormatted,
                            nextPrayerSecondsLeft = nextDiffSecs,
                            lastPrayer = lastP,
                            iqamaActivePrayer = activeIqamaPrayer,
                            iqamaSecondsLeft = iqamaSecsLeft,
                            elapsedProgressPercent = progressPct,
                            sunPositionAngleRad = sunAngle,
                            isDaytime = isDay,
                            schedule = schedule.copy(prayers = updatedPrayers)
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(currentTimeString = formattedTime, currentTotalSeconds = currentTotalSecs)
                    }
                }

                delay(1000)
            }
        }
    }

    private fun formatSecondsToCountdown(totalSecs: Int, prefs: AppPreferences): String {
        val s = totalSecs.coerceAtLeast(0)
        val hours = s / 3600
        val mins = (s % 3600) / 60
        val secs = s % 60
        val raw = String.format(Locale.US, "%02d:%02d:%02d", hours, mins, secs)
        return prefs.formatNumber(raw)
    }

    private fun updateOngoingNotification(schedule: DayPrayerSchedule) {
        val nextP = _uiState.value.nextPrayer
        val title = if (nextP != null) {
            "الصلاة القادمة: ${nextP.type.arabicName} (${nextP.adjustedTimeString})"
        } else {
            "حي على الصلاة - ${schedule.mosqueName}"
        }
        val text = "${schedule.mosqueName} • ${schedule.hijriDate}"

        val intent = Intent(getApplication(), PrayerNotificationService::class.java).apply {
            action = PrayerNotificationService.ACTION_UPDATE_NOTIFICATION
            putExtra(PrayerNotificationService.EXTRA_TITLE, title)
            putExtra(PrayerNotificationService.EXTRA_TEXT, text)
        }
        try {
            getApplication<Application>().startService(intent)
        } catch (e: Exception) {
            Log.w("PrayerTvViewModel", "Could not start ongoing notification: ${e.message}")
        }
    }

    private fun calculateMoonPhase(hijriDay: Int): Pair<String, Int> {
        val day = hijriDay.coerceIn(1, 30)
        return when {
            day in 1..2 -> Pair("المحاق (قمر جديد)", R.drawable.moon_new)
            day in 3..7 -> Pair("الهلال المتزايد", R.drawable.moon_waxing_crescent)
            day == 8 -> Pair("التربيع الأول", R.drawable.moon_first_quarter)
            day in 9..13 -> Pair("الأحدب المتزايد", R.drawable.moon_waxing_gibbous)
            day in 14..15 -> Pair("البدر (قمر كامل)", R.drawable.moon_full)
            day in 16..21 -> Pair("الأحدب المتناقص", R.drawable.moon_waning_gibbous)
            day in 22..23 -> Pair("التربيع الأخير", R.drawable.moon_third_quarter)
            else -> Pair("الهلال المتناقص", R.drawable.moon_waning_crescent)
        }
    }

    private fun calculateQiblaAngle(latitude: Double, longitude: Double): Float {
        val latMecca = Math.toRadians(21.4225)
        val lonMecca = Math.toRadians(39.8262)
        val latMosque = Math.toRadians(latitude)
        val lonMosque = Math.toRadians(longitude)

        val dLon = lonMecca - lonMosque
        val y = Math.sin(dLon)
        val x = Math.cos(latMosque) * Math.tan(latMecca) - Math.sin(latMosque) * Math.cos(dLon)

        var qiblaDeg = Math.toDegrees(Math.atan2(y, x)).toFloat()
        if (qiblaDeg < 0) qiblaDeg += 360f
        return qiblaDeg
    }
}
