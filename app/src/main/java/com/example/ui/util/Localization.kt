package com.example.ui.util

import com.example.model.AppLanguage
import com.example.model.PrayerType

object AppStrings {

    fun appName(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "حي على الصلاة"
        AppLanguage.ENGLISH -> "Come to Prayer"
    }

    fun appSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "مواقيت الصلاة للمساجد"
        AppLanguage.ENGLISH -> "Accurate Mosque Prayer Times"
    }

    fun tabMain(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "لوحة الصلاة"
        AppLanguage.ENGLISH -> "Prayer Board"
    }

    fun tabAstronomy(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "علم الفلك والقبلة"
        AppLanguage.ENGLISH -> "Astronomy & Qibla"
    }

    fun tabDailies(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "اليوميات والأذكار"
        AppLanguage.ENGLISH -> "Dailies & Dhikr"
    }

    fun tabSettings(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الإعدادات العامة"
        AppLanguage.ENGLISH -> "General Settings"
    }

    fun menu(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "القائمة"
        AppLanguage.ENGLISH -> "Menu"
    }

    fun home(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الرئيسية"
        AppLanguage.ENGLISH -> "Main"
    }

    fun exitApp(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "خروج من التطبيق"
        AppLanguage.ENGLISH -> "Exit App"
    }

    fun nextPrayer(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الصلاة القادمة"
        AppLanguage.ENGLISH -> "Next Prayer"
    }

    fun previousPrayer(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الصلاة السابقة"
        AppLanguage.ENGLISH -> "Previous Prayer"
    }

    fun timeToAthan(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "المتبقي للأذان"
        AppLanguage.ENGLISH -> "Time to Athan"
    }

    fun iqamaAfter(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الإقامة بعد"
        AppLanguage.ENGLISH -> "Iqama in"
    }

    fun sunCelestialDay(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "المسار الفلكي للشمس"
        AppLanguage.ENGLISH -> "Sun Celestial Arc"
    }

    fun sunCelestialNight(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "المسار الفلكي الليلي"
        AppLanguage.ENGLISH -> "Night Celestial Path"
    }

    fun prayerName(type: PrayerType, lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> type.arabicName
        AppLanguage.ENGLISH -> type.englishName
    }

    fun minutesSuffix(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "د"
        AppLanguage.ENGLISH -> "m"
    }

    fun dailyAyahHeader(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "آية اليوم من القرآن الكريم"
        AppLanguage.ENGLISH -> "Daily Qur'an Verse"
    }

    fun dailyDuaHeader(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "دعاء اليوم (حصن المسلم)"
        AppLanguage.ENGLISH -> "Daily Du'a (Hisn al-Muslim)"
    }

    fun dailyHadithHeader(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الحديث النبوي الشريف"
        AppLanguage.ENGLISH -> "Daily Hadith"
    }

    fun dailyDhikrHeader(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تسبيح اليوم"
        AppLanguage.ENGLISH -> "Daily Dhikr"
    }

    fun tapToCount(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "اضغط للتسبيح"
        AppLanguage.ENGLISH -> "Press OK to Count"
    }

    fun resetCounter(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تصفير العداد"
        AppLanguage.ENGLISH -> "Reset Counter"
    }

    fun languageSetting(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "لغة التطبيق (App Language):"
        AppLanguage.ENGLISH -> "App Language:"
    }

    fun languageCardTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "لغة التطبيق • Application Language 🌐"
        AppLanguage.ENGLISH -> "Application Language • لغة التطبيق 🌐"
    }

    fun languageCardSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "اختر لغة الواجهة المفضلة لعرض كافة الأقسام والإعدادات"
        AppLanguage.ENGLISH -> "Choose preferred interface language for all screens and settings"
    }

    fun arabicOptionLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "العربية (Arabic)"
        AppLanguage.ENGLISH -> "العربية (Arabic)"
    }

    fun englishOptionLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "English (الإنجليزية)"
        AppLanguage.ENGLISH -> "English (الإنجليزية)"
    }

    fun mosqueUrlTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "رابط أو رمز المسجد النشط (Mawaqit Mosque URL):"
        AppLanguage.ENGLISH -> "Active Mosque URL or Slug (Mawaqit):"
    }

    fun mosqueUrlSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "يمكنك إدخال الرابط الكامل أو الاسم المختصر (Slug) للمسجد النشط حالياً."
        AppLanguage.ENGLISH -> "Enter the full URL or short name (slug) for the currently active mosque."
    }

    fun savedMosquesTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "المساجد المحفوظة والتبديل السريع 🕌"
        AppLanguage.ENGLISH -> "Saved Mosques & Quick Switch 🕌"
    }

    fun savedMosquesSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "يمكنك حفظ عدة مساجد والتبديل الفوري بينها في أي وقت."
        AppLanguage.ENGLISH -> "Save multiple mosques and switch instantly anytime."
    }

    fun activeMosqueLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "النشط حالياً"
        AppLanguage.ENGLISH -> "Active"
    }

    fun switchToMosque(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تبديل إليه"
        AppLanguage.ENGLISH -> "Switch to"
    }

    fun addNewMosque(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "إضافة مسجد جديد إلى القائمة:"
        AppLanguage.ENGLISH -> "Add new mosque to list:"
    }

    fun addAndSave(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "إضافة وحفظ"
        AppLanguage.ENGLISH -> "Add & Save"
    }

    fun appearanceTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "المظهر ونظام الأرقام:"
        AppLanguage.ENGLISH -> "Appearance & Numeral System:"
    }

    fun darkModeLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الوضع الليلي (Dark Mode)"
        AppLanguage.ENGLISH -> "Dark Mode"
    }

    fun lightModeLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الوضع الفاتح (Light Mode)"
        AppLanguage.ENGLISH -> "Light Mode"
    }

    fun numeralSystemLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تنسيق عرض الأرقام (Numeral System):"
        AppLanguage.ENGLISH -> "Numeral System Format:"
    }

    fun athanAlertsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "إعدادات الأذان والتنبيهات:"
        AppLanguage.ENGLISH -> "Athan & Alert Settings:"
    }

    fun enableAthanLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تشغيل صوت الأذان في وقت الصلاة"
        AppLanguage.ENGLISH -> "Play Athan sound at prayer time"
    }

    fun fajrAthanLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "أذان الفجر (يشمل الصلاة خير من النوم)"
        AppLanguage.ENGLISH -> "Fajr Athan (with 'Prayer is better than sleep')"
    }

    fun volumeLabel(percent: Int, lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "%مستوى الصوت: $percent"
        AppLanguage.ENGLISH -> "Volume: $percent%"
    }

    fun preAthanTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "التذكير قبل الأذان (تكبيرة وتنبيه خلفي):"
        AppLanguage.ENGLISH -> "Pre-Athan Reminder (Takbeer & Background Alert):"
    }

    fun preAthanSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "يعمل فوق أي تطبيق آخر (مثل YouTube أو Netflix) لتنبيهك قبل حلول الصلاة."
        AppLanguage.ENGLISH -> "Works over any other app (like YouTube or Netflix) to alert you before prayer."
    }

    fun preAthanMinutesLabel(minutes: Int, lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "وقت التذكير: $minutes دقيقة قبل الأذان"
        AppLanguage.ENGLISH -> "Reminder time: $minutes minutes before Athan"
    }

    fun prayerOffsetsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تصحيح أوقات الصلاة الفردية (بالدقائق):"
        AppLanguage.ENGLISH -> "Individual Prayer Time Offsets (Minutes):"
    }

    fun prayerOffsetsSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "يمكنك إضافة أو خصم دقائق لكل صلاة على حدة"
        AppLanguage.ENGLISH -> "You can add or subtract minutes for each prayer individually"
    }

    fun eventsCalendarTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "المناسبات والتقويم الهجري:"
        AppLanguage.ENGLISH -> "Events & Hijri Calendar:"
    }

    fun specialDayBadgeLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "إظهار شارة المناسبات الخاصة (الأعياد ورمضان):"
        AppLanguage.ENGLISH -> "Show Special Events & Ramadan Badge:"
    }

    fun specialDayBadgeSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "عرض وسام المناسبة كعيد الفطر، الأضحى، وليالي العشر"
        AppLanguage.ENGLISH -> "Display badge for Eid al-Fitr, Eid al-Adha, Ramadan, etc."
    }

    fun imsakLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "إظهار الإمساك:"
        AppLanguage.ENGLISH -> "Show Imsak:"
    }

    fun hijriCorrectionLabel(days: Int, lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تصحيح التاريخ الهجري: $days يوم"
        AppLanguage.ENGLISH -> "Hijri Date Correction: $days days"
    }

    fun restoreDefaults(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "استعادة الإعدادات الافتراضية"
        AppLanguage.ENGLISH -> "Restore Defaults"
    }

    fun saveChanges(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "حفظ وتطبيق التغييرات"
        AppLanguage.ENGLISH -> "Save & Apply Changes"
    }

    fun testAthan(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تجربة صوت الأذان"
        AppLanguage.ENGLISH -> "Test Athan"
    }

    fun stopAudio(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "إيقاف الصوت"
        AppLanguage.ENGLISH -> "Stop Audio"
    }
}
