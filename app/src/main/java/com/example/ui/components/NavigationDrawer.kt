package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.TvScreenTab

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.model.AppLanguage
import com.example.ui.util.AppStrings

@Composable
fun TvNavigationDrawer(
    isOpen: Boolean,
    currentTab: TvScreenTab,
    language: AppLanguage = AppLanguage.ARABIC,
    onTabSelected: (TvScreenTab) -> Unit,
    onCloseDrawer: () -> Unit,
    onExitApp: () -> Unit,
    onToggleLanguage: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    AnimatedVisibility(
        visible = isOpen,
        enter = slideInHorizontally(initialOffsetX = { -it }),
        exit = slideOutHorizontally(targetOffsetX = { -it }),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(280.dp)
                .background(colors.cardBackground.copy(alpha = 0.98f))
                .border(width = 1.dp, color = colors.cardBorder)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top: Header & Tabs
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.app_logo),
                                contentDescription = "Logo",
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.appName(language),
                                color = colors.goldAccent,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(
                            onClick = onCloseDrawer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Menu",
                                tint = colors.textSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    DrawerItem(
                        title = AppStrings.tabMain(language),
                        icon = Icons.Default.Schedule,
                        isSelected = currentTab == TvScreenTab.MAIN,
                        colors = colors,
                        onClick = { onTabSelected(TvScreenTab.MAIN) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DrawerItem(
                        title = AppStrings.tabAstronomy(language),
                        icon = Icons.Default.Explore,
                        isSelected = currentTab == TvScreenTab.ASTRONOMY,
                        colors = colors,
                        onClick = { onTabSelected(TvScreenTab.ASTRONOMY) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DrawerItem(
                        title = AppStrings.tabDailies(language),
                        icon = Icons.Default.MenuBook,
                        isSelected = currentTab == TvScreenTab.DAILIES,
                        colors = colors,
                        onClick = { onTabSelected(TvScreenTab.DAILIES) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DrawerItem(
                        title = AppStrings.tabSettings(language),
                        icon = Icons.Default.Settings,
                        isSelected = currentTab == TvScreenTab.SETTINGS,
                        colors = colors,
                        onClick = { onTabSelected(TvScreenTab.SETTINGS) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (onToggleLanguage != null) {
                        DrawerItem(
                            title = if (language == AppLanguage.ARABIC) "English (الإنجليزية)" else "العربية (Arabic)",
                            icon = Icons.Default.Language,
                            isSelected = false,
                            colors = colors,
                            onClick = onToggleLanguage
                        )
                    }
                }

                // Bottom: Exit App Button
                DrawerItem(
                    title = AppStrings.exitApp(language),
                    icon = Icons.Default.ExitToApp,
                    isSelected = false,
                    isDestructive = true,
                    colors = colors,
                    onClick = onExitApp
                )
            }
        }
    }
}

@Composable
private fun DrawerItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    colors: AppColors,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val bgColor = when {
        isFocused -> colors.focusGlow.copy(alpha = 0.25f)
        isSelected -> colors.cardElevatedBackground
        else -> Color.Transparent
    }

    val contentColor = when {
        isDestructive -> colors.redAccent
        isSelected || isFocused -> colors.goldAccent
        else -> colors.textPrimary
    }

    val borderColor = when {
        isFocused -> colors.focusGlow
        isSelected -> colors.primaryAccent
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(
                width = if (isFocused || isSelected) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .focusable(interactionSource = interactionSource)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = contentColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            color = contentColor,
            fontSize = 15.sp,
            fontWeight = if (isSelected || isFocused) FontWeight.Bold else FontWeight.Medium
        )
    }
}
