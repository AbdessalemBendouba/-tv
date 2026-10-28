package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.PrayerTvUiState

@Composable
fun DailiesScreen(
    uiState: PrayerTvUiState,
    onBack: () -> Unit,
    onOpenDrawer: () -> Unit,
    onIncrementDhikr: () -> Unit,
    onResetDhikr: () -> Unit,
    modifier: Modifier = Modifier
) {
    val prefs = uiState.preferences
    val colors = LocalAppColors.current
    val wideScrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.screenBackground)
            .padding(14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Menu Button
                val menuInteraction = remember { MutableInteractionSource() }
                val isMenuFocused by menuInteraction.collectIsFocusedAsState()

                Button(
                    onClick = onOpenDrawer,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMenuFocused) colors.focusGlow else colors.cardElevatedBackground
                    ),
                    shape = RoundedCornerShape(8.dp),
                    interactionSource = menuInteraction,
                    modifier = Modifier.focusable(interactionSource = menuInteraction)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = if (isMenuFocused) colors.screenBackground else colors.textPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "القائمة",
                        color = if (isMenuFocused) colors.screenBackground else colors.textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Back to Main Button
                val backInteraction = remember { MutableInteractionSource() }
                val isBackFocused by backInteraction.collectIsFocusedAsState()

                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBackFocused) colors.focusGlow else colors.cardElevatedBackground
                    ),
                    shape = RoundedCornerShape(8.dp),
                    interactionSource = backInteraction,
                    modifier = Modifier.focusable(interactionSource = backInteraction)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = if (isBackFocused) colors.screenBackground else colors.textPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "الرئيسية",
                        color = if (isBackFocused) colors.screenBackground else colors.textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "اليوميات والأذكار 📖",
                color = colors.goldAccent,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // =========================================================================
        // 2:1 SPLIT LAYOUT (Matching Main Screen Structure)
        // Wide Column (2f): 3 Stacked Rows (Ayah, Du'a, Hadith)
        // Narrow Column (1f): Tasbih Counter Card
        // =========================================================================
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // WIDE COLUMN (Weight = 2f) - 3 Rows
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.cardBackground)
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(wideScrollState),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Row 1: Daily Qur'an Verse (آية اليوم من القرآن الكريم)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.cardElevatedBackground)
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "آية اليوم من القرآن الكريم (Daily Qur'an Verse)",
                                    color = colors.goldAccent,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                val ayah = uiState.dailyAyah
                                if (ayah != null) {
                                    Text(
                                        text = "[${ayah.surahName}: ${prefs.formatNumber(ayah.ayahNumber)}]",
                                        color = colors.textSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            val ayah = uiState.dailyAyah
                            if (ayah != null) {
                                val arText = ayah.arabicText.ifBlank {
                                    if (ayah.translationText.contains("lawful and good", ignoreCase = true)) {
                                        "وَكُلُوا مِمَّا رَزَقَكُمُ اللَّهُ حَلَالًا طَيِّبًا وَاتَّقُوا اللَّهَ الَّذِي أَنتُم بِهِ مُؤْمِنُونَ"
                                    } else {
                                        "إِنَّ الصَّلَاةَ كَانَتْ عَلَى الْمُؤْمِنِينَ كِتَابًا مَّوْقُوتًا"
                                    }
                                }
                                Text(
                                    text = "﴿ $arText ﴾",
                                    color = colors.textPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 24.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "\"${ayah.translationText}\"",
                                    color = colors.textSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    // Row 2: Daily Du'a (دعاء اليوم - حصن المسلم) - No teller bracket
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.cardElevatedBackground)
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "دعاء اليوم (Daily Du'a - حصن المسلم)",
                                    color = colors.secondaryAccent,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            val dua = uiState.dailyDua
                            if (dua != null) {
                                Text(
                                    text = dua.arabicText,
                                    color = colors.textPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 22.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = dua.translationText,
                                    color = colors.textSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    // Row 3: Daily Hadith (الحديث النبوي الشريف) - Showing proper teller/source
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.cardElevatedBackground)
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "الحديث النبوي الشريف (Daily Hadith)",
                                    color = colors.primaryAccent,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                val hadith = uiState.dailyHadith
                                val hadithSource = hadith?.source?.ifBlank { "أخرجه النسائي والحاكم" } ?: "أخرجه النسائي والحاكم"
                                Text(
                                    text = "[$hadithSource]",
                                    color = colors.textSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            val hadith = uiState.dailyHadith
                            if (hadith != null) {
                                Text(
                                    text = hadith.text,
                                    color = colors.textPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
            }

            // NARROW COLUMN (Weight = 1f) - Tasbih Dhikr Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.cardBackground)
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "تسبيح اليوم (Daily Dhikr)",
                            color = colors.goldAccent,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        val dhikr = uiState.dailyDhikr
                        if (dhikr != null) {
                            Text(
                                text = dhikr.arabicText,
                                color = colors.secondaryAccent,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                lineHeight = 26.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = dhikr.translationText,
                                color = colors.textSecondary,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Focal Circular Tap Button
                    val tapInteraction = remember { MutableInteractionSource() }
                    val isTapFocused by tapInteraction.collectIsFocusedAsState()

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(if (isTapFocused) colors.focusGlow else colors.primaryAccent)
                                .border(
                                    width = if (isTapFocused) 3.dp else 1.5.dp,
                                    color = if (isTapFocused) Color.White else colors.cardBorder,
                                    shape = CircleShape
                                )
                                .clickable(
                                    interactionSource = tapInteraction,
                                    indication = null,
                                    onClick = onIncrementDhikr
                                )
                                .focusable(interactionSource = tapInteraction),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = prefs.formatNumber(uiState.dhikrCount),
                                color = if (isTapFocused) colors.screenBackground else Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "اضغط للتسبيح",
                            color = colors.textMuted,
                            fontSize = 11.sp
                        )
                    }

                    // Reset button
                    val resetInteraction = remember { MutableInteractionSource() }
                    val isResetFocused by resetInteraction.collectIsFocusedAsState()

                    Button(
                        onClick = onResetDhikr,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isResetFocused) colors.focusGlow else colors.cardElevatedBackground
                        ),
                        shape = RoundedCornerShape(8.dp),
                        interactionSource = resetInteraction,
                        modifier = Modifier.focusable(interactionSource = resetInteraction)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
                            tint = if (isResetFocused) colors.screenBackground else colors.redAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "تصفير العداد",
                            color = if (isResetFocused) colors.screenBackground else colors.redAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
