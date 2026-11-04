package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AppLanguage
import com.example.ui.components.FarascooterTopBar
import com.example.ui.components.InAppChatDialog
import com.example.ui.components.SosEmergencyDialog
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CustomerHomeScreen
import com.example.ui.screens.DriverModeScreen
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.TripHistoryScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.FarasBlack
import com.example.ui.theme.FarascooterTheme
import com.example.viewmodel.AppMode
import com.example.viewmodel.CustomerScreen
import com.example.viewmodel.FarascooterViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: FarascooterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val layoutDirection = if (uiState.language == AppLanguage.ARABIC) {
                LayoutDirection.Rtl
            } else {
                LayoutDirection.Ltr
            }

            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                FarascooterTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = FarasBlack,
                        topBar = {
                            FarascooterTopBar(
                                currentMode = uiState.currentMode,
                                language = uiState.language,
                                walletBalance = uiState.walletBalance,
                                onModeSelected = { mode -> viewModel.setAppMode(mode) },
                                onLanguageToggle = { viewModel.toggleLanguage() },
                                onWalletClicked = {
                                    viewModel.setAppMode(AppMode.CUSTOMER)
                                    viewModel.setCustomerScreen(CustomerScreen.WALLET)
                                },
                                onProfileClicked = {
                                    viewModel.setAppMode(AppMode.CUSTOMER)
                                    viewModel.setCustomerScreen(CustomerScreen.PROFILE)
                                }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .background(FarasBlack)
                        ) {
                            when (uiState.currentMode) {
                                AppMode.CUSTOMER -> {
                                    when (uiState.customerScreen) {
                                        CustomerScreen.WALLET -> {
                                            WalletScreen(
                                                uiState = uiState,
                                                viewModel = viewModel,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                        CustomerScreen.PROFILE -> {
                                            ProfileSettingsScreen(
                                                uiState = uiState,
                                                viewModel = viewModel,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                        CustomerScreen.HISTORY -> {
                                            TripHistoryScreen(
                                                uiState = uiState,
                                                viewModel = viewModel,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                        else -> {
                                            CustomerHomeScreen(
                                                uiState = uiState,
                                                viewModel = viewModel,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                    }
                                }

                                AppMode.CAPTAIN -> {
                                    DriverModeScreen(
                                        uiState = uiState,
                                        viewModel = viewModel,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                AppMode.ADMIN -> {
                                    AdminDashboardScreen(
                                        uiState = uiState,
                                        viewModel = viewModel,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }

                            // In-App Chat Modal
                            if (uiState.showChatDialog) {
                                InAppChatDialog(
                                    language = uiState.language,
                                    captainName = uiState.activeTrip?.captain?.name ?: "الكابتن سيف",
                                    messages = uiState.activeTrip?.chatMessages ?: emptyList(),
                                    inputText = uiState.chatInputText,
                                    onInputChange = { viewModel.updateChatInput(it) },
                                    onSendMessage = { viewModel.sendChatMessage() },
                                    onDismiss = { viewModel.toggleChatDialog(false) }
                                )
                            }

                            // SOS Emergency Confirmation Modal
                            if (uiState.showSosConfirmDialog) {
                                SosEmergencyDialog(
                                    language = uiState.language,
                                    onConfirmSos = { viewModel.confirmSosAlert() },
                                    onDismiss = { viewModel.triggerSosDialog(false) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
