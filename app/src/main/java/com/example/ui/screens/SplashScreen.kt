package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.LocalAppColors
import com.example.ui.util.AppStrings
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    language: AppLanguage,
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }
    val colors = LocalAppColors.current

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(700))
        scale.animateTo(1f, animationSpec = tween(700))
        delay(1300)
        onSplashFinished()
    }

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF002142)) // Authentic Islamic deep navy from app logo
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onSplashFinished
            )
            .focusable(interactionSource = interactionSource),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
                .padding(32.dp)
        ) {
            // App Logo with glowing golden border
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(3.dp, CelestialGold, CircleShape)
                    .background(Color(0xFF001933)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = AppStrings.appName(language),
                    modifier = Modifier.size(110.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Name
            Text(
                text = AppStrings.appName(language),
                color = CelestialGold,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            if (language == AppLanguage.ARABIC) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Come to Prayer",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = AppStrings.appSubtitle(language),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator(
                color = CelestialGold,
                strokeWidth = 2.5.dp,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
