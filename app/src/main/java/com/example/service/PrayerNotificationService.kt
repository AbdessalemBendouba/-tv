package com.example.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R

class PrayerNotificationService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null
    private var focusRequest: AudioFocusRequest? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        createNotificationChannels()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: ACTION_UPDATE_NOTIFICATION

        when (action) {
            ACTION_UPDATE_NOTIFICATION -> {
                val title = intent?.getStringExtra(EXTRA_TITLE) ?: "حي على الصلاة"
                val text = intent?.getStringExtra(EXTRA_TEXT) ?: "مواقيت الصلاة نشطة"
                startForeground(NOTIFICATION_ID, buildOngoingNotification(title, text))
            }
            ACTION_PLAY_ATHAN -> {
                val prayerName = intent?.getStringExtra(EXTRA_PRAYER_NAME) ?: "الصلاة"
                val isFajr = intent?.getBooleanExtra(EXTRA_IS_FAJR, false) ?: false
                val volume = intent?.getFloatExtra(EXTRA_VOLUME, 0.8f) ?: 0.8f

                startForeground(
                    NOTIFICATION_ID,
                    buildOngoingNotification("حان وقت أذان $prayerName", "الله أكبر الله أكبر")
                )
                playAthanAudio(isFajr, volume)
            }
            ACTION_PLAY_PRE_ATHAN -> {
                val prayerName = intent?.getStringExtra(EXTRA_PRAYER_NAME) ?: "الصلاة"
                val mins = intent?.getIntExtra(EXTRA_MINS, 10) ?: 10
                val volume = intent?.getFloatExtra(EXTRA_VOLUME, 0.8f) ?: 0.8f

                startForeground(
                    NOTIFICATION_ID,
                    buildOngoingNotification("تذكير بقرب الصلاة", "متبقي $mins دقائق على أذان $prayerName")
                )
                playPreAthanSound(volume)
            }
            ACTION_STOP_AUDIO -> {
                stopAudio()
            }
        }

        return START_STICKY
    }

    private fun playAthanAudio(isFajr: Boolean, volume: Float) {
        stopAudio()
        requestTvAudioFocus()

        val soundRes = if (isFajr) R.raw.athan_fajr else R.raw.athan
        try {
            mediaPlayer = MediaPlayer.create(this, soundRes).apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                )
                setVolume(volume, volume)
                setOnCompletionListener {
                    Log.d(TAG, "Athan playback completed naturally")
                    abandonTvAudioFocus()
                }
                start()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error playing Athan audio", e)
            abandonTvAudioFocus()
        }
    }

    private fun playPreAthanSound(volume: Float) {
        stopAudio()
        requestTvAudioFocus()

        // Short takbira / notification cue from athan.mp3
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.athan).apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
                setVolume(volume * 0.9f, volume * 0.9f)
                start()

                // Cut off after 12 seconds for the Takbira alert
                android.os.Handler(mainLooper).postDelayed({
                    try {
                        if (mediaPlayer?.isPlaying == true) {
                            mediaPlayer?.stop()
                            abandonTvAudioFocus()
                        }
                    } catch (e: Exception) {}
                }, 12000)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error playing Pre-Athan alert", e)
            abandonTvAudioFocus()
        }
    }

    private fun stopAudio() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) it.stop()
                it.release()
            }
        } catch (e: Exception) {}
        mediaPlayer = null
        abandonTvAudioFocus()
    }

    private fun requestTvAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )
                .build()
            focusRequest?.let { audioManager?.requestAudioFocus(it) }
        } else {
            @Suppress("DEPRECATION")
            audioManager?.requestAudioFocus(
                null,
                AudioManager.STREAM_ALARM,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
            )
        }
    }

    private fun abandonTvAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest?.let { audioManager?.abandonAudioFocusRequest(it) }
        } else {
            @Suppress("DEPRECATION")
            audioManager?.abandonAudioFocus(null)
        }
    }

    private fun buildOngoingNotification(title: String, text: String): Notification {
        val launchIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_STATUS_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.app_logo)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val statusChannel = NotificationChannel(
                CHANNEL_STATUS_ID,
                "حالة الصلاة المستمرة",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "عرض الصلاة القادمة والوقت المتبقي"
                setShowBadge(false)
            }

            val alertChannel = NotificationChannel(
                CHANNEL_ALERT_ID,
                "تنبيهات الأذان",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "تشغيل صوت الأذان والتذكير عند حلول وقت الصلاة"
                enableVibration(false)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(statusChannel)
            notificationManager?.createNotificationChannel(alertChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAudio()
    }

    companion object {
        const val NOTIFICATION_ID = 1001
        const val CHANNEL_STATUS_ID = "hayya_status_channel"
        const val CHANNEL_ALERT_ID = "hayya_alert_channel"

        const val ACTION_UPDATE_NOTIFICATION = "com.example.ACTION_UPDATE_NOTIFICATION"
        const val ACTION_PLAY_ATHAN = "com.example.ACTION_PLAY_ATHAN"
        const val ACTION_PLAY_PRE_ATHAN = "com.example.ACTION_PLAY_PRE_ATHAN"
        const val ACTION_STOP_AUDIO = "com.example.ACTION_STOP_AUDIO"

        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_TEXT = "extra_text"
        const val EXTRA_PRAYER_NAME = "extra_prayer_name"
        const val EXTRA_IS_FAJR = "extra_is_fajr"
        const val EXTRA_VOLUME = "extra_volume"
        const val EXTRA_MINS = "extra_mins"
        private const val TAG = "PrayerNotificationService"
    }
}
