
// Global Constants
const PRAYERS = ["Fajr", "Dhuhr", "Asr", "Maghrib", "Isha"];

// Helper to calculate past date ISO strings securely across global contexts
function getPastDateStr(offset) {
  const d = new Date();
  d.setDate(d.getDate() - offset);
  return d.toISOString().split('T')[0];
}

const I18N = {
  en: {
    home: "Home", stats: "Stats", settings: "Settings", title: "Come to Prayer",
    urlLabel: "Mawaqit Mosque URL:", langLabel: "Language:", themeLabel: "Theme:",
    saveBtn: "Save & Fetch", fetching: "Fetching data...", success: "Saved successfully!", error: "Fetch error. Invalid URL.",
    fajr: "Fajr", dhuhr: "Dhuhr", asr: "Asr", maghrib: "Maghrib", isha: "Isha",
    jumuah: "Jumu'ah", imsak: "Imsak", iftar: " (Iftar)",
    clearConfirm: "Are you sure you want to delete all historical stats? This cannot be undone.",
    clearDone: "Historical statistics cleared!",
    trackedLabel: "Prayers Tracked",
    rollingWeek: "Rolling 7-Day Window", rollingMonth: "Rolling 30-Day Window", yearlyChart: "Monthly Averages (Yearly)",
    ashra1: "Mercy Ashra (First 10 Days)", ashra2: "Forgiveness Ashra (Second 10 Days)", ashra3: "Hellfire Salvation (Third 10 Days)",
    eidDaysLabel: "Eid Celebration Days:",
    eidFitr: "Day {day} of Eid al-Fitr 🎉",
    eidAdha: "Day {day} of Eid al-Adha 🐑",
    imsakConfigLabel: "Imsak Visibility:",
    imsakAlways: "Always Show Imsak",
    imsakRamadan: "Show Only in Ramadan",
    slidingChartsTitle: "Prayer Analysis Dashboards",
    weeklyAvgTitle: "Progress Gauges",
    clearStatsBtn: "Delete All Stats",
    lblRingDay: "Today", lblRingWeek: "Week", lblRingMonth: "Month", lblRingYear: "Year",
    trendTitle: "Prayer Trends",
    dir: "ltr",
    confirm: "Confirm",
    cancel: "Cancel",
    ramadan: "RAMADAN",
    fasting: "FASTING",
    rollingWeekTrend: "Weekly Drift Map (Today + 6 Days)",
    rollingMonthTrend: "Monthly Drift Map (Today + 29 Days)",
    yearlyTrend: "Yearly Drift Map (12 Months Overview)",
    defaultSettingsConfirm: "Are you sure you want to restore default settings?",
    defaultSettingsDone: "Default settings restored successfully!",
    preAthanLabel: "Enable Pre-Athan Reminder",
    preAthanTimeLabel: "Reminder Time (Minutes before):",
    defaultSettingsBtn: "Restore Default Settings",
    astro: "Astronomy",
    moonTitle: "Hijri Moon Phase",
    qiblaTitle: "Qibla Compass",
    kaabaDir: "Kaaba Direction",
    qiblaAngle: "Qibla: {angle}° from North",
    nextPrayerCountdown: "Next Prayer: {name}",
    iqamaActiveCountdown: "Time to Pray: {name}",
    volumeLabel: "Athan Volume:",
    hijriCorrectorLabel: "Hijri Date Corrector (Days):",
    eventNewYear: "Islamic New Year 🎉",
    eventAshura: "Day of Ashura 🕌",
    eventMawlid: "Mawlid al-Nabi 🌸",
    eventIsra: "Isra' and Mi'raj 🌟",
    eventShaban: "Mid-Sha'ban Night ✨",
    eventArafah: "Day of Arafah 🕋",
    streakHubTitle: "Holy Streak ⚡",
    currStreakLabel: "Current Streak: {days} Days",
    longStreakLabel: "Longest Streak: {days} Days",
    
    // Qada & Offsets translations
    qadaTab: "Qada",
    qadaTitle: "Qada Tracker (Make-up Obligatory Prayers)",
    offsetsTitle: "Prayer Time Offsets (Minutes):",
    dhikrTitle: "Daily Dhikr",
    quranTitle: "Daily Qur'an Verse",
    exportBtn: "Export Tracking Data (CSV)",
    sunSkyTitle: "Sun Position in the Sky",
    dailies: "Dailies",
    duaTitle: "Daily Du'a",
	hadithTitle: "Daily Hadith",


    // Revamped Qada Translations
    ledgerTitle: "Daftar Elqada' (Makeup Ledger)",
    backlogTitle: "Manual Qada' Backlog",
    qadaCommitTitle: "Daily Qada' Makeup Commitment:",
    qadaAssistantEmpty: "All your obligatory prayers are fully up-to-date! No pending Qada' debts. Al-Hamdulillah! 🎉",
    qadaReminderTitle: "Outstanding Qada' Debts",
    qadaLine: "• {name}: <span style='color:var(--primary);'>{debt} outstanding</span> (praying {commit}/day) -> Est. completion: <span style='color:var(--success);'>{date}</span>",
    qadaNoCommit: "• {name}: <span style='color:var(--primary);'>{debt} outstanding</span> (Configure a daily makeup commitment in settings to see estimated completion dates)"
  },
  ar: {
    home: "الرئيسية", stats: "الإحصائيات", settings: "الإعدادات", title: "حي على الصلاة",
    urlLabel: "رابط المسجد (مواقيت):", langLabel: "اللغة:", themeLabel: "المظهر:",
    saveBtn: "حفظ وتحديث", fetching: "جاري الجلب...", success: "تم الحفظ بنجاح!", error: "حدث خطأ. الرابط غير صحيح.",
    fajr: "الفجر", dhuhr: "الظهر", asr: "العصر", maghrib: "المغرب", isha: "العشاء",
    jumuah: "الجمعة", imsak: "الإمساك", iftar: " (إفطار)",
    clearConfirm: "هل أنت متأكد من مسح جميع إحصائيات الصلوات السابقة؟ لا يمكن التراجع عن هذا الإجراء.",
    clearDone: "تم تصفير جميع الإحصائيات بنجاح!",
    trackedLabel: "الصلوات المؤداة",
    rollingWeek: "نافذة الـ 7 أيام الماضية", rollingMonth: "نافذة الـ 30 يوماً الماضية", yearlyChart: "المعدل الشهري (حصيلة السنة)",
    ashra1: "عشر الرحمة (العشر الأوائل)", ashra2: "عشر المغفرة (العشر الأواسط)", ashra3: "عشر العتق من النار (العشر الأواخر)",
    eidDaysLabel: "أيام الاحتفال بالعيد:",
    eidFitr: "اليوم {day} من عيد الفطر المبارك 🎉",
    eidAdha: "اليوم {day} من عيد الأضحى المبارك 🐑",
    imsakConfigLabel: "إظهار الإمساك:",
    imsakAlways: "دائماً",
    imsakRamadan: "في رمضان فقط",
    slidingChartsTitle: "لوحات تحليل أوقات الصلاة",
    weeklyAvgTitle: "مؤشرات الإنجاز",
    clearStatsBtn: "تصفير الإحصائيات",
    lblRingDay: "اليوم", lblRingWeek: "الأسبوع", lblRingMonth: "الشهر", lblRingYear: "السنة",
    trendTitle: "منحنيات تغير أوقات الصلاة",
    dir: "rtl",
    confirm: "تأكيد",
    cancel: "إلغاء",
    ramadan: "رمضان",
    fasting: "صيام",
    rollingWeekTrend: "تغير أوقات الصلاة لـ 7 أيام (اليوم + 6)",
    rollingMonthTrend: "تغير أوقات الصلاة لـ 30 يوماً (اليوم + 29)",
    yearlyTrend: "حصيلة تغير أوقات الصلاة لـ 12 شهراً",
    defaultSettingsConfirm: "هل أنت متأكد من استعادة الإعدادات الافتراضية؟",
    defaultSettingsDone: "تم استعادة الإعدادات الافتراضية بنجاح!",
    preAthanLabel: "تفعيل التذكير قبل الأذن",
    preAthanTimeLabel: "وقت التذكير (بالدقائق قبل الأذان):",
    defaultSettingsBtn: "استعادة الإعدادات الافتراضية",
    astro: "الفلك",
    moonTitle: "أطوار القمر الهجري",
    qiblaTitle: "بوصلة القبلة",
    kaabaDir: "اتجاه الكعبة المشرفة",
    qiblaAngle: "اتجاه القبلة: {angle}° من اتجاه الشمال",
    nextPrayerCountdown: "الصلاة القادمة: {name}",
    iqamaActiveCountdown: "وقت الصلاة: {name}",
    volumeLabel: "صوت الأذان:",
    hijriCorrectorLabel: "تصحيح التاريخ الهجري (بالأيام):",
    eventNewYear: "رأس السنة الهجرية الجديدة 🎉",
    eventAshura: "يوم عاشوراء المبارك 🕌",
    eventMawlid: "المولد النبوي الشريف 🌸",
    eventIsra: "ليلة الإسراء والمعراج 🌟",
    eventShaban: "ليلة النصف من شعبان ✨",
    eventArafah: "يوم عرفة المبارك 🕋",
    streakHubTitle: "السلسلة المباركة ⚡",
    currStreakLabel: "السلسلة الحالية: {days} يوم",
    longStreakLabel: "أطول سلسلة: {days} يوم",
    
    // Qada & Offsets translations
    qadaTab: "القضاء",
    qadaTitle: "دفتر قضاء الصلوات المفروضة الفائتة",
    offsetsTitle: "تصحيح أوقات الصلاة الفردية (بالدقائق):",
    dhikrTitle: "ذكر اليوم",
    quranTitle: "الآية اليومية",
    exportBtn: "تصدير سجل الصلوات (CSV)",
    sunSkyTitle: "حركة الشمس في السماء",
    dailies: "اليوميات",
    duaTitle: "دعاء اليوم",
	hadithTitle: "الحديث اليومي",

    // Revamped Qada Translations
    ledgerTitle: "دفتر القضاء (الصلوات الفائتة)",
    backlogTitle: "تصحيح رصيد القضاء يدوياً",
    qadaCommitTitle: "الالتزام اليومي بقضاء الصلوات الفائتة:",
    qadaAssistantEmpty: "الحمد لله! رصيد صلواتك الفائتة خالٍ تماماً، صلواتك كلها مقضية وفي وقتها! 🎉",
    qadaReminderTitle: "سجل الصلوات الفائتة المتبقية (قضاء)",
    qadaLine: "• {name}: <span style='color:var(--primary);'>المتبقي {debt} صلوات</span> (التزام {commit} يومياً) -> تاريخ الانتهاء المقدر: <span style='color:var(--success);'>{date}</span>",
    qadaNoCommit: "• {name}: <span style='color:var(--primary);'>المتبقي {debt} صلوات</span> (حدد الالتزام اليومي في الإعدادات لعرض تاريخ الانتهاء المقدر)"
  }
};

const MOON_PHASES = {
  en: [
    { name: "New Moon", img: "new-moon.png" },
    { name: "Waxing Crescent", img: "waxing-crescent.png" },
    { name: "First Quarter", img: "first-quarter.png" },
    { name: "Waxing Gibbous", img: "waxing-gibbous.png" },
    { name: "Full Moon", img: "full-moon.png" },
    { name: "Waning Gibbous", img: "waning-gibbous.png" },
    { name: "Third Quarter", img: "third-quarter.png" },
    { name: "Waning Crescent", img: "waning-crescent.png" }
  ],
  ar: [
    { name: "المحاق (قمر جديد)", img: "new-moon.png" },
    { name: "الهلال المتزايد", img: "waxing-crescent.png" },
    { name: "التربيع الأول", img: "first-quarter.png" },
    { name: "الأحدب المتزايد", img: "waxing-gibbous.png" },
    { name: "البدر (قمر كامل)", img: "full-moon.png" },
    { name: "الأحدب المتناقص", img: "waning-gibbous.png" },
    { name: "التربيع الأخير", img: "third-quarter.png" },
    { name: "الهلال المتناقص", img: "waning-crescent.png" }
  ]
};

// Dhikr List mapped chronologically by Day of the Week (0 = Sunday, 6 = Saturday)
const ESTABLISHED_DHIKRS = [
  { ar: "لا حول ولا قوة إلا بالله", en: "La hawla wa la quwwata illa billah" }, // Sun
  { ar: "سبحان الله وبحمده", en: "SubhanAllahi wa bihamdihi" },                 // Mon
  { ar: "الحمد لله رب العالمين", en: "Alhamdulillah" },                         // Tue
  { ar: "لا إله إلا الله وحده لا شريك له", en: "La ilaha illa Allah" },        // Wed
  { ar: "أستغفر الله العظيم وأتوب إليه", en: "Astaghfirullah" },               // Thu
  { ar: "اللهم صل وسلم على نبينا محمد", en: "Allahumma salli wa sallim" },      // Fri
  { ar: "سبحان الله وبحمده سبحان الله العظيم", en: "SubhanAllah" }             // Sat
];

// Hisn al-Muslim Authentic Daily Du'as (0 = Sunday, 6 = Saturday)
const ESTABLISHED_DUAS = [
  { ar: "اللهم بك أصبحنا وبك أمسينا، وبك نحيا وبك نموت وإليك النشور", en: "O Allah, by Your leave we have reached the morning and by Your leave we have reached the evening, by Your leave we live and die, and unto You is our resurrection.", ref: "Abu Dawud" },
  { ar: "اللهم إني أسألك علماً نافعاً، ورزقاً طيباً، وعملاً متقبلاً", en: "O Allah, I ask You for knowledge that is of benefit, a good provision, and deeds that will be accepted.", ref: "Ibn Majah" },
  { ar: "يا حي يا قيوم برحمتك أستغيث، أصلح لي شأني كله ولا تكلني إلى نفسي طرفة عين", en: "O Ever Living, O Sustainer, in Your Mercy I seek relief. Correct all my affairs and do not leave me to myself even for the blink of an eye.", ref: "An-Nasa'i" },
  { ar: "اللهم إني أعوذ بك من الهم والحزن، والعجز والكسل، والبخل والجبن، وضلع الدين، وغلبة الرجال", en: "O Allah, I seek refuge in You from anxiety and sorrow, weakness and laziness, miserliness and cowardice, the burden of debts and from being overpowered by men.", ref: "Al-Bukhari" },
  { ar: "اللهم عافني في بدني، اللهم عافني في سمعي، اللهم عافني في بصري، لا إله إلا أنت", en: "O Allah, make me healthy in my body. O Allah, preserve for me my hearing. O Allah, preserve for me my sight. There is no deity but You.", ref: "Abu Dawud" },
  { ar: "اللهم إنك عفو تحب العفو فاعف عني", en: "O Allah, You are forgiving and love forgiveness, so forgive me.", ref: "At-Tirmidhi" },
  { ar: "اللهم أنت ربي لا إله إلا أنت، خلقتني وأنا عبدك، وأنا على عهدك ووعدك ما استطعت، أعوذ بك من شر ما صنعت، أبوء لك بنعمتك علي وأبوء بذنبي فاغفر لي فإنه لا يغفر الذنوب إلا أنت", en: "O Allah, You are my Lord. None has the right to be worshiped but You. You created me and I am Your slave, and I am faithful to Your covenant and my promise as much as I can. I seek refuge in You from all the evil I have done. I acknowledge before You all the blessings You have bestowed upon me, and I confess to You all my sins. So I entreat You to forgive my sins, for nobody can forgive sins except You.", ref: "Al-Bukhari" }
];

// Global canvas hover tracker states
const activeHovers = {
  'weekly-trend-chart': null,
  'monthly-trend-chart': null,
  'yearly-trend-chart': null
};

let countdownInterval = null;

document.addEventListener("DOMContentLoaded", async () => {
  console.log("[Popup] Initializing popup scripts...");

  // 1. Tab Navigation System
  document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const target = btn.dataset.target;
      console.log(`[Popup] Tab switched to: ${target}`);
      
      document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
      document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
      
      btn.classList.add('active');
      document.getElementById(target).classList.add('active');
      
      if (target === 'stats-tab' || target === 'astro-tab' || target === 'qada-tab') {
        renderCharts();
      }
    });
  });

  // 2. Load Local Settings
  console.log("[Popup] Loading settings from Local Storage...");
  const data = await chrome.storage.local.get(null);
  console.log("[Popup] Storage load complete.", data);
  
  if (data.lang) document.getElementById('lang').value = data.lang;
  if (data.theme) document.getElementById('theme').value = data.theme;
  if (data.mosqueUrl) document.getElementById('url').value = data.mosqueUrl;
  if (data.eidDays) document.getElementById('eid-days').value = data.eidDays;
  if (data.imsakConfig) document.getElementById('imsak-config').value = data.imsakConfig;
  if (data.hijriCorrector) document.getElementById('hijri-corrector').value = data.hijriCorrector;

  // Load offsets in settings inputs
  const offsets = data.prayerOffsets || { Fajr: 0, Dhuhr: 0, Asr: 0, Maghrib: 0, Isha: 0 };
  PRAYERS.forEach(p => {
    const input = document.getElementById(`offset-${p}`);
    if (input) input.value = offsets[p] || 0;
  });

  // Load Qada commitments
  const qadaCommit = data.qadaCommitment || { Fajr: 0, Dhuhr: 0, Asr: 0, Maghrib: 0, Isha: 0 };
  PRAYERS.forEach(p => {
    const input = document.getElementById(`commit-${p}`);
    if (input) input.value = qadaCommit[p] || 0;
  });

  // Load Qada values
  const qada = data.qadaValues || { Fajr: 0, Dhuhr: 0, Asr: 0, Maghrib: 0, Isha: 0 };
  PRAYERS.forEach(p => {
    safeSetText(`qada-val-${p}`, qada[p] || 0);
  });

  // Volume setup
  const volumeVal = data.athanVolume !== undefined ? Math.round(data.athanVolume * 100) : 50;
  document.getElementById('volume-slider').value = volumeVal;
  document.getElementById('volume-display').innerText = `${volumeVal}%`;

  // Pre-Athan states
  const preEnabled = data.preAthanEnabled !== undefined ? data.preAthanEnabled : true;
  document.getElementById('pre-athan-enable').checked = preEnabled;
  updatePreAthanUIState(preEnabled);
  if (data.preAthanMins) document.getElementById('pre-athan-mins').value = data.preAthanMins;
  
  applyTheme(data.theme || 'light');
  updateLanguageUI();
  updateTimesAndChecklistUI(data);
  fetchAuthenticHadith(data.lang || 'ar');
  fetchAyahOfTheDay();

  // Load and render Dhikr Tasbih
  const activeDayOfWeek = new Date().getDay();
  const todayDhikr = ESTABLISHED_DHIKRS[activeDayOfWeek];
  safeSetText('dhikr-arabic', todayDhikr.ar);
  safeSetText('dhikr-translation', todayDhikr.en);

  // Load and render Du'a
  const todayDua = ESTABLISHED_DUAS[activeDayOfWeek];
  safeSetText('dua-arabic', todayDua.ar);
  safeSetText('dua-translation', todayDua.en);
  safeSetText('dua-reference', `[${todayDua.ref}]`);

  // Dhikr Counter logic (Binds via safe check)
  let dhikrCount = data.dhikrCount || 0;
  const countBtn = document.getElementById('dhikr-count-btn');
  if (countBtn) {
    countBtn.innerText = dhikrCount;
    countBtn.addEventListener('click', async () => {
      dhikrCount++;
      countBtn.innerText = dhikrCount;
      await chrome.storage.local.set({ dhikrCount });
    });
  }

  // FIXED: Converted all Event Listeners to use safeAddEventListener to resolve Uncaught TypeErrors [1]!
  safeAddEventListener('dhikr-reset-btn', 'click', async () => {
    dhikrCount = 0;
    if (countBtn) countBtn.innerText = 0;
    await chrome.storage.local.set({ dhikrCount: 0 });
    console.log("[Dhikr] Tasbih counter reset.");
  });

  // Launch ticking countdown clock
  startCountdownTicking(data);
  renderCharts();

  // 3. User Setting Listeners (FIXED: Binds safely)
  safeAddEventListener('lang', 'change', async (e) => {
    const newLang = e.target.value;
    console.log(`[Popup] Language changing to: ${newLang}`);
    await chrome.storage.local.set({ lang: newLang });
    updateLanguageUI();
    fetchAuthenticHadith(newLang);
    fetchAyahOfTheDay();
    
    // Update daily Du'a language
    const freshDua = ESTABLISHED_DUAS[new Date().getDay()];
    safeSetText('dua-arabic', freshDua.ar);
    safeSetText('dua-translation', freshDua.en);

    chrome.runtime.sendMessage({ action: "force_fetch" }, async (res) => {
      const freshData = await chrome.storage.local.get(null);
      updateTimesAndChecklistUI(freshData);
      startCountdownTicking(freshData);
    });
  });

  safeAddEventListener('theme', 'change', (e) => {
    const newTheme = e.target.value;
    console.log(`[Popup] Theme changing to: ${newTheme}`);
    chrome.storage.local.set({ theme: newTheme });
    applyTheme(newTheme);
    renderCharts();
  });

  safeAddEventListener('eid-days', 'change', (e) => {
    const days = parseInt(e.target.value, 10) || 3;
    console.log(`[Settings] Eid days configuration set to: ${days}`);
    chrome.storage.local.set({ eidDays: days }).then(async () => {
      const freshData = await chrome.storage.local.get(null);
      updateTimesAndChecklistUI(freshData);
    });
  });

  safeAddEventListener('imsak-config', 'change', (e) => {
    const val = e.target.value;
    console.log(`[Settings] Imsak configuration set to: ${val}`);
    chrome.storage.local.set({ imsakConfig: val }).then(async () => {
      const freshData = await chrome.storage.local.get(null);
      updateTimesAndChecklistUI(freshData);
    });
  });

  safeAddEventListener('volume-slider', 'input', (e) => {
    const val = parseInt(e.target.value, 10);
    document.getElementById('volume-display').innerText = `${val}%`;
    chrome.storage.local.set({ athanVolume: val / 100 });
  });

  // Dedicated Settings 3-minute Volume Test Button
  safeAddEventListener('test-volume-btn', 'click', () => {
    const val = parseInt(document.getElementById('volume-slider').value, 10);
    const vol = val / 100;
    console.log(`[Settings] Manual volume Athan test triggered at real-time value: ${vol}`);
    chrome.runtime.sendMessage({ action: "test_athan", volume: vol, isTest: true });
  });

  safeAddEventListener('hijri-corrector', 'change', (e) => {
    const corrector = parseInt(e.target.value, 10) || 0;
    console.log(`[Settings] Hijri date corrector adjusted by: ${corrector} day(s)`);
    chrome.storage.local.set({ hijriCorrector: corrector }).then(() => {
      chrome.runtime.sendMessage({ action: "force_fetch" }, async () => {
        const freshData = await chrome.storage.local.get(null);
        updateTimesAndChecklistUI(freshData);
      });
    });
  });

  safeAddEventListener('pre-athan-enable', 'change', async (e) => {
    const enabled = e.target.checked;
    console.log(`[Settings] Pre-Athan Enable toggled to: ${enabled}`);
    updatePreAthanUIState(enabled);
    await chrome.storage.local.set({ preAthanEnabled: enabled });
  });

  safeAddEventListener('pre-athan-mins', 'change', async (e) => {
    const mins = parseInt(e.target.value, 10) || 10;
    console.log(`[Settings] Pre-Athan mins configured to: ${mins}`);
    await chrome.storage.local.set({ preAthanMins: mins });
  });

  // Bind Offset listeners
  PRAYERS.forEach(p => {
    safeAddEventListener(`offset-${p}`, 'change', async (e) => {
      const val = parseInt(e.target.value, 10) || 0;
      let db = await chrome.storage.local.get(['prayerOffsets']);
      let offsets = db.prayerOffsets || { Fajr: 0, Dhuhr: 0, Asr: 0, Maghrib: 0, Isha: 0 };
      offsets[p] = val;
      await chrome.storage.local.set({ prayerOffsets: offsets });
      console.log(`[Settings] ${p} Offset set to: ${val}`);
      
      const freshData = await chrome.storage.local.get(null);
      updateTimesAndChecklistUI(freshData);
      startCountdownTicking(freshData);
    });
  });

  // Bind Qada commitments settings listeners
  PRAYERS.forEach(p => {
    safeAddEventListener(`commit-${p}`, 'change', async (e) => {
      const val = parseInt(e.target.value, 10) || 0;
      let db = await chrome.storage.local.get(['qadaCommitment']);
      let commit = db.qadaCommitment || { Fajr: 0, Dhuhr: 0, Asr: 0, Maghrib: 0, Isha: 0 };
      commit[p] = val;
      await chrome.storage.local.set({ qadaCommitment: commit });
      console.log(`[Settings] Qada commitment for ${p} set to: ${val}`);
      renderCharts(); // immediately recalculate estimated dates in ledger card
    });
  });

  // Bind Qada counter actions
  document.querySelectorAll('.qada-btn').forEach(btn => {
    btn.addEventListener('click', async (e) => {
      const prayer = e.target.dataset.prayer;
      const action = e.target.dataset.action;
      
      let db = await chrome.storage.local.get(['qadaValues']);
      let qada = db.qadaValues || { Fajr: 0, Dhuhr: 0, Asr: 0, Maghrib: 0, Isha: 0 };
      
      let currentVal = qada[prayer] || 0;
      if (action === 'inc') {
        currentVal++;
      } else if (action === 'dec' && currentVal > 0) {
        currentVal--;
      }
      
      qada[prayer] = currentVal;
      await chrome.storage.local.set({ qadaValues: qada });
      safeSetText(`qada-val-${prayer}`, currentVal);
      console.log(`[Qada] ${prayer} adjusted to: ${currentVal}`);
      renderCharts(); // recalculate estimates
    });
  });

  // Export Tracking Database CSV Button
  safeAddEventListener('export-btn', 'click', () => {
    console.log("[Settings] Database CSV export requested.");
    exportTrackerDatabaseToCSV();
  });

  safeAddEventListener('save-btn', 'click', async () => {
    const url = document.getElementById('url').value;
    const status = document.getElementById('status');
    const lang = document.getElementById('lang').value;
    
    console.log(`[Popup] Attempting to save URL: ${url}`);
    status.style.display = 'block';
    status.style.color = 'var(--primary)';
    status.innerText = I18N[lang].fetching;

    if (url.includes('mawaqit.net')) {
      await chrome.storage.local.set({ mosqueUrl: url });
      chrome.runtime.sendMessage({ action: "force_fetch" }, async (res) => {
        if (res && res.success) {
          console.log("[Popup] Force fetch completed successfully.");
          const newData = await chrome.storage.local.get(null);
          updateTimesAndChecklistUI(newData);
          startCountdownTicking(newData);
          status.style.color = 'green';
          status.innerText = I18N[lang].success;
        } else {
          console.error("[Popup] Force fetch failed.");
          status.style.color = 'var(--danger)';
          status.innerText = I18N[lang].error;
        }
        setTimeout(() => { status.style.display = 'none'; }, 4000);
      });
    } else {
      status.style.color = 'var(--danger)';
      status.innerText = I18N[lang].error;
      setTimeout(() => { status.style.display = 'none'; }, 4000);
    }
  });

  // 4. Interactive Tracker Checkbox Handlers
  document.querySelectorAll('.prayer-check').forEach(chk => {
    chk.addEventListener('change', async (e) => {
      const prayer = e.target.dataset.prayer;
      const checked = e.target.checked;
      
      console.log(`[Tracker] Checkbox clicked -> Prayer: ${prayer} | Value: ${checked}`);
      
      const db = await chrome.storage.local.get(['tracker', 'prayerTimes', 'mosqueTimezone']);
      const tz = db.mosqueTimezone || "Africa/Algiers";
      const activeDayId = getActiveLogicalDayId(db.prayerTimes, tz);

      let tracker = db.tracker || { currentDayId: activeDayId, today: {}, history: {} };
      if (!tracker.today) tracker.today = {};
      if (!tracker.history) tracker.history = {};
      
      // Ensure currentDayId is persistently written during clicks!
      tracker.today[prayer] = checked;
      tracker.currentDayId = activeDayId; 
      
      await chrome.storage.local.set({ tracker });
      
      const row = document.getElementById(`row-${prayer}`);
      if (checked) {
        row.classList.add('prayed');
        chk.disabled = true; // Lock checked boxes: - [x] prayed prayer <disabled>
        console.log(`[Tracker] ${prayer} has been marked as prayed.`);
      } else {
        row.classList.remove('prayed');
      }
    });
  });

  // 5. Clear Statistics Handler (Modal Overlay Implementation)
  safeAddEventListener('clear-stats-btn', 'click', () => {
    const lang = document.getElementById('lang').value;
    console.log("[Settings] Clear stats overlay requested.");
    
    showConfirmModal(I18N[lang].clearConfirm, async () => {
      const freshTracker = { currentDayId: "", today: { Fajr: false, Dhuhr: false, Asr: false, Maghrib: false, Isha: false }, history: {} };
      await chrome.storage.local.set({ tracker: freshTracker });
      console.log("[Settings] Database wiped cleanly in local storage.");
      const updated = await chrome.storage.local.get(null);
      updateTimesAndChecklistUI(updated);
      renderCharts();
    });
  });

  // 6. Default Settings Button
  safeAddEventListener('default-settings-btn', 'click', () => {
    const lang = document.getElementById('lang').value;
    console.log("[Settings] Default settings overlay requested.");
    
    showConfirmModal(I18N[lang].defaultSettingsConfirm, async () => {
      // Keep only the historical progress data, remove configurations
      await chrome.storage.local.remove([
        'mosqueUrl', 'lang', 'theme', 'imsakConfig', 'eidDays', 
        'mosqueCalendar', 'mosqueTimezone', 'prayerTimes', 
        'hijriDate', 'hijriMonth', 'hijriDay', 'ramadanDay', 'imsakTime', 'iqamaWaits',
        'preAthanEnabled', 'preAthanMins', 'testOverrides', 'mosqueLat', 'mosqueLon', 'athanVolume', 'hijriCorrector', 'shuruqTime', 'qadaCommitment'
      ]);
      
      console.log("[Settings] Configurations restored to default factory states.");
      
      // Reset DOM Fields
      document.getElementById('url').value = "";
      document.getElementById('lang').value = "ar";
      document.getElementById('theme').value = "light";
      document.getElementById('imsak-config').value = "always";
      document.getElementById('eid-days').value = "3";
      document.getElementById('volume-slider').value = 50;
      document.getElementById('volume-display').innerText = "50%";
      document.getElementById('hijri-corrector').value = 0;
      document.getElementById('pre-athan-enable').checked = true;
      document.getElementById('pre-athan-mins').value = 10;
      document.getElementById('pre-athan-mins').disabled = false;
      document.getElementById('pre-athan-time-container').style.opacity = '1';
      document.getElementById('pre-athan-time-container').style.pointerEvents = 'auto';
      
      PRAYERS.forEach(p => {
        const input = document.getElementById(`commit-${p}`);
        if (input) input.value = 0;
      });

      applyTheme('light');
      updateLanguageUI();
      updateTimesAndChecklistUI({});
      
      // Clear Badge
      chrome.action.setBadgeText({ text: "" });
      renderCharts();
    });
  });

  // --- START TEMPORARY DEVELOPMENT BUTTONS ---
  safeAddEventListener('test-athan', 'click', () => {
    console.log("[Test] Manual standard Athan trigger requested.");
    chrome.runtime.sendMessage({ action: "test_athan" });
  });

  safeAddEventListener('test-athan-fajr', 'click', () => {
    console.log("[Test] Manual Fajr Athan trigger requested.");
    chrome.runtime.sendMessage({ action: "test_athan_fajr" });
  });

  safeAddEventListener('test-iqama', 'click', () => {
    console.log("[Test] Yellow Iqama badge preview requested.");
    chrome.runtime.sendMessage({ action: "test_iqama_badge" });
  });
  
  safeAddEventListener('test-friday', 'click', async () => {
    console.log("[Test] Simulating Jumu'ah Friday environment.");
    sessionStorage.setItem('force_friday', 'true');
    const d = await chrome.storage.local.get(null);
    updateTimesAndChecklistUI(d);
  });

  safeAddEventListener('force-enable-chk', 'click', async () => {
    console.log("[Test] Forcing all prayer tracker checkboxes to: ENABLED");
    await chrome.storage.local.set({ testOverrides: { forceCheckboxes: 'enabled' } });
    const d = await chrome.storage.local.get(null);
    updateTimesAndChecklistUI(d);
  });

  safeAddEventListener('force-disable-chk', 'click', async () => {
    console.log("[Test] Forcing all prayer tracker checkboxes to: DISABLED");
    await chrome.storage.local.set({ testOverrides: { forceCheckboxes: 'disabled' } });
    const d = await chrome.storage.local.get(null);
    updateTimesAndChecklistUI(d);
  });

  safeAddEventListener('force-future-chk', 'click', async () => {
    console.log("[Test] Forcing all prayer tracker checkboxes to: FUTURE (disabled)");
    await chrome.storage.local.set({ testOverrides: { forceCheckboxes: 'future' } });
    const d = await chrome.storage.local.get(null);
    updateTimesAndChecklistUI(d);
  });
  
  safeAddEventListener('test-ram-1', 'click', () => simulateRamadanDay(5));
  safeAddEventListener('test-ram-2', 'click', () => simulateRamadanDay(15));
  safeAddEventListener('test-ram-3', 'click', () => simulateRamadanDay(25));

  safeAddEventListener('test-eid-fitr', 'click', async () => {
    console.log("[Test] Simulating Day 1 of Eid al-Fitr...");
    sessionStorage.setItem('force_eid_fitr', 'true');
    sessionStorage.removeItem('force_eid_adha');
    sessionStorage.removeItem('force_ramadan');
    const d = await chrome.storage.local.get(null);
    updateTimesAndChecklistUI(d);
  });

  safeAddEventListener('test-eid-adha', 'click', async () => {
    console.log("[Test] Simulating Day 2 of Eid al-Adha...");
    sessionStorage.setItem('force_eid_adha', 'true');
    sessionStorage.removeItem('force_eid_fitr');
    sessionStorage.removeItem('force_ramadan');
    const d = await chrome.storage.local.get(null);
    updateTimesAndChecklistUI(d);
  });

  safeAddEventListener('test-event-newyear', 'click', async () => {
    console.log("[Test] Simulating Islamic New Year...");
    sessionStorage.setItem('force_hijri_event', 'new_year');
    const d = await chrome.storage.local.get(null);
    updateTimesAndChecklistUI(d);
  });

  safeAddEventListener('test-event-ashura', 'click', async () => {
    console.log("[Test] Simulating Day of Ashura...");
    sessionStorage.setItem('force_hijri_event', 'ashura');
    const d = await chrome.storage.local.get(null);
    updateTimesAndChecklistUI(d);
  });

  safeAddEventListener('test-event-mawlid', 'click', async () => {
    console.log("[Test] Simulating Mawlid al-Nabi...");
    sessionStorage.setItem('force_hijri_event', 'mawlid');
    const d = await chrome.storage.local.get(null);
    updateTimesAndChecklistUI(d);
  });

  safeAddEventListener('test-event-isra', 'click', async () => {
    console.log("[Test] Simulating Isra' & Mi'raj...");
    sessionStorage.setItem('force_hijri_event', 'isra');
    const d = await chrome.storage.local.get(null);
    updateTimesAndChecklistUI(d);
  });

  safeAddEventListener('test-event-shaban', 'click', async () => {
    console.log("[Test] Simulating Mid-Sha'ban Night...");
    sessionStorage.setItem('force_hijri_event', 'shaban');
    const d = await chrome.storage.local.get(null);
    updateTimesAndChecklistUI(d);
  });

  safeAddEventListener('test-event-arafah', 'click', async () => {
    console.log("[Test] Simulating Day of Arafah...");
    sessionStorage.setItem('force_hijri_event', 'arafah');
    const d = await chrome.storage.local.get(null);
    updateTimesAndChecklistUI(d);
  });

  safeAddEventListener('inject-data-btn', 'click', async () => {
    console.log("[Test] Injecting 365 Days of high-completion mock history...");
    let history = {};
    const now = new Date();
    for (let i = 365; i > 0; i--) {
      let d = new Date(now);
      d.setDate(now.getDate() - i);
      const dateStr = d.toISOString().split('T')[0];
      
      history[dateStr] = {
        Fajr: Math.random() > 0.3,
        Dhuhr: Math.random() > 0.15,
        Asr: Math.random() > 0.2,
        Maghrib: Math.random() > 0.1,
        Isha: Math.random() > 0.25
      };
    }
    
    let db = await chrome.storage.local.get(['tracker']);
    let tracker = db.tracker || {};
    tracker.history = history;
    if (!tracker.today) {
      tracker.today = { Fajr: true, Dhuhr: false, Asr: true, Maghrib: true, Isha: false };
    }
    await chrome.storage.local.set({ tracker });
    console.log("[Test] 365 days of high-completion mock history injected.");
    renderCharts();
  });

  safeAddEventListener('rollover-trigger-btn', 'click', async () => {
    console.log("[Test] Manually triggering Fajr logical day rollover...");
    const db = await chrome.storage.local.get(['tracker']);
    let tracker = db.tracker || { currentDayId: "" };
    
    if (tracker.currentDayId) {
      tracker.history[tracker.currentDayId] = { ...tracker.today };
    }
    const nextDay = new Date();
    nextDay.setDate(nextDay.getDate() + 1);
    const mockDayId = nextDay.toISOString().split('T')[0];
    
    tracker.currentDayId = mockDayId;
    tracker.today = { Fajr: false, Dhuhr: false, Asr: false, Maghrib: false, Isha: false };
    await chrome.storage.local.set({ tracker });
    console.log(`[Test] Rollover complete. New logical day ID is: ${mockDayId}`);
    const freshData = await chrome.storage.local.get(null);
    updateTimesAndChecklistUI(freshData);
  });

  /*safeAddEventListener('test-inject-streak', 'click', async () => {
    console.log("[Test] Forcing simulated active perfect streak...");
    sessionStorage.setItem('force_streak', 'true');
    renderCharts();
  });*/

  // Individual Streak tier simulated overrides
  safeAddEventListener('test-streak-bronze', 'click', () => simulateTestStreak(3));
  safeAddEventListener('test-streak-silver', 'click', () => simulateTestStreak(7));
  safeAddEventListener('test-streak-gold', 'click', () => simulateTestStreak(30));
  safeAddEventListener('test-streak-diamond', 'click', () => simulateTestStreak(90));

  safeAddEventListener('revert-tests-btn', 'click', async () => {
     console.log("[Test] Reverting all simulation overrides and clearing mock databases.");
    sessionStorage.clear();
    
    // Clear 365 days of injected history databases cleanly [1]
    let db = await chrome.storage.local.get(['tracker']);
    let tracker = db.tracker || {};
    tracker.history = {}; 
    
    await chrome.storage.local.set({ tracker });
    await chrome.storage.local.remove('testOverrides');
    chrome.runtime.sendMessage({ action: "force_fetch" }, async () => {
      const freshData = await chrome.storage.local.get(null);
      updateTimesAndChecklistUI(freshData);
      renderCharts();
    });
  });
  // --- END TEMPORARY DEVELOPMENT BUTTONS ---

  // Setup dynamic chart mouse hover listeners
  ['weekly-trend-chart', 'monthly-trend-chart', 'yearly-trend-chart'].forEach(id => {
    const canvas = document.getElementById(id);
    if (canvas) {
      canvas.addEventListener('mousemove', (e) => {
        const rect = canvas.getBoundingClientRect();
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;
        activeHovers[id] = { x, y };
        renderCharts(); // trigger redraw with active hover
      });
      canvas.addEventListener('mouseleave', () => {
        activeHovers[id] = null;
        renderCharts();
      });
    }
  });
});

// Defensive UI rendering utility to prevent uncaught TypeErrors from missing elements
function safeSetText(id, text) {
  const element = document.getElementById(id);
  if (element) {
    element.innerText = text;
  } else {
    console.warn(`[UI Warning] safeSetText: Element ID '${id}' was not found in the DOM.`);
  }
}

// Defensive UI listener binding utility to prevent uncaught TypeErrors during re-renders
function safeAddEventListener(id, event, callback) {
  const element = document.getElementById(id);
  if (element) {
    element.addEventListener(event, callback);
  } else {
    console.warn(`[UI Warning] safeAddEventListener: Element ID '${id}' was not found in the DOM.`);
  }
}

function applyTheme(theme) {
  document.documentElement.setAttribute("data-theme", theme);
}

// Helper to calculate custom simulated streaks
async function simulateTestStreak(days) {
  console.log(`[Test] Simulating ${days}-Day contiguous perfect streak...`);
  sessionStorage.setItem('force_streak', 'true');
  sessionStorage.setItem('force_streak_val', days.toString());
  renderCharts();
}

// Helper to handle Pre-Athan configuration elements opacity & availability
function updatePreAthanUIState(enabled) {
  const minsInput = document.getElementById('pre-athan-mins');
  const container = document.getElementById('pre-athan-time-container');
  if (minsInput && container) {
    minsInput.disabled = !enabled;
    container.style.opacity = enabled ? '1' : '0.4';
    container.style.pointerEvents = enabled ? 'auto' : 'none';
  }
}

// Helper to calculate streak contiguous sequences
function calculateStreakData(history, today) {
  const dates = Object.keys(history).sort();
  const todayPerfect = PRAYERS.every(p => today[p]);
  
  if (dates.length === 0) {
    return { current: todayPerfect ? 1 : 0, longest: todayPerfect ? 1 : 0 };
  }

  let longest = 0;
  let tempStreak = 0;

  // Calculate historical longest perfect streak
  dates.forEach(dStr => {
    const dayData = history[dStr];
    const isPerfect = PRAYERS.every(p => dayData[p]);
    if (isPerfect) {
      tempStreak++;
      if (tempStreak > longest) longest = tempStreak;
    } else {
      tempStreak = 0;
    }
  });

  // Calculate current active contiguous perfect streak
  const now = new Date();
  let checkOffset = 1;
  let activeStreak = 0;
  
  while (true) {
    let d = new Date(now);
    d.setDate(now.getDate() - checkOffset);
    const dStr = d.toISOString().split('T')[0];
    
    const dayData = history[dStr];
    if (dayData && PRAYERS.every(p => dayData[p])) {
      activeStreak++;
      checkOffset++;
    } else {
      break;
    }
  }

  const current = activeStreak + (todayPerfect ? 1 : 0);
  if (current > longest) longest = current;

  return { current, longest };
}

// Helper to determine logical tracking day id using timezone parameters
function getActiveLogicalDayId(prayerTimes, tz) {
  const now = new Date();
  const formatter = new Intl.DateTimeFormat('en-US', { timeZone: tz, year: 'numeric', month: '2-digit', day: '2-digit' });
  const parts = formatter.formatToParts(now);
  const y = parts.find(p => p.type === 'year').value;
  const m = parts.find(p => p.type === 'month').value;
  const d = parts.find(p => p.type === 'day').value;
  const todayStr = `${y}-${m}-${d}`;

  if (!prayerTimes || prayerTimes.length === 0) return todayStr;

  const [fajrHour, fajrMin] = prayerTimes[0].split(':').map(Number);
  const fajrTotalMins = fajrHour * 60 + fajrMin;

  const tzFormatter = new Intl.DateTimeFormat('en-US', { timeZone: tz, hour: 'numeric', minute: 'numeric', second: 'numeric', hour12: false });
  const tzParts = tzFormatter.formatToParts(now);
  const currentHour = parseInt(tzParts.find(p => p.type === 'hour').value, 10);
  const currentMinute = parseInt(tzParts.find(p => p.type === 'minute').value, 10);
  const currentTotalMins = currentHour * 60 + currentMinute;

  if (currentTotalMins < fajrTotalMins) {
    const yesterday = new Date(now);
    yesterday.setDate(now.getDate() - 1);
    const yFormatter = new Intl.DateTimeFormat('en-US', { timeZone: tz, year: 'numeric', month: '2-digit', day: '2-digit' });
    const yParts = yFormatter.formatToParts(yesterday);
    const yy = yParts.find(p => p.type === 'year').value;
    const ym = yParts.find(p => p.type === 'month').value;
    const yd = yParts.find(p => p.type === 'day').value;
    return `${yy}-${ym}-${yd}`;
  }
  return todayStr;
}

function updateLanguageUI() {
  const lang = document.getElementById('lang').value;
  const dict = I18N[lang];
  
  document.documentElement.dir = dict.dir;
  safeSetText('app-title', dict.title);
  safeSetText('tab-home', dict.home);
  safeSetText('tab-stats', dict.stats);
  safeSetText('tab-astro', dict.astro);
  safeSetText('tab-qada', dict.qadaTab);
  safeSetText('tab-settings', dict.settings);
  safeSetText('tab-dailies', dict.dailies);
  
  safeSetText('url-label', dict.urlLabel);
  safeSetText('lang-label', dict.langLabel);
  safeSetText('theme-label', dict.themeLabel);
  safeSetText('imsak-config-label', dict.imsakConfigLabel);
  safeSetText('save-btn', dict.saveBtn);
  safeSetText('eid-days-label', dict.eidDaysLabel);
  safeSetText('clear-stats-btn', dict.clearStatsBtn);
  safeSetText('default-settings-btn', dict.defaultSettingsBtn);
  safeSetText('export-btn', dict.exportBtn);
  
  safeSetText('imsak-lbl', dict.imsak);
  safeSetText('fajr-lbl', dict.fajr);
  safeSetText('asr-lbl', dict.asr);
  safeSetText('isha-lbl', dict.isha);

  safeSetText('weekly-avg-title', dict.weeklyAvgTitle);
  safeSetText('week-chart-lbl', dict.rollingWeek);
  safeSetText('month-chart-lbl', dict.rollingMonth);
  safeSetText('year-chart-lbl', dict.yearlyChart);
  safeSetText('accuweather-title', dict.trendTitle);
  safeSetText('sliding-charts-title', dict.slidingChartsTitle); // FIXED: Correctly translates Section Title on Stats Tab!

  safeSetText('lbl-ring-day', dict.lblRingDay);
  safeSetText('lbl-ring-week', dict.lblRingWeek);
  safeSetText('lbl-ring-month', dict.lblRingMonth);
  safeSetText('lbl-ring-year', dict.lblRingYear);

  safeSetText('imsak-wait-label', dict.ramadan);

  safeSetText('leg-fajr', dict.fajr);
  safeSetText('leg-dhuhr', dict.dhuhr);
  safeSetText('leg-asr', dict.asr);
  safeSetText('leg-maghrib', dict.maghrib);
  safeSetText('leg-isha', dict.isha);

  safeSetText('week-trend-lbl', dict.rollingWeekTrend);
  safeSetText('month-trend-lbl', dict.rollingMonthTrend);
  safeSetText('year-trend-lbl', dict.yearlyTrend);

  safeSetText('pre-athan-label', dict.preAthanLabel);
  safeSetText('pre-athan-time-label', dict.preAthanTimeLabel);

  safeSetText('moon-phase-title', dict.moonTitle);
  safeSetText('qibla-title', dict.qiblaTitle);
  safeSetText('qibla-direction-label', dict.kaabaDir);

  safeSetText('volume-label', dict.volumeLabel);
  safeSetText('hijri-corrector-label', dict.hijriCorrectorLabel);
  safeSetText('streak-hub-title', dict.streakHubTitle);

  safeSetText('qada-title', dict.qadaTitle);
  safeSetText('qada-fajr', dict.fajr);
  safeSetText('qada-dhuhr', dict.dhuhr);
  safeSetText('qada-asr', dict.asr);
  safeSetText('qada-maghrib', dict.maghrib);
  safeSetText('qada-isha', dict.isha);

  safeSetText('offsets-title-lbl', dict.offsetsTitle);
  safeSetText('off-fajr', dict.fajr);
  safeSetText('off-dhuhr', dict.dhuhr);
  safeSetText('off-asr', dict.asr);
  safeSetText('off-maghrib', dict.maghrib);
  safeSetText('off-isha', dict.isha);

  safeSetText('dhikr-title', dict.dhikrTitle);
  safeSetText('quran-title', dict.quranTitle);
  safeSetText('default-settings-btn', dict.defaultSettingsBtn); // FIXED: Correctly translates "Restore Default Settings" button!
  safeSetText('sun-sky-title', dict.sunSkyTitle);
  safeSetText('dua-title', dict.duaTitle);
  safeSetText('hadith-title', dict.hadithTitle); // Translates Daily Hadith Title!

  
  // Translate Qada Tab Labels
  safeSetText('qada-title', dict.qadaTitle);
  safeSetText('ledger-title', dict.ledgerTitle);
  safeSetText('backlog-title', dict.backlogTitle);
  
  // Translate Qada Settings Panel
  safeSetText('qada-commit-title-lbl', dict.qadaCommitTitle);
  safeSetText('commit-fajr', dict.fajr);
  safeSetText('commit-dhuhr', dict.dhuhr);
  safeSetText('commit-asr', dict.asr);
  safeSetText('commit-maghrib', dict.maghrib);
  safeSetText('commit-isha', dict.isha);

  // Translate Imsak select dropdown
  const selectImsak = document.getElementById('imsak-config');
  if (selectImsak && selectImsak.options.length === 2) {
    selectImsak.options[0].text = dict.imsakAlways;
    selectImsak.options[1].text = dict.imsakRamadan;
  }

  // Translate Theme select dropdown [1]
  const selectTheme = document.getElementById('theme');
  if (selectTheme && selectTheme.options.length === 2) {
    selectTheme.options[0].text = lang === 'ar' ? 'فاتح' : 'Light';
    selectTheme.options[1].text = lang === 'ar' ? 'داكن' : 'Dark';
  }
}

// Dynamic adjust time helper to apply calibrated Individual Offsets
function getOffsetAdjustedTimeStr(timeStr, offset) {
  if (!timeStr) return timeStr;
  const [h, m] = timeStr.split(':').map(Number);
  let total = h * 60 + m + offset;
  if (total < 0) total += 1440;
  if (total >= 1440) total -= 1440;
  return `${String(Math.floor(total / 60)).padStart(2, '0')}:${String(total % 60).padStart(2, '0')}`;
}

function updateTimesAndChecklistUI(data) {
  console.log("[Popup] Processing UI layout update...");
  const lang = document.getElementById('lang').value;
  const dict = I18N[lang];
  
  if (data.mosqueName) safeSetText('mosque-name', data.mosqueName);
  if (data.hijriDate) safeSetText('hijri-date', data.hijriDate);

  // Friday Logic
  const isFriday = new Date().getDay() === 5 || sessionStorage.getItem('force_friday') === 'true';
  safeSetText('dhuhr-lbl', isFriday ? dict.jumuah : dict.dhuhr);

  // Ramadan Logic
  const isRamadan = (data.hijriDate && (data.hijriDate.includes("رمضان") || data.hijriDate.includes("Ramadan"))) || sessionStorage.getItem('force_ramadan') === 'true';
  const rDay = data.ramadanDay || 0;
  const ashraDiv = document.getElementById('ashra-display');

  if (isRamadan) {
    safeSetText('maghrib-lbl', dict.maghrib + dict.iftar);
    safeSetText('imsak-wait-label', dict.ramadan);
    
    ashraDiv.style.display = 'block';
    if (rDay >= 1 && rDay <= 10) {
      ashraDiv.innerText = dict.ashra1;
    } else if (rDay >= 11 && rDay <= 20) {
      ashraDiv.innerText = dict.ashra2;
    } else if (rDay >= 21 && rDay <= 30) {
      ashraDiv.innerText = dict.ashra3;
    } else {
      ashraDiv.style.display = 'none';
    }
  } else {
    safeSetText('maghrib-lbl', dict.maghrib);
    safeSetText('imsak-wait-label', dict.fasting);
    ashraDiv.style.display = 'none';
  }

  // --- EID ALIGNED DISPLAY PORTAL ---
  // FIXED: Parse Hijri Day dynamically from text string using robust, offline regex
  // This is completely immune to browser numeral formatting inconsistencies!
  let hDay = 1;
  if (data.hijriDate) {
    const dayMatch = data.hijriDate.match(/(الموافق لـ|corresponding to)\s*(\d{1,2})/i);
    if (dayMatch && dayMatch[2]) {
      hDay = parseInt(dayMatch[2], 10);
      console.log("[Popup] Parsed Hijri Day from text string:", hDay);
    }
  }

  const isShawwal = data.hijriDate && (data.hijriDate.includes("شوال") || data.hijriDate.includes("Shawwal"));
  const isDhulHijjah = data.hijriDate && (data.hijriDate.includes("ذو الحجة") || data.hijriDate.includes("Hijjah"));

  const configEidDays = data.eidDays || 3;

  // Test Overrides
  const forceEvent = sessionStorage.getItem('force_hijri_event');
  if (forceEvent === 'new_year') { hDay = 1; }
  else if (forceEvent === 'ashura') { hDay = 10; }
  else if (forceEvent === 'mawlid') { hDay = 12; }
  else if (forceEvent === 'isra') { hDay = 27; }
  else if (forceEvent === 'shaban') { hDay = 15; }
  else if (forceEvent === 'arafah') { hDay = 9; }

  if (sessionStorage.getItem('force_eid_fitr') === 'true') {
    hDay = 1;
  } else if (sessionStorage.getItem('force_eid_adha') === 'true') {
    hDay = 10;
  }

  const eidDiv = document.getElementById('eid-display');
  eidDiv.style.display = 'none';

  let isFirstDayOfEid = false;

  // Dynamic Islamic Holiday occassions
  let eventText = "";
  if (forceEvent === 'new_year' || (data.hijriDate && (data.hijriDate.includes("محرم") || data.hijriDate.includes("Muharram")) && hDay === 1)) {
    eventText = dict.eventNewYear;
  } else if (forceEvent === 'ashura' || (data.hijriDate && (data.hijriDate.includes("محرم") || data.hijriDate.includes("Muharram")) && hDay === 10)) {
    eventText = dict.eventAshura;
  } else if (forceEvent === 'mawlid' || (data.hijriDate && (data.hijriDate.includes("ربيع الأول") || data.hijriDate.includes("Rabi")) && hDay === 12)) {
    eventText = dict.eventMawlid;
  } else if (forceEvent === 'isra' || (data.hijriDate && (data.hijriDate.includes("رجب") || data.hijriDate.includes("Rajab")) && hDay === 27)) {
    eventText = dict.eventIsra;
  } else if (forceEvent === 'shaban' || (data.hijriDate && (data.hijriDate.includes("شعبان") || data.hijriDate.includes("Sha'ban")) && hDay === 15)) {
    eventText = dict.eventShaban;
  } else if (forceEvent === 'arafah' || (isDhulHijjah && hDay === 9)) {
    eventText = dict.eventArafah;
  }

  if ((isShawwal && hDay >= 1 && hDay <= configEidDays) || sessionStorage.getItem('force_eid_fitr') === 'true') {
    const displayDay = sessionStorage.getItem('force_eid_fitr') === 'true' ? 1 : hDay;
    eidDiv.style.display = 'block';
    eidDiv.innerText = dict.eidFitr.replace('{day}', displayDay);
    if (displayDay === 1) isFirstDayOfEid = true;
  } else if ((isDhulHijjah && hDay >= 10 && hDay < (10 + configEidDays)) || sessionStorage.getItem('force_eid_adha') === 'true') {
    const displayDay = sessionStorage.getItem('force_eid_adha') === 'true' ? 1 : (hDay - 9);
    eidDiv.style.display = 'block';
    eidDiv.innerText = dict.eidAdha.replace('{day}', displayDay);
    if (dayOfAdha === 1) isFirstDayOfEid = true;
  } else if (eventText) {
    eidDiv.style.display = 'block';
    eidDiv.innerText = eventText;
  }

  // Imsak Visibility Logic
  const imsakConfig = data.imsakConfig || "always";
  const imsakRow = document.getElementById('imsak-row');
  
  if (isFirstDayOfEid) {
    imsakRow.style.display = 'none';
  } else if (imsakConfig === "always") {
    imsakRow.style.display = 'flex';
    if (data.imsakTime) safeSetText('imsak-time', data.imsakTime);
  } else if (imsakConfig === "ramadan_only" && isRamadan) {
    imsakRow.style.display = 'flex';
    if (data.imsakTime) safeSetText('imsak-time', data.imsakTime);
  } else {
    imsakRow.style.display = 'none';
  }

  // Populate Times and +Wait badges
  if (data.prayerTimes && data.prayerTimes.length === 5) {
    const times = data.prayerTimes;
    const waits = data.iqamaWaits || ["15", "10", "10", "5", "10"];

    safeSetText('fajr-time', times[0]);
    safeSetText('dhuhr-time', times[1]);
    safeSetText('asr-time', times[2]);
    safeSetText('maghrib-time', times[3]);
    safeSetText('isha-time', times[4]);

    const unit = lang === 'ar' ? 'دقيقة' : 'min';
    safeSetText('fajr-wait', `+${waits[0]} ${unit}`);
    safeSetText('dhuhr-wait', `+${waits[1]} ${unit}`);
    safeSetText('asr-wait', `+${waits[2]} ${unit}`);
    safeSetText('maghrib-wait', `+${waits[3]} ${unit}`);
    safeSetText('isha-wait', `+${waits[4]} ${unit}`);

    // --- SETUP SMART CHECKLIST STATE ENGINE (Now fully aligned with Mosque Timezone to prevent checklist lockouts) ---
    const now = new Date();
    const tz = data.mosqueTimezone || "Africa/Algiers";
    const tzFormatter = new Intl.DateTimeFormat('en-US', { timeZone: tz, hour: 'numeric', minute: 'numeric', hour12: false });
    const tzParts = tzFormatter.formatToParts(now);
    const currentHour = parseInt(tzParts.find(p => p.type === 'hour').value, 10);
    const currentMinute = parseInt(tzParts.find(p => p.type === 'minute').value, 10);
    const currentTotalMins = currentHour * 60 + currentMinute;
    
    const tracker = data.tracker || { today: {} };
    const todayLog = tracker.today || {};
    
    const testOverrides = data.testOverrides || {};
    const forceCheckboxes = testOverrides.forceCheckboxes;

    PRAYERS.forEach((p, idx) => {
      const chk = document.getElementById(`check-${p}`);
      const row = document.getElementById(`row-${p}`);
      const [pHour, pMin] = times[idx].split(':').map(Number);
      const prayerMins = pHour * 60 + pMin;
      
      const isChecked = !!todayLog[p];
      chk.checked = isChecked;

      // Reset styling classes
      row.className = "prayer-row";

      if (isChecked) {
        row.classList.add('prayed');
        chk.disabled = true;
      } else {
        const hasArrived = currentTotalMins >= prayerMins;
        if (hasArrived) {
          chk.disabled = false;
        } else {
          row.classList.add('future');
          chk.disabled = true;
        }
      }

      // Apply Persistent Manual Overrides if present
      if (forceCheckboxes === 'enabled') {
        chk.disabled = false;
        row.className = "prayer-row"; 
      } else if (forceCheckboxes === 'disabled') {
        chk.disabled = true;
      } else if (forceCheckboxes === 'future') {
        row.classList.add('future');
        chk.disabled = true;
      }
    });
  }

  // Draw dynamic astronomy moon phase (FIXED: Bypasses numeric extraction using the parsed hDay value)
  try {
    const activeHijriDay = hDay || 1;
    let phaseIdx = 0;
    
    if (activeHijriDay === 1 || activeHijriDay === 2) phaseIdx = 0; // New Moon
    else if (activeHijriDay >= 3 && activeHijriDay <= 7) phaseIdx = 1; // Waxing Crescent
    else if (activeHijriDay === 8) phaseIdx = 2; // First Quarter
    else if (activeHijriDay >= 9 && activeHijriDay <= 13) phaseIdx = 3; // Waxing Gibbous
    else if (activeHijriDay === 14 || activeHijriDay === 15) phaseIdx = 4; // Full Moon
    else if (activeHijriDay >= 16 && activeHijriDay <= 21) phaseIdx = 5; // Waning Gibbous
    else if (activeHijriDay === 22) phaseIdx = 6; // Third Quarter
    else phaseIdx = 7; // Waning Crescent

    const phase = MOON_PHASES[lang][phaseIdx];
    document.getElementById('moon-image').src = phase.img;
    safeSetText('moon-name-display', phase.name);
    
    // Set matching Hijri date in Astronomy card
    if (data.hijriDate) {
      const matchOnlyHijri = data.hijriDate.match(/(الموافق لـ|corresponding to) (.*)/i);
      if (matchOnlyHijri && matchOnlyHijri[2]) {
        safeSetText('hijri-date-display', matchOnlyHijri[2]);
      } else {
        safeSetText('hijri-date-display', data.hijriDate);
      }
    }
  } catch (err) {
    console.error("[Popup] Moon Phase calculation crashed:", err);
  }

  console.log("[Popup] updateTimesAndChecklistUI finished");
}

// Custom Confirmation Modal Overlay System
function showConfirmModal(message, onConfirm) {
  const lang = document.getElementById('lang').value;
  const modal = document.getElementById('confirm-modal');
  const msgEl = document.getElementById('modal-message');
  const confirmBtn = document.getElementById('modal-confirm');
  const cancelBtn = document.getElementById('modal-cancel');

  confirmBtn.innerText = I18N[lang].confirm;
  cancelBtn.innerText = I18N[lang].cancel;
  msgEl.innerText = message;
  modal.style.display = 'flex';

  const cleanUp = () => {
    modal.style.display = 'none';
    confirmBtn.replaceWith(confirmBtn.cloneNode(true));
    cancelBtn.replaceWith(cancelBtn.cloneNode(true));
  };

  document.getElementById('modal-confirm').addEventListener('click', () => {
    onConfirm();
    cleanUp();
  });

  document.getElementById('modal-cancel').addEventListener('click', cleanUp);
}

// Authentic Ahadith Fetcher from Mawaqit's Public API
async function fetchAuthenticHadith(lang) {
  const hadithBox = document.getElementById('hadith-box');
  const textEl = document.getElementById('hadith-text');
  const sourceEl = document.getElementById('hadith-source');
  
  try {
    const response = await fetch(`https://mawaqit.net/api/2.0/hadith/random?lang=${lang}`);
    if (!response.ok) throw new Error("Hadith API returned non-200");
    const parsed = await response.json();
    
    if (parsed && parsed.text) {
      textEl.innerText = `"${parsed.text}"`;
      sourceEl.innerText = parsed.source || "";
      hadithBox.style.display = 'block';
      console.log("[Hadith] Authentic Hadith fetched successfully.");
    } else {
      hadithBox.style.display = 'none';
    }
  } catch (err) {
    console.error("[Hadith] Failed to fetch authentic Hadith from Mawaqit API. Hiding hadith display.", err.message);
    hadithBox.style.display = 'none'; 
  }
}

// Live real-time ticking clock countdown system (incorporating dynamic Offsets & Elapsed Progress)
function startCountdownTicking(data) {
  if (countdownInterval) clearInterval(countdownInterval);
  
  const tick = () => {
    const lang = document.getElementById('lang').value;
    const dict = I18N[lang];
    const clockEl = document.getElementById('countdown-clock');
    const labelEl = document.getElementById('countdown-label');

    if (!data.prayerTimes || data.prayerTimes.length !== 5) {
      clockEl.innerText = "--h --m --s";
      return;
    }

    const tz = data.mosqueTimezone || "Africa/Algiers";
    const now = new Date();

    // Calculate current time in mosque's specific timezone
    const tzFormatter = new Intl.DateTimeFormat('en-US', { timeZone: tz, hour: 'numeric', minute: 'numeric', second: 'numeric', hour12: false });
    const tzParts = tzFormatter.formatToParts(now);
    const currentHour = parseInt(tzParts.find(p => p.type === 'hour').value, 10);
    const currentMinute = parseInt(tzParts.find(p => p.type === 'minute').value, 10);
    const currentSecond = parseInt(tzParts.find(p => p.type === 'second').value, 10);
    
    const currentTotalSecs = currentHour * 3600 + currentMinute * 60 + currentSecond;

    // Apply offsets dynamically inside calculations
    const offsets = data.prayerOffsets || { Fajr: 0, Dhuhr: 0, Asr: 0, Maghrib: 0, Isha: 0 };
    const adjustedTimes = PRAYERS.map((p, idx) => getOffsetAdjustedTimeStr(data.prayerTimes[idx], parseInt(offsets[p] || 0, 10)));

    let activeIqamaPrayerIdx = -1;
    let activeIqamaSecsLeft = -1;
    let nextPrayerIdx = -1;
    let nextPrayerSecsLeft = Infinity;

    const [fajrHour, fajrMin] = adjustedTimes[0].split(':').map(Number);
    const fajrTotalSecs = (fajrHour * 60 + fajrMin) * 60;

    for (let i = 0; i < 5; i++) {
      const pTime = adjustedTimes[i];
      const waitMins = parseInt(data.iqamaWaits ? data.iqamaWaits[i] : 10, 10);
      const [pHour, pMin] = pTime.split(':').map(Number);
      const pTotalSecs = (pHour * 60 + pMin) * 60;
      const iTotalSecs = pTotalSecs + waitMins * 60;

      // Evaluation #1: Check if Iqama is active (FIXED: iTotalMins renamed to iTotalSecs to prevent ReferenceErrors)
      if (currentTotalSecs >= pTotalSecs && currentTotalSecs < iTotalSecs) {
        activeIqamaPrayerIdx = i;
        activeIqamaSecsLeft = iTotalSecs - currentTotalSecs;
      }

      // Evaluation #2: Next prayer
      let diffSecs = pTotalSecs - currentTotalSecs;
      if (diffSecs <= 0) {
        if (i === 4) { // Isha passed, evaluation tomorrow's Fajr
          const nextFajrSecs = ((fajrHour + 24) * 60 + fajrMin) * 60;
          const fajrDiff = nextFajrSecs - currentTotalSecs;
          if (fajrDiff < nextPrayerSecsLeft) {
            nextPrayerSecsLeft = fajrDiff;
            nextPrayerIdx = 0;
          }
        }
      } else if (diffSecs < nextPrayerSecsLeft) {
        nextPrayerSecsLeft = diffSecs;
        nextPrayerIdx = i;
      }
    }

    const formatSecs = (totalS) => {
      const h = String(Math.floor(totalS / 3600)).padStart(2, '0');
      const m = String(Math.floor((totalS % 3600) / 60)).padStart(2, '0');
      const s = String(totalS % 60).padStart(2, '0');
      return lang === 'ar' ? `${h}سا ${m}د ${s}ث` : `${h}h ${m}m ${s}s`;
    };

    if (activeIqamaPrayerIdx !== -1) {
      // Iqama Active
      labelEl.innerText = dict.kaabaDir; 
      labelEl.style.color = 'var(--wait)';
      clockEl.style.color = 'var(--wait)';
      
      const pName = I18N[lang][PRAYERS[activeIqamaPrayerIdx].toLowerCase()];
      labelEl.innerText = dict.iqamaActiveCountdown.replace('{name}', pName);
      clockEl.innerText = formatSecs(activeIqamaSecsLeft);
    } else if (nextPrayerIdx !== -1) {
      // Normal countdown (FIXED: Simplified countdown clock binding to prevent "{time}" literal bugs)
      labelEl.style.color = 'var(--primary)';
      clockEl.style.color = 'var(--success)';
      
      const pName = I18N[lang][PRAYERS[nextPrayerIdx].toLowerCase()];
      labelEl.innerText = dict.nextPrayerCountdown.replace('{name}', pName);
      clockEl.innerText = formatSecs(nextPrayerSecsLeft);
    }

    // --- DYNAMIC ELAPSED PRAYER INTERVAL PROGRESS ENGINE ---
    try {
      let prevSecs = 0;
      let nextSecs = 0;
      let prevName = "";
      let nextName = "";

      if (currentTotalSecs < fajrTotalSecs) {
        // Before today's Fajr: Active interval is yesterday's Isha to today's Fajr
        const [ishaH, ishaM] = adjustedTimes[4].split(':').map(Number);
        prevSecs = (ishaH - 24) * 3600 + ishaM * 60;
        nextSecs = fajrTotalSecs;
        prevName = PRAYERS[4];
        nextName = PRAYERS[0];
      } else {
        for (let i = 0; i < 5; i++) {
          const [pH, pM] = adjustedTimes[i].split(':').map(Number);
          const pSecs = (pH * 60 + pM) * 60;
          
          if (currentTotalSecs >= pSecs) {
            prevSecs = pSecs;
            prevName = PRAYERS[i];
            
            if (i === 4) {
              const [fH, fM] = adjustedTimes[0].split(':').map(Number);
              nextSecs = ((fH + 24) * 60 + fM) * 60;
              nextName = PRAYERS[0];
            } else {
              const [nH, nM] = adjustedTimes[i + 1].split(':').map(Number);
              nextSecs = (nH * 60 + nM) * 60;
              nextName = PRAYERS[i + 1];
            }
          }
        }
      }

      const totalInterval = nextSecs - prevSecs;
      const elapsedSecs = currentTotalSecs - prevSecs;
      const pct = Math.max(0, Math.min(100, (elapsedSecs / totalInterval) * 100));

      const bar = document.getElementById('progress-bar');
      if (bar) bar.style.width = `${pct}%`;
      safeSetText('progress-percent', `${Math.round(pct)}%`);
      safeSetText('progress-start', I18N[lang][prevName.toLowerCase()]);
      safeSetText('progress-end', I18N[lang][nextName.toLowerCase()]);
    } catch (e) {
      console.error("[Progress] Failed to compute interval progress:", e);
    }
  };

  tick();
  countdownInterval = setInterval(tick, 1000);
}

// --- SECURE, LIGHTWEIGHT NATIVE GRAPH CHIP ENGINE ---
async function renderCharts() {
  const theme = document.documentElement.getAttribute('data-theme') || 'light';
  const isDark = theme === 'dark';
  console.log(`[Charts] Initializing Canvas Engine. Theme: ${theme}`);
  
  const dataColor = isDark ? '#64B5F6' : '#1E88E5';
  const textColor = isDark ? '#e0e0e0' : '#333333';
  const gridColor = isDark ? '#333333' : '#eeeeee';

  const db = await chrome.storage.local.get(['tracker', 'mosqueCalendar', 'mosqueTimezone', 'prayerTimes', 'lang', 'hijriDate', 'imsakConfig', 'mosqueLat', 'mosqueLon', 'prayerOffsets', 'shuruqTime', 'qadaCommitment', 'qadaValues']);
  const tracker = db.tracker || { history: {}, today: {} };
  const history = tracker.history || {};

  const now = new Date();
  const getPastDateStr = (offset) => {
    let d = new Date(now);
    d.setDate(now.getDate() - offset);
    return d.toISOString().split('T')[0];
  };

  // --- STREAK FLAME HUB ---
  const lang = db.lang || 'ar';
  let currentStreak = 0;
  let longestStreak = 0;
  
  const isSimulatedStreak = sessionStorage.getItem('force_streak') === 'true';

  if (isSimulatedStreak) {
    currentStreak = parseInt(sessionStorage.getItem('force_streak_val') || "5", 10);
    longestStreak = Math.max(currentStreak, 12);
  } else {
    const streakData = calculateStreakData(history, tracker.today);
    currentStreak = streakData.current;
    longestStreak = streakData.longest;
  }

  const currStreakLabel = document.getElementById('curr-streak-label');
  const longStreakLabel = document.getElementById('long-streak-label');
  if (currStreakLabel) {
    currStreakLabel.innerHTML = I18N[lang].currStreakLabel.replace('{days}', `<span id="current-streak-val" style="color: #FF9800; font-size: 18px; font-weight: 800;">${currentStreak}</span>`);
  }
  if (longStreakLabel) {
    longStreakLabel.innerHTML = I18N[lang].longStreakLabel.replace('{days}', `<span id="longest-streak-val" style="font-weight: 800; color:var(--text);">${longestStreak}</span>`);
  }
  
  // Dynamic Holy Streak Flame Badge Awarding
  const badgeImg = document.getElementById('streak-badge-img');
  if (badgeImg) {
    if (currentStreak >= 90) {
      badgeImg.src = "cosmic.png"; // Cosmic Diamond
      badgeImg.style.filter = "drop-shadow(0 0 8px #9C27B0)";
    } else if (currentStreak >= 30) {
      badgeImg.src = "gold.png"; // Neon Gold
      badgeImg.style.filter = "drop-shadow(0 0 8px #FFD700)";
    } else if (currentStreak >= 7) {
      badgeImg.src = "silver.png"; // Silver
      badgeImg.style.filter = "drop-shadow(0 0 8px #C0C0C0)";
    } else if (currentStreak >= 3) {
      badgeImg.src = "bronze.png"; // Bronze
      badgeImg.style.filter = "drop-shadow(0 0 8px #CD7F32)";
    }  else {
      badgeImg.src = "none.png"; // No active streak
      badgeImg.style.filter = "none";
    }
  }

  // --- 1. PLOT 4 PROGRESS GAUGES (Day, Week, Month, Year) ---
  const renderGaugeRing = (canvasId, ratio, labelText) => {
    const canvas = document.getElementById(canvasId);
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, 70, 70);

    // Draw background track
    ctx.beginPath();
    ctx.arc(35, 35, 28, 0, 2 * Math.PI);
    ctx.strokeStyle = gridColor;
    ctx.lineWidth = 6;
    ctx.stroke();

    // Draw active progress
    ctx.beginPath();
    ctx.arc(35, 35, 28, -0.5 * Math.PI, (-0.5 + 2 * ratio) * Math.PI);
    ctx.strokeStyle = '#4CAF50';
    ctx.lineWidth = 6;
    ctx.lineCap = 'round';
    ctx.stroke();

    // Inner Text value
    ctx.fillStyle = textColor;
    ctx.font = 'bold 12px Segoe UI';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    ctx.fillText(labelText, 35, 35);
  };

  // Day calculations
  let dayPrayed = 0;
  PRAYERS.forEach(p => { if (tracker.today && tracker.today[p]) dayPrayed++; });
  renderGaugeRing('ring-day', dayPrayed / 5, `${dayPrayed}/5`);

  // Week calculations
  let weekTotal = 0, weekPrayed = 0;
  for (let i = 0; i < 7; i++) {
    const dStr = getPastDateStr(i);
    const dayData = (i === 0) ? tracker.today : history[dStr];
    if (dayData) {
      PRAYERS.forEach(p => {
        weekTotal++;
        if (dayData[p]) weekPrayed++;
      });
    }
  }
  renderGaugeRing('ring-week', weekTotal > 0 ? (weekPrayed / weekTotal) : 0, `${Math.round((weekTotal > 0 ? (weekPrayed / weekTotal) : 0) * 100)}%`);

  // Month calculations
  let monthTotal = 0, monthPrayed = 0;
  for (let i = 0; i < 30; i++) {
    const dStr = getPastDateStr(i);
    const dayData = (i === 0) ? tracker.today : history[dStr];
    if (dayData) {
      PRAYERS.forEach(p => {
        monthTotal++;
        if (dayData[p]) monthPrayed++;
      });
    }
  }
  renderGaugeRing('ring-month', monthTotal > 0 ? (monthPrayed / monthTotal) : 0, `${Math.round((monthTotal > 0 ? (monthPrayed / monthTotal) : 0) * 100)}%`);

  // Year calculations (Rolling 365 Days)
  let yearTotal = 0, yearPrayed = 0;
  for (let i = 0; i < 365; i++) {
    const dStr = getPastDateStr(i);
    const dayData = (i === 0) ? tracker.today : history[dStr];
    if (dayData) {
      PRAYERS.forEach(p => {
        yearTotal++;
        if (dayData[p]) yearPrayed++;
      });
    }
  }
  renderGaugeRing('ring-year', yearTotal > 0 ? (yearPrayed / yearTotal) : 0, `${Math.round((yearTotal > 0 ? (yearPrayed / yearTotal) : 0) * 100)}%`);


  // --- 2. RENDER WEEKLY BAR CHART (Rolling 7 days) ---
  const weekCanvas = document.getElementById('weekly-bar-chart');
  if (weekCanvas) {
    try {
      const ctx = weekCanvas.getContext('2d');
      ctx.clearRect(0, 0, 300, 90);
      const barWidth = 25;
      const spacing = 15;
      const startX = 15;
      for (let i = 6; i >= 0; i--) {
        const dStr = getPastDateStr(i);
        const dayData = (i === 0) ? tracker.today : history[dStr];
        let prayCount = 0;
        
        // Dynamic Streak test injector
        if (isSimulatedStreak) {
          prayCount = (i % 2 === 0) ? 5 : 3;
        } else {
          if (dayData) {
            PRAYERS.forEach(p => { if (dayData[p]) prayCount++; });
          }
        }

        const idx = 6 - i;
        const x = startX + idx * (barWidth + spacing);
        const height = (prayCount / 5) * 60;
        const y = 70 - height;

        ctx.fillStyle = dataColor;
        ctx.fillRect(x, y, barWidth, height);

        // STREAK GOLD GLOW PORTAL
        if (prayCount === 5) {
          ctx.shadowColor = '#FF9800';
          ctx.shadowBlur = 8;
          ctx.strokeStyle = '#FF9800';
          ctx.lineWidth = 1.5;
          ctx.strokeRect(x, y, barWidth, height);
          ctx.shadowBlur = 0; // reset
        }

        ctx.fillStyle = textColor;
        ctx.font = '9px Segoe UI';
        const labelStr = dStr.substring(8, 10);
        ctx.fillText(labelStr, x + 6, 85);
      }
    } catch (e) { console.error("[Charts] Drawing weekly bar chart failed:", e); }
  }

  // --- 3. RENDER MONTHLY BAR CHART (Rolling 30 days) ---
  const monthCanvas = document.getElementById('monthly-bar-chart');
  if (monthCanvas) {
    try {
      const ctx = monthCanvas.getContext('2d');
      ctx.clearRect(0, 0, 300, 90);
      const barWidth = 6;
      const spacing = 3;
      const startX = 15;
      for (let i = 29; i >= 0; i--) {
        const dStr = getPastDateStr(i);
        const dayData = (i === 0) ? tracker.today : history[dStr];
        let prayCount = 0;
        
        if (isSimulatedStreak) {
          prayCount = (i % 3 === 0) ? 5 : 4;
        } else {
          if (dayData) {
            PRAYERS.forEach(p => { if (dayData[p]) prayCount++; });
          }
        }

        const idx = 29 - i;
        const x = startX + idx * (barWidth + spacing);
        const height = (prayCount / 5) * 60;
        const y = 70 - height;

        ctx.fillStyle = '#4CAF50';
        ctx.fillRect(x, y, barWidth, height);

        // STREAK GOLD GLOW PORTAL
        if (prayCount === 5) {
          ctx.shadowColor = '#FF9800';
          ctx.shadowBlur = 8;
          ctx.strokeStyle = '#FF9800';
          ctx.lineWidth = 1;
          ctx.strokeRect(x, y, barWidth, height);
          ctx.shadowBlur = 0; // reset
        }
      }
      ctx.strokeStyle = gridColor;
      ctx.beginPath();
      ctx.moveTo(10, 70);
      ctx.lineTo(290, 70);
      ctx.stroke();
    } catch (e) { console.error("[Charts] Drawing monthly bar chart failed:", e); }
  }

  // --- 4. RENDER YEARLY BAR CHART (Rolling 12 months) ---
  const yearCanvas = document.getElementById('yearly-bar-chart');
  if (yearCanvas) {
    try {
      const ctx = yearCanvas.getContext('2d');
      ctx.clearRect(0, 0, 300, 90);
      const monthlyStats = {};
      const keys = Object.keys(history);
      keys.forEach(k => {
        const mStr = k.substring(0, 7);
        if (!monthlyStats[mStr]) monthlyStats[mStr] = { total: 0, prayed: 0 };
        PRAYERS.forEach(p => {
          monthlyStats[mStr].total++;
          if (history[k][p]) monthlyStats[mStr].prayed++;
        });
      });
      const barWidth = 15;
      const spacing = 8;
      const startX = 15;
      for (let i = 11; i >= 0; i--) {
        let d = new Date(now);
        d.setMonth(now.getMonth() - i);
        const mStr = d.toISOString().substring(0, 7);
        const monthLog = monthlyStats[mStr];
        const ratio = (monthLog && monthLog.total > 0) ? (monthLog.prayed / monthLog.total) : 0;
        const idx = 11 - i;
        const x = startX + idx * (barWidth + spacing);
        const height = ratio * 60;
        const y = 70 - height;

        ctx.fillStyle = '#9C27B0';
        ctx.fillRect(x, y, barWidth, height);

        ctx.fillStyle = textColor;
        ctx.font = '8px Segoe UI';
        ctx.fillText(mStr.substring(5, 7), x + 2, 82);
      }
    } catch (e) { console.error("[Charts] Drawing yearly bar chart failed:", e); }
  }

  // --- 5. ACCUWEATHER-STYLE 7-DAY MULTI-LINE CHART (Enforces strict drawing verification on 'weekly-trend-chart') ---
  if (document.getElementById('weekly-trend-chart') && db.mosqueCalendar && db.mosqueCalendar.length === 12) {
    const tz = db.mosqueTimezone || "Africa/Algiers";
    const calendar = db.mosqueCalendar;
    
    // Evaluate if Ramadan is simulated or calculated dynamically
    const isRamadanActive = (db.hijriDate && (db.hijriDate.includes("رمضان") || db.hijriDate.includes("Ramadan"))) || sessionStorage.getItem('force_ramadan') === 'true';
    const imsakConfig = db.imsakConfig || "always";

    // Weekly Trend (7 Days)
    drawTrendChart('weekly-trend-chart', 7, calendar, tz, isDark, gridColor, textColor, isRamadanActive, imsakConfig, db.prayerOffsets);
    // Monthly Trend (30 Days)
    drawTrendChart('monthly-trend-chart', 30, calendar, tz, isDark, gridColor, textColor, isRamadanActive, imsakConfig, db.prayerOffsets);
    // Yearly Trend (12 Months Overview)
    drawTrendChart('yearly-trend-chart', 12, calendar, tz, isDark, gridColor, textColor, isRamadanActive, imsakConfig, db.prayerOffsets);

    // Render interactive spherical Qibla direction Compass (FIXED: Now fully offline!)
    try {
      const lat = db.mosqueLat;
      const lon = db.mosqueLon;
      
      if (lat !== undefined && lon !== undefined) {
        // Math equations: Great-Circle angle calculation to Mecca (Kaaba)
        const latMecca = 21.4225 * (Math.PI / 180);
        const lonMecca = 39.8262 * (Math.PI / 180);
        const latMosque = lat * (Math.PI / 180);
        const lonMosque = lon * (Math.PI / 180);

        const dLon = lonMecca - lonMosque;
        const y = Math.sin(dLon);
        const x = Math.cos(latMosque) * Math.tan(latMecca) - Math.sin(latMosque) * Math.cos(dLon);
        
        let qiblaDeg = Math.atan2(y, x) * (180 / Math.PI);
        if (qiblaDeg < 0) qiblaDeg += 360;

        const displayAngle = qiblaDeg.toFixed(1);
        safeSetText('qibla-angle-display', I18N[db.lang || 'ar'].qiblaAngle.replace('{angle}', displayAngle));

        drawQiblaCompass('qibla-compass', qiblaDeg, isDark, db.lang || 'ar');
      }
    } catch (e) {
      console.error("[Charts] Drawing Qibla Compass failed:", e);
    }

    // --- 6. PLOT CELLESTIAL "SUN IN THE SKY" ARC CHART ---
    const skyCanvas = document.getElementById('sun-sky-chart');
    if (skyCanvas && db.prayerTimes && db.shuruqTime) {
      try {
        const ctx = skyCanvas.getContext('2d');
		ctx.clearRect(0, 0, 300, 100);

        // Fetch local adjusted Fajr & Maghrib (Sunset) times
        const offsets = db.prayerOffsets || { Fajr: 0, Dhuhr: 0, Asr: 0, Maghrib: 0, Isha: 0 };
        const adjFajr = getOffsetAdjustedTimeStr(db.prayerTimes[0], parseInt(offsets.Fajr || 0, 10));
        const adjMaghrib = getOffsetAdjustedTimeStr(db.prayerTimes[3], parseInt(offsets.Maghrib || 0, 10));
        const adjShuruq = getOffsetAdjustedTimeStr(db.shuruqTime, 0); // Sunrise does not typically have offset
        const adjDhuhr = getOffsetAdjustedTimeStr(db.prayerTimes[1], parseInt(offsets.Dhuhr || 0, 10));
        const adjIsha = getOffsetAdjustedTimeStr(db.prayerTimes[4], parseInt(offsets.Isha || 0, 10));

        const toMins = (hM) => {
          const [h, m] = hM.split(':').map(Number);
          return h * 60 + m;
        };

        const fMins = toMins(adjFajr);
        const sMins = toMins(adjShuruq);
        const dMins = toMins(adjDhuhr);
        const mMins = toMins(adjMaghrib);
        const iMins = toMins(adjIsha);

        // Fetch current localized timezone time
        const tzFormatter = new Intl.DateTimeFormat('en-US', { timeZone: tz, hour: 'numeric', minute: 'numeric', second: 'numeric', hour12: false });
        const tzParts = tzFormatter.formatToParts(now);
        const currentHour = parseInt(tzParts.find(p => p.type === 'hour').value, 10);
        const currentMinute = parseInt(tzParts.find(p => p.type === 'minute').value, 10);
        const currentTotalMins = currentHour * 60 + currentMinute;

        // Draw Sky Arc Path (Fajr/Sunrise to Maghrib/Sunset)
        // Draw Ground horizon baseline (Solid theme color matching your mockup)
        ctx.strokeStyle = isDark ? '#333333' : '#e0e0e0';
		ctx.lineWidth = 3;
		ctx.beginPath();
		ctx.moveTo(15, 80);
		ctx.lineTo(285, 80);
		ctx.stroke();

        // Define the ellipse dimensions so they match the line and the sun tracking perfectly
		const radiusX = 100; // Wider (increased from 40)
		const radiusY = 50;  // Shorter (decreased from 40)
		
		ctx.strokeStyle = isDark ? '#424242' : '#bdbdbd';
		ctx.lineWidth = 3;
		ctx.beginPath();
		// Replaced ctx.arc with ctx.ellipse (x, y, radiusX, radiusY, rotation, startAngle, endAngle)
		ctx.ellipse(150, 80, radiusX, radiusY, 0, Math.PI, 2 * Math.PI);
		ctx.stroke();
		
		let sunX = 150;
		let sunY = 80;
		let isDaytime = false;

        // Draw Sky Arc Path (Solid, smooth sweeping curve)
        if (currentTotalMins >= sMins && currentTotalMins < mMins) {
          isDaytime = true;
          let progress = 0;
          let angleRad = Math.PI;
        
          if (currentTotalMins < dMins) {
            progress = (currentTotalMins - sMins) / (dMins - sMins);
            angleRad = Math.PI + progress * (Math.PI / 2); 
          } else {
            progress = (currentTotalMins - dMins) / (mMins - dMins);
            angleRad = 1.5 * Math.PI + progress * (Math.PI / 2); 
          }
        
          // Updated tracking math to use the ellipse's non-uniform radii
          sunX = 150 + radiusX * Math.cos(angleRad);
          sunY = 80 + radiusY * Math.sin(angleRad);
        } else {
          isDaytime = false;
          let progress = 0;
          let angleRad = Math.PI;
        
          if (currentTotalMins >= iMins || currentTotalMins < fMins) {
            const nightLength = (1440 - iMins) + fMins;
            const elapsed = currentTotalMins >= iMins ? (currentTotalMins - iMins) : ((1440 - iMins) + currentTotalMins);
            progress = elapsed / nightLength;
          } else if (currentTotalMins >= mMins && currentTotalMins < iMins) {
            progress = (currentTotalMins - mMins) / (iMins - mMins);
          } else if (currentTotalMins >= fMins && currentTotalMins < sMins) {
            progress = (currentTotalMins - fMins) / (sMins - fMins);
          }
        
          angleRad = Math.PI + progress * Math.PI;
          
          // Updated tracking math to use the ellipse's non-uniform radii
          sunX = 150 + radiusX * Math.cos(angleRad);
          sunY = 80 + radiusY * Math.sin(angleRad);
        }

        // Draw beautiful hollow slider node (Matches your custom hand-drawn sketch)
		ctx.fillStyle = isDark ? '#1e1e1e' : '#ffffff'; // Matches card bg color
		ctx.strokeStyle = isDaytime ? '#FFD700' : '#80DEEA'; // Gold glow for sun, blue for moon
		ctx.lineWidth = 3.5;
		
		ctx.shadowColor = isDaytime ? '#FFD700' : '#80DEEA';
		ctx.shadowBlur = 10;
		
		ctx.beginPath();
		ctx.arc(sunX, sunY, 7, 0, 2 * Math.PI); // Keeps the sun node itself as a perfect little circle
		ctx.fill();
		ctx.stroke();
		
		ctx.shadowBlur = 0; // reset

        // Label Dawn/Sunrise & Sunset at the corners of the horizon
        ctx.fillStyle = textColor;
        ctx.font = '8px Segoe UI';
        ctx.textAlign = 'left';
        ctx.fillText(lang === 'ar' ? 'الشروق' : 'Sunrise', 22, 98);
        ctx.textAlign = 'right';
        ctx.fillText(lang === 'ar' ? 'الغروب' : 'Sunset', 278, 98);

      } catch (err) {
        console.error("[Charts] Drawing celestial sky path failed:", err);
      }
    }

    // --- 7. DRAW REVAMPED QADA' LEDGER CARD ---
    try {
      const qadaValues = db.qadaValues || { Fajr: 0, Dhuhr: 0, Asr: 0, Maghrib: 0, Isha: 0 };
      const qadaCommitment = db.qadaCommitment || { Fajr: 0, Dhuhr: 0, Asr: 0, Maghrib: 0, Isha: 0 };
      
      const ledgerContainer = document.getElementById('ledger-container');
      const assistantContent = document.getElementById('qada-assistant-content');
      const reminderCard = document.getElementById('qada-reminder-card');
      const reminderCounts = document.getElementById('qada-reminder-counts');

      if (ledgerContainer && assistantContent) {
        ledgerContainer.innerHTML = ""; // reset ledger
        
        const missedLedger = { Fajr: [], Dhuhr: [], Asr: [], Maghrib: [], Isha: [] };
        let totalHistoricalDebt = 0;

        // Scan chronological history for unprayed (false) days
        const sortedHistoryDates = Object.keys(history).sort();
        sortedHistoryDates.forEach(dateStr => {
          const dayLog = history[dateStr];
          PRAYERS.forEach(p => {
            if (dayLog[p] === false) {
              missedLedger[p].push(dateStr);
              totalHistoricalDebt++;
            }
          });
        });

        const totalManualDebt = Object.values(qadaValues).reduce((a, b) => a + b, 0);
        const grandTotalDebt = totalHistoricalDebt + totalManualDebt;

        console.log(`[Qada Ledger] Total Historical Missed: ${totalHistoricalDebt} | Total Manual Backlog: ${totalManualDebt}`);

        if (grandTotalDebt === 0) {
          // Safe Empty state rendering [1]
          ledgerContainer.innerHTML = `<div style="text-align:center; padding:15px; font-weight:bold; color:var(--success); font-size:12px;">${I18N[lang].qadaAssistantEmpty}</div>`;
          assistantContent.innerHTML = `<div style="text-align:center; font-weight:bold; color:var(--success);">${I18N[lang].qadaAssistantEmpty}</div>`;
          if (reminderCard) reminderCard.style.display = 'none';
        } else {
          // Setup active ledger rows
          let ledgerHtml = "";
          PRAYERS.forEach(p => {
            const missedDays = missedLedger[p];
            if (missedDays.length > 0) {
              const pNameTranslated = I18N[lang][p.toLowerCase()] || p;
              ledgerHtml += `<div style="font-weight:bold; color:var(--primary); margin:8px 0 4px 0; font-size:12px; border-bottom:1.5px solid var(--border); padding-bottom:2px;">${pNameTranslated} (${missedDays.length})</div>`;
              
              missedDays.forEach(dateStr => {
                const formattedDate = dateStr.split('-').reverse().join('/'); // DD/MM/YYYY
                ledgerHtml += `
                  <div class="qada-row" style="padding:6px 0;">
                    <span style="font-size:12px; font-weight:600;">${pNameTranslated} - ${formattedDate}</span>
                    <input type="checkbox" class="qada-ledger-log-check" data-date="${dateStr}" data-prayer="${p}" style="transform:scale(1.2); cursor:pointer;">
                  </div>
                `;
              });
            }
          });
          ledgerContainer.innerHTML = ledgerHtml;

          // Attach click listeners to ledger checkboxes [1]
          document.querySelectorAll('.qada-ledger-log-check').forEach(chk => {
            chk.addEventListener('change', async (e) => {
              const pKey = e.target.dataset.prayer;
              const dStr = e.target.dataset.date;
              console.log(`[Qada Ledger] Logging make-up prayer -> ${pKey} on ${dStr}`);
              
              let currentDb = await chrome.storage.local.get(['tracker']);
              let tracker = currentDb.tracker || { history: {} };
              if (!tracker.history) tracker.history = {};
              
              // Retroactively set this prayer to true (Alhamdulillah, prayed!) [1]
              if (tracker.history[dStr]) {
                tracker.history[dStr][pKey] = true;
              }
              await chrome.storage.local.set({ tracker });
              
              // Refresh graphs, progress and ledger list instantly [1]
              const freshData = await chrome.storage.local.get(null);
              updateTimesAndChecklistUI(freshData);
              renderCharts();
            });
          });

          // Setup Qada' Completion Assistant (المنقذ المساعد) projections [1]
          let assistantHtml = "";
          PRAYERS.forEach(p => {
            const commitRate = parseInt(qadaCommitment[p] || 0, 10);
            const prayerDebt = (qadaValues[p] || 0) + missedLedger[p].length;
            const pNameTranslated = I18N[lang][p.toLowerCase()] || p;

            if (prayerDebt > 0) {
              if (commitRate > 0) {
                const daysLeft = Math.ceil(prayerDebt / commitRate);
                let completionDate = new Date();
                completionDate.setDate(completionDate.getDate() + daysLeft);
                
                const dateOptions = { month: 'short', day: 'numeric', year: 'numeric' };
                const formattedCompletionDate = completionDate.toLocaleDateString(lang === 'ar' ? 'ar' : 'en', dateOptions);
                
                let lineText = I18N[lang].qadaLine
                  .replace('{name}', pNameTranslated)
                  .replace('{debt}', prayerDebt)
                  .replace('{commit}', commitRate)
                  .replace('{date}', formattedCompletionDate);
                assistantHtml += `<div style="margin-bottom:6px; font-weight:bold;">${lineText}</div>`;
              } else {
                let lineText = I18N[lang].qadaNoCommit
                  .replace('{name}', pNameTranslated)
                  .replace('{debt}', prayerDebt);
                assistantHtml += `<div style="margin-bottom:6px; font-weight:bold; opacity:0.6;">${lineText}</div>`;
              }
            }
          });
          assistantContent.innerHTML = assistantHtml;

          // Setup Home Tab Qada' Quick Reminder Card
          if (reminderCard && reminderCounts) {
            reminderCard.style.display = 'block';
            let reminderHtml = "";
            PRAYERS.forEach(p => {
              const totalP = (qadaValues[p] || 0) + missedLedger[p].length;
              if (totalP > 0) {
                const pNameShort = I18N[lang][p.toLowerCase()].substring(0, 4);
                reminderHtml += `<span style="margin: 0 4px;">${pNameShort}: <span style="color:var(--danger);">${totalP}</span></span>`;
              }
            });
            reminderCounts.innerHTML = reminderHtml;
          }
        }
      }
    } catch (qadaErr) {
      console.error("[Charts] Drawing Qada' tab metrics failed:", qadaErr);
    }
  }
}

// Reusable AccuWeather-Style Multi-Line Chart Builder with Hover tooltips & Strict Left Y-Axis Alignment
function drawTrendChart(canvasId, totalPoints, calendar, tz, isDark, gridColor, textColor, isRamadanActive, imsakConfig, userOffsets) {
  const canvas = document.getElementById(canvasId);
  if (!canvas) return;
  const ctx = canvas.getContext('2d');
  ctx.clearRect(0, 0, 300, 150);

  const plotPoints = { Fajr: [], Dhuhr: [], Asr: [], Maghrib: [], Isha: [], Imsak: [] };
  const dayLabels = [];

  const now = new Date();
  const offsets = userOffsets || { Fajr: 0, Dhuhr: 0, Asr: 0, Maghrib: 0, Isha: 0 };

  const getOffsetVal = (prayerKey) => parseInt(offsets[prayerKey] || 0, 10);

  // Extract date-specific points
  if (totalPoints === 12) {
    // Yearly Trend: Plot 1st day of each month
    for (let i = 11; i >= 0; i--) {
      let targetDate = new Date(now);
      targetDate.setMonth(now.getMonth() - i);
      targetDate.setDate(1); // 1st day

      const formatter = new Intl.DateTimeFormat('en-US', { timeZone: tz, month: 'numeric' });
      const targetMonth = parseInt(formatter.format(targetDate), 10);
      const targetDay = 1;

      dayLabels.push(`${targetMonth}/${targetDay}`);

      const monthData = calendar[targetMonth - 1];
      if (monthData) {
        const dayTimes = monthData[targetDay.toString()];
        if (dayTimes && dayTimes.length === 6) {
          const toAdjustedMins = (hM, prayerKey) => {
            const [h, m] = hM.split(':').map(Number);
            let total = h * 60 + m + getOffsetVal(prayerKey);
            if (total < 0) total += 1440;
            if (total >= 1440) total -= 1440;
            return total;
          };
          plotPoints.Fajr.push(toAdjustedMins(dayTimes[0], 'Fajr'));
          plotPoints.Dhuhr.push(toAdjustedMins(dayTimes[2], 'Dhuhr'));
          plotPoints.Asr.push(toAdjustedMins(dayTimes[3], 'Asr'));
          plotPoints.Maghrib.push(toAdjustedMins(dayTimes[4], 'Maghrib'));
          plotPoints.Isha.push(toAdjustedMins(dayTimes[5], 'Isha'));
          
          // Imsak calculation (Fajr - 10m)
          plotPoints.Imsak.push(toAdjustedMins(dayTimes[0], 'Fajr') - 10);
        }
      }
    }
  } else {
    // Weekly (7) / Monthly (30) Trend
    for (let i = 0; i < totalPoints; i++) {
      const targetDate = new Date(now);
      targetDate.setDate(now.getDate() + i);

      const formatter = new Intl.DateTimeFormat('en-US', { timeZone: tz, month: 'numeric', day: 'numeric' });
      const parts = formatter.formatToParts(targetDate);
      const targetMonth = parseInt(parts.find(p => p.type === 'month').value, 10);
      const targetDay = parseInt(parts.find(p => p.type === 'day').value, 10);

      // Only label every 5th day for monthly to keep clean
      if (totalPoints === 30) {
        if (i % 5 === 0) dayLabels.push(`${targetMonth}/${targetDay}`);
        else dayLabels.push("");
      } else {
        dayLabels.push(`${targetMonth}/${targetDay}`);
      }

      const monthData = calendar[targetMonth - 1];
      if (monthData) {
        const dayTimes = monthData[targetDay.toString()];
        if (dayTimes && dayTimes.length === 6) {
          const toAdjustedMins = (hM, prayerKey) => {
            const [h, m] = hM.split(':').map(Number);
            let total = h * 60 + m + getOffsetVal(prayerKey);
            if (total < 0) total += 1440;
            if (total >= 1440) total -= 1440;
            return total;
          };
          plotPoints.Fajr.push(toAdjustedMins(dayTimes[0], 'Fajr'));
          plotPoints.Dhuhr.push(toAdjustedMins(dayTimes[2], 'Dhuhr'));
          plotPoints.Asr.push(toAdjustedMins(dayTimes[3], 'Asr'));
          plotPoints.Maghrib.push(toAdjustedMins(dayTimes[4], 'Maghrib'));
          plotPoints.Isha.push(toAdjustedMins(dayTimes[5], 'Isha'));
          plotPoints.Imsak.push(toAdjustedMins(dayTimes[0], 'Fajr') - 10);
        }
      }
    }
  }

  // Ensure arrays are filled
  if (plotPoints.Fajr.length < totalPoints) return;

  const allVals = [...plotPoints.Fajr, ...plotPoints.Dhuhr, ...plotPoints.Asr, ...plotPoints.Maghrib, ...plotPoints.Isha];
  const minVal = Math.min(...allVals) - 20; 
  const maxVal = Math.max(...allVals) + 20;
  const valRange = maxVal - minVal;

  const scaleY = (val) => 130 - ((val - minVal) / valRange) * 110; 

  const colors = { Fajr: '#E53935', Dhuhr: '#F57C00', Asr: '#FBC02D', Maghrib: '#4CAF50', Isha: '#1E88E5', Imsak: '#777777' };
  const startX = 35;
  const spacingX = (300 - startX - 10) / (totalPoints - 1);

  // Draw Horizontal Grid lines
  ctx.strokeStyle = gridColor;
  ctx.lineWidth = 1;
  for (let g = 0; g <= 4; g++) {
    const yVal = minVal + (valRange / 4) * g;
    const yPos = scaleY(yVal);
    ctx.beginPath();
    ctx.moveTo(startX, yPos);
    ctx.lineTo(290, yPos);
    ctx.stroke();

    // STRICT LEFT ALIGNMENT: Enforce ctx.textAlign = 'left' to prevent Arabic clipping
    ctx.fillStyle = textColor;
    ctx.font = '8px Segoe UI';
    ctx.textAlign = 'left';
    ctx.textBaseline = 'middle';
    
    const hrs = String(Math.floor(yVal / 60)).padStart(2, '0');
    const mns = String(Math.round(yVal % 60)).padStart(2, '0');
    ctx.fillText(`${hrs}:${mns}`, 2, yPos);
  }

  let hoveredNode = null;
  const mouse = activeHovers[canvasId];

  // Draw Lines
  Object.keys(plotPoints).forEach(pKey => {
    const isRamadan = (isRamadanActive || sessionStorage.getItem('force_ramadan') === 'true');
    if (pKey === 'Imsak') {
      if (imsakConfig === 'ramadan_only' && !isRamadan) return;
    }

    const pts = plotPoints[pKey];
    ctx.strokeStyle = colors[pKey];
    ctx.lineWidth = pKey === 'Imsak' ? 1.5 : 2.5;
    if (pKey === 'Imsak') ctx.setLineDash([4, 4]); 
    else ctx.setLineDash([]);

    ctx.beginPath();
    ctx.moveTo(startX, scaleY(pts[0]));
    for (let i = 1; i < totalPoints; i++) {
      ctx.lineTo(startX + i * spacingX, scaleY(pts[i]));
    }
    ctx.stroke();

    // Draw nodes and evaluate proximity hovers
    ctx.fillStyle = colors[pKey];
    for (let i = 0; i < totalPoints; i++) {
      const nodeX = startX + i * spacingX;
      const nodeY = scaleY(pts[i]);

      ctx.beginPath();
      ctx.arc(nodeX, nodeY, 3, 0, 2 * Math.PI);
      ctx.fill();

      // Proximity check
      if (mouse) {
        const dist = Math.sqrt((mouse.x - nodeX) ** 2 + (mouse.y - nodeY) ** 2);
        if (dist < 6) {
          hoveredNode = { x: nodeX, y: nodeY, value: pts[i], label: pKey };
        }
      }
    }
  });

  // Plot X-Axis labels
  ctx.setLineDash([]);
  ctx.fillStyle = textColor;
  ctx.font = '8px Segoe UI';
  ctx.textAlign = 'center';
  for (let i = 0; i < totalPoints; i++) {
    if (dayLabels[i]) {
      ctx.fillText(dayLabels[i], startX + i * spacingX, 145);
    }
  }

  // Draw interactive Tooltip if mouse is near a point (FIXED: Natively localized tooltip labels)
  const lang = document.getElementById('lang').value;
  if (hoveredNode) {
    const localizedLabel = I18N[lang][hoveredNode.label.toLowerCase()] || hoveredNode.label;
    const tooltipText = `${localizedLabel}: ${String(Math.floor(hoveredNode.value / 60)).padStart(2, '0')}:${String(Math.round(hoveredNode.value % 60)).padStart(2, '0')}`;
    ctx.font = 'bold 9px Segoe UI';
    const textWidth = ctx.measureText(tooltipText).width;

    let tipX = hoveredNode.x - textWidth / 2 - 5;
    let tipY = hoveredNode.y - 25;

    if (tipX < 5) tipX = 5;
    if (tipX + textWidth + 10 > 300) tipX = 300 - textWidth - 12;

    ctx.fillStyle = 'rgba(0, 0, 0, 0.8)';
    ctx.fillRect(tipX, tipY, textWidth + 10, 16);
    ctx.strokeStyle = '#FFFFFF';
    ctx.lineWidth = 0.5;
    ctx.strokeRect(tipX, tipY, textWidth + 10, 16);

    ctx.fillStyle = '#FFFFFF';
    ctx.textAlign = 'left';
    ctx.textBaseline = 'top';
    ctx.fillText(tooltipText, tipX + 5, tipY + 3);
  }
}

// Drawing Spherical Compass dial onto Canvas
function drawQiblaCompass(canvasId, qiblaAngle, isDark, lang) {
  const canvas = document.getElementById(canvasId);
  if (!canvas) return;
  const ctx = canvas.getContext('2d');
  ctx.clearRect(0, 0, 120, 120);

  const cx = 60;
  const cy = 60;
  const r = 50;

  // Draw dial ring
  ctx.strokeStyle = isDark ? '#424242' : '#e0e0e0';
  ctx.lineWidth = 4;
  ctx.beginPath();
  ctx.arc(cx, cy, r, 0, 2 * Math.PI);
  ctx.stroke();

  // Draw Cardinal directions (North is aligned directly to top)
  ctx.fillStyle = isDark ? '#aaaaaa' : '#555555';
  ctx.font = 'bold 10px Segoe UI';
  ctx.textAlign = 'center';
  ctx.textBaseline = 'middle';
  ctx.fillText(lang === 'ar' ? 'ش' : 'N', cx, cy - r + 10);
  ctx.fillText(lang === 'ar' ? 'ج' : 'S', cx, cy + r - 10);
  ctx.fillText(lang === 'ar' ? 'ق' : 'E', cx + r - 10, cy);
  ctx.fillText(lang === 'ar' ? 'غ' : 'W', cx - r + 10, cy);

  // Draw arrow indicating Qibla direction
  const angleRad = (qiblaAngle - 90) * (Math.PI / 180);
  const arrowX = cx + (r - 12) * Math.cos(angleRad);
  const arrowY = cy + (r - 12) * Math.sin(angleRad);

  ctx.strokeStyle = '#4CAF50'; // Islamic Green
  ctx.lineWidth = 3;
  ctx.beginPath();
  ctx.moveTo(cx, cy);
  ctx.lineTo(arrowX, arrowY);
  ctx.stroke();

  ctx.fillStyle = '#4CAF50';
  ctx.beginPath();
  ctx.arc(arrowX, arrowY, 5, 0, 2 * Math.PI);
  ctx.fill();

  // Center pin
  ctx.fillStyle = isDark ? '#ffffff' : '#333333';
  ctx.beginPath();
  ctx.arc(cx, cy, 4, 0, 2 * Math.PI);
  ctx.fill();
}

// Helper to export local storage tracker historical database to CSV cleanly
async function exportTrackerDatabaseToCSV() {
  const db = await chrome.storage.local.get(['tracker']);
  const tracker = db.tracker || { history: {} };
  const history = tracker.history || {};

  const dates = Object.keys(history).sort();
  if (dates.length === 0) {
    console.warn("[Export] Aborted: No historical tracking logs found.");
    return;
  }

  // Construct CSV String
  let csvContent = "data:text/csv;charset=utf-8,";
  csvContent += "Date,Fajr,Dhuhr,Asr,Maghrib,Isha\n";

  dates.forEach(dateStr => {
    const day = history[dateStr];
    const f = day.Fajr ? "Prayed" : "Missed";
    const d = day.Dhuhr ? "Prayed" : "Missed";
    const a = day.Asr ? "Prayed" : "Missed";
    const m = day.Maghrib ? "Prayed" : "Missed";
    const i = day.Isha ? "Prayed" : "Missed";
    csvContent += `${dateStr},${f},${d},${a},${m},${i}\n`;
  });

  // Safe programmatic downloader anchor
  const encodedUri = encodeURI(csvContent);
  const link = document.createElement("a");
  link.setAttribute("href", encodedUri);
  link.setAttribute("download", `Hayya_Al_Salah_Tracker_Logs_${new Date().toISOString().split('T')[0]}.csv`);
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  console.log("[Export] Complete. CSV File Download initiated.");
}

// Dynamic Ramadan Day Simulator Helper
async function simulateRamadanDay(day) {
  sessionStorage.setItem('force_ramadan', 'true');
  sessionStorage.removeItem('force_eid_fitr');
  sessionStorage.removeItem('force_eid_adha');
  await chrome.storage.local.set({ ramadanDay: day });
  const d = await chrome.storage.local.get(null);
  updateTimesAndChecklistUI(d);
  console.log(`[Test] Simulating Ramadan calendar. Activated Day: ${day}`);
}


// Defensive UI listener binding utility to prevent uncaught TypeErrors during re-renders
function safeAddEventListener(id, event, callback) {
  const element = document.getElementById(id);
  if (element) {
    element.addEventListener(event, callback);
  } else {
    console.warn(`[UI Warning] safeAddEventListener: Element ID '${id}' was not found in the DOM.`);
  }
}

// Dynamic Ayah of the Day Fetching Engine (Deterministic 1-to-6236 Daily index)
async function fetchAyahOfTheDay() {
  const quranBox = document.getElementById('quran-box');
  const textEl = document.getElementById('quran-text');
  const transEl = document.getElementById('quran-translation');
  const refEl = document.getElementById('quran-reference');

  try {
    const dateKey = new Date().toISOString().split('T')[0]; // e.g. "2026-05-21"
    
    let hash = 0;
    for (let i = 0; i < dateKey.length; i++) {
      hash = dateKey.charCodeAt(i) + ((hash << 5) - hash);
    }
    const ayahNumber = Math.abs(hash) % 6236 + 1; // Maps stable index inside Quranic boundaries (6236 ayas total)

    const response = await fetch(`https://api.alquran.cloud/v1/ayah/${ayahNumber}/editions/quran-simple-clean,en.sahih`);
    if (!response.ok) throw new Error("Quran API returned non-200");
    
    const parsed = await response.json();
    if (parsed && parsed.code === 200 && parsed.data && parsed.data.length === 2) {
      const arData = parsed.data[0];
      const enData = parsed.data[1];

      textEl.innerText = `${arData.text}`;
      transEl.innerText = `"${enData.text}"`;
      refEl.innerText = `[${enData.surah.englishName}: ${enData.numberInSurah}]`;
      quranBox.style.display = 'block';
      console.log(`[Quran] Deterministic Ayah #${ayahNumber} loaded.`);
    } else {
      quranBox.style.display = 'none';
    }
  } catch (err) {
    console.error("[Quran] Failed to fetch Quranic verse. Hiding display.", err.message);
    quranBox.style.display = 'none'; // Strictly no placeholders
  }
}