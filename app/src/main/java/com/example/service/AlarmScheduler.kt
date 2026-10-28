package com.example.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.data.MawaqitScraper
import com.example.data.PreferencesManager
import com.example.model.DayPrayerSchedule
import com.example.model.PrayerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

object AlarmScheduler {

    private const val TAG = "AlarmScheduler"

    fun schedulePrayerAlarms(
        context: Context,
        schedule: DayPrayerSchedule,
        volume: Float,
        athanEnabled: Boolean,
        fajrAthanEnabled: Boolean,
        preAthanEnabled: Boolean,
        preAthanMins: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val tz = TimeZone.getTimeZone(schedule.timezone)
        val now = System.currentTimeMillis()

        // Cancel previous alarms
        cancelExistingAlarms(context, alarmManager)

        if (!athanEnabled && !preAthanEnabled) {
            Log.d(TAG, "Audio notifications are disabled in preferences")
            return
        }

        val dailyPrayers = schedule.prayers.filter {
            it.type in listOf(PrayerType.FAJR, PrayerType.DHUHR, PrayerType.ASR, PrayerType.MAGHRIB, PrayerType.ISHA)
        }

        dailyPrayers.forEachIndexed { index, item ->
            val isFajr = (item.type == PrayerType.FAJR)
            val shouldPlayThisAthan = if (isFajr) (athanEnabled && fajrAthanEnabled) else athanEnabled

            val targetCal = Calendar.getInstance(tz).apply {
                timeInMillis = now
                set(Calendar.HOUR_OF_DAY, item.totalMinutes / 60)
                set(Calendar.MINUTE, item.totalMinutes % 60)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            var prayerTimeMillis = targetCal.timeInMillis
            // If already passed today, schedule for tomorrow
            if (prayerTimeMillis <= now) {
                targetCal.add(Calendar.DAY_OF_YEAR, 1)
                prayerTimeMillis = targetCal.timeInMillis
            }

            // 1. Schedule Athan Alarm
            if (shouldPlayThisAthan) {
                val intent = Intent(context, AthanAlarmReceiver::class.java).apply {
                    action = AthanAlarmReceiver.ACTION_TRIGGER_ATHAN
                    putExtra(AthanAlarmReceiver.EXTRA_PRAYER_NAME, item.type.arabicName)
                    putExtra(AthanAlarmReceiver.EXTRA_IS_FAJR, isFajr)
                    putExtra(AthanAlarmReceiver.EXTRA_VOLUME, volume)
                }

                val requestCode = 2000 + index
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                scheduleExactAlarm(alarmManager, prayerTimeMillis, pendingIntent)
                Log.d(TAG, "Scheduled Athan for ${item.type.englishName} at $prayerTimeMillis (in ${(prayerTimeMillis - now)/60000} mins)")
            }

            // 2. Schedule Pre-Athan Reminder Alarm
            if (preAthanEnabled && preAthanMins > 0) {
                val preAthanMillis = prayerTimeMillis - (preAthanMins * 60 * 1000L)
                if (preAthanMillis > now) {
                    val preIntent = Intent(context, AthanAlarmReceiver::class.java).apply {
                        action = AthanAlarmReceiver.ACTION_TRIGGER_PRE_ATHAN
                        putExtra(AthanAlarmReceiver.EXTRA_PRAYER_NAME, item.type.arabicName)
                        putExtra(AthanAlarmReceiver.EXTRA_MINS, preAthanMins)
                        putExtra(AthanAlarmReceiver.EXTRA_VOLUME, volume)
                    }

                    val preRequestCode = 3000 + index
                    val prePendingIntent = PendingIntent.getBroadcast(
                        context,
                        preRequestCode,
                        preIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    scheduleExactAlarm(alarmManager, preAthanMillis, prePendingIntent)
                    Log.d(TAG, "Scheduled Pre-Athan for ${item.type.englishName} ($preAthanMins min prior)")
                }
            }
        }
    }

    private fun scheduleExactAlarm(
        alarmManager: AlarmManager,
        triggerMillis: Long,
        pendingIntent: PendingIntent
    ) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            Log.w(TAG, "Exact alarm permission missing, falling back to set(): ${e.message}")
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
        }
    }

    private fun cancelExistingAlarms(context: Context, alarmManager: AlarmManager) {
        for (i in 0..5) {
            // Cancel Athan
            val intent = Intent(context, AthanAlarmReceiver::class.java)
            val pi = PendingIntent.getBroadcast(
                context,
                2000 + i,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            pi?.let { alarmManager.cancel(it) }

            // Cancel Pre-Athan
            val prePi = PendingIntent.getBroadcast(
                context,
                3000 + i,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            prePi?.let { alarmManager.cancel(it) }
        }
    }

    fun rescheduleToday(context: Context) {
        val prefsManager = PreferencesManager(context)
        val prefs = prefsManager.getPreferences()
        if (prefs.mosqueUrl.isBlank()) return

        CoroutineScope(Dispatchers.IO).launch {
            val scraper = MawaqitScraper()
            val result = scraper.fetchSchedule(prefs.mosqueUrl, prefs)
            result.onSuccess { schedule ->
                schedulePrayerAlarms(
                    context = context,
                    schedule = schedule,
                    volume = prefs.athanVolume,
                    athanEnabled = prefs.athanEnabled,
                    fajrAthanEnabled = prefs.fajrAthanEnabled,
                    preAthanEnabled = prefs.preAthanEnabled,
                    preAthanMins = prefs.preAthanMinutes
                )
            }
        }
    }
}
