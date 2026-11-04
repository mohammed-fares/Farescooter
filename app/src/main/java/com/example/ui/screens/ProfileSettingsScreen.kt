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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
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
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasLightCard
import com.example.ui.theme.FarasLightGreyBg
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenDark
import com.example.ui.theme.FarasNeonGreenLight
import com.example.ui.theme.FarasSurfaceVariant
import com.example.ui.theme.FarasTextPrimary
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
        color = FarasLightGreyBg,
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
                        tint = FarasTextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.get("profile_title", language),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // User Info Card in Egypt
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasWhite),
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
                            .background(FarasNeonGreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = FarasNeonGreenDark,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "م. فاروق زكي (مصر 🇪🇬)" else "Farooq Zaki (Egypt 🇪🇬)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasTextPrimary
                        )
                        Text(
                            text = "+20 10 9876 5432 • mf.z.hussein.pal@gmail.com",
                            style = MaterialTheme.typography.labelSmall,
                            color = FarasTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Multi-Language Switch Section (Exclusive to Profile Screen as requested)
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasWhite),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, FarasNeonGreen.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(FarasNeonGreenLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = FarasNeonGreenDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (language == AppLanguage.ARABIC) "إعدادات لغة التطبيق" else "App Language Settings",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasTextPrimary
                            )
                            Text(
                                text = if (language == AppLanguage.ARABIC) "يتم تغيير لغة التطبيق حصرياً من هنا" else "Change app language exclusively from here",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasNeonGreenDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = AppStrings.get("language_exclusive_notice", language),
                        style = MaterialTheme.typography.bodySmall,
                        color = FarasTextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Language Option: Arabic (Cairo)
                    LanguageChoiceItemLight(
                        title = AppStrings.get("arabic_option", language),
                        subtitle = "خط كابرو الشهير حصرياً مع دعم كامل للاتجاه من اليمين لليسار (RTL)",
                        isSelected = language == AppLanguage.ARABIC,
                        onClick = { viewModel.setLanguage(AppLanguage.ARABIC) },
                        testTag = "lang_choice_arabic"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Language Option: English (Modern Sans)
                    LanguageChoiceItemLight(
                        title = AppStrings.get("english_option", language),
                        subtitle = "Clean modern sans-serif typography, matching Uber design standards",
                        isSelected = language == AppLanguage.ENGLISH,
                        onClick = { viewModel.setLanguage(AppLanguage.ENGLISH) },
                        testTag = "lang_choice_english"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Safety Standards in Egypt
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasWhite),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = FarasNeonGreenDark,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.get("safety_title", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    SafetyRuleBulletLight(
                        title = if (language == AppLanguage.ARABIC) "خوذة الركاب المعقمة إجبارية" else "Sanitized Passenger Helmet",
                        desc = if (language == AppLanguage.ARABIC) "يلتزم كابتن السكوتر تشغيلياً بتوفير خوذة إضافية معقمة مطابقة للمواصفات ليرتديها الراكب طوال المشوار." else "Captain strictly provides a sanitized safety helmet for the passenger."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    SafetyRuleBulletLight(
                        title = if (language == AppLanguage.ARABIC) "الحد الأقصى للطرود 15 كجم" else "15 kg Cargo Max Weight",
                        desc = if (language == AppLanguage.ARABIC) "لضمان ثبات السكوتر في شوارع مصر، سعة الصندوق محددة بحد أقصى 15 كجم." else "Cargo box max capacity strictly capped at 15kg for balance and safety."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    SafetyRuleBulletLight(
                        title = if (language == AppLanguage.ARABIC) "زر استغاثة الطوارئ SOS" else "SOS Emergency Dispatch",
                        desc = if (language == AppLanguage.ARABIC) "مرتبط مباشرة بغرفة عمليات مصر وشرطة النجدة (122)." else "Directly linked to Central Operations and Egypt Police (122)."
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation to Trips History
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(FarasWhite)
                    .border(1.dp, FarasCardBorder, RoundedCornerShape(16.dp))
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
                        Icon(Icons.Default.History, contentDescription = null, tint = FarasNeonGreenDark)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = AppStrings.get("trips_history", language),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = FarasTextPrimary
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
private fun LanguageChoiceItemLight(
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
            .background(if (isSelected) FarasNeonGreenLight else FarasLightGreyBg)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
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
                    color = if (isSelected) FarasNeonGreenDark else FarasTextPrimary
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
                    tint = FarasNeonGreenDark,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun SafetyRuleBulletLight(
    title: String,
    desc: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(FarasNeonGreenDark)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = FarasTextPrimary
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.labelSmall,
                color = FarasTextSecondary
            )
        }
    }
}
