package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.TvNavigationDrawer
import com.example.ui.screens.*
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LocalAppColors
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.PrayerTvUiState
import com.example.ui.viewmodel.PrayerTvViewModel
import com.example.ui.viewmodel.TvScreenTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: PrayerTvViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = uiState.preferences.isDarkMode) {
                // TV Back button handling
                BackHandler {
                    when {
                        uiState.isDrawerOpen -> viewModel.toggleDrawer(false)
                        uiState.activeTab != TvScreenTab.MAIN -> viewModel.selectTab(TvScreenTab.MAIN)
                        else -> finish()
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LocalAppColors.current.screenBackground
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Main Tab Views
                        when (uiState.activeTab) {
                            TvScreenTab.MAIN -> {
                                MainBoardScreen(
                                    uiState = uiState,
                                    onOpenDrawer = { viewModel.toggleDrawer(true) }
                                )
                            }
                            TvScreenTab.ASTRONOMY -> {
                                AstronomyScreen(
                                    uiState = uiState,
                                    onBack = { viewModel.selectTab(TvScreenTab.MAIN) },
                                    onOpenDrawer = { viewModel.toggleDrawer(true) }
                                )
                            }
                            TvScreenTab.DAILIES -> {
                                DailiesScreen(
                                    uiState = uiState,
                                    onBack = { viewModel.selectTab(TvScreenTab.MAIN) },
                                    onOpenDrawer = { viewModel.toggleDrawer(true) },
                                    onIncrementDhikr = { viewModel.incrementDhikr() },
                                    onResetDhikr = { viewModel.resetDhikr() }
                                )
                            }
                            TvScreenTab.SETTINGS -> {
                                SettingsScreen(
                                    uiState = uiState,
                                    onBack = { viewModel.selectTab(TvScreenTab.MAIN) },
                                    onOpenDrawer = { viewModel.toggleDrawer(true) },
                                    onSavePreferences = { viewModel.updatePreferences(it) },
                                    onRestoreDefaults = { viewModel.restoreDefaultSettings() },
                                    onSwitchMosque = { viewModel.switchMosque(it) },
                                    onAddMosque = { name, url -> viewModel.addMosque(name, url) },
                                    onDeleteMosque = { viewModel.removeMosque(it) },
                                    onTestAthan = { viewModel.testAthanSound() },
                                    onStopAthan = { viewModel.stopAthanSound() }
                                )
                            }
                        }

                        // Side Navigation Rail Drawer Overlay (Accessible on any tab)
                        TvNavigationDrawer(
                            isOpen = uiState.isDrawerOpen,
                            currentTab = uiState.activeTab,
                            onTabSelected = { viewModel.selectTab(it) },
                            onCloseDrawer = { viewModel.toggleDrawer(false) },
                            onExitApp = { finish() }
                        )

                        // Loading Indicator
                        if (uiState.isLoading && !uiState.isOnboardingVisible) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = CelestialGold)
                            }
                        }

                        // First Launch Onboarding Dialog with validation
                        if (uiState.isOnboardingVisible) {
                            OnboardingDialog(
                                initialUrl = uiState.preferences.mosqueUrl,
                                onValidateAndConfirm = { url, callback ->
                                    viewModel.validateAndSetMosqueUrl(url, callback)
                                }
                            )
                        }

                        // Error Banner Toast
                        uiState.errorMessage?.let { error ->
                            if (!uiState.isOnboardingVisible) {
                                Snackbar(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(16.dp),
                                    action = {
                                        TextButton(onClick = { viewModel.loadSchedule() }) {
                                            Text("إعادة المحاولة", color = CelestialGold)
                                        }
                                    }
                                ) {
                                    Text(error, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
