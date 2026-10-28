package com.example.data

import android.util.Log
import com.example.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MawaqitScraper {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    suspend fun fetchSchedule(
        rawUrlOrSlug: String,
        preferences: AppPreferences
    ): Result<DayPrayerSchedule> = withContext(Dispatchers.IO) {
        try {
            val normalizedUrl = normalizeUrl(rawUrlOrSlug)
            Log.d(TAG, "Fetching Mawaqit data from: $normalizedUrl")

            val request = Request.Builder()
                .url(normalizedUrl)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) HayyaAlSalahTV/1.0")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "ar,en-US;q=0.7,en;q=0.3")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                val errorMsg = if (response.code == 404) {
                    "تعذر العثور على المسجد عبر هذا الرابط (خطأ 404). يرجى التأكد من كتابة الرابط أو الرمز بشكل صحيح."
                } else {
                    "حدث خطأ في الاتصال بموقع مواقيت (${response.code}). يرجى التحقق من الاتصال بالإنترنت."
                }
                return@withContext Result.failure(Exception(errorMsg))
            }

            val html = response.body?.string() ?: ""
            if (html.isEmpty()) {
                return@withContext Result.failure(Exception("استجابة فارغة من موقع مواقيت."))
            }

            val confJson = extractConfDataJson(html)
            if (confJson == null) {
                return@withContext Result.failure(Exception("لم يتم العثور على بيانات المسجد (confData). يرجى التأكد من أن الرابط يخص صفحة مسجد على مواقيت."))
            }

            val schedule = parseHtml(html, normalizedUrl, preferences)
            Result.success(schedule)
        } catch (e: Exception) {
            Log.e(TAG, "Error scraping Mawaqit data", e)
            Result.failure(e)
        }
    }

    private fun normalizeUrl(input: String): String {
        val trimmed = input.trim()
        return when {
            trimmed.startsWith("http://") || trimmed.startsWith("https://") -> {
                // Ensure language slug is /ar/ or /en/
                trimmed.replace(Regex("mawaqit\\.net/[a-z]{2}/"), "mawaqit.net/ar/")
            }
            trimmed.contains("mawaqit.net/") -> {
                "https://${trimmed.replace(Regex("mawaqit\\.net/[a-z]{2}/"), "mawaqit.net/ar/")}"
            }
            else -> {
                // It's just a mosque slug like "mosquee-de-paris"
                "https://mawaqit.net/ar/$trimmed"
            }
        }
    }

    private fun parseHtml(
        html: String,
        sourceUrl: String,
        preferences: AppPreferences
    ): DayPrayerSchedule {
        // Extract Coordinates
        var lat = 0.0
        var lon = 0.0
        val latMatch = Regex("latitude\\s*=\\s*([-\\d.]+)").find(html)
        val lonMatch = Regex("longitude\\s*=\\s*([-\\d.]+)").find(html)
        if (latMatch != null && lonMatch != null) {
            lat = latMatch.groupValues[1].toDoubleOrNull() ?: 0.0
            lon = lonMatch.groupValues[1].toDoubleOrNull() ?: 0.0
        }

        // Extract balanced confData JSON
        val confJson = extractConfDataJson(html)
        var mosqueName = "المسجد"
        var timezone = "Africa/Algiers"
        var mawaqitHijriAdj = 0
        var calendarArray: JSONArray? = null
        var iqamaCalendarArray: JSONArray? = null
        var fallbackTimesArray: JSONArray? = null
        var shuruqTime = ""

        if (confJson != null) {
            try {
                val conf = JSONObject(confJson)
                mosqueName = conf.optString("name", "المسجد")
                timezone = conf.optString("timezone", "Africa/Algiers")
                mawaqitHijriAdj = conf.optInt("hijriAdjustment", 0)
                shuruqTime = conf.optString("shuruq", "")
                calendarArray = conf.optJSONArray("calendar")
                iqamaCalendarArray = conf.optJSONArray("iqamaCalendar")
                fallbackTimesArray = conf.optJSONArray("times")
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing confData JSON", e)
            }
        }

        // Determine current date in Mosque's Timezone
        val tz = TimeZone.getTimeZone(timezone)
        val cal = Calendar.getInstance(tz)
        val tzYear = cal.get(Calendar.YEAR)
        val tzMonth = cal.get(Calendar.MONTH) + 1 // 1..12
        val tzDay = cal.get(Calendar.DAY_OF_MONTH)
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val isFriday = (dayOfWeek == Calendar.FRIDAY)

        // Extract today's prayer times from calendar
        var rawFajr = "05:00"
        var rawShuruq = shuruqTime.ifEmpty { "06:30" }
        var rawDhuhr = "12:30"
        var rawAsr = "15:45"
        var rawMaghrib = "18:30"
        var rawIsha = "20:00"

        var foundInCalendar = false
        if (calendarArray != null && calendarArray.length() == 12) {
            val monthObj = calendarArray.optJSONObject(tzMonth - 1)
            if (monthObj != null) {
                val dayTimes = monthObj.optJSONArray(tzDay.toString())
                if (dayTimes != null && dayTimes.length() >= 5) {
                    if (dayTimes.length() >= 6) {
                        // [Fajr, Shuruq, Dhuhr, Asr, Maghrib, Isha]
                        rawFajr = dayTimes.optString(0, rawFajr)
                        rawShuruq = dayTimes.optString(1, rawShuruq)
                        rawDhuhr = dayTimes.optString(2, rawDhuhr)
                        rawAsr = dayTimes.optString(3, rawAsr)
                        rawMaghrib = dayTimes.optString(4, rawMaghrib)
                        rawIsha = dayTimes.optString(5, rawIsha)
                    } else {
                        // 5 items: [Fajr, Dhuhr, Asr, Maghrib, Isha]
                        rawFajr = dayTimes.optString(0, rawFajr)
                        rawDhuhr = dayTimes.optString(1, rawDhuhr)
                        rawAsr = dayTimes.optString(2, rawAsr)
                        rawMaghrib = dayTimes.optString(3, rawMaghrib)
                        rawIsha = dayTimes.optString(4, rawIsha)
                    }
                    foundInCalendar = true
                }
            }
        }

        // Fallback to conf.times if calendar not found
        if (!foundInCalendar && fallbackTimesArray != null) {
            if (fallbackTimesArray.length() >= 6) {
                rawFajr = fallbackTimesArray.optString(0, rawFajr)
                rawShuruq = fallbackTimesArray.optString(1, rawShuruq)
                rawDhuhr = fallbackTimesArray.optString(2, rawDhuhr)
                rawAsr = fallbackTimesArray.optString(3, rawAsr)
                rawMaghrib = fallbackTimesArray.optString(4, rawMaghrib)
                rawIsha = fallbackTimesArray.optString(5, rawIsha)
            } else if (fallbackTimesArray.length() >= 5) {
                rawFajr = fallbackTimesArray.optString(0, rawFajr)
                rawDhuhr = fallbackTimesArray.optString(1, rawDhuhr)
                rawAsr = fallbackTimesArray.optString(2, rawAsr)
                rawMaghrib = fallbackTimesArray.optString(3, rawMaghrib)
                rawIsha = fallbackTimesArray.optString(4, rawIsha)
            }
        }

        // Extract Iqama waits
        var waitFajr = 15
        var waitDhuhr = 10
        var waitAsr = 10
        var waitMaghrib = 5
        var waitIsha = 10

        if (iqamaCalendarArray != null && iqamaCalendarArray.length() == 12) {
            val monthObj = iqamaCalendarArray.optJSONObject(tzMonth - 1)
            if (monthObj != null) {
                val dayWaits = monthObj.optJSONArray(tzDay.toString())
                if (dayWaits != null && dayWaits.length() >= 5) {
                    waitFajr = dayWaits.optString(0, "15").replace("+", "").trim().toIntOrNull() ?: 15
                    waitDhuhr = dayWaits.optString(1, "10").replace("+", "").trim().toIntOrNull() ?: 10
                    waitAsr = dayWaits.optString(2, "10").replace("+", "").trim().toIntOrNull() ?: 10
                    waitMaghrib = dayWaits.optString(3, "5").replace("+", "").trim().toIntOrNull() ?: 5
                    waitIsha = dayWaits.optString(4, "10").replace("+", "").trim().toIntOrNull() ?: 10
                }
            }
        }

        // Extract Imsak Time from DOM or fallback 10m before Fajr
        var imsakTime = ""
        val imsakMatch = Regex("<div class=\"time imsak-id\"><div>(\\d{2}:\\d{2})</div></div>").find(html)
        if (imsakMatch != null) {
            imsakTime = imsakMatch.groupValues[1]
        } else {
            val (fh, fm) = parseTimeToMinutes(rawFajr)
            val imsakMins = (fh * 60 + fm - 10 + 1440) % 1440
            imsakTime = formatMinutesToTime(imsakMins)
        }

        // Build PrayerTimeItems with user offsets
        val prayerEntries = listOf(
            Triple(PrayerType.FAJR, rawFajr, waitFajr),
            Triple(PrayerType.SHURUQ, rawShuruq, 0),
            Triple(PrayerType.DHUHR, rawDhuhr, waitDhuhr),
            Triple(PrayerType.ASR, rawAsr, waitAsr),
            Triple(PrayerType.MAGHRIB, rawMaghrib, waitMaghrib),
            Triple(PrayerType.ISHA, rawIsha, waitIsha)
        )

        val prayerItems = prayerEntries.map { (type, baseTime, wait) ->
            val offset = preferences.getOffset(type)
            val baseTotalMinutes = timeStringToTotalMinutes(baseTime)
            val adjustedTotalMinutes = (baseTotalMinutes + offset + 1440) % 1440
            val adjustedTimeString = formatMinutesToTime(adjustedTotalMinutes)

            PrayerTimeItem(
                type = type,
                baseTimeString = baseTime,
                adjustedTimeString = adjustedTimeString,
                totalMinutes = adjustedTotalMinutes,
                waitMinutes = wait,
                iqamaTotalMinutes = adjustedTotalMinutes + wait
            )
        }

        // Calculate Hijri Date using robust calendar math
        val adjustedCalendar = Calendar.getInstance(tz)
        val totalHijriOffset = mawaqitHijriAdj + preferences.hijriCorrection
        adjustedCalendar.add(Calendar.DAY_OF_YEAR, totalHijriOffset)

        val hijriDate = calculateHijriDate(adjustedCalendar)
        val specialDay = evaluateSpecialDay(hijriDate.month, hijriDate.day, preferences.eidCelebrationDays)

        // Format dates
        val gregFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("ar", "DZ"))
        gregFormat.timeZone = tz
        val gregorianDateStr = gregFormat.format(cal.time)

        val hijriDateStr = "${hijriDate.day} ${getHijriMonthNameAr(hijriDate.month)} ${hijriDate.year} هـ"

        // Extract slug from URL
        val mosqueSlug = sourceUrl.substringAfterLast("/").substringBefore("?")

        // Parse calendarRaw for Astronomy trend charts
        val calendarRawList = mutableListOf<Map<String, List<String>>>()
        if (calendarArray != null) {
            for (m in 0 until calendarArray.length()) {
                val monthObj = calendarArray.optJSONObject(m)
                val monthMap = mutableMapOf<String, List<String>>()
                if (monthObj != null) {
                    val keys = monthObj.keys()
                    while (keys.hasNext()) {
                        val key = keys.next()
                        val arr = monthObj.optJSONArray(key)
                        if (arr != null) {
                            val list = mutableListOf<String>()
                            for (i in 0 until arr.length()) {
                                list.add(arr.optString(i))
                            }
                            monthMap[key] = list
                        }
                    }
                }
                calendarRawList.add(monthMap)
            }
        }

        return DayPrayerSchedule(
            mosqueName = mosqueName,
            mosqueSlug = mosqueSlug,
            timezone = timezone,
            latitude = lat,
            longitude = lon,
            gregorianDate = gregorianDateStr,
            hijriDate = hijriDateStr,
            hijriDay = hijriDate.day,
            hijriMonth = hijriDate.month,
            hijriYear = hijriDate.year,
            prayers = prayerItems,
            imsakTime = imsakTime,
            shuruqTime = rawShuruq,
            specialDay = specialDay,
            calendarRaw = calendarRawList
        )
    }

    /**
     * Extracts balanced curly brace JSON string following `let confData = `
     * Completely immune to newlines, multiline quotes, and special day announcements.
     */
    private fun extractConfDataJson(html: String): String? {
        val marker = "confData"
        val markerIdx = html.indexOf(marker)
        if (markerIdx == -1) return null

        val equalsIdx = html.indexOf("=", markerIdx)
        if (equalsIdx == -1) return null

        val startBrace = html.indexOf("{", equalsIdx)
        if (startBrace == -1) return null

        var depth = 0
        var inString = false
        var escape = false

        for (i in startBrace until html.length) {
            val char = html[i]
            if (escape) {
                escape = false
                continue
            }
            if (char == '\\') {
                escape = true
                continue
            }
            if (char == '"') {
                inString = !inString
                continue
            }
            if (!inString) {
                if (char == '{') depth++
                else if (char == '}') {
                    depth--
                    if (depth == 0) {
                        return html.substring(startBrace, i + 1)
                    }
                }
            }
        }
        return null
    }

    data class HijriDateResult(val year: Int, val month: Int, val day: Int)

    /**
     * Kuwaiti Algorithm for reliable civil Islamic calendar calculations
     */
    private fun calculateHijriDate(calendar: Calendar): HijriDateResult {
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var month = calendar.get(Calendar.MONTH) // 0-based
        var year = calendar.get(Calendar.YEAR)

        var m = month + 1
        var y = year
        if (m < 3) {
            y -= 1
            m += 12
        }

        val a = Math.floor(y / 100.0)
        val b = 2 - a + Math.floor(a / 4.0)
        val jd = Math.floor(365.25 * (y + 4716)) + Math.floor(30.6001 * (m + 1)) + day + b - 1524

        val epochAstro = 1948084
        val l = (jd - epochAstro).toLong() + 10632
        val n = Math.floor((l - 1) / 10631.0).toLong()
        val l1 = l - 10631 * n + 354
        val j = (Math.floor((10985 - l1) / 5316.0) * Math.floor((50 * l1) / 17719.0) +
                Math.floor(l1 / 5670.0) * Math.floor((43 * l1) / 15238.0)).toLong()
        val l2 = l1 - (Math.floor((30 - j) / 15.0) * Math.floor((17719 * j) / 50.0) +
                Math.floor(j / 16.0) * Math.floor((15238 * j) / 43.0)).toLong() + 29
        val mHijri = Math.floor((24 * l2) / 709.0).toInt()
        val dHijri = (l2 - Math.floor((709 * mHijri) / 24.0)).toInt()
        val yHijri = (30 * n + j - 30).toInt()

        return HijriDateResult(
            year = yHijri,
            month = mHijri.coerceIn(1, 12),
            day = dHijri.coerceIn(1, 30)
        )
    }

    private fun evaluateSpecialDay(hMonth: Int, hDay: Int, eidCelebrationDays: Int): SpecialDay {
        return when (hMonth) {
            1 -> {
                when (hDay) {
                    1 -> SpecialDay.IslamicNewYear
                    10 -> SpecialDay.Ashura
                    else -> SpecialDay.None
                }
            }
            3 -> {
                if (hDay == 12) SpecialDay.MawlidAlNabi else SpecialDay.None
            }
            7 -> {
                if (hDay == 27) SpecialDay.IsraWalMiraj else SpecialDay.None
            }
            8 -> {
                if (hDay == 15) SpecialDay.MidShaban else SpecialDay.None
            }
            9 -> {
                // Ramadan
                val ashra = when {
                    hDay <= 10 -> 1
                    hDay <= 20 -> 2
                    else -> 3
                }
                SpecialDay.Ramadan(hDay, ashra)
            }
            10 -> {
                // Shawwal: Eid al-Fitr
                if (hDay in 1..eidCelebrationDays) {
                    SpecialDay.EidAlFitr(hDay)
                } else SpecialDay.None
            }
            12 -> {
                // Dhu al-Hijjah: Arafah on 9th, Eid al-Adha starts 10th
                when {
                    hDay == 9 -> SpecialDay.DayOfArafah
                    hDay in 10 until (10 + eidCelebrationDays) -> {
                        SpecialDay.EidAlAdha(hDay - 9)
                    }
                    else -> SpecialDay.None
                }
            }
            else -> SpecialDay.None
        }
    }

    private fun getHijriMonthNameAr(month: Int): String {
        return when (month) {
            1 -> "محرم"
            2 -> "صفر"
            3 -> "ربيع الأول"
            4 -> "ربيع الآخر"
            5 -> "جمادى الأولى"
            6 -> "جمادى الآخرة"
            7 -> "رجب"
            8 -> "شعبان"
            9 -> "رمضان"
            10 -> "شوال"
            11 -> "ذو القعدة"
            12 -> "ذو الحجة"
            else -> ""
        }
    }

    private fun parseTimeToMinutes(timeStr: String): Pair<Int, Int> {
        val parts = timeStr.split(":").map { it.trim().toIntOrNull() ?: 0 }
        val h = if (parts.isNotEmpty()) parts[0] else 0
        val m = if (parts.size > 1) parts[1] else 0
        return Pair(h, m)
    }

    private fun timeStringToTotalMinutes(timeStr: String): Int {
        val (h, m) = parseTimeToMinutes(timeStr)
        return h * 60 + m
    }

    private fun formatMinutesToTime(totalMinutes: Int): String {
        val normalized = (totalMinutes % 1440 + 1440) % 1440
        val h = normalized / 60
        val m = normalized % 60
        return String.format(Locale.US, "%02d:%02d", h, m)
    }

    companion object {
        private const val TAG = "MawaqitScraper"
    }
}
