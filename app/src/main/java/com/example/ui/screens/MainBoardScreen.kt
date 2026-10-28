package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ImsakVisibility
import com.example.model.PrayerTimeItem
import com.example.model.PrayerType
import com.example.ui.components.SpecialDayBanner
import com.example.ui.components.SunSkyArc
import com.example.ui.theme.*
import com.example.ui.viewmodel.PrayerTvUiState

@Composable
fun MainBoardScreen(
    uiState: PrayerTvUiState,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val schedule = uiState.schedule
    val prefs = uiState.preferences
    val colors = LocalAppColors.current
    val wideScrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(colors.screenBackground)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // =========================================================================
        // 1. WIDE SECTION (Weight = 2f)
        // Scrollable if height is constrained. Matches User's Layout Drawing (Image 3)
        // Header -> Celestial Arc -> Big Clock -> Side-by-Side Cards (Next + Last)
        // =========================================================================
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .background(colors.cardBackground)
                .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(wideScrollState),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Row: Hamburger Menu on Left & Mosque Name on Right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val interactionSource = remember { MutableInteractionSource() }
                    val isFocused by interactionSource.collectIsFocusedAsState()

                    // D-Pad focusable hamburger button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isFocused) colors.focusGlow.copy(alpha = 0.3f)
                                else colors.cardElevatedBackground
                            )
                            .border(
                                width = if (isFocused) 2.dp else 1.dp,
                                color = if (isFocused) colors.focusGlow else colors.cardBorder,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable(interactionSource = interactionSource, indication = null, onClick = onOpenDrawer)
                            .focusable(interactionSource = interactionSource)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = if (isFocused) colors.goldAccent else colors.textPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "القائمة",
                                color = if (isFocused) colors.goldAccent else colors.textPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Mosque Title
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = schedule?.mosqueName?.ifBlank { "حي على الصلاة" } ?: "حي على الصلاة",
                            color = colors.goldAccent,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "مواقيت الصلاة الرسمية",
                            color = colors.textSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Celestial Sun-in-the-Sky Arc (Placed gracefully directly over the clock)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (uiState.isDaytime) "مسار حركة الشمس في السماء" else "المسار الفلكي الليلي",
                        color = colors.textSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    SunSkyArc(
                        sunAngleRad = uiState.sunPositionAngleRad,
                        isDaytime = uiState.isDaytime,
                        modifier = Modifier.height(95.dp)
                    )
                }

                // Focal Large Digital Clock
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.currentTimeString,
                        color = colors.textPrimary,
                        fontSize = 54.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Side-by-Side Cards (Next Prayer on Left + Last Prayer on Right)
                val nextP = uiState.nextPrayer
                val lastP = uiState.lastPrayer
                val activeIqama = uiState.iqamaActivePrayer

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card 1: Next Prayer Card (Weight 1.4f)
                    Box(
                        modifier = Modifier
                            .weight(1.4f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(colors.cardElevatedBackground, colors.cardBackground)
                                )
                            )
                            .border(
                                width = 2.dp,
                                color = if (activeIqama != null) colors.orangeAccent else colors.primaryAccent,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(14.dp)
                    ) {
                        Column {
                            if (activeIqama != null) {
                                // Active Iqama Wait Mode
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = "Iqama Alert",
                                        tint = colors.orangeAccent,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "وقت إقامة صلاة ${activeIqama.type.arabicName}",
                                        color = colors.orangeAccent,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                val iqamaSecs = uiState.iqamaSecondsLeft
                                val iqamaMins = iqamaSecs / 60
                                val iqamaRemainderSecs = iqamaSecs % 60
                                val iqamaCountdown = prefs.formatNumber(
                                    String.format("%02d:%02d", iqamaMins, iqamaRemainderSecs)
                                )
                                Text(
                                    text = "متبقي للإقامة: $iqamaCountdown",
                                    color = colors.goldAccent,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            } else if (nextP != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "الصلاة القادمة",
                                            color = colors.textSecondary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = nextP.type.arabicName,
                                            color = colors.primaryAccent,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "المتبقي للأذان",
                                            color = colors.textSecondary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = uiState.nextPrayerCountdownFormatted,
                                            color = colors.goldAccent,
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                LinearProgressIndicator(
                                    progress = { uiState.elapsedProgressPercent / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = colors.primaryAccent,
                                    trackColor = colors.cardBorder,
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${prefs.formatNumber(nextP.adjustedTimeString)} وقت الأذان",
                                        color = colors.textMuted,
                                        fontSize = 10.5.sp
                                    )
                                    Text(
                                        text = "الإقامة بعد +${prefs.formatNumber(nextP.waitMinutes)} دقيقة",
                                        color = colors.textSecondary,
                                        fontSize = 10.5.sp
                                    )
                                }
                            }
                        }
                    }

                    // Card 2: Last Prayer Card (Weight 0.7f - User's Image 3 Request!)
                    Box(
                        modifier = Modifier
                            .weight(0.7f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(colors.cardElevatedBackground)
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(14.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "الصلاة السابقة",
                                color = colors.textSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            if (lastP != null) {
                                Text(
                                    text = lastP.type.arabicName,
                                    color = colors.secondaryAccent,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "(${prefs.formatNumber(lastP.adjustedTimeString)})",
                                    color = colors.textSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            } else {
                                Text(
                                    text = "--",
                                    color = colors.textMuted,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // =========================================================================
        // 2. NARROW SECTION (Weight = 1f)
        // Dates, SpecialDay Badge, Today's Complete Prayer Timetable
        // =========================================================================
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .background(colors.cardBackground)
                .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top: Dual Gregorian and Hijri Dates
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = prefs.formatNumber(schedule?.hijriDate ?: ""),
                        color = colors.goldAccent,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = prefs.formatNumber(schedule?.gregorianDate ?: ""),
                        color = colors.textSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // Special Day Badge (visible only during Eid, Ramadan, Ashura, etc. AND if enabled in settings)
                if (schedule != null && prefs.showSpecialDayBadge) {
                    SpecialDayBanner(specialDay = schedule.specialDay)
                }

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    color = colors.cardBorder,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Prayer Timetable List
                val displayPrayers = buildList {
                    // Check Imsak visibility
                    val isRamadan = schedule?.specialDay is com.example.model.SpecialDay.Ramadan
                    val showImsak = when (prefs.imsakVisibility) {
                        ImsakVisibility.ALWAYS -> true
                        ImsakVisibility.RAMADAN_ONLY -> isRamadan
                        ImsakVisibility.NEVER -> false
                    }

                    if (showImsak && schedule != null && schedule.imsakTime.isNotBlank()) {
                        add(
                            PrayerTimeItem(
                                type = PrayerType.FAJR,
                                baseTimeString = schedule.imsakTime,
                                adjustedTimeString = schedule.imsakTime,
                                totalMinutes = 0,
                                waitMinutes = 0,
                                iqamaTotalMinutes = 0
                            ) to "الإمساك"
                        )
                    }

                    schedule?.prayers?.forEach { p ->
                        add(p to null)
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(displayPrayers) { (item, customLabel) ->
                        PrayerRowCard(
                            item = item,
                            customLabel = customLabel,
                            prefs = prefs
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PrayerRowCard(
    item: PrayerTimeItem,
    customLabel: String?,
    prefs: com.example.model.AppPreferences
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val colors = LocalAppColors.current

    val isPassed = item.isPassed
    val isNext = item.isNext
    val isCurrent = item.isCurrent

    val label = customLabel ?: item.type.arabicName

    val cardBg = when {
        isFocused -> colors.focusGlow.copy(alpha = 0.25f)
        isCurrent -> colors.orangeAccent.copy(alpha = 0.2f)
        isNext -> colors.primaryAccent.copy(alpha = 0.18f)
        else -> colors.cardElevatedBackground
    }

    val borderColor = when {
        isFocused -> colors.focusGlow
        isCurrent -> colors.orangeAccent
        isNext -> colors.primaryAccent
        else -> colors.cardBorder
    }

    val textColor = when {
        isCurrent -> colors.orangeAccent
        isNext -> colors.primaryAccent
        isPassed -> colors.textMuted
        else -> colors.textPrimary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(cardBg)
            .border(
                width = if (isFocused || isNext || isCurrent) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .focusable(interactionSource = interactionSource)
            .padding(horizontal = 12.dp, vertical = 9.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Prayer Name & Badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                color = textColor,
                fontSize = 15.sp,
                fontWeight = if (isNext || isCurrent || isFocused) FontWeight.Bold else FontWeight.Medium
            )

            if (isNext) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .background(colors.primaryAccent, RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "القادمة",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Time & Iqama
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = prefs.formatNumber(item.adjustedTimeString),
                color = if (isNext) colors.goldAccent else textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            if (item.waitMinutes > 0 && customLabel == null) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "+${prefs.formatNumber(item.waitMinutes)}د",
                    color = colors.textSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
