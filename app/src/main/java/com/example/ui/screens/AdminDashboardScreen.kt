package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.model.AiMarketingCampaign
import com.example.model.Captain
import com.example.model.CaptainNeedRequest
import com.example.model.DeviceDownloadRecord
import com.example.ui.theme.FarasAmber
import com.example.ui.theme.FarasAmberBg
import com.example.ui.theme.FarasBlue
import com.example.ui.theme.FarasBlueBg
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasLightCard
import com.example.ui.theme.FarasLightGreyBg
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenDark
import com.example.ui.theme.FarasNeonGreenLight
import com.example.ui.theme.FarasRedAlert
import com.example.ui.theme.FarasRedAlertBg
import com.example.ui.theme.FarasSurfaceVariant
import com.example.ui.theme.FarasTextMuted
import com.example.ui.theme.FarasTextPrimary
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
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var selectedAdminTab by remember { mutableIntStateOf(0) }
    var broadcastMessageText by remember { mutableStateOf("") }

    BackHandler {
        viewModel.setAppMode(AppMode.CUSTOMER)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FarasLightGreyBg)
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
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FarasNeonGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = FarasWhite,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = AppStrings.get("admin_title", language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                    Text(
                        text = if (language == AppLanguage.ARABIC) "متابعة الأجهزة، الكباتن، التسويق بالذكاء الاصطناعي، والتسعير" else "Devices, Captains, AI Marketing & Pricing",
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation Tabs inside Admin Panel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(FarasWhite)
                    .border(1.dp, FarasCardBorder, RoundedCornerShape(14.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AdminTabButton(
                    title = if (language == AppLanguage.ARABIC) "الأجهزة والتنزيلات" else "Devices",
                    icon = Icons.Default.Devices,
                    isSelected = selectedAdminTab == 0,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedAdminTab = 0 }
                )
                AdminTabButton(
                    title = if (language == AppLanguage.ARABIC) "الكباتن والطلبات" else "Captains",
                    icon = Icons.Default.TwoWheeler,
                    isSelected = selectedAdminTab == 1,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedAdminTab = 1 }
                )
                AdminTabButton(
                    title = if (language == AppLanguage.ARABIC) "تسويق الـ AI ✨" else "AI Ads",
                    icon = Icons.Default.AutoAwesome,
                    isSelected = selectedAdminTab == 2,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedAdminTab = 2 }
                )
                AdminTabButton(
                    title = if (language == AppLanguage.ARABIC) "البث والتسعير" else "Ops",
                    icon = Icons.Default.Campaign,
                    isSelected = selectedAdminTab == 3,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedAdminTab = 3 }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tab 0: Devices & Downloads (الأجهزة والتنزيلات في مصر)
            if (selectedAdminTab == 0) {
                // Summary KPIs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiBox(
                        title = AppStrings.get("admin_total_downloads", language),
                        value = "48,250",
                        subtitle = "مصر 🇪🇬",
                        color = FarasNeonGreen,
                        modifier = Modifier.weight(1f)
                    )
                    KpiBox(
                        title = AppStrings.get("admin_active_devices", language),
                        value = "1,840",
                        subtitle = "متصل الآن",
                        color = FarasBlue,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Governorate Breakdown
                Card(
                    colors = CardDefaults.cardColors(containerColor = FarasWhite),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = AppStrings.get("admin_devices_governorates", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasTextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        GovernorateRow("القاهرة (Cairo)", "23,160 جهاز (48%)", FarasNeonGreen)
                        GovernorateRow("الجيزة (Giza)", "13,510 جهاز (28%)", FarasBlue)
                        GovernorateRow("الإسكندرية (Alexandria)", "6,750 جهاز (14%)", FarasAmber)
                        GovernorateRow("المنصورة وطنطا (Delta)", "4,830 جهاز (10%)", Color(0xFF8B5CF6))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Real Devices List
                Text(
                    text = if (language == AppLanguage.ARABIC) "سجل الأجهزة الحديثة التي قامت بالتنزيل" else "Recent Device Downloads",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.deviceDownloads.forEach { dev ->
                        DeviceRecordCard(record = dev)
                    }
                }
            }

            // Tab 1: Captains & Needs (إدارة الكباتن واحتياجاتهم)
            else if (selectedAdminTab == 1) {
                // Captains Needs Section
                Text(
                    text = AppStrings.get("admin_captains_needs", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary
                )
                Text(
                    text = AppStrings.get("admin_captains_needs_sub", language),
                    style = MaterialTheme.typography.labelSmall,
                    color = FarasTextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.captainNeedRequests.forEach { need ->
                        CaptainNeedCard(
                            need = need,
                            onFulfill = {
                                viewModel.fulfillCaptainNeed(need.id)
                                Toast.makeText(context, "تمت الموافقة وتجهيز الطلب للكابتن بنجاح ✅", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Captains Verification & Document Audit
                Text(
                    text = if (language == AppLanguage.ARABIC) "توثيق الكباتن والفحص الجنائي" else "Captains Verification & Records",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    uiState.captainsList.forEach { captain ->
                        CaptainAuditCardLight(
                            captain = captain,
                            language = language,
                            onToggleVerify = { viewModel.toggleCaptainVerification(captain.id) }
                        )
                    }
                }
            }

            // Tab 2: AI Social Media & Marketing Suite (الذكاء الاصطناعي والدعاية)
            else if (selectedAdminTab == 2) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = FarasWhite),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarasNeonGreen.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(FarasNeonGreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = FarasNeonGreenDark,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = AppStrings.get("admin_ai_marketing_title", language),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = FarasTextPrimary
                                )
                                Text(
                                    text = AppStrings.get("admin_ai_marketing_desc", language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FarasTextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Generate New Campaign Button
                        Button(
                            onClick = {
                                viewModel.generateAiMarketingCampaign()
                                Toast.makeText(context, "تم توليد حملة إعلانية جديدة بالذكاء الاصطناعي بنجاح! ✨", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("generate_ai_ad_btn")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = FarasWhite)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.get("admin_generate_ai_campaign", language),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = if (language == AppLanguage.ARABIC) "الحملات الإعلانية المولدة لوسائل التواصل الاجتماعي" else "Generated Social Media Campaigns",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    uiState.aiMarketingCampaigns.forEach { campaign ->
                        AiCampaignCard(
                            campaign = campaign,
                            onPublish = {
                                Toast.makeText(context, "تم نشر الإعلان تلقائياً على منصة ${campaign.platform} بنجاح! 🚀", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }

            // Tab 3: Broadcast, Pricing & Safety Ops (البث والتسعير)
            else if (selectedAdminTab == 3) {
                // Broadcast Notification Sender
                Card(
                    colors = CardDefaults.cardColors(containerColor = FarasWhite),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = FarasNeonGreen, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.get("admin_broadcast_title", language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasTextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = AppStrings.get("admin_broadcast_desc", language),
                            style = MaterialTheme.typography.labelSmall,
                            color = FarasTextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = broadcastMessageText,
                            onValueChange = { broadcastMessageText = it },
                            placeholder = { Text("اكتب رسالة الإشعار الترويجي (مثال: كود خصم 30% لسكان التجمع والدقي!)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FarasNeonGreen,
                                unfocusedBorderColor = FarasCardBorder,
                                focusedContainerColor = FarasLightGreyBg,
                                unfocusedContainerColor = FarasLightGreyBg,
                                focusedTextColor = FarasTextPrimary,
                                unfocusedTextColor = FarasTextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (broadcastMessageText.isNotBlank()) {
                                    Toast.makeText(context, "تم إرسال الإشعار لـ 48,250 جهاز في مصر بنجاح 🔔", Toast.LENGTH_SHORT).show()
                                    broadcastMessageText = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FarasTextPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = FarasWhite)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.get("admin_send_broadcast_btn", language),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dynamic Surge Slider
                Card(
                    colors = CardDefaults.cardColors(containerColor = FarasWhite),
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
                                Icon(Icons.Default.Bolt, contentDescription = null, tint = FarasAmber, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = AppStrings.get("admin_surge_control", language),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = FarasTextPrimary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(FarasAmberBg)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${String.format(java.util.Locale.US, "%.1f", uiState.surgeMultiplier)}x",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = FarasAmber
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Slider(
                            value = uiState.surgeMultiplier.toFloat(),
                            onValueChange = { viewModel.setSurgeMultiplier(String.format(java.util.Locale.US, "%.1f", it).toDouble()) },
                            valueRange = 1.0f..2.0f,
                            steps = 9,
                            colors = SliderDefaults.colors(
                                thumbColor = FarasNeonGreen,
                                activeTrackColor = FarasNeonGreen,
                                inactiveTrackColor = FarasCardBorder
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminTabButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) FarasNeonGreen else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) FarasWhite else FarasTextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) FarasWhite else FarasTextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun KpiBox(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(FarasWhite)
            .border(1.dp, FarasCardBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = FarasTextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = color)
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = FarasTextMuted)
        }
    }
}

@Composable
private fun GovernorateRow(name: String, stat: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = FarasTextPrimary)
        }
        Text(text = stat, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = FarasTextSecondary)
    }
}

@Composable
private fun DeviceRecordCard(record: DeviceDownloadRecord) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(FarasWhite)
            .border(1.dp, FarasCardBorder, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(FarasSurfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = FarasTextPrimary, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "${record.deviceModel} (${record.osType})", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = FarasTextPrimary)
                    Text(text = "${record.governorate} • ${record.downloadDate}", style = MaterialTheme.typography.labelSmall, color = FarasTextSecondary)
                }
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (record.isOnline) FarasNeonGreenLight else FarasSurfaceVariant)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = if (record.isOnline) "متصل الآن 🟢" else "غير متصل",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (record.isOnline) FarasNeonGreenDark else FarasTextMuted,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CaptainNeedCard(
    need: CaptainNeedRequest,
    onFulfill: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(FarasWhite)
            .border(1.dp, if (need.isFulfilled) FarasNeonGreen.copy(alpha = 0.4f) else FarasAmber.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = need.category, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = FarasNeonGreenDark)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "• ${need.captainName}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = FarasTextPrimary)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = need.description, style = MaterialTheme.typography.bodySmall, color = FarasTextSecondary)
                Text(text = "تاريخ الطلب: ${need.date}", style = MaterialTheme.typography.labelSmall, color = FarasTextMuted)
            }

            if (need.isFulfilled) {
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(FarasNeonGreenLight).padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("تم التوفير ✅", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = FarasNeonGreenDark)
                }
            } else {
                Button(
                    onClick = onFulfill,
                    colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("تنفيذ الطلب", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = FarasWhite)
                }
            }
        }
    }
}

@Composable
private fun AiCampaignCard(
    campaign: AiMarketingCampaign,
    onPublish: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = FarasWhite),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FarasBlueBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = campaign.platform, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = FarasBlue)
                }
                Text(text = "الميزانية المقترحة: ${campaign.suggestedBudgetEgp} ج.م", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = FarasNeonGreenDark)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = campaign.headline, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = FarasTextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = campaign.dialectCaption, style = MaterialTheme.typography.bodyMedium, color = FarasTextSecondary)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = campaign.hashtags, style = MaterialTheme.typography.labelSmall, color = FarasBlue)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "الوصول المتوقع: ${campaign.estimatedReach}", style = MaterialTheme.typography.labelSmall, color = FarasTextMuted)
                Button(
                    onClick = onPublish,
                    colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = FarasWhite, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("نشر الحملة الآن", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = FarasWhite)
                }
            }
        }
    }
}

@Composable
private fun CaptainAuditCardLight(
    captain: Captain,
    language: AppLanguage,
    onToggleVerify: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(FarasWhite)
            .border(1.dp, FarasCardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = captain.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = FarasTextPrimary)
                    Spacer(modifier = Modifier.width(6.dp))
                    if (captain.isVerified) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = FarasNeonGreen, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "الرقم القومي: ${captain.nationalId} • لوحة: ${captain.plateNumber}", style = MaterialTheme.typography.labelSmall, color = FarasTextSecondary)
                Text(text = "${captain.licenseStatus} • الفيش الجنائي معتمد ومفحوص", style = MaterialTheme.typography.labelSmall, color = FarasNeonGreenDark)
            }

            Button(
                onClick = onToggleVerify,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (captain.isVerified) FarasSurfaceVariant else FarasNeonGreen
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (captain.isVerified) AppStrings.get("admin_suspend", language) else AppStrings.get("admin_approve", language),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (captain.isVerified) FarasRedAlert else FarasWhite
                )
            }
        }
    }
}
