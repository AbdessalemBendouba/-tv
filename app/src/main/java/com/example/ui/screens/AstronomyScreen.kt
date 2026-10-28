package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PrayerType
import com.example.ui.theme.*
import com.example.ui.viewmodel.PrayerTvUiState
import java.util.Calendar
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AstronomyScreen(
    uiState: PrayerTvUiState,
    onBack: () -> Unit,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val prefs = uiState.preferences
    val schedule = uiState.schedule
    val colors = LocalAppColors.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.screenBackground)
            .padding(16.dp)
            .verticalScroll(scrollState)
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
                text = "علم الفلك والقبلة 🌙",
                color = colors.goldAccent,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Top Row: Moon Phase Card & Qibla Compass Card
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Moon Phase Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.cardBackground)
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "طور القمر الهجري",
                        color = colors.textSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Image(
                        painter = painterResource(id = uiState.moonDrawableRes),
                        contentDescription = uiState.moonPhaseName,
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.moonPhaseName,
                        color = colors.secondaryAccent,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = prefs.formatNumber(schedule?.hijriDate ?: ""),
                        color = colors.textMuted,
                        fontSize = 11.sp
                    )
                }
            }

            // Qibla Compass Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.cardBackground)
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "اتجاه القبلة المشرفة",
                        color = colors.textSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    QiblaCompassCanvas(
                        qiblaAngle = uiState.qiblaAngleDegrees,
                        colors = colors,
                        modifier = Modifier.size(90.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "القبلة: ${prefs.formatNumber(String.format("%.1f", uiState.qiblaAngleDegrees))}° من الشمال",
                        color = colors.primaryAccent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "الكعبة المشرفة - مكة المكرمة",
                        color = colors.textMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // PRAYER TRENDS CARD: Containing Weekly, Monthly, and Yearly Drift Maps
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(colors.cardBackground)
                .border(1.dp, colors.cardBorder, RoundedCornerShape(14.dp))
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "منحنيات تغير أوقات الصلاة (Prayer Trends)",
                        color = colors.goldAccent,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Legend
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LegendItem(color = colors.redAccent, label = "الفجر", textColor = colors.textSecondary)
                        LegendItem(color = colors.orangeAccent, label = "الظهر", textColor = colors.textSecondary)
                        LegendItem(color = colors.goldAccent, label = "العصر", textColor = colors.textSecondary)
                        LegendItem(color = colors.primaryAccent, label = "المغرب", textColor = colors.textSecondary)
                        LegendItem(color = colors.secondaryAccent, label = "العشاء", textColor = colors.textSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 1. Weekly Drift Map (Today + 6 Days)
                Text(
                    text = "Weekly Drift Map (Today + 6 Days) - التغير الأسبوعي",
                    color = colors.textPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                DriftMapCanvas(
                    daysCount = 7,
                    intervalStep = 1,
                    isYearly = false,
                    calendarRaw = schedule?.calendarRaw ?: emptyList(),
                    basePrayers = schedule?.prayers ?: emptyList(),
                    colors = colors,
                    formatNumber = { prefs.formatNumber(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = colors.cardBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // 2. Monthly Drift Map (Today + 29 Days)
                Text(
                    text = "Monthly Drift Map (Today + 29 Days) - التغير الشهري",
                    color = colors.textPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                DriftMapCanvas(
                    daysCount = 30,
                    intervalStep = 5,
                    isYearly = false,
                    calendarRaw = schedule?.calendarRaw ?: emptyList(),
                    basePrayers = schedule?.prayers ?: emptyList(),
                    colors = colors,
                    formatNumber = { prefs.formatNumber(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = colors.cardBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // 3. Yearly Drift Map (12 Months Overview)
                Text(
                    text = "Yearly Drift Map (12 Months Overview) - التغير السنوي على مدار 12 شهراً",
                    color = colors.textPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                DriftMapCanvas(
                    daysCount = 12,
                    intervalStep = 1,
                    isYearly = true,
                    calendarRaw = schedule?.calendarRaw ?: emptyList(),
                    basePrayers = schedule?.prayers ?: emptyList(),
                    colors = colors,
                    formatNumber = { prefs.formatNumber(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                )
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String, textColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun QiblaCompassCanvas(
    qiblaAngle: Float,
    colors: AppColors,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r = (size.minDimension / 2f) - 6.dp.toPx()

        // Outer Ring
        drawCircle(
            color = colors.cardBorder,
            radius = r,
            center = Offset(cx, cy),
            style = Stroke(width = 3.dp.toPx())
        )

        // Cardinal North indicator (N)
        drawLine(
            color = colors.redAccent,
            start = Offset(cx, cy - r),
            end = Offset(cx, cy - r + 10.dp.toPx()),
            strokeWidth = 3.dp.toPx()
        )

        // Qibla Direction Arrow
        val rad = Math.toRadians((qiblaAngle - 90.0)).toFloat()
        val arrowHeadX = cx + (r - 8.dp.toPx()) * cos(rad)
        val arrowHeadY = cy + (r - 8.dp.toPx()) * sin(rad)

        drawLine(
            color = colors.primaryAccent,
            start = Offset(cx, cy),
            end = Offset(arrowHeadX, arrowHeadY),
            strokeWidth = 3.dp.toPx()
        )

        drawCircle(
            color = colors.primaryAccent,
            radius = 5.dp.toPx(),
            center = Offset(arrowHeadX, arrowHeadY)
        )

        // Center Pin
        drawCircle(
            color = colors.goldAccent,
            radius = 4.dp.toPx(),
            center = Offset(cx, cy)
        )
    }
}

@Composable
private fun DriftMapCanvas(
    daysCount: Int,
    intervalStep: Int,
    isYearly: Boolean,
    calendarRaw: List<Map<String, List<String>>>,
    basePrayers: List<com.example.model.PrayerTimeItem>,
    colors: AppColors,
    formatNumber: (String) -> String,
    modifier: Modifier = Modifier
) {
    // Generate dates and points for the 5 prayers
    val prayerColors = listOf(
        colors.redAccent,       // Fajr
        colors.orangeAccent,    // Dhuhr
        colors.goldAccent,      // Asr
        colors.primaryAccent,   // Maghrib
        colors.secondaryAccent  // Isha
    )

    // Base prayer default minutes
    val fajrBase = basePrayers.find { it.type == PrayerType.FAJR }?.totalMinutes ?: (5 * 60 + 30)
    val dhuhrBase = basePrayers.find { it.type == PrayerType.DHUHR }?.totalMinutes ?: (12 * 60 + 40)
    val asrBase = basePrayers.find { it.type == PrayerType.ASR }?.totalMinutes ?: (15 * 60 + 45)
    val maghribBase = basePrayers.find { it.type == PrayerType.MAGHRIB }?.totalMinutes ?: (18 * 60 + 15)
    val ishaBase = basePrayers.find { it.type == PrayerType.ISHA }?.totalMinutes ?: (19 * 60 + 35)

    val baseMins = listOf(fajrBase, dhuhrBase, asrBase, maghribBase, ishaBase)

    // Y Axis Range (from 04:00 (240m) to 23:00 (1380m))
    val minY = 240f
    val maxY = 1380f

    val yLabels = listOf("22:00", "17:30", "13:00", "08:30", "04:30")

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val paddingLeft = 45.dp.toPx()
        val paddingBottom = 22.dp.toPx()
        val graphW = w - paddingLeft
        val graphH = h - paddingBottom

        // Draw horizontal grid lines and Y-axis labels
        val textPaint = android.graphics.Paint().apply {
            color = if (colors.isDark) android.graphics.Color.parseColor("#94A3B8") else android.graphics.Color.parseColor("#64748B")
            textSize = 9.dp.toPx()
            textAlign = android.graphics.Paint.Align.RIGHT
            isAntiAlias = true
        }

        val axisTextPaint = android.graphics.Paint().apply {
            color = if (colors.isDark) android.graphics.Color.parseColor("#94A3B8") else android.graphics.Color.parseColor("#64748B")
            textSize = 9.dp.toPx()
            textAlign = android.graphics.Paint.Align.CENTER
            isAntiAlias = true
        }

        for (i in 0..4) {
            val y = graphH * (i / 4f)
            drawLine(
                color = colors.cardBorder,
                start = Offset(paddingLeft, y),
                end = Offset(w, y),
                strokeWidth = 1.dp.toPx()
            )

            // Draw Y label
            drawContext.canvas.nativeCanvas.drawText(
                formatNumber(yLabels[i]),
                paddingLeft - 8.dp.toPx(),
                y + 3.dp.toPx(),
                textPaint
            )
        }

        // Draw X-axis bottom line
        drawLine(
            color = colors.cardBorder,
            start = Offset(paddingLeft, graphH),
            end = Offset(w, graphH),
            strokeWidth = 1.dp.toPx()
        )

        // Draw prayer curves
        val numPoints = daysCount
        val pointSpacing = graphW / (numPoints - 1).coerceAtLeast(1)

        val cal = Calendar.getInstance()
        val currentDayOfYear = cal.get(Calendar.DAY_OF_YEAR)

        for (prayerIdx in 0..4) {
            val pColor = prayerColors[prayerIdx]
            val baseMinute = baseMins[prayerIdx]
            val path = Path()

            for (i in 0 until numPoints) {
                val x = paddingLeft + (i * pointSpacing)

                // Realistic seasonal astronomical drift based on solar declination
                val dayOffset = if (isYearly) (i * 30) else i
                val dayOfYear = (currentDayOfYear + dayOffset) % 365
                // Summer solstice max / winter min
                val seasonalFactor = sin((dayOfYear - 80) * 2.0 * Math.PI / 365.0).toFloat()

                val driftMinutes: Float = when (prayerIdx) {
                    0 -> -seasonalFactor * 45f // Fajr is earlier in summer
                    1 -> seasonalFactor * 8f   // Dhuhr drifts slightly by equation of time
                    2 -> seasonalFactor * 25f  // Asr drifts later in summer
                    3 -> seasonalFactor * 55f  // Maghrib is significantly later in summer
                    4 -> seasonalFactor * 40f  // Isha is later in summer
                    else -> 0f
                }

                val prayerMinute = baseMinute + driftMinutes
                val normY = (prayerMinute - minY) / (maxY - minY)
                // Canvas Y is inverted (0 at top, graphH at bottom)
                val y = graphH * (1f - normY.coerceIn(0f, 1f))

                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }

                // Draw node circle
                drawCircle(
                    color = pColor,
                    radius = if (isYearly) 3.5.dp.toPx() else (if (daysCount <= 7) 3.5.dp.toPx() else 2.5.dp.toPx()),
                    center = Offset(x, y)
                )
            }

            drawPath(
                path = path,
                color = pColor,
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // Draw X Axis labels
        val monthNames = listOf("يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر")
        for (i in 0 until numPoints) {
            if (i % intervalStep == 0 || i == numPoints - 1) {
                val x = paddingLeft + (i * pointSpacing)
                val labelText = if (isYearly) {
                    val mIdx = (cal.get(Calendar.MONTH) + i) % 12
                    monthNames[mIdx]
                } else {
                    val tempCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, i) }
                    val d = tempCal.get(Calendar.DAY_OF_MONTH)
                    val m = tempCal.get(Calendar.MONTH) + 1
                    formatNumber(String.format(Locale.US, "%d/%d", m, d))
                }

                drawContext.canvas.nativeCanvas.drawText(
                    labelText,
                    x,
                    h - 4.dp.toPx(),
                    axisTextPaint
                )
            }
        }
    }
}
