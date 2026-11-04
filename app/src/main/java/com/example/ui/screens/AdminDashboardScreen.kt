package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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
import com.example.ui.theme.FarasTextMuted
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite
import com.example.viewmodel.AppMode
import com.example.viewmodel.FarascooterUiState
import com.example.viewmodel.FarascooterViewModel

@Composable
fun AdminDashboardScreen(
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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FarasNeonGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = FarasBlack,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = AppStrings.get("admin_title", language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = FarasWhite
                    )
                    Text(
                        text = if (language == AppLanguage.ARABIC) "مراقبة الأسطول، التسعير الديناميكي، واعتماد الكباتن" else "Fleet supervision, surge pricing & verification",
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 1. Dynamic Surge Pricing Controls (Surge Pricing Algorithm)
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasCardBg),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = FarasAmber,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.get("admin_surge_control", language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (uiState.surgeMultiplier > 1.0) FarasAmber.copy(alpha = 0.2f) else FarasSurfaceDark)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${String.format(java.util.Locale.US, "%.1f", uiState.surgeMultiplier)}x",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (uiState.surgeMultiplier > 1.0) FarasAmber else FarasTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = AppStrings.get("admin_surge_desc", language),
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Slider(
                        value = uiState.surgeMultiplier.toFloat(),
                        onValueChange = { viewModel.setSurgeMultiplier(String.format(java.util.Locale.US, "%.1f", it).toDouble()) },
                        valueRange = 1.0f..2.0f,
                        steps = 9,
                        colors = SliderDefaults.colors(
                            thumbColor = FarasNeonGreen,
                            activeTrackColor = FarasNeonGreen,
                            inactiveTrackColor = FarasCardBorder
                        ),
                        modifier = Modifier.testTag("surge_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("1.0x (Standard)", style = MaterialTheme.typography.labelSmall, color = FarasTextMuted)
                        Text("1.5x (High Demand)", style = MaterialTheme.typography.labelSmall, color = FarasTextMuted)
                        Text("2.0x (Peak Storm)", style = MaterialTheme.typography.labelSmall, color = FarasTextMuted)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Live Fleet KPI Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KpiMetricCard(
                    title = AppStrings.get("admin_active_scooters", language),
                    value = "${uiState.nearbyScooters.size} / 5",
                    icon = Icons.Default.ElectricScooter,
                    color = FarasNeonGreen,
                    modifier = Modifier.weight(1f)
                )

                KpiMetricCard(
                    title = if (language == AppLanguage.ARABIC) "رحلات اليوم" else "Trips Today",
                    value = "142",
                    icon = Icons.Default.VerifiedUser,
                    color = FarasWhite,
                    modifier = Modifier.weight(1f)
                )

                KpiMetricCard(
                    title = if (language == AppLanguage.ARABIC) "تنبيهات SOS" else "SOS Alerts",
                    value = "${uiState.sosIncidentLogs.count { it.status == "ACTIVE_DISPATCHED" }}",
                    icon = Icons.Default.Warning,
                    color = if (uiState.sosIncidentLogs.any { it.status == "ACTIVE_DISPATCHED" }) FarasRedAlert else FarasTextSecondary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Captain Verification & Document Audit (National ID, License, Scooter Ownership, Criminal Record)
            Text(
                text = AppStrings.get("admin_captains_verification", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = FarasWhite
            )

            Text(
                text = AppStrings.get("admin_captains_desc", language),
                style = MaterialTheme.typography.labelSmall,
                color = FarasTextSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                uiState.captainsList.forEach { captain ->
                    CaptainAuditCard(
                        captain = captain,
                        language = language,
                        onToggleVerify = { viewModel.toggleCaptainVerification(captain.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Live SOS Emergency Log
            Text(
                text = AppStrings.get("admin_sos_log", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = FarasWhite
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.sosIncidentLogs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(FarasCardBg)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = FarasNeonGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.get("admin_sos_none", language),
                            style = MaterialTheme.typography.bodySmall,
                            color = FarasTextSecondary
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.sosIncidentLogs.forEach { alert ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (alert.status == "RESOLVED") FarasCardBg else FarasRedAlertBg)
                                .border(1.dp, if (alert.status == "RESOLVED") FarasCardBorder else FarasRedAlert, RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${alert.id} • ${alert.locationDescription}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = FarasWhite
                                    )
                                    Text(
                                        text = "${alert.timestamp} • ${alert.captainName}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = FarasTextSecondary
                                    )
                                }

                                if (alert.status == "ACTIVE_DISPATCHED") {
                                    Button(
                                        onClick = { viewModel.resolveSosAlert(alert.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.testTag("resolve_sos_${alert.id}")
                                    ) {
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "إنهاء البلاغ" else "Resolve",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = FarasBlack,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Text(
                                        text = if (language == AppLanguage.ARABIC) "تم التعامل" else "Resolved",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = FarasNeonGreen
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

@Composable
private fun KpiMetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(FarasCardBg)
            .border(1.dp, FarasCardBorder, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = FarasWhite
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = FarasTextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun CaptainAuditCard(
    captain: com.example.model.Captain,
    language: AppLanguage,
    onToggleVerify: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(FarasCardBg)
            .border(
                1.dp,
                if (captain.isVerified) FarasCardBorder else FarasAmber.copy(alpha = 0.5f),
                RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = captain.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = FarasWhite
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    if (captain.isVerified) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = FarasNeonGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "ID: ${captain.nationalId} • Plate: ${captain.plateNumber}",
                    style = MaterialTheme.typography.labelSmall,
                    color = FarasTextSecondary
                )

                Text(
                    text = "الفيش الجنائي سليم • رخصة سارية • خوذة إضافية متوفرة",
                    style = MaterialTheme.typography.labelSmall,
                    color = FarasNeonGreen
                )
            }

            Button(
                onClick = onToggleVerify,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (captain.isVerified) FarasSurfaceDark else FarasNeonGreen
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("captain_audit_${captain.id}")
            ) {
                Text(
                    text = if (captain.isVerified) AppStrings.get("admin_suspend", language) else AppStrings.get("admin_approve", language),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (captain.isVerified) FarasRedAlert else FarasBlack
                )
            }
        }
    }
}
