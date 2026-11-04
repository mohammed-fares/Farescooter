package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.model.PaymentMethod
import com.example.model.PlaceItem
import com.example.model.ServiceType
import com.example.model.TripOrder
import com.example.model.TripStatus
import com.example.ui.components.ErrandFormSheet
import com.example.ui.components.InteractiveDarkMap
import com.example.ui.components.PackageFormSheet
import com.example.ui.components.TripRatingSheet
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
import com.example.ui.theme.FarasSurfaceVariant
import com.example.ui.theme.FarasTextMuted
import com.example.ui.theme.FarasTextPrimary
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite
import com.example.viewmodel.CustomerScreen
import com.example.viewmodel.FarascooterUiState
import com.example.viewmodel.FarascooterViewModel

@Composable
fun CustomerHomeScreen(
    uiState: FarascooterUiState,
    viewModel: FarascooterViewModel,
    modifier: Modifier = Modifier
) {
    val language = uiState.language

    BackHandler(enabled = uiState.customerScreen != CustomerScreen.HOME) {
        when (uiState.customerScreen) {
            CustomerScreen.PACKAGE_FORM, CustomerScreen.ERRAND_FORM, CustomerScreen.SEARCH_DESTINATION -> {
                viewModel.setCustomerScreen(CustomerScreen.HOME)
            }
            CustomerScreen.CONFIRM_ORDER -> {
                viewModel.setCustomerScreen(CustomerScreen.HOME)
            }
            CustomerScreen.DISPATCHING -> {
                viewModel.cancelOrder()
            }
            else -> viewModel.setCustomerScreen(CustomerScreen.HOME)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 1. Interactive Map Background
        InteractiveDarkMap(
            nearbyScooters = uiState.nearbyScooters,
            currentTrip = uiState.activeTrip,
            captainProgressRatio = uiState.captainProgressRatio,
            modifier = Modifier.fillMaxSize(),
            onMapTapped = { x, y ->
                if (uiState.customerScreen in listOf(CustomerScreen.HOME, CustomerScreen.SEARCH_DESTINATION)) {
                    viewModel.setMapCustomDestination(x, y)
                }
            }
        )

        // Floating SOS notification banner if triggered
        AnimatedVisibility(
            visible = uiState.sosAlertNotification != null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(FarasRedAlert)
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = FarasWhite)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = uiState.sosAlertNotification ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = FarasWhite
                        )
                    }
                    IconButton(onClick = { viewModel.dismissSosNotification() }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = FarasWhite)
                    }
                }
            }
        }

        // Bottom Flow Sheets depending on current state
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            when (uiState.customerScreen) {
                CustomerScreen.HOME, CustomerScreen.SEARCH_DESTINATION -> {
                    CustomerHomeControlsSheet(
                        uiState = uiState,
                        viewModel = viewModel
                    )
                }

                CustomerScreen.PACKAGE_FORM -> {
                    PackageFormSheet(
                        language = language,
                        initialDraft = uiState.packageDraft,
                        onBack = { viewModel.setCustomerScreen(CustomerScreen.HOME) },
                        onConfirm = { draft ->
                            viewModel.updatePackageDraft(draft)
                            viewModel.confirmPackageForm()
                        }
                    )
                }

                CustomerScreen.ERRAND_FORM -> {
                    ErrandFormSheet(
                        language = language,
                        initialDraft = uiState.errandDraft,
                        onBack = { viewModel.setCustomerScreen(CustomerScreen.HOME) },
                        onConfirm = { draft ->
                            viewModel.updateErrandDraft(draft)
                            viewModel.confirmErrandForm()
                        }
                    )
                }

                CustomerScreen.CONFIRM_ORDER -> {
                    ConfirmOrderSheet(
                        uiState = uiState,
                        viewModel = viewModel
                    )
                }

                CustomerScreen.DISPATCHING -> {
                    DispatchingRadarSheet(
                        uiState = uiState,
                        viewModel = viewModel
                    )
                }

                CustomerScreen.LIVE_TRACKING -> {
                    LiveTrackingSheet(
                        uiState = uiState,
                        viewModel = viewModel
                    )
                }

                CustomerScreen.TRIP_RATING -> {
                    TripRatingSheet(
                        language = language,
                        trip = uiState.activeTrip,
                        onSubmit = { stars, tip ->
                            viewModel.submitTripRating(stars, tip)
                        }
                    )
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun CustomerHomeControlsSheet(
    uiState: FarascooterUiState,
    viewModel: FarascooterViewModel
) {
    val language = uiState.language
    val scrollState = rememberScrollState()

    Surface(
        color = FarasWhite,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(scrollState)
        ) {
            // Drag handle indicator
            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(FarasCardBorder)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Peak Rush Hour Banner in Cairo (if active)
            if (uiState.surgeMultiplier > 1.0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(FarasAmberBg)
                        .border(1.dp, FarasAmber.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Surge",
                            tint = FarasAmber,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${AppStrings.get("surge_active", language)} (${uiState.surgeMultiplier}x)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasAmber
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Uber-Style "Where to?" Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(FarasLightGreyBg)
                    .border(1.dp, FarasCardBorder, RoundedCornerShape(16.dp))
                    .clickable { viewModel.setCustomerScreen(CustomerScreen.SEARCH_DESTINATION) }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .testTag("where_to_search_bar")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = FarasNeonGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = AppStrings.get("where_to", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = FarasTextPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(FarasNeonGreenLight)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "سكوتر متاح 🛵",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = FarasNeonGreenDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // The Three Core Services Grid (Ride, Send Package, Buy & Get)
            Text(
                text = if (language == AppLanguage.ARABIC) "خدمات فارسكوتر في مصر" else "Farascooter Core Services",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = FarasTextSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 1. Ride (مشوار)
                ServiceGridCardLight(
                    title = AppStrings.get("service_ride", language),
                    sub = if (language == AppLanguage.ARABIC) "تجاوز زحمة أكتوبر" else "Skip traffic",
                    icon = Icons.Default.ElectricScooter,
                    isSelected = uiState.selectedServiceType == ServiceType.RIDE,
                    modifier = Modifier.weight(1f),
                    badge = "أسرع 45%",
                    onClick = { viewModel.selectServiceType(ServiceType.RIDE) }
                )

                // 2. Send Package (إرسال طرد)
                ServiceGridCardLight(
                    title = AppStrings.get("service_send_package", language),
                    sub = if (language == AppLanguage.ARABIC) "أمان حتى 15 كجم" else "Up to 15kg",
                    icon = Icons.Default.Inventory2,
                    isSelected = uiState.selectedServiceType == ServiceType.SEND_PACKAGE,
                    modifier = Modifier.weight(1f),
                    badge = "15 كجم",
                    onClick = { viewModel.selectServiceType(ServiceType.SEND_PACKAGE) }
                )

                // 3. Buy & Get (جلب غرض)
                ServiceGridCardLight(
                    title = AppStrings.get("service_buy_and_get", language),
                    sub = if (language == AppLanguage.ARABIC) "صيدلية وماركت" else "Buy & bring",
                    icon = Icons.Default.LocalMall,
                    isSelected = uiState.selectedServiceType == ServiceType.BUY_AND_GET,
                    modifier = Modifier.weight(1f),
                    badge = "توصيل سريع",
                    onClick = { viewModel.selectServiceType(ServiceType.BUY_AND_GET) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Destinations in Cairo & Giza
            Text(
                text = AppStrings.get("popular_places", language),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = FarasTextSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                uiState.popularPlaces.forEach { place ->
                    PlaceRowItemLight(
                        place = place,
                        language = language,
                        onClick = { viewModel.selectDestination(place) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ServiceGridCardLight(
    title: String,
    sub: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    badge: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) FarasNeonGreenLight else FarasLightGreyBg)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) FarasNeonGreen else FarasCardBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) FarasNeonGreen else FarasWhite),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) FarasWhite else FarasNeonGreen,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = FarasTextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = sub,
                style = MaterialTheme.typography.labelSmall,
                color = FarasTextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun PlaceRowItemLight(
    place: PlaceItem,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(FarasLightGreyBg)
            .border(1.dp, FarasCardBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(FarasWhite),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = FarasNeonGreen,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (language == AppLanguage.ARABIC) place.nameAr else place.nameEn,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary,
                    maxLines = 1
                )
                Text(
                    text = if (language == AppLanguage.ARABIC) place.addressAr else place.addressEn,
                    style = MaterialTheme.typography.labelSmall,
                    color = FarasTextSecondary,
                    maxLines = 1
                )
            }
        }

        Text(
            text = "${place.distanceKm} ${AppStrings.get("km", language)}",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = FarasNeonGreenDark
        )
    }
}

@Composable
private fun ConfirmOrderSheet(
    uiState: FarascooterUiState,
    viewModel: FarascooterViewModel
) {
    val language = uiState.language
    val dest = uiState.selectedDestination ?: uiState.popularPlaces[0]
    val service = uiState.selectedServiceType

    // Intelligent Scooter vs Car Calculation based on distance in Egypt:
    // Scooter: speed 35km/h in congested traffic, bypasses standstill jams, lane filtering.
    // Car: standstill on 6th October bridge/Mehwar, 15km/h avg speed during traffic.
    val scooterDurationMin = (dest.distanceKm * 2.2).toInt().coerceAtLeast(5)
    val carDurationMin = (dest.distanceKm * 4.8).toInt().coerceAtLeast(15)
    val timeSavedMin = carDurationMin - scooterDurationMin

    // Fare calculation in EGP
    val scooterBase = 12.0
    val scooterKmRate = 3.5
    val scooterMinuteRate = 0.6
    val scooterTotal = (scooterBase + (dest.distanceKm * scooterKmRate) + (scooterDurationMin * scooterMinuteRate)) * uiState.surgeMultiplier

    val carEstimatedFare = (25.0 + (dest.distanceKm * 7.5) + (carDurationMin * 1.2)) * uiState.surgeMultiplier
    val moneySaved = carEstimatedFare - scooterTotal

    Surface(
        color = FarasWhite,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = when (service) {
                            ServiceType.RIDE -> AppStrings.get("service_ride", language)
                            ServiceType.SEND_PACKAGE -> AppStrings.get("service_send_package", language)
                            ServiceType.BUY_AND_GET -> AppStrings.get("service_buy_and_get", language)
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                    Text(
                        text = "${dest.distanceKm} ${AppStrings.get("km", language)} • القاهرة / الجيزة",
                        style = MaterialTheme.typography.labelMedium,
                        color = FarasTextSecondary
                    )
                }

                IconButton(
                    onClick = { viewModel.setCustomerScreen(CustomerScreen.HOME) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = FarasTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // CRUCIAL: Scooter vs Car Comparison Card (Mandated in prompt!)
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasNeonGreenLight),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarasNeonGreen.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Speed, contentDescription = null, tint = FarasNeonGreenDark, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.get("scooter_vs_car_title", language),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = FarasNeonGreenDark
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Scooter Metric
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ElectricScooter, contentDescription = null, tint = FarasNeonGreenDark, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("السكوتر:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = FarasTextPrimary)
                            }
                            Text(
                                text = "$scooterDurationMin دقيقة فقط ⚡",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasNeonGreenDark
                            )
                            Text(
                                text = "${String.format(java.util.Locale.US, "%.1f", scooterTotal)} ج.م",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasTextPrimary
                            )
                        }

                        // Divider
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(50.dp)
                                .background(FarasNeonGreen.copy(alpha = 0.3f))
                        )

                        // Car Metric
                        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = FarasTextSecondary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("السيارة / التاكسي:", style = MaterialTheme.typography.labelSmall, color = FarasTextSecondary)
                            }
                            Text(
                                text = "$carDurationMin دقيقة (في الزحام) 🐢",
                                style = MaterialTheme.typography.titleSmall,
                                color = FarasTextSecondary
                            )
                            Text(
                                text = "${String.format(java.util.Locale.US, "%.1f", carEstimatedFare)} ج.م",
                                style = MaterialTheme.typography.labelMedium,
                                color = FarasTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Savings Summary Badge
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(FarasWhite)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "🎉 وفرت $timeSavedMin دقيقة من وقتك ووفرت ${String.format(java.util.Locale.US, "%.1f", moneySaved)} ج.م!",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = FarasNeonGreenDark,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Mandatory Sanitized Helmet Notice
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(FarasSurfaceVariant)
                    .border(1.dp, FarasCardBorder, RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.HealthAndSafety,
                        contentDescription = "Safety Helmet",
                        tint = FarasNeonGreenDark,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.get("safety_helmet_badge", language),
                        style = MaterialTheme.typography.bodySmall,
                        color = FarasTextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fare & Payment Method Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = AppStrings.get("estimated_fare", language),
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = String.format(java.util.Locale.US, "%.1f", scooterTotal),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasTextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = AppStrings.get("currency", language),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = FarasNeonGreenDark
                        )
                    }
                }

                // Payment selector
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(FarasLightGreyBg)
                        .border(1.dp, FarasCardBorder, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    PaymentTogglePillLight(
                        label = AppStrings.get("wallet", language),
                        icon = Icons.Default.AccountBalanceWallet,
                        isSelected = uiState.selectedPaymentMethod == PaymentMethod.WALLET,
                        onClick = { viewModel.selectPaymentMethod(PaymentMethod.WALLET) }
                    )
                    PaymentTogglePillLight(
                        label = AppStrings.get("cash", language),
                        icon = Icons.Default.Money,
                        isSelected = uiState.selectedPaymentMethod == PaymentMethod.CASH,
                        onClick = { viewModel.selectPaymentMethod(PaymentMethod.CASH) }
                    )
                    PaymentTogglePillLight(
                        label = AppStrings.get("credit_card", language),
                        icon = Icons.Default.CreditCard,
                        isSelected = uiState.selectedPaymentMethod == PaymentMethod.CREDIT_CARD,
                        onClick = { viewModel.selectPaymentMethod(PaymentMethod.CREDIT_CARD) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Button
            Button(
                onClick = { viewModel.requestOrder() },
                colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("confirm_order_button")
            ) {
                Text(
                    text = when (service) {
                        ServiceType.RIDE -> AppStrings.get("confirm_ride", language)
                        ServiceType.SEND_PACKAGE -> AppStrings.get("confirm_package", language)
                        ServiceType.BUY_AND_GET -> AppStrings.get("confirm_errand", language)
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasWhite
                )
            }
        }
    }
}

@Composable
private fun PaymentTogglePillLight(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) FarasNeonGreen else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) FarasWhite else FarasTextSecondary,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun DispatchingRadarSheet(
    uiState: FarascooterUiState,
    viewModel: FarascooterViewModel
) {
    val language = uiState.language

    Surface(
        color = FarasWhite,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(FarasNeonGreenLight),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = FarasNeonGreen,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(76.dp)
                )
                Icon(
                    imageVector = Icons.Default.ElectricScooter,
                    contentDescription = null,
                    tint = FarasNeonGreenDark,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = AppStrings.get("dispatching_title", language),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = FarasTextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${AppStrings.get("dispatching_sub", language)} (${uiState.dispatchCountdownSec}s)",
                style = MaterialTheme.typography.bodySmall,
                color = FarasTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = { viewModel.cancelOrder() },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("cancel_dispatch_btn")
            ) {
                Text(
                    text = AppStrings.get("cancel_request", language),
                    style = MaterialTheme.typography.labelLarge,
                    color = FarasRedAlert
                )
            }
        }
    }
}

@Composable
private fun LiveTrackingSheet(
    uiState: FarascooterUiState,
    viewModel: FarascooterViewModel
) {
    val language = uiState.language
    val trip = uiState.activeTrip
    val captain = trip?.captain ?: viewModel.uiState.value.captainsList.first()

    Surface(
        color = FarasWhite,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Status & SOS Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = when (trip?.status) {
                            TripStatus.ON_THE_WAY -> AppStrings.get("rider_on_way", language)
                            TripStatus.ARRIVED_PICKUP -> if (language == AppLanguage.ARABIC) "وصل الفارس لنقطة الانطلاق 🛵" else "Captain arrived at pickup 🛵"
                            TripStatus.IN_TRANSIT -> AppStrings.get("trip_in_progress", language)
                            else -> AppStrings.get("rider_assigned", language)
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                    Text(
                        text = "${AppStrings.get("rider_arriving_in", language)} ${(trip?.durationMin ?: 5)} ${AppStrings.get("minutes", language)}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = FarasNeonGreenDark
                    )
                }

                // SOS Trigger Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(FarasRedAlert)
                        .clickable { viewModel.triggerSosDialog(true) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("sos_emergency_btn")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "SOS",
                            tint = FarasWhite,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "SOS",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasWhite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { uiState.captainProgressRatio },
                color = FarasNeonGreen,
                trackColor = FarasCardBorder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Captain Info Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(FarasLightGreyBg)
                    .border(1.dp, FarasCardBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(FarasNeonGreenLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ElectricScooter,
                                contentDescription = null,
                                tint = FarasNeonGreenDark,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = captain.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasTextPrimary
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = FarasAmber,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${captain.rating} (${captain.totalRides})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FarasTextPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "• ${captain.plateNumber}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = FarasNeonGreenDark
                                )
                            }
                            Text(
                                text = captain.scooterModel,
                                style = MaterialTheme.typography.labelSmall,
                                color = FarasTextSecondary
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = { /* Masked Call */ },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(FarasWhite)
                                .border(1.dp, FarasCardBorder, CircleShape)
                                .testTag("captain_call_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call",
                                tint = FarasNeonGreenDark,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.toggleChatDialog(true) },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(FarasWhite)
                                .border(1.dp, FarasCardBorder, CircleShape)
                                .testTag("captain_chat_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "Chat",
                                tint = FarasNeonGreenDark,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step progression button for testing
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = FarasNeonGreenDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.ARABIC) "رحلة مؤمنة وخوذة معقمة متوفرة" else "Insured Ride & Sanitized Helmet",
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(FarasSurfaceVariant)
                        .clickable { viewModel.advanceTripMilestone() }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("fast_forward_milestone_btn")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = null,
                            tint = FarasNeonGreenDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ARABIC) "المرحلة التالية" else "Next Step",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = FarasTextPrimary
                        )
                    }
                }
            }
        }
    }
}
