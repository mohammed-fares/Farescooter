package com.example

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.createDynamicTypography
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

            val currentFont = if (uiState.language == AppLanguage.ARABIC) {
                uiState.selectedArabicFont
            } else {
                uiState.selectedEnglishFont
            }

            val appPrimaryColor = Color(uiState.customPrimaryColorHex)
            val appBackgroundColor = Color(uiState.customBackgroundColorHex)

            // Permissions Launcher to request required runtime permissions
            val permissionsLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { _ ->
                viewModel.completePermissions()
            }

            // Immediately launch system permissions when auth completes
            LaunchedEffect(uiState.isAuthCompleted, uiState.hasPermissionsGranted) {
                if (uiState.isAuthCompleted && !uiState.hasPermissionsGranted) {
                    val perms = mutableListOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_CONTACTS
                    )
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        perms.add(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    permissionsLauncher.launch(perms.toTypedArray())
                }
            }

            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                FarascooterTheme(
                    primaryColor = appPrimaryColor,
                    backgroundColor = appBackgroundColor,
                    typography = createDynamicTypography(currentFont)
                ) {
                    // Check First-Time Flow: 1) Auth, 2) Permissions, 3) Main App
                    if (!uiState.isAuthCompleted) {
                        AuthScreen(
                            language = uiState.language,
                            onCustomerSuccess = { name, phone, email ->
                                viewModel.registerAsCustomer(name, phone, email)
                            },
                            onCaptainSuccess = { name, phone, email, model, plate, nid ->
                                viewModel.registerAsCaptain(name, phone, email, model, plate, nid)
                            },
                            onAdminLogin = { u, p ->
                                viewModel.loginAsAdmin(u, p)
                            },
                            onSkipGuest = {
                                viewModel.skipAuthAsGuest()
                            }
                        )
                    } else if (!uiState.hasPermissionsGranted) {
                        PermissionsScreen(
                            language = uiState.language,
                            onPermissionsComplete = {
                                val perms = mutableListOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.READ_CONTACTS
                                )
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                    perms.add(Manifest.permission.POST_NOTIFICATIONS)
                                }
                                permissionsLauncher.launch(perms.toTypedArray())
                                viewModel.completePermissions()
                            }
                        )
                    } else {
                        // Main Application Shell
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            containerColor = appBackgroundColor,
                            topBar = {
                                FarascooterTopBar(
                                    currentMode = uiState.currentMode,
                                    userRole = uiState.currentUserRole,
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
                                    .background(appBackgroundColor)
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
