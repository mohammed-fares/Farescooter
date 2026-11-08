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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.model.AppFontFamily
import com.example.model.Captain
import com.example.model.CaptainNeedRequest
import com.example.model.CaptainStatus
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
                    .horizontalScroll(rememberScrollState())
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AdminTabButton(
                    title = if (language == AppLanguage.ARABIC) "الأجهزة والتنزيلات" else "Devices",
                    icon = Icons.Default.Devices,
                    isSelected = selectedAdminTab == 0,
                    onClick = { selectedAdminTab = 0 }
                )
                AdminTabButton(
                    title = if (language == AppLanguage.ARABIC) "إدارة الكباتن" else "Captains",
                    icon = Icons.Default.TwoWheeler,
                    isSelected = selectedAdminTab == 1,
                    onClick = { selectedAdminTab = 1 }
                )
                AdminTabButton(
                    title = if (language == AppLanguage.ARABIC) "تسويق AI ✨" else "AI Ads",
                    icon = Icons.Default.AutoAwesome,
                    isSelected = selectedAdminTab == 2,
                    onClick = { selectedAdminTab = 2 }
                )
                AdminTabButton(
                    title = if (language == AppLanguage.ARABIC) "البث والتسعير" else "Ops",
                    icon = Icons.Default.Campaign,
                    isSelected = selectedAdminTab == 3,
                    onClick = { selectedAdminTab = 3 }
                )
                AdminTabButton(
                    title = if (language == AppLanguage.ARABIC) "المظهر والخطوط 🎨" else "Theme & Fonts",
                    icon = Icons.Default.Palette,
                    isSelected = selectedAdminTab == 4,
                    onClick = { selectedAdminTab = 4 }
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

                // Captains Governance & Oversight (إدارة الكباتن والموافقة/الحظر/التوقيف)
                Text(
                    text = if (language == AppLanguage.ARABIC) "إدارة ومراقبة أداء الكباتن" else "Captains Governance & Performance",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary
                )
                Text(
                    text = if (language == AppLanguage.ARABIC) "الموافقة على الكباتن الجدد، الحظر، التوقيف المؤقت، ومراقبة التقييمات" else "Approve, suspend, block captains and monitor performance",
                    style = MaterialTheme.typography.labelSmall,
                    color = FarasTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    uiState.captainsList.forEach { captain ->
                        CaptainAuditCardLight(
                            captain = captain,
                            language = language,
                            onApprove = {
                                viewModel.approveCaptain(captain.id)
                                Toast.makeText(context, "تم اعتماد وتفعيل الكابتن ${captain.name} بنجاح ✅", Toast.LENGTH_SHORT).show()
                            },
                            onSuspend = {
                                viewModel.suspendCaptain(captain.id)
                                Toast.makeText(context, "تم توقيف الكابتن ${captain.name} مؤقتاً ⏸️", Toast.LENGTH_SHORT).show()
                            },
                            onBlock = {
                                viewModel.blockCaptain(captain.id)
                                Toast.makeText(context, "تم حظر الكابتن ${captain.name} نهائياً 🚫", Toast.LENGTH_SHORT).show()
                            },
                            onReactivate = {
                                viewModel.reactivateCaptain(captain.id)
                                Toast.makeText(context, "تم إعادة تفعيل الكابتن ${captain.name} بنجاح ⚡", Toast.LENGTH_SHORT).show()
                            }
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

            // Tab 4: Live Theme & Fonts Customization (المظهر والخطوط)
            else if (selectedAdminTab == 4) {
                ThemeCustomizationSection(
                    uiState = uiState,
                    viewModel = viewModel,
                    language = language
                )
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
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) FarasWhite else FarasTextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
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
    onApprove: () -> Unit,
    onSuspend: () -> Unit,
    onBlock: () -> Unit,
    onReactivate: () -> Unit
) {
    val statusColor = when (captain.status) {
        CaptainStatus.APPROVED -> FarasNeonGreenDark
        CaptainStatus.PENDING -> FarasAmber
        CaptainStatus.SUSPENDED -> Color(0xFFF97316)
        CaptainStatus.BLOCKED -> FarasRedAlert
    }

    val statusBg = when (captain.status) {
        CaptainStatus.APPROVED -> FarasNeonGreenLight
        CaptainStatus.PENDING -> FarasAmberBg
        CaptainStatus.SUSPENDED -> Color(0xFFFFF7ED)
        CaptainStatus.BLOCKED -> FarasRedAlertBg
    }

    val statusText = when (captain.status) {
        CaptainStatus.APPROVED -> if (language == AppLanguage.ARABIC) "معتمد وفعال 🟢" else "Approved 🟢"
        CaptainStatus.PENDING -> if (language == AppLanguage.ARABIC) "في انتظار الموافقة 🟡" else "Pending 🟡"
        CaptainStatus.SUSPENDED -> if (language == AppLanguage.ARABIC) "موقوف مؤقتاً ⏸️" else "Suspended ⏸️"
        CaptainStatus.BLOCKED -> if (language == AppLanguage.ARABIC) "محظور نهائياً 🚫" else "Blocked 🚫"
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = FarasWhite),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Name + Verification badge + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(FarasSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TwoWheeler,
                            contentDescription = null,
                            tint = FarasTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = captain.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasTextPrimary
                            )
                            if (captain.isVerified) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = FarasNeonGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Text(
                            text = "هاتف: ${captain.phone}",
                            style = MaterialTheme.typography.labelSmall,
                            color = FarasTextSecondary
                        )
                    }
                }

                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Details and Performance Metrics
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(FarasLightGreyBg)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "الدراجة: ${captain.scooterModel}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = FarasTextPrimary
                    )
                    Text(
                        text = "لوحة: ${captain.plateNumber} • قومي: ${captain.nationalId}",
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = FarasAmber, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${captain.rating}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${captain.totalRides} مشوار)",
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons based on status (الموافقة على الكابتن أو حظره أو توقيفه مؤقتاً أو إعادة تفعيله)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (captain.status) {
                    CaptainStatus.PENDING -> {
                        Button(
                            onClick = onApprove,
                            colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = FarasWhite, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "اعتماد الكابتن ✓" else "Approve ✓",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                        }
                        OutlinedButton(
                            onClick = onBlock,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = FarasRedAlert),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Block, contentDescription = null, tint = FarasRedAlert, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "رفض وحظر 🚫" else "Block 🚫",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    CaptainStatus.APPROVED -> {
                        Button(
                            onClick = onSuspend,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97316)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Pause, contentDescription = null, tint = FarasWhite, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "توقيف مؤقت ⏸️" else "Suspend ⏸️",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                        }
                        OutlinedButton(
                            onClick = onBlock,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = FarasRedAlert),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Block, contentDescription = null, tint = FarasRedAlert, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "حظر الكابتن 🚫" else "Block 🚫",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    CaptainStatus.SUSPENDED -> {
                        Button(
                            onClick = onApprove,
                            colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = FarasWhite, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "إلغاء التوقيف وتفعيل ✓" else "Unsuspend ✓",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                        }
                        OutlinedButton(
                            onClick = onBlock,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = FarasRedAlert),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Block, contentDescription = null, tint = FarasRedAlert, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "حظر نهائي 🚫" else "Block 🚫",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    CaptainStatus.BLOCKED -> {
                        Button(
                            onClick = onReactivate,
                            colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.RestartAlt, contentDescription = null, tint = FarasWhite, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "فك الحظر وتفعيل الكابتن ⚡" else "Unblock & Reactivate ⚡",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeCustomizationSection(
    uiState: FarascooterUiState,
    viewModel: FarascooterViewModel,
    language: AppLanguage
) {
    val context = LocalContext.current
    var customHexInput by remember { mutableStateOf("") }

    val primaryColorPresets = listOf(
        Pair(0xFF00B44A, if (language == AppLanguage.ARABIC) "أخضر FareScooter" else "FareScooter Green"),
        Pair(0xFF1E3A8A, if (language == AppLanguage.ARABIC) "أزرق ملكي" else "Royal Navy"),
        Pair(0xFF7C3AED, if (language == AppLanguage.ARABIC) "بنفسجي كهربائي" else "Electric Purple"),
        Pair(0xFFF97316, if (language == AppLanguage.ARABIC) "برتقالي حيوي" else "Vibrant Orange"),
        Pair(0xFF111827, if (language == AppLanguage.ARABIC) "أسود أنيق" else "Obsidian Black"),
        Pair(0xFF059669, if (language == AppLanguage.ARABIC) "زمردي كلاسيكي" else "Classic Emerald"),
        Pair(0xFFDC2626, if (language == AppLanguage.ARABIC) "أحمر قرمزي" else "Crimson Red")
    )

    val backgroundColorPresets = listOf(
        Pair(0xFFE7E8EA, if (language == AppLanguage.ARABIC) "رمادي فاتح (الافتراضي)" else "Light Grey (Default)"),
        Pair(0xFFFFFFFF, if (language == AppLanguage.ARABIC) "أبيض ناصع" else "Pure White"),
        Pair(0xFFF3F4F6, if (language == AppLanguage.ARABIC) "رمادي ثلجي ناعم" else "Soft Snow Grey"),
        Pair(0xFF0F172A, if (language == AppLanguage.ARABIC) "داكن حديث" else "Dark Slate Mode")
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Section Header
        Card(
            colors = CardDefaults.cardColors(containerColor = FarasWhite),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(uiState.customPrimaryColorHex)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = FarasWhite, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "تخصيص ألوان التطبيق والخطوط" else "Theme & Typography Customization",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasTextPrimary
                        )
                        Text(
                            text = if (language == AppLanguage.ARABIC) "تغيير فوري ومباشر لألوان الهوية وخطوط الواجهة (عربي وإنجليزي)" else "Live dynamic changes to app identity colors & fonts",
                            style = MaterialTheme.typography.labelSmall,
                            color = FarasTextSecondary
                        )
                    }
                }
            }
        }

        // 1. Primary App Color
        Card(
            colors = CardDefaults.cardColors(containerColor = FarasWhite),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FormatColorFill, contentDescription = null, tint = Color(uiState.customPrimaryColorHex), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ARABIC) "اللون الأساسي للتطبيق (Primary Color)" else "Primary App Color",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Presets swatches
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    primaryColorPresets.forEach { (hexVal, name) ->
                        val isSelected = uiState.customPrimaryColorHex == hexVal
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(hexVal).copy(alpha = 0.15f) else FarasLightGreyBg)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) Color(hexVal) else FarasCardBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    viewModel.setPrimaryColor(hexVal)
                                    Toast.makeText(context, "تم تطبيق اللون: $name", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(Color(hexVal))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(hexVal) else FarasTextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Custom Hex input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = customHexInput,
                        onValueChange = { customHexInput = it },
                        placeholder = { Text("كود لون مخصص (مثال: #00B44A)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = FarasLightGreyBg,
                            unfocusedContainerColor = FarasLightGreyBg,
                            focusedBorderColor = Color(uiState.customPrimaryColorHex),
                            unfocusedBorderColor = FarasCardBorder
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val clean = customHexInput.trim().removePrefix("#")
                            val parsed = clean.toLongOrNull(16)
                            if (parsed != null) {
                                val fullHex = if (clean.length == 6) 0xFF000000L or parsed else parsed
                                viewModel.setPrimaryColor(fullHex)
                                Toast.makeText(context, "تم تطبيق اللون المخصص بنجاح 🎨", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "يرجى إدخال كود لون سداسي عشري صحيح", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(uiState.customPrimaryColorHex)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (language == AppLanguage.ARABIC) "تطبيق" else "Apply", color = FarasWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 2. Background Color
        Card(
            colors = CardDefaults.cardColors(containerColor = FarasWhite),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ColorLens, contentDescription = null, tint = Color(uiState.customPrimaryColorHex), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ARABIC) "لون خلفية التطبيق (Background Color)" else "App Background Color",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    backgroundColorPresets.forEach { (hexVal, name) ->
                        val isSelected = uiState.customBackgroundColorHex == hexVal
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) FarasNeonGreenLight else FarasLightGreyBg)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) Color(uiState.customPrimaryColorHex) else FarasCardBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    viewModel.setBackgroundColor(hexVal)
                                    Toast.makeText(context, "تم تغيير لون الخلفية: $name", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(Color(hexVal))
                                        .border(1.dp, FarasTextMuted.copy(alpha = 0.4f), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = FarasTextPrimary
                                )
                            }
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(uiState.customPrimaryColorHex), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // 3. Arabic Font Selector
        Card(
            colors = CardDefaults.cardColors(containerColor = FarasWhite),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FontDownload, contentDescription = null, tint = Color(uiState.customPrimaryColorHex), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ARABIC) "خط اللغة العربية المعتمد" else "Arabic Font Family",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                val arabicFonts = listOf(
                    Pair(AppFontFamily.CAIRO, "كايرو (Cairo) - الخط الرسمي الافتراضي"),
                    Pair(AppFontFamily.TAJAWAL, "تجوال (Tajawal) - انسيابي وعصري"),
                    Pair(AppFontFamily.ALMARAI, "المراعي (Almarai) - دقيق وواضح"),
                    Pair(AppFontFamily.SYSTEM_DEFAULT, "خط النظام الافتراضي (System Default)")
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    arabicFonts.forEach { (font, label) ->
                        val isSelected = uiState.selectedArabicFont == font
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) FarasNeonGreenLight else FarasLightGreyBg)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) Color(uiState.customPrimaryColorHex) else FarasCardBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    viewModel.setArabicFont(font)
                                    Toast.makeText(context, "تم تفعيل خط العربية: $label", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = FarasTextPrimary
                            )
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(uiState.customPrimaryColorHex), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // 4. English Font Selector
        Card(
            colors = CardDefaults.cardColors(containerColor = FarasWhite),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FontDownload, contentDescription = null, tint = Color(uiState.customPrimaryColorHex), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ARABIC) "خط اللغة الإنجليزية المعتمد" else "English Font Family",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                val englishFonts = listOf(
                    Pair(AppFontFamily.CAIRO, "Cairo (Default Global)"),
                    Pair(AppFontFamily.INTER, "Inter (Modern Minimalist)"),
                    Pair(AppFontFamily.ROBOTO, "Roboto (Android Standard)"),
                    Pair(AppFontFamily.SYSTEM_DEFAULT, "System Default")
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    englishFonts.forEach { (font, label) ->
                        val isSelected = uiState.selectedEnglishFont == font
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) FarasNeonGreenLight else FarasLightGreyBg)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) Color(uiState.customPrimaryColorHex) else FarasCardBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    viewModel.setEnglishFont(font)
                                    Toast.makeText(context, "English font activated: $label", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = FarasTextPrimary
                            )
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(uiState.customPrimaryColorHex), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // 5. Live Interactive Preview Card
        Card(
            colors = CardDefaults.cardColors(containerColor = FarasWhite),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(uiState.customPrimaryColorHex)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == AppLanguage.ARABIC) "معاينة حية للمظهر الجديد 📱" else "Live Theme Preview 📱",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(uiState.customBackgroundColorHex))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "FareScooter مصر 🛵",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(uiState.customPrimaryColorHex)
                            )
                            Text(
                                text = "تخطي زحمة كوبري 6 أكتوبر فوراً",
                                style = MaterialTheme.typography.bodySmall,
                                color = FarasTextPrimary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(uiState.customPrimaryColorHex))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "22.5 ج.م",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                        }
                    }
                }
            }
        }

        // 6. Reset Defaults Button
        OutlinedButton(
            onClick = {
                viewModel.resetThemeDefaults()
                Toast.makeText(context, "تمت استعادة الألوان والخطوط الافتراضية بنجاح 🔄", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = FarasTextSecondary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = FarasTextSecondary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == AppLanguage.ARABIC) "استعادة الإعدادات الافتراضية للتطبيق" else "Reset Theme to Defaults",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

