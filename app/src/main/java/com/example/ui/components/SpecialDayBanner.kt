package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SpecialDay
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.TextPrimary

@Composable
fun SpecialDayBanner(
    specialDay: SpecialDay,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = specialDay.isSpecial,
        enter = fadeIn() + expandVertically()
    ) {
        val (title, subtitle, borderColors, bgBrush) = when (specialDay) {
            is SpecialDay.EidAlFitr -> {
                Quadruple(
                    specialDay.title,
                    specialDay.subtitle,
                    listOf(EmeraldGreen, CelestialGold),
                    Brush.horizontalGradient(listOf(Color(0xFF064E3B), Color(0xFF0F392B)))
                )
            }
            is SpecialDay.EidAlAdha -> {
                Quadruple(
                    specialDay.title,
                    specialDay.subtitle,
                    listOf(CelestialGold, EmeraldGreen),
                    Brush.horizontalGradient(listOf(Color(0xFF78350F), Color(0xFF064E3B)))
                )
            }
            is SpecialDay.Ramadan -> {
                Quadruple(
                    specialDay.title,
                    specialDay.subtitle,
                    listOf(CelestialGold, Color(0xFF38BDF8)),
                    Brush.horizontalGradient(listOf(Color(0xFF1E3A8A), Color(0xFF0F172A)))
                )
            }
            is SpecialDay.DayOfArafah -> {
                Quadruple(
                    specialDay.title,
                    specialDay.subtitle,
                    listOf(CelestialGold, Color.White),
                    Brush.horizontalGradient(listOf(Color(0xFF713F12), Color(0xFF1F2937)))
                )
            }
            is SpecialDay.Ashura -> {
                Quadruple(
                    specialDay.title,
                    specialDay.subtitle,
                    listOf(Color(0xFF38BDF8), EmeraldGreen),
                    Brush.horizontalGradient(listOf(Color(0xFF0C4A6E), Color(0xFF064E3B)))
                )
            }
            is SpecialDay.MawlidAlNabi -> {
                Quadruple(
                    specialDay.title,
                    specialDay.subtitle,
                    listOf(Color(0xFFF472B6), CelestialGold),
                    Brush.horizontalGradient(listOf(Color(0xFF831843), Color(0xFF1E293B)))
                )
            }
            is SpecialDay.IslamicNewYear -> {
                Quadruple(
                    specialDay.title,
                    specialDay.subtitle,
                    listOf(EmeraldGreen, Color(0xFF38BDF8)),
                    Brush.horizontalGradient(listOf(Color(0xFF064E3B), Color(0xFF0F172A)))
                )
            }
            is SpecialDay.IsraWalMiraj -> {
                Quadruple(
                    specialDay.title,
                    specialDay.subtitle,
                    listOf(Color(0xFFA855F7), CelestialGold),
                    Brush.horizontalGradient(listOf(Color(0xFF581C87), Color(0xFF0F172A)))
                )
            }
            is SpecialDay.MidShaban -> {
                Quadruple(
                    specialDay.title,
                    specialDay.subtitle,
                    listOf(Color(0xFF38BDF8), CelestialGold),
                    Brush.horizontalGradient(listOf(Color(0xFF075985), Color(0xFF0F172A)))
                )
            }
            SpecialDay.None -> Quadruple("", "", emptyList(), Brush.linearGradient(emptyList()))
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .background(bgBrush, RoundedCornerShape(10.dp))
                .border(
                    width = 1.5.dp,
                    brush = Brush.horizontalGradient(borderColors),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    color = CelestialGold,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
