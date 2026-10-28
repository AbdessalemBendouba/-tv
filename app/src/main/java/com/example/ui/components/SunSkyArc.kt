package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.SkyBlue
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SunSkyArc(
    sunAngleRad: Float,
    isDaytime: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        val w = size.width
        val h = size.height

        val horizonY = h * 0.85f
        val centerX = w / 2f
        val radiusX = w * 0.38f
        val radiusY = h * 0.70f

        // 1. Draw Horizon Baseline
        drawLine(
            color = Color(0xFF22384D),
            start = Offset(x = w * 0.08f, y = horizonY),
            end = Offset(x = w * 0.92f, y = horizonY),
            strokeWidth = 3.dp.toPx()
        )

        // 2. Draw Elliptical Celestial Arc
        val arcTop = horizonY - radiusY
        val arcLeft = centerX - radiusX
        val arcWidth = radiusX * 2
        val arcHeight = radiusY * 2

        drawArc(
            color = if (isDaytime) Color(0xFF334E68) else Color(0xFF1E354D),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(arcLeft, arcTop),
            size = Size(arcWidth, arcHeight),
            style = Stroke(
                width = 2.5.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
            )
        )

        // 3. Compute Sphere Center using the angle
        val sphereX = centerX + radiusX * cos(sunAngleRad)
        val sphereY = horizonY + radiusY * sin(sunAngleRad)

        // 4. Draw Glow & Sphere (Sun or Moon)
        val sphereColor = if (isDaytime) CelestialGold else SkyBlue
        val glowColor = if (isDaytime) Color(0x66FBBF24) else Color(0x6638BDF8)

        // Outer Glow
        drawCircle(
            color = glowColor,
            radius = 14.dp.toPx(),
            center = Offset(sphereX, sphereY)
        )

        // Core Sphere
        drawCircle(
            color = sphereColor,
            radius = 7.dp.toPx(),
            center = Offset(sphereX, sphereY)
        )

        // Center highlight
        drawCircle(
            color = Color.White,
            radius = 3.dp.toPx(),
            center = Offset(sphereX, sphereY)
        )

        // Markers at Horizon
        drawCircle(
            color = Color(0xFF64748B),
            radius = 3.dp.toPx(),
            center = Offset(centerX - radiusX, horizonY)
        )
        drawCircle(
            color = Color(0xFF64748B),
            radius = 3.dp.toPx(),
            center = Offset(centerX + radiusX, horizonY)
        )
    }
}
