package com.example.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.viewmodel.MapPinTarget

@Composable
fun PackageFormSheet(
    language: AppLanguage,
    initialDraft: PackageDetails,
    onBack: () -> Unit,
    onConfirm: (PackageDetails) -> Unit,
    onSelectLocationOnMap: (MapPinTarget) -> Unit = {},
    onUseCurrentGpsLocation: (((String) -> Unit)) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var pickupLocation by remember { mutableStateOf(initialDraft.pickupLocation) }
    var deliveryLocation by remember { mutableStateOf(initialDraft.deliveryLocation) }
    var description by remember { mutableStateOf(initialDraft.description) }
    var receiverName by remember { mutableStateOf(initialDraft.receiverName) }
    var receiverPhone by remember { mutableStateOf(initialDraft.receiverPhone) }
    var weightKg by remember { mutableStateOf(initialDraft.weightKg.toString()) }

    // Sync if draft updated externally (e.g. from Map Pin selection or GPS)
    LaunchedEffect(initialDraft.pickupLocation, initialDraft.deliveryLocation) {
        pickupLocation = initialDraft.pickupLocation
        deliveryLocation = initialDraft.deliveryLocation
    }

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
                .verticalScroll(scrollState)
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

            // 1. Pickup Location (مكان استلام الطرد - كتابة أو تحديد عبر اللوكيشن / الخريطة)
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = pickupLocation,
                    onValueChange = { pickupLocation = it },
                    label = { Text(AppStrings.get("package_pickup_loc", language)) },
                    placeholder = { Text(if (language == AppLanguage.ARABIC) "اكتب العنوان بالتفصيل أو حدده من الخريطة" else "Type address or pick on map") },
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = FarasNeonGreenDark)
                    },
                    colors = textFieldColorsLight(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("package_pickup_input")
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Pick on Map Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(FarasNeonGreen.copy(alpha = 0.12f))
                            .clickable { onSelectLocationOnMap(MapPinTarget.PACKAGE_PICKUP) }
                            .padding(vertical = 8.dp, horizontal = 10.dp)
                            .testTag("package_pickup_map_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Map, contentDescription = null, tint = FarasNeonGreenDark, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.get("pick_from_map", language),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasNeonGreenDark
                            )
                        }
                    }

                    // Use Real GPS Location Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(FarasLightGreyBg)
                            .border(1.dp, FarasCardBorder, RoundedCornerShape(8.dp))
                            .clickable {
                                onUseCurrentGpsLocation { addr -> pickupLocation = addr }
                            }
                            .padding(vertical = 8.dp, horizontal = 10.dp)
                            .testTag("package_pickup_gps_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MyLocation, contentDescription = null, tint = FarasTextPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.get("use_my_location", language),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasTextPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Dropoff Delivery Location (مكان تسليم الطرد - كتابة أو تحديد عبر اللوكيشن / الخريطة)
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = deliveryLocation,
                    onValueChange = { deliveryLocation = it },
                    label = { Text(AppStrings.get("package_dropoff_loc", language)) },
                    placeholder = { Text(if (language == AppLanguage.ARABIC) "اكتب عنوان المستلم أو حدده من الخريطة" else "Type destination or pick on map") },
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = FarasAmber)
                    },
                    colors = textFieldColorsLight(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("package_dropoff_input")
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Pick on Map Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(FarasAmber.copy(alpha = 0.12f))
                            .clickable { onSelectLocationOnMap(MapPinTarget.PACKAGE_DROPOFF) }
                            .padding(vertical = 8.dp, horizontal = 10.dp)
                            .testTag("package_dropoff_map_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Map, contentDescription = null, tint = FarasAmber, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.get("pick_from_map", language),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasAmber
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Description Field
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

            // 4. Receiver Name Field
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

            // 5. Receiver Phone Field
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
                            pickupLocation = pickupLocation,
                            deliveryLocation = deliveryLocation,
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
