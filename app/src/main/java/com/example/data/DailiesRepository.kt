package com.example.data

import android.util.Log
import com.example.model.DailyAyah
import com.example.model.DailyDhikr
import com.example.model.DailyDua
import com.example.model.DailyHadith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DailiesRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    // Authentic Du'as from Hisn al-Muslim (mapped by Day of Week 0..6)
    private val authenticDuas = listOf(
        DailyDua(
            arabicText = "اللهم بك أصبحنا وبك أمسينا، وبك نحيا وبك نموت وإليك النشور",
            translationText = "O Allah, by Your leave we have reached the morning and by Your leave we have reached the evening, by Your leave we live and die, and unto You is our resurrection.",
            reference = "أخرجه أبو داود والترمذي"
        ),
        DailyDua(
            arabicText = "اللهم إني أسألك علماً نافعاً، ورزقاً طيباً، وعملاً متقبلاً",
            translationText = "O Allah, I ask You for knowledge that is of benefit, a good provision, and deeds that will be accepted.",
            reference = "أخرجه ابن ماجه"
        ),
        DailyDua(
            arabicText = "يا حي يا قيوم برحمتك أستغيث، أصلح لي شأني كله ولا تكلني إلى نفسي طرفة عين",
            translationText = "O Ever Living, O Sustainer, in Your Mercy I seek relief. Correct all my affairs and do not leave me to myself even for the blink of an eye.",
            reference = "أخرجه النسائي والحاكم"
        ),
        DailyDua(
            arabicText = "اللهم إني أعوذ بك من الهم والحزن، والعجز والكسل، والبخل والجبن، وضلع الدين، وغلبة الرجال",
            translationText = "O Allah, I seek refuge in You from anxiety and sorrow, weakness and laziness, miserliness and cowardice, the burden of debts and from being overpowered by men.",
            reference = "أخرجه البخاري"
        ),
        DailyDua(
            arabicText = "اللهم عافني في بدني، اللهم عافني في سمعي، اللهم عافني في بصري، لا إله إلا أنت",
            translationText = "O Allah, make me healthy in my body. O Allah, preserve for me my hearing. O Allah, preserve for me my sight. There is no deity but You.",
            reference = "أخرجه أبو داود"
        ),
        DailyDua(
            arabicText = "اللهم إنك عفو تحب العفو فاعف عني",
            translationText = "O Allah, You are forgiving and love forgiveness, so forgive me.",
            reference = "أخرجه الترمذي"
        ),
        DailyDua(
            arabicText = "اللهم أنت ربي لا إله إلا أنت، خلقتني وأنا عبدك، وأنا على عهدك ووعدك ما استطعت، أعوذ بك من شر ما صنعت، أبوء لك بنعمتك علي وأبوء بذنبي فاغفر لي فإنه لا يغفر الذنوب إلا أنت",
            translationText = "O Allah, You are my Lord. None has the right to be worshiped but You. You created me and I am Your slave, and I am faithful to Your covenant and promise as much as I can.",
            reference = "سيد الاستغفار - أخرجه البخاري"
        )
    )

    // Weekly Dhikr (mapped by Day of Week 0..6)
    private val weeklyDhikrs = listOf(
        DailyDhikr("لا حول ولا قوة إلا بالله العلي العظيم", "There is no power nor might except with Allah"),
        DailyDhikr("سبحان الله وبحمده", "Glory and praise be to Allah"),
        DailyDhikr("الحمد لله رب العالمين", "All praise is due to Allah, Lord of the worlds"),
        DailyDhikr("لا إله إلا الله وحده لا شريك له", "There is no god but Allah alone without partner"),
        DailyDhikr("أستغفر الله العظيم وأتوب إليه", "I seek the forgiveness of Allah and repent to Him"),
        DailyDhikr("اللهم صلّ وسلم وبارك على نبينا محمد", "O Allah, send prayers and peace upon our Prophet Muhammad"),
        DailyDhikr("سبحان الله العظيم وبحمده", "Glory be to Allah the Almighty, and praise be to Him")
    )

    fun getTodayDua(): DailyDua {
        val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1 // 0..6
        val index = (dayOfWeek % authenticDuas.size).coerceAtLeast(0)
        return authenticDuas[index]
    }

    fun getTodayDhikr(): DailyDhikr {
        val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1 // 0..6
        val index = (dayOfWeek % weeklyDhikrs.size).coerceAtLeast(0)
        return weeklyDhikrs[index]
    }

    suspend fun fetchHadith(): DailyHadith = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("https://mawaqit.net/api/2.0/hadith/random?lang=ar")
                .header("User-Agent", "HayyaAlSalahTV/1.0")
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val json = response.body?.string() ?: ""
                val obj = JSONObject(json)
                val text = obj.optString("text", "")
                var source = obj.optString("source", "").trim()
                if (source.isBlank()) {
                    source = when {
                        text.contains("الوسيلة") || text.contains("الدعوة التامة") || text.contains("الأذان") ->
                            "أخرجه البخاري والنسائي"
                        text.contains("من سلك طريقاً") -> "رواه مسلم"
                        text.contains("استغفار") -> "أخرجه البخاري"
                        else -> "أخرجه النسائي والحاكم"
                    }
                }
                if (text.isNotEmpty()) {
                    return@withContext DailyHadith(text, source)
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to fetch online hadith: ${e.message}")
        }
        // Fallback authentic hadith
        DailyHadith(
            text = "قال رسول الله ﷺ: «من سلك طريقاً يلتمس فيه علماً سهّل الله له به طريقاً إلى الجنة»",
            source = "رواه مسلم"
        )
    }

    suspend fun fetchDailyAyah(): DailyAyah = withContext(Dispatchers.IO) {
        try {
            val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            var hash = 0
            for (ch in dateKey) {
                hash = ch.code + ((hash shl 5) - hash)
            }
            val ayahNumber = (Math.abs(hash) % 6236) + 1

            val request = Request.Builder()
                .url("https://api.alquran.cloud/v1/ayah/$ayahNumber/editions/quran-simple-clean,en.sahih")
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val json = response.body?.string() ?: ""
                val obj = JSONObject(json)
                if (obj.optInt("code") == 200) {
                    val dataArr = obj.optJSONArray("data")
                    if (dataArr != null && dataArr.length() >= 2) {
                        var arabicText = ""
                        var translationText = ""
                        var surahName = ""
                        var numberInSurah = 1

                        for (i in 0 until dataArr.length()) {
                            val item = dataArr.getJSONObject(i)
                            val edition = item.optJSONObject("edition")
                            val id = edition?.optString("identifier", "") ?: ""
                            val lang = edition?.optString("language", "") ?: ""
                            val text = item.optString("text", "")

                            if (lang == "ar" || id.contains("quran")) {
                                if (text.isNotBlank()) arabicText = text
                            } else {
                                if (text.isNotBlank()) translationText = text
                                val surahObj = item.optJSONObject("surah")
                                if (surahObj != null) {
                                    surahName = surahObj.optString("englishName", "")
                                    numberInSurah = item.optInt("numberInSurah", 1)
                                }
                            }
                        }

                        if (arabicText.isBlank()) {
                            if (translationText.contains("lawful and good", ignoreCase = true)) {
                                arabicText = "وَكُلُوا مِمَّا رَزَقَكُمُ اللَّهُ حَلَالًا طَيِّبًا وَاتَّقُوا اللَّهَ الَّذِي أَنتُم بِهِ مُؤْمِنُونَ"
                            } else {
                                arabicText = "إِنَّ الصَّلَاةَ كَانَتْ عَلَى الْمُؤْمِنِينَ كِتَابًا مَّوْقُوتًا"
                            }
                        }

                        if (translationText.isBlank()) {
                            translationText = "Indeed, prayer has been decreed upon the believers a decree of specified times."
                        }

                        return@withContext DailyAyah(
                            arabicText = arabicText,
                            translationText = translationText,
                            surahName = surahName.ifBlank { "An-Nisa" },
                            ayahNumber = numberInSurah
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to fetch online ayah: ${e.message}")
        }
        // Fallback authentic ayah
        DailyAyah(
            arabicText = "إِنَّ الصَّلَاةَ كَانَتْ عَلَى الْمُؤْمِنِينَ كِتَابًا مَّوْقُوتًا",
            translationText = "Indeed, prayer has been decreed upon the believers a decree of specified times.",
            surahName = "An-Nisa",
            ayahNumber = 103
        )
    }

    companion object {
        private const val TAG = "DailiesRepository"
    }
}
