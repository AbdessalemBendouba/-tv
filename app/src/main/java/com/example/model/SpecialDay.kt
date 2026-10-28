package com.example.model

sealed class SpecialDay {
    data class EidAlFitr(val day: Int) : SpecialDay() {
        val title: String = "عيد الفطر المبارك 🎉"
        val subtitle: String = "اليوم $day من عيد الفطر - تقبل الله طاعتكم"
    }

    data class EidAlAdha(val day: Int) : SpecialDay() {
        val title: String = "عيد الأضحى المبارك 🐑"
        val subtitle: String = "اليوم $day من عيد الأضحى - عيد مبارك"
    }

    data class Ramadan(val day: Int, val ashra: Int) : SpecialDay() {
        val title: String = "شهر رمضان المبارك 🌙"
        val subtitle: String = when (ashra) {
            1 -> "اليوم $day من رمضان - عشر الرحمة (العشر الأوائل)"
            2 -> "اليوم $day من رمضان - عشر المغفرة (العشر الأواسط)"
            else -> "اليوم $day من رمضان - عشر العتق من النار (العشر الأواخر)"
        }
    }

    data object DayOfArafah : SpecialDay() {
        val title: String = "يوم عرفة المبارك 🕋"
        val subtitle: String = "خير الدعاء دعاء يوم عرفة - صيام يكفر سنتين"
    }

    data object Ashura : SpecialDay() {
        val title: String = "يوم عاشوراء المبارك 🕌"
        val subtitle: String = "صيام عاشوراء يكفر السنة الماضية"
    }

    data object MawlidAlNabi : SpecialDay() {
        val title: String = "المولد النبوي الشريف 🌸"
        val subtitle: String = "ذكرى مولد رسول الله ﷺ، صلوا عليه وسلموا تسليماً"
    }

    data object IslamicNewYear : SpecialDay() {
        val title: String = "رأس السنة الهجرية 🎉"
        val subtitle: String = "كل عام وأنتم بخير بمناسبة حلول العام الهجري الجديد"
    }

    data object IsraWalMiraj : SpecialDay() {
        val title: String = "ذكرى الإسراء والمعراج 🌟"
        val subtitle: String = "سبحان الذي أسرى بعبده ليلاً من المسجد الحرام إلى المسجد الأقصى"
    }

    data object MidShaban : SpecialDay() {
        val title: String = "ليلة النصف من شعبان ✨"
        val subtitle: String = "ليلة مباركة يستحب فيها قيام الليل والدعاء"
    }

    data object None : SpecialDay()

    val isSpecial: Boolean
        get() = this !is None
}
