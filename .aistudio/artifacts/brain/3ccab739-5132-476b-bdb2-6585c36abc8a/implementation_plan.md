# Hayya Al-Salah: Android TV Prayer Times & Background Alert System

Comprehensive architectural blueprint for the Android TV application. Featuring a 2:1 split-screen digital signage dashboard, background audio Athan & Pre-Athan Takbira playback while running other TV apps (Netflix, YouTube), persistent ongoing notification, resilient Mawaqit data scraper, typed `SpecialDay` sealed hierarchy, and full Astronomy, Dailies, and Settings tabs (with Qada' & historical tracking removed and Arabic vs. Eastern Arabic-Indic numerals toggle).

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The following revisions have been incorporated based on your latest guidance:

- **Confirmed Decision 1 (Background Audio & App-agnostic Alerts)**:
  - When watching Netflix, YouTube, or browsing the TV home screen, the app will play the **Athan** right on time, and play a **Pre-Athan Takbira reminder** $X$ minutes before prayer.
  - Implemented via `AlarmManager.setExactAndAllowWhileIdle()` coupled with a dedicated `PrayerNotificationService` / `AthanPlaybackService`.
  - An **ongoing notification** displays the current/next prayer and live remaining countdown.
- **Confirmed Decision 2 (Simplified Tab Suite without Progress/Qada Tracking)**:
  - **Main Dashboard**: 2:1 split-screen with large clock, next/last prayer, countdown, sun-in-sky celestial arc, dual Hijri/Gregorian dates, `SpecialDay` badge, and daily prayer list.
  - **Astronomy Tab**: Lunar phase display, spherical Qibla compass bearing to the Kaaba, 7-day, 30-day, and 12-month AccuWeather-style prayer drift charts, and celestial path.
  - **Dailies Tab**: Daily Quran Ayah (Arabic & English), authentic Hadith from Mawaqit API, authentic Hisn al-Muslim Du'a, and interactive Tasbih Dhikr counter.
  - **Settings Tab**: Mosque URL/code setup, numeral system selector (Arabic `1, 2, 3` vs Eastern Arabic-Indic `١, ٢, ٣`), Athan volume & test playback, Pre-Athan reminder minutes, Imsak visibility, Eid days, Hijri correction days, and prayer minute offsets.
  - *Excluded*: Qada' debt ledger, backlog counters, and historical checklist stats.
- **Confirmed Decision 3 (Special Days Sealed Hierarchy & Bug Fixes)**:
  - Resolves legacy `dayOfAdha` undefined variable crash, 6-vs-5 array fallback validation bug, multiline regex breaking on holiday announcements, and locale numeral mismatch.

---

## 1. Overview & Core Concept

- **What It Does**: A comprehensive prayer companion designed specifically for any Android TV device (Chromecast with Google TV, Xiaomi Mi Box, Android TV smart televisions). Operates both as a 10-foot ambient visual dashboard and as a background prayer alert service that plays the Athan and pre-athan reminders even while users are engaged in other TV media apps.
- **Target Audience / Persona**: Individuals and families using Android TV who desire punctual prayer notifications, accurate mosque-synchronized prayer times, astronomical indicators, and daily Islamic inspiration without interrupting their daily TV utility.
- **Key Value**: Never miss a prayer during movie or video streaming; hands-off remote control navigation; robust offline and special-day handling.

---

## 2. User Experience & Visual Design

### 1. Main Prayer Dashboard (2:1 TV Split)
- **Wide Section (2/3)**:
  - Large digital clock with smooth typography.
  - Next prayer card featuring prayer name, time, and live ticking countdown (`hh:mm:ss`).
  - Last prayer and Iqama wait indicator.
  - "Sun in the Sky" celestial canvas arc: displays the sun's trajectory across the horizon between Sunrise and Sunset (and the moon between Maghrib and Fajr).
  - Hamburger menu icon revealing the side navigation rail.
- **Narrow Section (1/3)**:
  - Top header: Today's date in Gregorian and calibrated Hijri formats.
  - **`SpecialDay` Badge**: Invisible during ordinary days; illuminates automatically during special occasions (e.g., "Day 1 of Eid al-Fitr 🎉", "Day 10 of Eid al-Adha 🐑", "Ramadan - Mercy Ashra", "Day of Ashura", "Day of Arafah").
  - Vertical list of today's prayer times (Fajr, Shuruq, Dhuhr, Asr, Maghrib, Isha, and optional Imsak) showing Iqama wait times and arrival statuses.

### 2. Navigation Drawer (Side Rail)
Hidden by default, opened via D-Pad Left or selecting the hamburger button:
- **Main (Prayer Board)**: The 2:1 primary digital dashboard.
- **Astronomy**: Hijri moon phase artwork, trigonometric Qibla compass dial pointing directly to Mecca, and 7-day/30-day/yearly prayer drift line charts.
- **Dailies**: Daily Quranic Ayah with translation, authentic Hadith, Hisn al-Muslim Du'a, and interactive Tasbih counter.
- **Settings**: Complete configuration suite.
- **Exit**: Cleanly close the visual activity while leaving background alarm monitors active.

### 3. Remote Control & 10-Foot UI Design
- Full D-Pad navigation (Up, Down, Left, Right, Center/Select, Back).
- Clear, luminous focus indicators (amber/emerald glowing borders) for active elements.
- Scaled typography legible from across the room (couch distance).
- Numeral formatting switch: Standard Arabic numerals (`1, 2, 3`) or Eastern Arabic-Indic numerals (`١, ٢, ٣`), allowing North African and Western users to read numbers in their customary system alongside Arabic labels.

---

## 3. Background Alert & Audio Engine

### Background Athan Playback (While in Netflix, YouTube, etc.)
- **Alarm Scheduling**:
  - Uses `AlarmManager.setExactAndAllowWhileIdle()` to schedule alarms for each upcoming prayer time and pre-athan trigger.
  - Reschedules automatically on boot (`BOOT_COMPLETED`), date rollover, or settings update.
- **Audio Playback**:
  - `AthanAudioService` manages `MediaPlayer` to stream local audio assets (`athan.mp3` for standard prayers, `athan_fajr.mp3` for Fajr).
  - Requests audio focus with ducking/transient focus so TV media politely lowers volume or pauses while the Athan plays.
  - Includes a pre-athan reminder chime/takbira that alerts the viewer $X$ minutes prior to the Athan.
- **Ongoing Notification**:
  - Displays a persistent, low-priority ongoing notification in the Android system showing:
    - `"Next: Asr in 24m | Mosque: Grand Mosque Paris"`
  - Updated regularly as time progresses.

---

## 4. Technical Architecture & Data Strategy

### System Diagram

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Android TV Architecture                         │
│                                                                        │
│   ┌────────────────────────────────┐   ┌────────────────────────────┐  │
│   │   System Boot & Time Alarms    │   │      MawaqitScraper        │  │
│   │   - AlarmManager Exact Alarms  │   │   - HTML Fetch (OkHttp)    │  │
│   │   - Athan & Pre-Athan Triggers │   │   - Balanced JSON Parser   │  │
│   └───────────────┬────────────────┘   └─────────────┬──────────────┘  │
│                   │                                  │                 │
│                   ▼                                  ▼                 │
│   ┌────────────────────────────────┐   ┌────────────────────────────┐  │
│   │     AthanAudioService          │   │      PrayerRepository      │  │
│   │   - Background MediaPlayer     │   │   - DataStore Preferences  │  │
│   │   - Persistent Notification    │◄──┤   - Mosque Times & Calendar│  │
│   │   - Pre-Athan Takbira Alert    │   │   - Hijri Adjustment Data  │  │
│   └────────────────────────────────┘   └─────────────┬──────────────┘  │
│                                                      │                 │
│                                                      ▼                 │
│   ┌─────────────────────────────────────────────────────────────────┐  │
│   │                     PrayerTvViewModel                           │  │
│   │   - StateFlow<PrayerUiState>                                    │  │
│   │   - SpecialDayEvaluator (Eid, Ramadan, Ashura sealed class)     │  │
│   │   - Live 1-Second Ticking Clock & Countdown                     │  │
│   │   - Sun & Moon Celestial Angle Calculations                    │  │
│   │   - Numeral System Formatter (Arabic vs Eastern Arabic-Indic)   │  │
│   └──────────────────────────────────┬──────────────────────────────┘  │
│                                      │                                 │
│                                      ▼                                 │
│   ┌─────────────────────────────────────────────────────────────────┐  │
│   │                     Compose TV Interface                        │  │
│   │                                                                 │  │
│   │   ┌─────────────────────────┐     ┌─────────────────────────┐   │  │
│   │   │  Wide Area (2/3)        │     │  Narrow Column (1/3)    │   │  │
│   │   │  - Ambient Clock        │     │  - Dual Hijri/Gregorian │   │  │
│   │   │  - Next Prayer & Timer  │     │  - SpecialDay Badge     │   │  │
│   │   │  - Celestial Sun Arc    │     │  - Daily Prayer Cards   │   │  │
│   │   │  - Menu Trigger Button  │     │  - Iqama Waits          │   │  │
│   │   └─────────────────────────┘     └─────────────────────────┘   │  │
│   │                                                                 │  │
│   │   ┌─────────────────────────────────────────────────────────┐   │  │
│   │   │  Navigation Drawer: Main | Astronomy | Dailies | Settings│  │
│   │   └─────────────────────────────────────────────────────────┘   │  │
│   └─────────────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────────────┘
```

### Special Day Sealed Hierarchy
```kotlin
sealed class SpecialDay {
    data class EidAlFitr(val day: Int) : SpecialDay()
    data class EidAlAdha(val day: Int) : SpecialDay()
    data class Ramadan(val day: Int, val ashra: Int) : SpecialDay()
    data object DayOfArafah : SpecialDay()
    data object Ashura : SpecialDay()
    data object MawlidAlNabi : SpecialDay()
    data object IslamicNewYear : SpecialDay()
    data object IsraWalMiraj : SpecialDay()
    data object MidShaban : SpecialDay()
    data object None : SpecialDay()
}
```

### Settings & Preference Properties
- `mosqueUrl`: Mawaqit mosque link or slug.
- `numeralSystem`: `ARABIC` (`1, 2, 3`) or `EASTERN_ARABIC_INDIC` (`١, ٢, ٣`).
- `athanVolume`: 0.0 to 1.0 (with instant test button).
- `preAthanEnabled`: boolean toggle.
- `preAthanMinutes`: integer (1 to 59 minutes).
- `imsakVisibility`: `ALWAYS` or `RAMADAN_ONLY`.
- `eidCelebrationDays`: 1 to 10 days.
- `hijriCorrection`: -5 to +5 days.
- `prayerOffsets`: Individual offsets (-15 to +15 minutes) for Fajr, Dhuhr, Asr, Maghrib, Isha.
