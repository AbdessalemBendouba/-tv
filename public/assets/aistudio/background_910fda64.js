const PRAYERS = ["Fajr", "Dhuhr", "Asr", "Maghrib", "Isha"];
const PRAYER_NAMES = {
  Fajr: { en: "Fajr", ar: "الفجر" },
  Dhuhr: { en: "Dhuhr", ar: "الظهر" },
  Asr: { en: "Asr", ar: "العصر" },
  Maghrib: { en: "Maghrib", ar: "المغرب" },
  Isha: { en: "Isha", ar: "العشاء" }
};

let lastAthanPlayed = "";
let lastPreAthanPlayed = "";

chrome.runtime.onInstalled.addListener(() => {
  chrome.alarms.create("check_time", { periodInMinutes: 1 });
  chrome.alarms.create("fetch_data", { periodInMinutes: 720 });
  fetchData();
});

chrome.runtime.onStartup.addListener(() => fetchData());

chrome.alarms.onAlarm.addListener((alarm) => {
  if (alarm.name === "check_time") checkPrayerTimes();
  if (alarm.name === "fetch_data") fetchData();
});

chrome.runtime.onMessage.addListener((msg, sender, sendResponse) => {
  if (msg.action === "force_fetch") {
    console.log("[Message] Manual force_fetch triggered from popup.");
    fetchData().then((success) => sendResponse({ success }));
    return true; 
  }
  
  // --- TEMPORARY TEST ACTIONS (Volume parameters now fully active) ---
  if (msg.action === "test_athan") {
    const vol = msg.volume !== undefined ? msg.volume : 0.5;
    playAthan(1, 'en', vol, true); 
    sendResponse({ success: true });
  } else if (msg.action === "test_athan_fajr") {
    const vol = msg.volume !== undefined ? msg.volume : 0.5;
    playAthan(0, 'en', vol, true); 
    sendResponse({ success: true });
  } else if (msg.action === "test_iqama_badge") {
    updateBadgeStyles("10m", '#FBC02D', '#000000'); // Yellow (Black text)
    sendResponse({ success: true });
  }
  // --- END TEMPORARY TEST ACTIONS ---
});

async function fetchData() {
  const data = await chrome.storage.local.get(['mosqueUrl', 'lang', 'hijriCorrector']);
  if (!data.mosqueUrl) {
    console.warn("[Fetch] Aborted: No mosque URL found in storage.");
    return false;
  }

  const lang = data.lang || 'ar';
  
  // DYNAMIC URL MODIFICATION
  const fetchUrl = data.mosqueUrl.replace(/mawaqit\.net\/[a-z]{2}\//, `mawaqit.net/${lang}/`);
  
  console.log(`[Fetch] Initiating fetch request to: ${fetchUrl} (Language: ${lang})`);

  try {
    const response = await fetch(fetchUrl);
    console.log(`[Fetch] Network response received. Status: ${response.status} (${response.statusText})`);
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const html = await response.text();
    console.log(`[Fetch] HTML content successfully loaded. Length: ${html.length} characters.`);

    let times = [];
    let name = "";
    let waits = ["15", "10", "10", "5", "10"]; // Default fallback wait times
    let hijriDate = "";
    let ramadanDay = 0;
    let hMonthNum = 0;
    let hDayNum = 0;
    let calendar = [];
    let timezone = "Africa/Algiers";
    let latVal = 0;
    let lonVal = 0;
    let shuruqTime = "";
    
    // Extract coordinates once during fetch stage
    const latMatch = html.match(/latitude\s*=\s*([-\d.]+)/);
    const lonMatch = html.match(/longitude\s*=\s*([-\d.]+)/);
    if (latMatch && lonMatch) {
      latVal = parseFloat(latMatch[1]);
      lonVal = parseFloat(lonMatch[1]);
      console.log(`[Fetch] Coordinates found -> Latitude: ${latVal}, Longitude: ${lonVal}`);
    }

    // 1. Extract JSON confData
    const confMatch = html.match(/let confData\s*=\s*(\{.*?\});/);
    if (confMatch && confMatch[1]) {
      try {
        const conf = JSON.parse(confMatch[1]);
        console.log("[Fetch] Successfully parsed confData JSON.");
        name = conf.name;
        timezone = conf.timezone || "Africa/Algiers";
        calendar = conf.calendar || [];
        shuruqTime = conf.shuruq || "";

        // Calculate current date based on Mosque Timezone (Leap Year Safe)
        const formatter = new Intl.DateTimeFormat('en-US', { timeZone: timezone, month: 'numeric', day: 'numeric', year: 'numeric' });
        const parts = formatter.formatToParts(new Date());
        const tzMonth = parseInt(parts.find(p => p.type === 'month').value, 10); // 1-12
        const tzDay = parseInt(parts.find(p => p.type === 'day').value, 10);     // 1-31
        const tzYear = parseInt(parts.find(p => p.type === 'year').value, 10);

        console.log(`[Fetch] Local Mosque Timezone Date -> Year: ${tzYear}, Month: ${tzMonth}, Day: ${tzDay}`);

        // --- EXTRACT EXACT TIMES FOR TODAY ---
        if (conf.calendar && conf.calendar.length === 12) {
          const monthData = conf.calendar[tzMonth - 1]; // Array is index 0-11
          if (monthData) {
            const dayTimes = monthData[tzDay.toString()]; // Key is day number as string
            if (dayTimes && dayTimes.length === 6) {
              // Mawaqit array: [Fajr, Shuruq, Dhuhr, Asr, Maghrib, Isha]
              times = [dayTimes[0], dayTimes[2], dayTimes[3], dayTimes[4], dayTimes[5]];
              shuruqTime = dayTimes[1]; // Grab sunrise dynamically
              console.log(`[Fetch] Extracted local times for today:`, times);
            }
          }
        }

        // --- EXTRACT EXACT IQAMA WAITS FOR TODAY ---
        if (conf.iqamaCalendar && conf.iqamaCalendar.length === 12) {
          const monthData = conf.iqamaCalendar[tzMonth - 1];
          if (monthData) {
            const dayWaits = monthData[tzDay.toString()];
            if (dayWaits && dayWaits.length === 5) {
              waits = dayWaits.map(w => w.replace('+', ''));
              console.log(`[Fetch] Extracted local Iqama waits for today:`, waits);
            }
          }
        }

        // --- DYNAMICALLY CALCULATE CORRECT HIJRI & GREGORIAN DATES ---
        try {
          const gregorianDate = new Date(tzYear, tzMonth - 1, tzDay);
          const hijriDateObj = new Date(tzYear, tzMonth - 1, tzDay);
          
          const mawaqitAdjustment = conf.hijriAdjustment !== undefined ? conf.hijriAdjustment : 0;
          const userCorrector = data.hijriCorrector !== undefined ? parseInt(data.hijriCorrector, 10) : 0;
          const adjustedAdjustment = mawaqitAdjustment + 1 + userCorrector; 
          
          hijriDateObj.setDate(hijriDateObj.getDate() + adjustedAdjustment);
          console.log(`[Fetch] Mosque Date Obj:`, gregorianDate);
          console.log(`[Fetch] Mawaqit Adjustment: ${mawaqitAdjustment} | Calibrated Chrome Adjustment: ${adjustedAdjustment}`);
          console.log(`[Fetch] Calibrated Hijri Date Obj:`, hijriDateObj);

          // Configure Hijri format options
          const hijriFormatOpt = { calendar: 'islamic-umalqura', numberingSystem: 'latn' };
          const locale = lang === 'ar' ? 'ar' : 'en';

          const fHijriDay = new Intl.DateTimeFormat(locale, { ...hijriFormatOpt, day: '2-digit' });
          const fHijriMonth = new Intl.DateTimeFormat(locale, { ...hijriFormatOpt, month: 'long' });
          const fHijriYear = new Intl.DateTimeFormat(locale, { ...hijriFormatOpt, year: 'numeric' });

          // Force en-US with latn numbering system for numeric extractions so parseInt never returns NaN
          const fHijriMonthNum = new Intl.DateTimeFormat('en-US', { calendar: 'islamic-umalqura', numberingSystem: 'latn', month: 'numeric' });
          const fHijriDayNum = new Intl.DateTimeFormat('en-US', { calendar: 'islamic-umalqura', numberingSystem: 'latn', day: 'numeric' });

          const hDay = fHijriDay.format(hijriDateObj);
          const hMonth = fHijriMonth.format(hijriDateObj);
          const hYear = fHijriYear.format(hijriDateObj).replace(/[^\d]/g, '');
          
          hMonthNum = parseInt(fHijriMonthNum.format(hijriDateObj), 10);
          hDayNum = parseInt(fHijriDayNum.format(hijriDateObj), 10);

          // Configure Gregorian date format parts (ar-DZ forces Algerian months like "ماي")
          const gregLocale = lang === 'ar' ? 'ar-DZ' : 'en-US';
          const fGregDay = new Intl.DateTimeFormat(gregLocale, { day: 'numeric' });
          const fGregMonth = new Intl.DateTimeFormat(gregLocale, { month: 'long' });
          const fGregYear = new Intl.DateTimeFormat(gregLocale, { year: 'numeric' });
          const fGregWeekday = new Intl.DateTimeFormat(gregLocale, { weekday: 'long' });

          const gDay = fGregDay.format(gregorianDate);
          const gMonth = fGregMonth.format(gregorianDate);
          const gYear = fGregYear.format(gregorianDate).replace(/[^\d]/g, '');
          const gWeekday = fGregWeekday.format(gregorianDate);

          // Assemble global date strings perfectly
          hijriDate = lang === 'ar' 
            ? `${gWeekday} ${gDay} ${gMonth} ${gYear} الموافق لـ ${hDay} ${hMonth} ${hYear}` 
            : `${gWeekday}, ${gDay} ${gMonth} ${gYear} corresponding to ${hDay} ${hMonth} ${hYear}`;

          // Detect Ramadan Month 9
          const isRamadan = hMonthNum === 9; 
          if (isRamadan) {
            ramadanDay = hDayNum;
          }

          console.log(`[Fetch] Dynamic Hijri Calculated -> Month: ${hMonthNum}, Day: ${hDayNum}`);
          console.log("[Fetch] Successfully generated global date string:", hijriDate);
        } catch (hijriErr) {
          console.error("[Fetch] Exception during dynamic Hijri/Gregorian calculation:", hijriErr);
        }

        if (times.length !== 5 && conf.times) times = conf.times;
      } catch (parseErr) {
        console.error("[Fetch] Exception while processing parsed JSON calendar structures.");
        console.error(" - Error Type:", parseErr.name);
        console.error(" - Error Message:", parseErr.message);
      }
    } else {
      console.warn("[Fetch] Failed to match confData regex in HTML.");
    }

    // 3. Extract Imsak Time
    let imsak = "";
    const imsakMatch = html.match(/<div class="time imsak-id"><div>(\d{2}:\d{2})<\/div><\/div>/);
    if (imsakMatch) {
      imsak = imsakMatch[1];
      console.log("[Fetch] Extracted Imsak Time from DOM:", imsak);
    } else if (times[0]) {
      // Fallback: 10 minutes before Fajr
      let [fh, fm] = times[0].split(':').map(Number);
      let total = fh * 60 + fm - 10;
      imsak = `${String(Math.floor(total/60)).padStart(2,'0')}:${String(total%60).padStart(2,'0')}`;
      console.log("[Fetch] Calculated fallback Imsak Time (Fajr - 10m):", imsak);
    }

    if (times.length === 5) {
      await chrome.storage.local.set({
        prayerTimes: times,
        mosqueName: name,
        hijriDate: hijriDate,
        hijriMonth: hMonthNum,
        hijriDay: hDayNum,
        ramadanDay: ramadanDay,
        imsakTime: imsak,
        iqamaWaits: waits,
        mosqueCalendar: calendar,
        mosqueTimezone: timezone,
        mosqueLat: latVal,
        mosqueLon: lonVal,
        shuruqTime: shuruqTime
      });
      console.log("[Fetch] All offset-free data successfully saved to Chrome Local Storage.");
      await checkPrayerTimes();
      return true;
    } else {
      console.error("[Fetch] Aborting save: Prayer times calculation generated an invalid array length:", times.length);
      return false;
    }
  } catch (err) {
    console.error("================ FETCH EXCEPTION ================");
    return false;
  }
}

async function checkPrayerTimes() {
  const data = await chrome.storage.local.get(['prayerTimes', 'iqamaWaits', 'lang', 'tracker', 'mosqueTimezone', 'preAthanEnabled', 'preAthanMins', 'athanVolume', 'prayerOffsets']);
  if (!data.prayerTimes) return;

  const lang = data.lang || 'ar';
  const tz = data.mosqueTimezone || "Africa/Algiers";
  
  const now = new Date();
  const tzFormatter = new Intl.DateTimeFormat('en-US', { timeZone: tz, hour: 'numeric', minute: 'numeric', second: 'numeric', hour12: false });
  const tzParts = tzFormatter.formatToParts(now);
  const currentHour = parseInt(tzParts.find(p => p.type === 'hour').value, 10);
  const currentMinute = parseInt(tzParts.find(p => p.type === 'minute').value, 10);
  const currentTotalMins = currentHour * 60 + currentMinute;
  const currentTimeStr = `${String(currentHour).padStart(2, '0')}:${String(currentMinute).padStart(2, '0')}`;

  const [fajrHour, fajrMin] = data.prayerTimes[0].split(':').map(Number);
  const fajrTotalMins = fajrHour * 60 + fajrMin;

  const formatter = new Intl.DateTimeFormat('en-US', { timeZone: tz, year: 'numeric', month: '2-digit', day: '2-digit' });
  const parts = formatter.formatToParts(now);
  const y = parts.find(p => p.type === 'year').value;
  const m = parts.find(p => p.type === 'month').value;
  const d = parts.find(p => p.type === 'day').value;
  
  let activeDayId = `${y}-${m}-${d}`;

  if (currentTotalMins < fajrTotalMins) {
    const yesterday = new Date(now);
    yesterday.setDate(now.getDate() - 1);
    const yFormatter = new Intl.DateTimeFormat('en-US', { timeZone: tz, year: 'numeric', month: '2-digit', day: '2-digit' });
    const yParts = yFormatter.formatToParts(yesterday);
    const yy = yParts.find(p => p.type === 'year').value;
    const ym = yParts.find(p => p.type === 'month').value;
    const yd = yParts.find(p => p.type === 'day').value;
    activeDayId = `${yy}-${ym}-${yd}`;
  }

  // 2. Manage Logical Tracker Rollover
  let tracker = data.tracker || { currentDayId: activeDayId, today: {}, history: {} };
  
  if (!tracker.today) {
    tracker.today = { Fajr: false, Dhuhr: false, Asr: false, Maghrib: false, Isha: false };
  }

  // DOUBLE-LAYER ROLLOVER GUARD
  if (!tracker.currentDayId) {
    console.log("[Tracker] Initialized currentDayId with activeDayId:", activeDayId);
    tracker.currentDayId = activeDayId;
    await chrome.storage.local.set({ tracker });
  } else if (tracker.currentDayId !== activeDayId) {
    if (activeDayId > tracker.currentDayId) {
      console.log(`[Tracker] Logical Day Rollover! Archiving ${tracker.currentDayId} and switching to ${activeDayId}`);
      tracker.history[tracker.currentDayId] = { ...tracker.today };
      tracker.today = { Fajr: false, Dhuhr: false, Asr: false, Maghrib: false, Isha: false };
      tracker.currentDayId = activeDayId;
      await chrome.storage.local.set({ tracker });
    } else {
      console.log(`[Tracker] Ignored rollover. activeDayId (${activeDayId}) is older/equal to currentDayId (${tracker.currentDayId})`);
    }
  }

  // 3. Update Badge and Alarm Triggers
  let activeIqamaMinsLeft = -1;
  let nextPrayerMinsLeft = Infinity;

  const preAthanEnabled = data.preAthanEnabled !== undefined ? data.preAthanEnabled : true;
  const preAthanMins = data.preAthanMins !== undefined ? parseInt(data.preAthanMins, 10) : 10;
  const volume = data.athanVolume !== undefined ? parseFloat(data.athanVolume) : 0.5;

  for (let i = 0; i < 5; i++) {
    const pTime = data.prayerTimes[i];
    const waitTime = parseInt(data.iqamaWaits[i] || 10);
    const [pHour, pMin] = pTime.split(':').map(Number);
    const pTotalMins = pHour * 60 + pMin;
    const iTotalMins = pTotalMins + waitTime;

    // Athan trigger
    if (pTime === currentTimeStr && lastAthanPlayed !== currentTimeStr) {
      lastAthanPlayed = currentTimeStr;
      console.log(`[CheckTime] Time matched for prayer index ${i}. Triggering Athan.`);
      playAthan(i, lang, volume);
    }

    // Pre-Athan Reminder trigger
    const preTriggerMins = pTotalMins - preAthanMins;
    if (preAthanEnabled && currentTotalMins === preTriggerMins && lastPreAthanPlayed !== `${currentTimeStr}_${i}`) {
      lastPreAthanPlayed = `${currentTimeStr}_${i}`;
      console.log(`[CheckTime] Pre-Athan reached for prayer index ${i}. Triggering reminder.`);
      triggerPreAthanNotification(i, preAthanMins, lang);
    }

    if (currentTotalMins >= pTotalMins && currentTotalMins < iTotalMins) {
      activeIqamaMinsLeft = iTotalMins - currentTotalMins;
    }

    let diff = pTotalMins - currentTotalMins;
    if (diff <= 0) {
      if (i === 4) {
         const nextFajrTotalMins = (fajrHour + 24) * 60 + fajrMin; 
         const fajrDiff = nextFajrTotalMins - currentTotalMins;
         if (fajrDiff < nextPrayerMinsLeft) nextPrayerMinsLeft = fajrDiff;
      }
    } else if (diff < nextPrayerMinsLeft) {
      nextPrayerMinsLeft = diff;
    }
  }

  if (activeIqamaMinsLeft !== -1) {
    updateBadgeStyles(`${activeIqamaMinsLeft}m`, '#FBC02D', '#000000');
  } else if (nextPrayerMinsLeft !== Infinity) {
     const hours = Math.floor(nextPrayerMinsLeft / 60);
    const mins = nextPrayerMinsLeft % 60;
    const badgeText = hours >= 1 ? `${hours}h` : `${mins}m`;
    
    // Uses high-contrast soft, light pastel colors for older Chromium runtimes [2]
    const badgeBgColor = hours >= 1 ? '#B3E5FC' : '#FFCDD2'; // Light Blue / Light Red
    updateBadgeStyles(badgeText, badgeBgColor, '#000000'); // Forces Black text
  } else {
    chrome.action.setBadgeText({ text: "" });
  }
}

function updateBadgeStyles(text, bgColor, textColor) {
  chrome.action.setBadgeText({ text });
  chrome.action.setBadgeBackgroundColor({ color: bgColor });
  if (typeof chrome.action.setBadgeTextColor === 'function') {
    try {
      chrome.action.setBadgeTextColor({ color: textColor });
    } catch (e) {}
  }
}

function triggerPreAthanNotification(idx, mins, lang) {
  const pName = PRAYER_NAMES[PRAYERS[idx]][lang];
  const title = lang === 'ar' ? 'تذكير بالصلاة' : 'Prayer Reminder';
  const message = lang === 'ar' 
    ? `متبقي ${mins} دقائق على أذان ${pName}` 
    : `${mins} minutes remaining until ${pName} Athan`;

  chrome.notifications.create({
    type: 'basic',
    iconUrl: 'icon.png',
    title: title,
    message: message,
    priority: 2
  });
}

async function playAthan(idx, lang, volume = 0.5, isTest = false) {
  const pName = PRAYER_NAMES[PRAYERS[idx]][lang];
  const title = lang === 'ar' ? 'حان وقت الصلاة' : 'Prayer Time';
  const message = lang === 'ar' ? `حان الآن موعد أذان ${pName}` : `It is now time for ${pName} prayer`;
  const athanFile = idx === 0 ? 'athan_fajr.mp3' : 'athan.mp3';

  chrome.notifications.create({ type: 'basic', iconUrl: 'icon.png', title, message, priority: 2 });

  try {
    await chrome.offscreen.createDocument({ url: 'offscreen.html', reasons: ['AUDIO_PLAYBACK'], justification: 'Play Athan' });
  } catch (err) {}
  
  setTimeout(() => chrome.runtime.sendMessage({ action: 'play_athan', file: athanFile, volume, isTest }), 500);
}