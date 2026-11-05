package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AppLanguage
import com.example.ui.components.ComplaintsSheet
import com.example.ui.components.FarascooterTopBar
import com.example.ui.components.InAppChatDialog
import com.example.ui.components.PaymentMethodsSheet
import com.example.ui.components.ReferralDialog
import com.example.ui.components.SosEmergencyDialog
import com.example.ui.components.TripDetailDialog
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CustomerHomeScreen
import com.example.ui.screens.DriverModeScreen
import com.example.ui.screens.PermissionsScreen
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.TripHistoryScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.FarasLightGreyBg
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
                    // Check First-Time Flow: 1) Auth, 2) Permissions, 3) Main App
                    if (!uiState.isAuthCompleted) {
                        AuthScreen(
                            language = uiState.language,
                            onAuthSuccess = { name, phone, email ->
                                viewModel.loginWithOtp(name, phone, email)
                            },
                            onSkipGuest = {
                                viewModel.skipAuthAsGuest()
                            }
                        )
                    } else if (!uiState.hasPermissionsGranted) {
                        PermissionsScreen(
                            language = uiState.language,
                            onPermissionsComplete = {
                                viewModel.completePermissions()
                            }
                        )
                    } else {
                        // Main Application Shell
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            containerColor = FarasLightGreyBg,
                            topBar = {
                                FarascooterTopBar(
                                    currentMode = uiState.currentMode,
                                    language = uiState.language,
                                    onModeSelected = { mode -> viewModel.setAppMode(mode) },
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
                                    .background(FarasLightGreyBg)
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

                                // 1. In-App Chat Dialog
                                if (uiState.showChatDialog) {
                                    InAppChatDialog(
                                        language = uiState.language,
                                        captainName = uiState.activeTrip?.captain?.name ?: "كابتن سيف الدين",
                                        messages = uiState.activeTrip?.chatMessages ?: emptyList(),
                                        inputText = uiState.chatInputText,
                                        onInputChange = { viewModel.updateChatInput(it) },
                                        onSendMessage = { viewModel.sendChatMessage() },
                                        onDismiss = { viewModel.toggleChatDialog(false) }
                                    )
                                }

                                // 2. SOS Emergency Confirmation Dialog
                                if (uiState.showSosConfirmDialog) {
                                    SosEmergencyDialog(
                                        language = uiState.language,
                                        onConfirmSos = { viewModel.confirmSosAlert() },
                                        onDismiss = { viewModel.triggerSosDialog(false) }
                                    )
                                }

                                // 3. Referral & Invite Friends Dialog
                                if (uiState.showReferralDialog) {
                                    ReferralDialog(
                                        language = uiState.language,
                                        referralCode = uiState.userProfile.referralCode,
                                        contactsList = uiState.contactInvites,
                                        onInviteContact = { contactId ->
                                            viewModel.inviteContact(contactId)
                                        },
                                        onDismiss = { viewModel.toggleReferralDialog(false) }
                                    )
                                }

                                // 4. Payment Methods Management Sheet
                                if (uiState.showPaymentSheet) {
                                    PaymentMethodsSheet(
                                        language = uiState.language,
                                        savedCards = uiState.savedCards,
                                        savedWallets = uiState.savedWallets,
                                        onAddCard = { card -> viewModel.addSavedCard(card) },
                                        onAddWallet = { wallet -> viewModel.addSavedWallet(wallet) },
                                        onSetDefaultCard = { cardId -> viewModel.setDefaultCard(cardId) },
                                        onBack = { viewModel.togglePaymentSheet(false) }
                                    )
                                }

                                // 5. Customer Complaints & Support Sheet
                                if (uiState.showComplaintsSheet) {
                                    ComplaintsSheet(
                                        language = uiState.language,
                                        tickets = uiState.complaintTickets,
                                        initialTripId = uiState.complaintTargetTripId,
                                        initialCaptainName = uiState.complaintTargetCaptainName,
                                        onSubmitComplaint = { ticket ->
                                            viewModel.submitComplaint(ticket)
                                        },
                                        onDismiss = { viewModel.toggleComplaintsSheet(false) }
                                    )
                                }

                                // 6. Past Trip Details & Receipt Breakdown Dialog
                                uiState.selectedTripForDetail?.let { trip ->
                                    TripDetailDialog(
                                        language = uiState.language,
                                        trip = trip,
                                        onFileComplaint = { tripId, captainName ->
                                            viewModel.dismissTripDetail()
                                            viewModel.toggleComplaintsSheet(true, tripId, captainName)
                                        },
                                        onDismiss = { viewModel.dismissTripDetail() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
