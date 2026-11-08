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
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.model.ErrandDetails
import com.example.ui.theme.FarasAmber
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasLightGreyBg
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenDark
import com.example.ui.theme.FarasTextPrimary
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite
import com.example.viewmodel.MapPinTarget

@Composable
fun ErrandFormSheet(
    language: AppLanguage,
    initialDraft: ErrandDetails,
    onBack: () -> Unit,
    onConfirm: (ErrandDetails) -> Unit,
    onSelectLocationOnMap: (MapPinTarget) -> Unit = {},
    onUseCurrentGpsLocation: (((String) -> Unit)) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var storeName by remember { mutableStateOf(initialDraft.storeName) }
    var deliveryLocation by remember { mutableStateOf(initialDraft.deliveryLocation) }
    var itemsList by remember { mutableStateOf(initialDraft.itemsList) }
    var budget by remember { mutableStateOf(initialDraft.estimatedBudget.toString()) }

    LaunchedEffect(initialDraft.storeName, initialDraft.deliveryLocation) {
        storeName = initialDraft.storeName
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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("errand_back_btn")
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
                        text = AppStrings.get("errand_info_title", language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                    Text(
                        text = AppStrings.get("service_buy_and_get_desc", language),
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Store / Purchase Location (مكان الشراء - كتابة أو خريطة)
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = storeName,
                    onValueChange = { storeName = it },
                    label = { Text(AppStrings.get("errand_store_loc", language)) },
                    placeholder = { Text(if (language == AppLanguage.ARABIC) "اسم ومكان المحل/الصيدلية أو حدده من الخريطة" else "Store name or pick on map") },
                    leadingIcon = {
                        Icon(Icons.Default.Store, contentDescription = null, tint = FarasNeonGreenDark)
                    },
                    colors = textFieldColorsLight(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("store_name_input")
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
                            .clickable { onSelectLocationOnMap(MapPinTarget.ERRAND_STORE) }
                            .padding(vertical = 8.dp, horizontal = 10.dp)
                            .testTag("errand_store_map_btn"),
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
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Delivery Destination (مكان استلام وتوصيل الطلب - كتابة أو خريطة أو موقعي)
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = deliveryLocation,
                    onValueChange = { deliveryLocation = it },
                    label = { Text(AppStrings.get("errand_delivery_loc", language)) },
                    placeholder = { Text(if (language == AppLanguage.ARABIC) "اكتب عنوان التوصيل إليك أو اختر موقعك" else "Delivery address or current location") },
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = FarasAmber)
                    },
                    colors = textFieldColorsLight(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("errand_delivery_input")
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
                            .clickable { onSelectLocationOnMap(MapPinTarget.ERRAND_DELIVERY) }
                            .padding(vertical = 8.dp, horizontal = 10.dp)
                            .testTag("errand_delivery_map_btn"),
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

                    // Use Real GPS Location Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(FarasLightGreyBg)
                            .border(1.dp, FarasCardBorder, RoundedCornerShape(8.dp))
                            .clickable {
                                onUseCurrentGpsLocation { addr -> deliveryLocation = addr }
                            }
                            .padding(vertical = 8.dp, horizontal = 10.dp)
                            .testTag("errand_delivery_gps_btn"),
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

            // 3. Items List
            OutlinedTextField(
                value = itemsList,
                onValueChange = { itemsList = it },
                label = { Text(AppStrings.get("items_list", language)) },
                placeholder = { Text(if (language == AppLanguage.ARABIC) "اكتب الطلبات بالتفصيل (مثل: 1x بنادول إكسترا، زجاجة ماء)" else "List items to purchase...") },
                leadingIcon = {
                    Icon(Icons.Default.ListAlt, contentDescription = null, tint = FarasNeonGreenDark)
                },
                minLines = 3,
                maxLines = 5,
                colors = textFieldColorsLight(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("items_list_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4. Budget in EGP
            OutlinedTextField(
                value = budget,
                onValueChange = { budget = it },
                label = { Text("${AppStrings.get("max_budget", language)} (${AppStrings.get("currency", language)})") },
                leadingIcon = {
                    Icon(Icons.Default.Payments, contentDescription = null, tint = FarasNeonGreenDark)
                },
                colors = textFieldColorsLight(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("budget_input")
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val b = budget.toDoubleOrNull() ?: 80.0
                    onConfirm(
                        ErrandDetails(
                            storeName = storeName,
                            deliveryLocation = deliveryLocation,
                            itemsList = itemsList,
                            estimatedBudget = b
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("confirm_errand_btn")
            ) {
                Text(
                    text = AppStrings.get("confirm_errand", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasWhite
                )
            }
        }
    }
}
