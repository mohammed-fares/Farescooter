package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.model.PackageDetails
import com.example.ui.theme.FarasAmber
import com.example.ui.theme.FarasAmberBg
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasLightGreyBg
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenDark
import com.example.ui.theme.FarasTextPrimary
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite

@Composable
fun PackageFormSheet(
    language: AppLanguage,
    initialDraft: PackageDetails,
    onBack: () -> Unit,
    onConfirm: (PackageDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    var description by remember { mutableStateOf(initialDraft.description) }
    var receiverName by remember { mutableStateOf(initialDraft.receiverName) }
    var receiverPhone by remember { mutableStateOf(initialDraft.receiverPhone) }
    var weightKg by remember { mutableStateOf(initialDraft.weightKg.toString()) }

    Surface(
        color = FarasWhite,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with Back
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("package_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = FarasTextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = AppStrings.get("package_info_title", language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                    Text(
                        text = AppStrings.get("service_send_package_desc", language),
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 15 kg Safety Notice Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(FarasAmberBg)
                    .border(1.dp, FarasAmber.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Weight Limit Notice",
                        tint = FarasAmber,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.get("weight_notice", language),
                        style = MaterialTheme.typography.bodySmall,
                        color = FarasTextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(AppStrings.get("package_desc", language)) },
                leadingIcon = {
                    Icon(Icons.Default.LocalShipping, contentDescription = null, tint = FarasNeonGreenDark)
                },
                colors = textFieldColorsLight(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("package_desc_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Receiver Name Field
            OutlinedTextField(
                value = receiverName,
                onValueChange = { receiverName = it },
                label = { Text(AppStrings.get("receiver_name", language)) },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = FarasNeonGreenDark)
                },
                colors = textFieldColorsLight(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("receiver_name_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Receiver Phone Field
            OutlinedTextField(
                value = receiverPhone,
                onValueChange = { receiverPhone = it },
                label = { Text(AppStrings.get("receiver_phone", language)) },
                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = FarasNeonGreenDark)
                },
                colors = textFieldColorsLight(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("receiver_phone_input")
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Button
            Button(
                onClick = {
                    val w = weightKg.toFloatOrNull() ?: 2.5f
                    onConfirm(
                        PackageDetails(
                            description = description,
                            receiverName = receiverName,
                            receiverPhone = receiverPhone,
                            weightKg = w.coerceAtMost(15.0f)
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("confirm_package_btn")
            ) {
                Text(
                    text = AppStrings.get("confirm_package", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasWhite
                )
            }
        }
    }
}

@Composable
fun textFieldColorsLight() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = FarasNeonGreen,
    unfocusedBorderColor = FarasCardBorder,
    focusedContainerColor = FarasLightGreyBg,
    unfocusedContainerColor = FarasLightGreyBg,
    focusedTextColor = FarasTextPrimary,
    unfocusedTextColor = FarasTextPrimary,
    focusedLabelColor = FarasNeonGreenDark,
    unfocusedLabelColor = FarasTextSecondary
)
