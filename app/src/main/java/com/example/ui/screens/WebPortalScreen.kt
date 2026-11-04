package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.AppLanguage
import com.example.data.AppStrings
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
import com.example.ui.theme.FarasSurfaceVariant
import com.example.ui.theme.FarasTextMuted
import com.example.ui.theme.FarasTextPrimary
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite
import com.example.viewmodel.AppMode
import com.example.viewmodel.FarascooterUiState
import com.example.viewmodel.FarascooterViewModel

@Composable
fun WebPortalScreen(
    uiState: FarascooterUiState,
    viewModel: FarascooterViewModel,
    modifier: Modifier = Modifier
) {
    val language = uiState.language
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
            // Browser Address Bar Mockup
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(FarasWhite)
                    .border(1.dp, FarasCardBorder, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "Website",
                        tint = FarasNeonGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "https://www.farascooter.eg/cairo",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = FarasTextSecondary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(FarasNeonGreenLight)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "SSL SECURED 🔒",
                            style = MaterialTheme.typography.labelSmall,
                            color = FarasNeonGreenDark,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hero Section Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasWhite),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(FarasNeonGreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricScooter,
                            contentDescription = null,
                            tint = FarasNeonGreen,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = AppStrings.get("web_portal_title", language),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = AppStrings.get("web_portal_subtitle", language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = FarasTextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Direct Launch Admin Panel Link (Mandatory requirement)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(FarasNeonGreen.copy(alpha = 0.12f))
                            .border(1.dp, FarasNeonGreen.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .clickable { viewModel.setAppMode(AppMode.ADMIN) }
                            .padding(14.dp)
                            .testTag("web_enter_admin_portal")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(FarasNeonGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = FarasWhite,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = AppStrings.get("web_launch_admin", language),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = FarasTextPrimary
                                    )
                                    Text(
                                        text = if (language == AppLanguage.ARABIC) "مراقبة الأجهزة والتنزيلات، إدارة الكباتن، وتسويق الذكاء الاصطناعي" else "Device downloads, captain tools & AI marketing suite",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = FarasTextSecondary
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = FarasNeonGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Download App Buttons for all platforms
                    Text(
                        text = AppStrings.get("web_download_app", language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // 1. Android / Google Play
                        DownloadButton(
                            title = AppStrings.get("web_download_android", language),
                            subtitle = "v2.4.0 • APK & Play Store (متاح لجميع أجهزة أندرويد)",
                            icon = Icons.Default.Android,
                            btnColor = FarasNeonGreen,
                            onClick = {
                                Toast.makeText(context, "جاري بدء تحميل تطبيق أندرويد...", Toast.LENGTH_SHORT).show()
                            },
                            testTag = "download_android_btn"
                        )

                        // 2. iOS / App Store
                        DownloadButton(
                            title = AppStrings.get("web_download_ios", language),
                            subtitle = "iOS 15.0+ • iPhone & iPad",
                            icon = Icons.Default.PhoneIphone,
                            btnColor = FarasTextPrimary,
                            onClick = {
                                Toast.makeText(context, "جاري التوجيه إلى App Store...", Toast.LENGTH_SHORT).show()
                            },
                            testTag = "download_ios_btn"
                        )

                        // 3. Direct APK & Web App
                        DownloadButton(
                            title = AppStrings.get("web_download_apk", language),
                            subtitle = "تنزيل سريع مباشر برابط مشفر ومفحوص أمنياً",
                            icon = Icons.Default.TouchApp,
                            btnColor = FarasBlue,
                            onClick = {
                                Toast.makeText(context, "تم بدء تحميل ملف APK المباشر...", Toast.LENGTH_SHORT).show()
                            },
                            testTag = "download_apk_btn"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Why Scooter in Egypt? (Traffic & Time saving facts)
            Text(
                text = AppStrings.get("web_why_scooter", language),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = FarasTextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                WebFeatureCard(
                    title = AppStrings.get("web_reason_1_title", language),
                    desc = AppStrings.get("web_reason_1_desc", language),
                    icon = Icons.Default.Speed,
                    tagColor = FarasNeonGreen
                )

                WebFeatureCard(
                    title = AppStrings.get("web_reason_2_title", language),
                    desc = AppStrings.get("web_reason_2_desc", language),
                    icon = Icons.Default.Star,
                    tagColor = FarasAmber
                )

                WebFeatureCard(
                    title = AppStrings.get("web_reason_3_title", language),
                    desc = AppStrings.get("web_reason_3_desc", language),
                    icon = Icons.Default.HealthAndSafety,
                    tagColor = FarasBlue
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Egyptian Legal and Support Hotline Footer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(FarasWhite)
                    .border(1.dp, FarasCardBorder, RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "فارسكوتر مصر - شركة النقل الذكي للسكوترات المرخصة",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "الخط الساخن الموحد بمصر: 19442 • خدمة عملاء 24 ساعة • مرخص من وزارة النقل",
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun DownloadButton(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    btnColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(btnColor)
            .clickable { onClick() }
            .padding(14.dp)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = FarasWhite,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasWhite
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = FarasWhite.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@Composable
private fun WebFeatureCard(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tagColor: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = FarasWhite),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(tagColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tagColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = FarasTextSecondary
                )
            }
        }
    }
}
