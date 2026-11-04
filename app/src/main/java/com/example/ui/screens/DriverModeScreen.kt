package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.ui.theme.FarasAmber
import com.example.ui.theme.FarasBlack
import com.example.ui.theme.FarasCardBg
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasRedAlert
import com.example.ui.theme.FarasRedAlertBg
import com.example.ui.theme.FarasSurfaceDark
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite
import com.example.viewmodel.AppMode
import com.example.viewmodel.FarascooterUiState
import com.example.viewmodel.FarascooterViewModel

@Composable
fun DriverModeScreen(
    uiState: FarascooterUiState,
    viewModel: FarascooterViewModel,
    modifier: Modifier = Modifier
) {
    val language = uiState.language
    val scrollState = rememberScrollState()

    BackHandler {
        viewModel.setAppMode(AppMode.CUSTOMER)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FarasBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // Online / Offline Master Switch Card
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasCardBg),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = if (uiState.isDriverOnline) FarasNeonGreen else FarasCardBorder
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(if (uiState.isDriverOnline) FarasNeonGreen else FarasSurfaceDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PowerSettingsNew,
                                contentDescription = null,
                                tint = if (uiState.isDriverOnline) FarasBlack else FarasTextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (uiState.isDriverOnline) AppStrings.get("driver_online_status", language) else AppStrings.get("driver_offline_status", language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                            Text(
                                text = if (uiState.isDriverOnline) "DXB-9281-S • Xiaomi Pro Max" else if (language == AppLanguage.ARABIC) "اضغط للاتصال بالشبكة" else "Tap switch to go online",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (uiState.isDriverOnline) FarasNeonGreen else FarasTextSecondary
                            )
                        }
                    }

                    Switch(
                        checked = uiState.isDriverOnline,
                        onCheckedChange = { viewModel.toggleDriverOnline() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = FarasBlack,
                            checkedTrackColor = FarasNeonGreen,
                            uncheckedThumbColor = FarasTextSecondary,
                            uncheckedTrackColor = FarasSurfaceDark
                        ),
                        modifier = Modifier.testTag("driver_online_toggle")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mandatory Safety Checklist (Helmets standard)
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarasNeonGreen.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Default.HealthAndSafety,
                            contentDescription = "Helmet Check",
                            tint = if (uiState.driverHelmetConfirmed) FarasNeonGreen else FarasAmber,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppStrings.get("driver_safety_check", language),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                            Text(
                                text = if (language == AppLanguage.ARABIC) "شرط تشغيلي إلزامي قبل استقبال الركاب" else "Mandatory policy before accepting rides",
                                style = MaterialTheme.typography.labelSmall,
                                color = FarasTextSecondary
                            )
                        }
                    }

                    Switch(
                        checked = uiState.driverHelmetConfirmed,
                        onCheckedChange = { viewModel.toggleDriverHelmetCheck() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = FarasBlack,
                            checkedTrackColor = FarasNeonGreen
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Today's Earnings and Stats Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Earnings
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(FarasCardBg)
                        .border(1.dp, FarasCardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = AppStrings.get("driver_earnings_today", language),
                            style = MaterialTheme.typography.labelMedium,
                            color = FarasTextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${uiState.driverTodayEarnings.toInt()}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = AppStrings.get("currency", language),
                                style = MaterialTheme.typography.titleSmall,
                                color = FarasNeonGreen
                            )
                        }
                    }
                }

                // Completed Trips
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(FarasCardBg)
                        .border(1.dp, FarasCardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = AppStrings.get("driver_completed_trips", language),
                            style = MaterialTheme.typography.labelMedium,
                            color = FarasTextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${uiState.driverCompletedCount}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasWhite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Simulated Dispatch Order Trigger (For testing the 30s Captain workflow)
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasCardBg),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "محاكي استقبال طلبات الكابتن" else "Driver Dispatch Simulation",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = FarasWhite
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (language == AppLanguage.ARABIC) "اختبر استلام طلب ركوب سكوتر فوري مع مهلة قبول 30 ثانية ونغمة تنبيه." else "Simulate incoming order pop-up with 30s countdown and GPS dispatch.",
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.simulateDriverIncomingOrder() },
                        enabled = uiState.isDriverOnline && !uiState.hasDriverIncomingOrder,
                        colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("simulate_driver_order_btn")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = FarasBlack)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "إرسال طلب تجريبي للكابتن 🛵" else "Simulate Incoming Ride Request",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = FarasBlack
                            )
                        }
                    }
                }
            }

            // Incoming Order Dialog / Banner (30-second decision timer)
            AnimatedVisibility(visible = uiState.hasDriverIncomingOrder) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = FarasSurfaceDark),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, FarasNeonGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = AppStrings.get("incoming_order", language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasNeonGreen
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(FarasRedAlertBg)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "30s",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = FarasRedAlert
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "32.0 ${AppStrings.get("currency", language)}",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasWhite
                        )

                        Text(
                            text = if (language == AppLanguage.ARABIC) "مشوار سكوتر: برج خليفة ➔ مركز دبي المالي (2.8 كم)" else "Ride: Downtown ➔ DIFC (2.8 km)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = FarasTextSecondary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.declineDriverOrder() },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("driver_decline_btn")
                            ) {
                                Text(
                                    text = AppStrings.get("decline_order", language),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = FarasRedAlert
                                )
                            }

                            Button(
                                onClick = { viewModel.acceptDriverOrder() },
                                colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1.4f)
                                    .testTag("driver_accept_btn")
                            ) {
                                Text(
                                    text = AppStrings.get("accept_order", language),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = FarasBlack
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
