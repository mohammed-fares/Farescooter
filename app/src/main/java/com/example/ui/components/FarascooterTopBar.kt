package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.ui.theme.FarasBlack
import com.example.ui.theme.FarasCardBg
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite
import com.example.viewmodel.AppMode

@Composable
fun FarascooterTopBar(
    currentMode: AppMode,
    language: AppLanguage,
    walletBalance: Double,
    onModeSelected: (AppMode) -> Unit,
    onLanguageToggle: () -> Unit,
    onWalletClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = FarasBlack.copy(alpha = 0.92f),
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Top Row: Brand + Wallet + Lang + Profile
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo & Brand
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onModeSelected(AppMode.CUSTOMER) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(FarasNeonGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricScooter,
                            contentDescription = "Farascooter Logo",
                            tint = FarasBlack,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = AppStrings.get("app_name", language),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = FarasWhite
                        )
                        Text(
                            text = if (language == AppLanguage.ARABIC) "أوبر السكوتر الذكي" else "The Scooter Ride",
                            style = MaterialTheme.typography.labelSmall,
                            color = FarasNeonGreen
                        )
                    }
                }

                // Actions: Quick Lang Switch, Wallet, Profile
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Language Toggle Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(FarasCardBg)
                            .border(1.dp, FarasCardBorder, RoundedCornerShape(16.dp))
                            .clickable { onLanguageToggle() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("language_toggle_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = FarasNeonGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "EN" else "عربي",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                        }
                    }

                    // Wallet Quick Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(FarasCardBg)
                            .border(1.dp, FarasCardBorder, RoundedCornerShape(16.dp))
                            .clickable { onWalletClicked() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("topbar_wallet_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = "Wallet",
                                tint = FarasNeonGreen,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${walletBalance.toInt()} ${AppStrings.get("currency", language)}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = FarasWhite
                            )
                        }
                    }

                    // Profile Icon
                    IconButton(
                        onClick = onProfileClicked,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(FarasCardBg)
                            .testTag("topbar_profile_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = FarasWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Row: All-in-One Mode Selector (Customer, Captain, Admin)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(FarasCardBg)
                    .padding(3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ModeTabPill(
                    title = AppStrings.get("role_customer", language),
                    icon = Icons.Default.ElectricScooter,
                    isSelected = currentMode == AppMode.CUSTOMER,
                    modifier = Modifier.weight(1f),
                    onClick = { onModeSelected(AppMode.CUSTOMER) }
                )
                ModeTabPill(
                    title = AppStrings.get("role_captain", language),
                    icon = Icons.Default.TwoWheeler,
                    isSelected = currentMode == AppMode.CAPTAIN,
                    modifier = Modifier.weight(1f),
                    onClick = { onModeSelected(AppMode.CAPTAIN) }
                )
                ModeTabPill(
                    title = AppStrings.get("role_admin", language),
                    icon = Icons.Default.AdminPanelSettings,
                    isSelected = currentMode == AppMode.ADMIN,
                    modifier = Modifier.weight(1f),
                    onClick = { onModeSelected(AppMode.ADMIN) }
                )
            }
        }
    }
}

@Composable
private fun ModeTabPill(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(9.dp))
            .background(if (isSelected) FarasNeonGreen else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) FarasBlack else FarasTextSecondary,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) FarasBlack else FarasTextSecondary,
                maxLines = 1
            )
        }
    }
}
