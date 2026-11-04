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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.theme.FarasSurfaceDark
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite
import com.example.viewmodel.CustomerScreen
import com.example.viewmodel.FarascooterUiState
import com.example.viewmodel.FarascooterViewModel

@Composable
fun ProfileSettingsScreen(
    uiState: FarascooterUiState,
    viewModel: FarascooterViewModel,
    modifier: Modifier = Modifier
) {
    val language = uiState.language
    val scrollState = rememberScrollState()

    BackHandler {
        viewModel.setCustomerScreen(CustomerScreen.HOME)
    }

    Surface(
        color = FarasBlack,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // Header with Back
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.setCustomerScreen(CustomerScreen.HOME) },
                    modifier = Modifier.testTag("profile_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = FarasWhite
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.get("profile_title", language),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = FarasWhite
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // User Info Card
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasCardBg),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(FarasNeonGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = FarasBlack,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "محمد فاروق (عميل VIP)" else "Mohammed Farooq (VIP Customer)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasWhite
                        )
                        Text(
                            text = "+971 50 771 8822 • mf.z.hussein.pal@gmail.com",
                            style = MaterialTheme.typography.labelSmall,
                            color = FarasTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Multi-Language Switch Section (Mandatory from prompt)
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasCardBg),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarasNeonGreen.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = FarasNeonGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.ARABIC) "إعدادات اللغة (Language Settings)" else "Language Settings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = AppStrings.get("language_settings", language),
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Language Option: Arabic (Cairo)
                    LanguageChoiceItem(
                        title = AppStrings.get("arabic_option", language),
                        subtitle = "خط كابرو الشهير حصرياً مع دعم كامل للاتجاه من اليمين لليسار (RTL)",
                        isSelected = language == AppLanguage.ARABIC,
                        onClick = { viewModel.setLanguage(AppLanguage.ARABIC) },
                        testTag = "lang_choice_arabic"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Language Option: English (Modern Sans)
                    LanguageChoiceItem(
                        title = AppStrings.get("english_option", language),
                        subtitle = "Clean modern sans-serif typography, matching Uber design standards",
                        isSelected = language == AppLanguage.ENGLISH,
                        onClick = { viewModel.setLanguage(AppLanguage.ENGLISH) },
                        testTag = "lang_choice_english"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Scooter Safety Standards & Rules Card
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasCardBg),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = FarasNeonGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.get("safety_title", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    SafetyRuleBullet(
                        title = if (language == AppLanguage.ARABIC) "خوذة الركاب المعقمة" else "Sanitized Passenger Helmet",
                        desc = if (language == AppLanguage.ARABIC) "يلتزم الكابتن تشغيلياً بتوفير خوذة إضافية معقمة مطابقة لمعايير السلامة لارتدائها طوال الرحلة." else "Captain strictly provides a sanitized safety helmet for the passenger."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    SafetyRuleBullet(
                        title = if (language == AppLanguage.ARABIC) "الحد الأقصى للطرود 15 كجم" else "15 kg Cargo Max Weight",
                        desc = if (language == AppLanguage.ARABIC) "لا تتجاوز الأبعاد السعة الاستيعابية للصندوق الخلفي القياسي المبتكر للسكوتر." else "Cargo box max capacity strictly capped at 15kg for balance and safety."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    SafetyRuleBullet(
                        title = if (language == AppLanguage.ARABIC) "زر استغاثة الطوارئ SOS" else "SOS Emergency Dispatch",
                        desc = if (language == AppLanguage.ARABIC) "متاح دائماً على شاشة الرحلة مع تتبع مباشر للعمليات وتنبيه الطوارئ." else "Always accessible on active trips with instant control room GPS broadcast."
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Navigation to Trips History
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(FarasCardBg)
                    .clickable { viewModel.setCustomerScreen(CustomerScreen.HISTORY) }
                    .padding(16.dp)
                    .testTag("nav_to_history_btn")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.History, contentDescription = null, tint = FarasNeonGreen)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = AppStrings.get("trips_history", language),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = FarasWhite
                        )
                    }
                    Text(
                        text = "❯",
                        style = MaterialTheme.typography.titleMedium,
                        color = FarasTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageChoiceItem(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) FarasNeonGreen.copy(alpha = 0.12f) else FarasSurfaceDark)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) FarasNeonGreen else FarasCardBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(14.dp)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) FarasNeonGreen else FarasWhite
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = FarasTextSecondary
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = FarasNeonGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun SafetyRuleBullet(
    title: String,
    desc: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(FarasNeonGreen)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = FarasWhite
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.labelSmall,
                color = FarasTextSecondary
            )
        }
    }
}
