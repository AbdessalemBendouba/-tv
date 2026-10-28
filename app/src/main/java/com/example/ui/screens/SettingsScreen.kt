package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.SavedMosque
import com.example.model.AppPreferences
import com.example.model.ImsakVisibility
import com.example.model.NumeralSystem
import com.example.model.PrayerType
import com.example.ui.theme.*
import com.example.ui.util.AppStrings
import com.example.ui.viewmodel.PrayerTvUiState

@Composable
fun SettingsScreen(
    uiState: PrayerTvUiState,
    onBack: () -> Unit,
    onOpenDrawer: () -> Unit,
    onSavePreferences: (AppPreferences) -> Unit,
    onRestoreDefaults: () -> Unit,
    onSwitchMosque: (String) -> Unit = {},
    onAddMosque: (String, String) -> Unit = { _, _ -> },
    onDeleteMosque: (String) -> Unit = {},
    onTestAthan: () -> Unit,
    onStopAthan: () -> Unit,
    modifier: Modifier = Modifier
) {
    var draftPrefs by remember(uiState.preferences) { mutableStateOf(uiState.preferences) }
    var mosqueUrlText by remember(uiState.preferences.mosqueUrl) { mutableStateOf(uiState.preferences.mosqueUrl) }
    var isTestingSound by remember { mutableStateOf(false) }
    var newMosqueName by remember { mutableStateOf("") }
    var newMosqueUrl by remember { mutableStateOf("") }
    var addErrorMsg by remember { mutableStateOf<String?>(null) }

    val colors = LocalAppColors.current
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.screenBackground)
    ) {
        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .padding(bottom = 90.dp) // Leave clearance for floating bottom bar
        ) {
            // Header with Menu Button, Back Button, and Quick Language Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Menu Button (Opens Drawer from Settings)
                    val menuInteraction = remember { MutableInteractionSource() }
                    val isMenuFocused by menuInteraction.collectIsFocusedAsState()

                    Button(
                        onClick = onOpenDrawer,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isMenuFocused) colors.focusGlow else colors.cardElevatedBackground
                        ),
                        shape = RoundedCornerShape(8.dp),
                        interactionSource = menuInteraction,
                        modifier = Modifier
                            .testTag("btn_settings_menu")
                            .focusable(interactionSource = menuInteraction)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = if (isMenuFocused) colors.screenBackground else colors.textPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.menu(draftPrefs.language),
                            color = if (isMenuFocused) colors.screenBackground else colors.textPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Back to Main
                    val backInteraction = remember { MutableInteractionSource() }
                    val isBackFocused by backInteraction.collectIsFocusedAsState()

                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBackFocused) colors.focusGlow else colors.cardElevatedBackground
                        ),
                        shape = RoundedCornerShape(8.dp),
                        interactionSource = backInteraction,
                        modifier = Modifier
                            .testTag("btn_settings_back")
                            .focusable(interactionSource = backInteraction)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = if (isBackFocused) colors.screenBackground else colors.textPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.home(draftPrefs.language),
                            color = if (isBackFocused) colors.screenBackground else colors.textPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Header Fast Language Switcher Button
                    val headerLangInteraction = remember { MutableInteractionSource() }
                    val isHeaderLangFocused by headerLangInteraction.collectIsFocusedAsState()

                    Button(
                        onClick = {
                            val nextLang = if (draftPrefs.language == AppLanguage.ARABIC) AppLanguage.ENGLISH else AppLanguage.ARABIC
                            draftPrefs = draftPrefs.copy(language = nextLang)
                            onSavePreferences(draftPrefs.copy(language = nextLang))
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isHeaderLangFocused) colors.focusGlow else colors.cardElevatedBackground
                        ),
                        shape = RoundedCornerShape(8.dp),
                        interactionSource = headerLangInteraction,
                        modifier = Modifier
                            .testTag("btn_header_language_toggle")
                            .focusable(interactionSource = headerLangInteraction)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Toggle Language",
                            tint = if (isHeaderLangFocused) colors.screenBackground else colors.goldAccent
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (draftPrefs.language == AppLanguage.ARABIC) "English 🇬🇧" else "العربية 🇸🇦",
                            color = if (isHeaderLangFocused) colors.screenBackground else colors.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                Text(
                    text = "${AppStrings.tabSettings(draftPrefs.language)} ⚙️",
                    color = colors.goldAccent,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // =========================================================================
            // CARD 1: DEDICATED APPLICATION LANGUAGE SWITCHER (MANDATORY & UNMISSABLE)
            // =========================================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.cardBackground)
                    .border(1.5.dp, colors.goldAccent.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = colors.goldAccent,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.languageCardTitle(draftPrefs.language),
                                color = colors.goldAccent,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Badge indicating active selection
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(colors.primaryAccent.copy(alpha = 0.2f))
                                .border(1.dp, colors.primaryAccent, RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (draftPrefs.language == AppLanguage.ARABIC) "اللغة الحالية: العربية 🇸🇦" else "Current: English 🇬🇧",
                                color = colors.primaryAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = AppStrings.languageCardSubtitle(draftPrefs.language),
                        color = colors.textSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AppLanguage.entries.forEach { lang ->
                            val isSelected = draftPrefs.language == lang
                            val btnInteraction = remember { MutableInteractionSource() }
                            val isBtnFocused by btnInteraction.collectIsFocusedAsState()

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when {
                                            isBtnFocused -> colors.focusGlow
                                            isSelected -> colors.primaryAccent.copy(alpha = 0.2f)
                                            else -> colors.cardElevatedBackground
                                        }
                                    )
                                    .border(
                                        width = if (isBtnFocused) 2.dp else if (isSelected) 2.dp else 1.dp,
                                        color = if (isBtnFocused) colors.focusGlow else if (isSelected) colors.primaryAccent else colors.cardBorder,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable(interactionSource = btnInteraction, indication = null) {
                                        draftPrefs = draftPrefs.copy(language = lang)
                                        onSavePreferences(draftPrefs.copy(language = lang))
                                    }
                                    .focusable(interactionSource = btnInteraction)
                                    .testTag(if (lang == AppLanguage.ARABIC) "btn_lang_ar" else "btn_lang_en")
                                    .padding(vertical = 12.dp, horizontal = 16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (lang == AppLanguage.ARABIC) "🇸🇦" else "🇬🇧",
                                            fontSize = 20.sp
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = if (lang == AppLanguage.ARABIC) "العربية (Arabic)" else "English (الإنجليزية)",
                                                color = if (isBtnFocused) colors.screenBackground else if (isSelected) colors.goldAccent else colors.textPrimary,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = if (lang == AppLanguage.ARABIC) "واجهة عربية من اليمين لليسار" else "Left-to-right English interface",
                                                color = if (isBtnFocused) colors.screenBackground.copy(alpha = 0.8f) else colors.textMuted,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }

                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = if (isBtnFocused) colors.screenBackground else colors.primaryAccent,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Card 2: Active Mosque URL / Slug setup
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.cardBackground)
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = AppStrings.mosqueUrlTitle(draftPrefs.language),
                        color = colors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AppStrings.mosqueUrlSubtitle(draftPrefs.language),
                        color = colors.textSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = mosqueUrlText,
                        onValueChange = {
                            mosqueUrlText = it
                            draftPrefs = draftPrefs.copy(mosqueUrl = it)
                        },
                        singleLine = true,
                        placeholder = { Text("مثال: https://mawaqit.net/ar/ain-tedeles", color = colors.textMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = colors.textPrimary,
                            unfocusedTextColor = colors.textPrimary,
                            focusedBorderColor = colors.primaryAccent,
                            unfocusedBorderColor = colors.cardBorder,
                            focusedContainerColor = colors.cardElevatedBackground,
                            unfocusedContainerColor = colors.cardElevatedBackground
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Card 3: Saved Mosques & Quick Switching
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.cardBackground)
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = AppStrings.savedMosquesTitle(draftPrefs.language),
                                color = colors.goldAccent,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = AppStrings.savedMosquesSubtitle(draftPrefs.language),
                                color = colors.textSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Saved Mosques List
                    val savedList = draftPrefs.savedMosques
                    if (savedList.isEmpty()) {
                        Text(
                            text = "لا توجد مساجد محفوظة بعد.",
                            color = colors.textMuted,
                            fontSize = 12.sp
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            savedList.forEach { mosque ->
                                val isActive = mosque.url.trim().equals(draftPrefs.mosqueUrl.trim(), ignoreCase = true)
                                val itemInteraction = remember { MutableInteractionSource() }
                                val isItemFocused by itemInteraction.collectIsFocusedAsState()

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isActive) colors.primaryAccent.copy(alpha = 0.12f) else colors.cardElevatedBackground)
                                        .border(
                                            width = if (isActive) 1.5.dp else 1.dp,
                                            color = if (isActive) colors.primaryAccent else colors.cardBorder,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = mosque.name,
                                                    color = if (isActive) colors.goldAccent else colors.textPrimary,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                if (isActive) {
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(4.dp))
                                                            .background(colors.primaryAccent)
                                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                                    ) {
                                                        Text(
                                                            text = "النشط حالياً",
                                                            color = Color.White,
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = mosque.url,
                                                color = colors.textSecondary,
                                                fontSize = 11.sp,
                                                maxLines = 1
                                            )
                                        }

                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                            if (!isActive) {
                                                val switchInteraction = remember { MutableInteractionSource() }
                                                val isSwitchFocused by switchInteraction.collectIsFocusedAsState()

                                                Button(
                                                    onClick = {
                                                        mosqueUrlText = mosque.url
                                                        draftPrefs = draftPrefs.copy(mosqueUrl = mosque.url)
                                                        onSwitchMosque(mosque.url)
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = if (isSwitchFocused) colors.focusGlow else colors.primaryAccent
                                                    ),
                                                    shape = RoundedCornerShape(6.dp),
                                                    interactionSource = switchInteraction,
                                                    modifier = Modifier.focusable(interactionSource = switchInteraction),
                                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = "تبديل إليه",
                                                        color = if (isSwitchFocused) colors.screenBackground else Color.White,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }

                                            // Delete Mosque Button (if more than 1 mosque)
                                            if (savedList.size > 1) {
                                                val delInteraction = remember { MutableInteractionSource() }
                                                val isDelFocused by delInteraction.collectIsFocusedAsState()

                                                IconButton(
                                                    onClick = {
                                                        val updatedList = draftPrefs.savedMosques.filterNot { it.url == mosque.url }
                                                        val newActive = if (draftPrefs.mosqueUrl == mosque.url) updatedList.firstOrNull()?.url ?: "" else draftPrefs.mosqueUrl
                                                        draftPrefs = draftPrefs.copy(savedMosques = updatedList, mosqueUrl = newActive)
                                                        mosqueUrlText = newActive
                                                        onDeleteMosque(mosque.url)
                                                    },
                                                    interactionSource = delInteraction,
                                                    modifier = Modifier
                                                        .size(36.dp)
                                                        .focusable(interactionSource = delInteraction)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Delete",
                                                        tint = if (isDelFocused) colors.focusGlow else colors.redAccent,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Add Mosque Form
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(colors.cardElevatedBackground)
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "إضافة مسجد جديد إلى القائمة:",
                                color = colors.primaryAccent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = newMosqueName,
                                    onValueChange = { newMosqueName = it },
                                    singleLine = true,
                                    placeholder = { Text("اسم المسجد (مثال: مسجد الحي)", color = colors.textMuted, fontSize = 11.sp) },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = colors.textPrimary,
                                        unfocusedTextColor = colors.textPrimary,
                                        focusedBorderColor = colors.primaryAccent,
                                        unfocusedBorderColor = colors.cardBorder,
                                        focusedContainerColor = colors.cardBackground,
                                        unfocusedContainerColor = colors.cardBackground
                                    )
                                )

                                OutlinedTextField(
                                    value = newMosqueUrl,
                                    onValueChange = {
                                        newMosqueUrl = it
                                        addErrorMsg = null
                                    },
                                    singleLine = true,
                                    placeholder = { Text("الرابط أو الرمز (مثلاً: ain-tedeles)", color = colors.textMuted, fontSize = 11.sp) },
                                    modifier = Modifier.weight(1.5f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = colors.textPrimary,
                                        unfocusedTextColor = colors.textPrimary,
                                        focusedBorderColor = colors.primaryAccent,
                                        unfocusedBorderColor = colors.cardBorder,
                                        focusedContainerColor = colors.cardBackground,
                                        unfocusedContainerColor = colors.cardBackground
                                    )
                                )

                                val addInteraction = remember { MutableInteractionSource() }
                                val isAddFocused by addInteraction.collectIsFocusedAsState()

                                Button(
                                    onClick = {
                                        val trimmedUrl = newMosqueUrl.trim()
                                        val trimmedName = newMosqueName.trim().ifBlank { "مسجد جديد" }
                                        if (trimmedUrl.isNotBlank()) {
                                            val currentMosques = draftPrefs.savedMosques.toMutableList()
                                            val existingIndex = currentMosques.indexOfFirst { it.url.trim().equals(trimmedUrl, ignoreCase = true) }
                                            if (existingIndex >= 0) {
                                                currentMosques[existingIndex] = SavedMosque(trimmedName, trimmedUrl)
                                            } else {
                                                currentMosques.add(SavedMosque(trimmedName, trimmedUrl))
                                            }
                                            draftPrefs = draftPrefs.copy(savedMosques = currentMosques, mosqueUrl = trimmedUrl)
                                            mosqueUrlText = trimmedUrl
                                            onAddMosque(trimmedName, trimmedUrl)
                                            newMosqueName = ""
                                            newMosqueUrl = ""
                                            addErrorMsg = null
                                        } else {
                                            addErrorMsg = "يرجى كتابة الرابط أو الرمز"
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isAddFocused) colors.focusGlow else colors.goldAccent
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    interactionSource = addInteraction,
                                    modifier = Modifier
                                        .height(54.dp)
                                        .focusable(interactionSource = addInteraction)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add",
                                        tint = if (isAddFocused) colors.screenBackground else Color.Black
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "إضافة وحفظ",
                                        color = if (isAddFocused) colors.screenBackground else Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            if (addErrorMsg != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = addErrorMsg ?: "",
                                    color = colors.redAccent,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Card 4: Appearance, Theme & Numeral System
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.cardBackground)
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = AppStrings.appearanceTitle(draftPrefs.language),
                        color = colors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Language Quick Selection inside Appearance
                    Column {
                        Text(
                            text = AppStrings.languageSetting(draftPrefs.language),
                            color = colors.textSecondary,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AppLanguage.entries.forEach { lang ->
                                val isSelected = draftPrefs.language == lang
                                val langInteraction = remember { MutableInteractionSource() }
                                val isLangFocused by langInteraction.collectIsFocusedAsState()

                                Button(
                                    onClick = {
                                        draftPrefs = draftPrefs.copy(language = lang)
                                        onSavePreferences(draftPrefs.copy(language = lang))
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isLangFocused) colors.focusGlow else if (isSelected) colors.primaryAccent else colors.cardElevatedBackground
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    interactionSource = langInteraction,
                                    modifier = Modifier
                                        .weight(1f)
                                        .focusable(interactionSource = langInteraction)
                                ) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "العربية 🇸🇦 (Arabic)" else "English 🇬🇧 (الإنجليزية)",
                                        color = if (isLangFocused) colors.screenBackground else if (isSelected) Color.White else colors.textPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dark / Light Mode Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (draftPrefs.isDarkMode) AppStrings.darkModeLabel(draftPrefs.language) else AppStrings.lightModeLabel(draftPrefs.language),
                            color = colors.textSecondary,
                            fontSize = 13.sp
                        )

                        Switch(
                            checked = draftPrefs.isDarkMode,
                            onCheckedChange = { draftPrefs = draftPrefs.copy(isDarkMode = it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colors.primaryAccent,
                                checkedTrackColor = colors.primaryAccent.copy(alpha = 0.35f),
                                checkedBorderColor = colors.primaryAccent,
                                uncheckedThumbColor = colors.textMuted,
                                uncheckedTrackColor = colors.cardBorder
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Numeral System: Arabic vs Eastern Arabic Indic
                    Column {
                        Text(
                            text = AppStrings.numeralSystemLabel(draftPrefs.language),
                            color = colors.textSecondary,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            NumeralSystem.entries.forEach { sys ->
                                val isSelected = draftPrefs.numeralSystem == sys
                                Button(
                                    onClick = { draftPrefs = draftPrefs.copy(numeralSystem = sys) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSelected) colors.primaryAccent else colors.cardElevatedBackground
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = if (draftPrefs.language == AppLanguage.ARABIC) sys.labelAr else sys.labelEn,
                                        color = if (isSelected) Color.White else colors.textPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Card 3: Athan Audio & Pre-Athan Takbira Settings (Consistent Emerald Green Switch)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.cardBackground)
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "صوت الأذان والتنبيه المسبق:",
                        color = colors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Volume row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "مستوى الصوت: ${draftPrefs.formatNumber((draftPrefs.athanVolume * 100).toInt())}%",
                            color = colors.primaryAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    val current = draftPrefs.athanVolume
                                    draftPrefs = draftPrefs.copy(athanVolume = (current - 0.1f).coerceAtLeast(0.1f))
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = colors.cardElevatedBackground)
                            ) {
                                Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                            }

                            Button(
                                onClick = {
                                    val current = draftPrefs.athanVolume
                                    draftPrefs = draftPrefs.copy(athanVolume = (current + 0.1f).coerceAtMost(1.0f))
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = colors.cardElevatedBackground)
                            ) {
                                Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                            }

                            // Test Voice Button
                            val testInteraction = remember { MutableInteractionSource() }
                            val isTestFocused by testInteraction.collectIsFocusedAsState()

                            Button(
                                onClick = {
                                    if (isTestingSound) {
                                        onStopAthan()
                                        isTestingSound = false
                                    } else {
                                        onTestAthan()
                                        isTestingSound = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isTestFocused) colors.focusGlow else colors.goldAccent
                                ),
                                interactionSource = testInteraction,
                                modifier = Modifier.focusable(interactionSource = testInteraction)
                            ) {
                                Icon(
                                    imageVector = if (isTestingSound) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = "Test Audio",
                                    tint = if (isTestFocused) colors.screenBackground else Color.Black
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isTestingSound) "إيقاف الصوت" else "تجربة صوت الأذان",
                                    color = if (isTestFocused) colors.screenBackground else Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Pre-Athan Switch (Single consistent Emerald Green color scheme!)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "التذكير قبل الأذان (تكبيرة وتنبيه خلفي):",
                                color = colors.textPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "يعمل فوق أي تطبيق آخر (مثل Netflix أو YouTube)",
                                color = colors.textSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Switch(
                            checked = draftPrefs.preAthanEnabled,
                            onCheckedChange = { draftPrefs = draftPrefs.copy(preAthanEnabled = it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colors.primaryAccent,
                                checkedTrackColor = colors.primaryAccent.copy(alpha = 0.35f),
                                checkedBorderColor = colors.primaryAccent,
                                uncheckedThumbColor = colors.textMuted,
                                uncheckedTrackColor = colors.cardBorder
                            )
                        )
                    }

                    if (draftPrefs.preAthanEnabled) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "وقت التذكير: ${draftPrefs.formatNumber(draftPrefs.preAthanMinutes)} دقيقة قبل الأذان",
                                color = colors.goldAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(5, 10, 15, 20).forEach { m ->
                                    val isSelected = draftPrefs.preAthanMinutes == m
                                    Button(
                                        onClick = { draftPrefs = draftPrefs.copy(preAthanMinutes = m) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSelected) colors.primaryAccent else colors.cardElevatedBackground
                                        )
                                    ) {
                                        Text(
                                            text = "${draftPrefs.formatNumber(m)}د",
                                            color = if (isSelected) Color.White else colors.textPrimary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Card 4: ALL 5 PRAYERS OFFSETS (Fajr, Dhuhr, Asr, Maghrib, Isha)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.cardBackground)
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "تصحيح أوقات الصلاة الفردية (بالدقائق):",
                        color = colors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "يمكنك إضافة أو خصم دقائق لكل صلاة على حدة",
                        color = colors.textSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val canonicalPrayers = listOf(
                        PrayerType.FAJR,
                        PrayerType.DHUHR,
                        PrayerType.ASR,
                        PrayerType.MAGHRIB,
                        PrayerType.ISHA
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        canonicalPrayers.forEach { p ->
                            val currentOffset = draftPrefs.getOffset(p)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = p.arabicName,
                                    color = colors.goldAccent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${draftPrefs.formatNumber(currentOffset)} د",
                                    color = colors.textPrimary,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Button(
                                        onClick = {
                                            val newOffsets = draftPrefs.prayerOffsets.toMutableMap()
                                            newOffsets[p] = (currentOffset - 1).coerceAtLeast(-15)
                                            draftPrefs = draftPrefs.copy(prayerOffsets = newOffsets)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = colors.cardElevatedBackground),
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                        modifier = Modifier.defaultMinSize(minWidth = 32.dp, minHeight = 32.dp)
                                    ) {
                                        Text("-", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                                    }

                                    Button(
                                        onClick = {
                                            val newOffsets = draftPrefs.prayerOffsets.toMutableMap()
                                            newOffsets[p] = (currentOffset + 1).coerceAtMost(15)
                                            draftPrefs = draftPrefs.copy(prayerOffsets = newOffsets)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = colors.cardElevatedBackground),
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                        modifier = Modifier.defaultMinSize(minWidth = 32.dp, minHeight = 32.dp)
                                    ) {
                                        Text("+", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Card 5: Events, Hijri Calendar & Special Day Badge Setting
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.cardBackground)
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "المناسبات والتقويم الهجري:",
                        color = colors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Setting requested: Disable Special Day Badge completely under Hijri Date settings
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "إظهار شارة المناسبات الخاصة (الأعياد ورمضان):",
                                color = colors.textPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "عرض وسام المناسبة كعيد الفطر، الأضحى، وليالي العشر",
                                color = colors.textSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Switch(
                            checked = draftPrefs.showSpecialDayBadge,
                            onCheckedChange = { draftPrefs = draftPrefs.copy(showSpecialDayBadge = it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colors.primaryAccent,
                                checkedTrackColor = colors.primaryAccent.copy(alpha = 0.35f),
                                checkedBorderColor = colors.primaryAccent,
                                uncheckedThumbColor = colors.textMuted,
                                uncheckedTrackColor = colors.cardBorder
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Imsak Visibility
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "إظهار الإمساك:",
                            color = colors.textSecondary,
                            fontSize = 12.sp
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            ImsakVisibility.entries.forEach { opt ->
                                val isSelected = draftPrefs.imsakVisibility == opt
                                Button(
                                    onClick = { draftPrefs = draftPrefs.copy(imsakVisibility = opt) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSelected) colors.primaryAccent else colors.cardElevatedBackground
                                    )
                                ) {
                                    Text(
                                        text = opt.labelAr,
                                        color = if (isSelected) Color.White else colors.textPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Hijri Corrector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "تصحيح التاريخ الهجري: ${draftPrefs.formatNumber(draftPrefs.hijriCorrection)} يوم",
                            color = colors.textSecondary,
                            fontSize = 12.sp
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Button(
                                onClick = { draftPrefs = draftPrefs.copy(hijriCorrection = draftPrefs.hijriCorrection - 1) },
                                colors = ButtonDefaults.buttonColors(containerColor = colors.cardElevatedBackground)
                            ) {
                                Text("-1", color = colors.textPrimary)
                            }
                            Button(
                                onClick = { draftPrefs = draftPrefs.copy(hijriCorrection = 0) },
                                colors = ButtonDefaults.buttonColors(containerColor = colors.cardElevatedBackground)
                            ) {
                                Text("0", color = colors.textPrimary)
                            }
                            Button(
                                onClick = { draftPrefs = draftPrefs.copy(hijriCorrection = draftPrefs.hijriCorrection + 1) },
                                colors = ButtonDefaults.buttonColors(containerColor = colors.cardElevatedBackground)
                            ) {
                                Text("+1", color = colors.textPrimary)
                            }
                        }
                    }
                }
            }
        }

        // =========================================================================
        // FLOATING ACTION BAR AT THE BOTTOM OF THE SCREEN (Relative to Screen, Above Content)
        // Contains "حفظ وتطبيق التغييرات" and "استعادة الإعدادات الافتراضية"
        // =========================================================================
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(colors.bottomBarBackground)
                .border(width = 1.dp, color = colors.cardBorder)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Restore Defaults Button (Resets everything except URL)
                val resetInteraction = remember { MutableInteractionSource() }
                val isResetFocused by resetInteraction.collectIsFocusedAsState()

                Button(
                    onClick = {
                        val currentUrl = draftPrefs.mosqueUrl
                        draftPrefs = draftPrefs.resetToDefaults(preserveMosqueUrl = true)
                        onRestoreDefaults()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isResetFocused) colors.focusGlow else colors.cardElevatedBackground
                    ),
                    shape = RoundedCornerShape(10.dp),
                    interactionSource = resetInteraction,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_restore_defaults")
                        .focusable(interactionSource = resetInteraction)
                ) {
                    Icon(
                        imageVector = Icons.Default.Restore,
                        contentDescription = "Restore Defaults",
                        tint = if (isResetFocused) colors.screenBackground else colors.redAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.restoreDefaults(draftPrefs.language),
                        color = if (isResetFocused) colors.screenBackground else colors.redAccent,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Save & Apply Button
                val saveInteraction = remember { MutableInteractionSource() }
                val isSaveFocused by saveInteraction.collectIsFocusedAsState()

                Button(
                    onClick = {
                        onSavePreferences(draftPrefs)
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaveFocused) colors.focusGlow else colors.primaryAccent
                    ),
                    shape = RoundedCornerShape(10.dp),
                    interactionSource = saveInteraction,
                    modifier = Modifier
                        .weight(1.3f)
                        .height(48.dp)
                        .testTag("btn_save_settings")
                        .focusable(interactionSource = saveInteraction)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Save",
                        tint = if (isSaveFocused) colors.screenBackground else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.saveChanges(draftPrefs.language),
                        color = if (isSaveFocused) colors.screenBackground else Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}
