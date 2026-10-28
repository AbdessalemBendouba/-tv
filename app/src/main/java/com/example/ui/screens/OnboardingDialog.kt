package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun OnboardingDialog(
    initialUrl: String,
    onValidateAndConfirm: (String, (Boolean, String?) -> Unit) -> Unit
) {
    var textInput by remember { mutableStateOf(initialUrl) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val colors = LocalAppColors.current

    Dialog(onDismissRequest = {}) {
        Box(
            modifier = Modifier
                .width(540.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colors.cardBackground)
                .border(2.dp, colors.goldAccent, RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "مرحباً بك في تطبيق حي على الصلاة 🕌",
                    color = colors.goldAccent,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "للحصول على مواقيت الصلاة الدقيقة لمسجدك، يرجى البحث عن مسجدك أولاً على موقع mawaqit.net وإدخال رابطه أو اسمه المختصر (Slug) هنا:",
                    color = colors.textSecondary,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = textInput,
                    onValueChange = {
                        textInput = it
                        errorMessage = null
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

                // Inline Error Warning Banner if 404 or bad URL
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(colors.redAccent.copy(alpha = 0.15f))
                            .border(1.dp, colors.redAccent, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = colors.redAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errorMessage ?: "",
                                color = if (colors.isDark) Color(0xFFFECACA) else Color(0xFF991B1B),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Popular Presets for Quick Testing
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "نماذج جاهزة:",
                        color = colors.textMuted,
                        fontSize = 11.sp
                    )

                    PresetChip(label = "عين تادلس", slug = "ain-tedeles") {
                        textInput = it
                        errorMessage = null
                    }
                    PresetChip(label = "الجزائر (القدس)", slug = "mosquee-al-quds") {
                        textInput = it
                        errorMessage = null
                    }
                    PresetChip(label = "باريس", slug = "grand-mosque-paris") {
                        textInput = it
                        errorMessage = null
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                val saveInteraction = remember { MutableInteractionSource() }
                val isSaveFocused by saveInteraction.collectIsFocusedAsState()

                Button(
                    onClick = {
                        val trimmed = textInput.trim()
                        if (trimmed.isNotBlank() && !isLoading) {
                            isLoading = true
                            errorMessage = null
                            onValidateAndConfirm(trimmed) { success, error ->
                                isLoading = false
                                if (!success) {
                                    errorMessage = error ?: "التطبيق لم يعثر على هذا الرابط (خطأ 404)، يرجى التأكد وإعادة المحاولة."
                                }
                            }
                        }
                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaveFocused) colors.focusGlow else colors.primaryAccent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    interactionSource = saveInteraction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .focusable(interactionSource = saveInteraction)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Text(
                            text = "تأكيد والبدء",
                            color = if (isSaveFocused) colors.screenBackground else Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PresetChip(
    label: String,
    slug: String,
    onClick: (String) -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val isFocused by interaction.collectIsFocusedAsState()

    Button(
        onClick = { onClick("https://mawaqit.net/ar/$slug") },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFocused) FocusedBorderColor else ElevatedNavyCard
        ),
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        interactionSource = interaction,
        modifier = Modifier.focusable(interactionSource = interaction)
    ) {
        Text(
            text = label,
            color = if (isFocused) MidnightNavy else SkyBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
