package com.example.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class AthanAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        Log.d(TAG, "AthanAlarmReceiver received action: $action")

        when (action) {
            ACTION_TRIGGER_ATHAN -> {
                val prayerName = intent.getStringExtra(EXTRA_PRAYER_NAME) ?: "الصلاة"
                val isFajr = intent.getBooleanExtra(EXTRA_IS_FAJR, false)
                val volume = intent.getFloatExtra(EXTRA_VOLUME, 0.8f)

                val serviceIntent = Intent(context, PrayerNotificationService::class.java).apply {
                    this.action = PrayerNotificationService.ACTION_PLAY_ATHAN
                    putExtra(PrayerNotificationService.EXTRA_PRAYER_NAME, prayerName)
                    putExtra(PrayerNotificationService.EXTRA_IS_FAJR, isFajr)
                    putExtra(PrayerNotificationService.EXTRA_VOLUME, volume)
                }
                startServiceSafely(context, serviceIntent)
            }
            ACTION_TRIGGER_PRE_ATHAN -> {
                val prayerName = intent.getStringExtra(EXTRA_PRAYER_NAME) ?: "الصلاة"
                val mins = intent.getIntExtra(EXTRA_MINS, 10)
                val volume = intent.getFloatExtra(EXTRA_VOLUME, 0.8f)

                val serviceIntent = Intent(context, PrayerNotificationService::class.java).apply {
                    this.action = PrayerNotificationService.ACTION_PLAY_PRE_ATHAN
                    putExtra(PrayerNotificationService.EXTRA_PRAYER_NAME, prayerName)
                    putExtra(PrayerNotificationService.EXTRA_MINS, mins)
                    putExtra(PrayerNotificationService.EXTRA_VOLUME, volume)
                }
                startServiceSafely(context, serviceIntent)
            }
            Intent.ACTION_BOOT_COMPLETED,
            ACTION_RESCHEDULE -> {
                AlarmScheduler.rescheduleToday(context)
            }
        }
    }

    private fun startServiceSafely(context: Context, intent: Intent) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start PrayerNotificationService from receiver", e)
        }
    }

    companion object {
        const val ACTION_TRIGGER_ATHAN = "com.example.ACTION_TRIGGER_ATHAN"
        const val ACTION_TRIGGER_PRE_ATHAN = "com.example.ACTION_TRIGGER_PRE_ATHAN"
        const val ACTION_RESCHEDULE = "com.example.ACTION_RESCHEDULE"

        const val EXTRA_PRAYER_NAME = "extra_prayer_name"
        const val EXTRA_IS_FAJR = "extra_is_fajr"
        const val EXTRA_VOLUME = "extra_volume"
        const val EXTRA_MINS = "extra_mins"

        private const val TAG = "AthanAlarmReceiver"
    }
}
