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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Star
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
import com.example.model.ScooterVsCarComparison
import com.example.model.ServiceType
import com.example.model.TripOrder
import com.example.ui.theme.FarasAmber
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasLightGreyBg
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenDark
import com.example.ui.theme.FarasNeonGreenLight
import com.example.ui.theme.FarasTextPrimary
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite
import com.example.viewmodel.CustomerScreen
import com.example.viewmodel.FarascooterUiState
import com.example.viewmodel.FarascooterViewModel

@Composable
fun TripHistoryScreen(
    uiState: FarascooterUiState,
    viewModel: FarascooterViewModel,
    modifier: Modifier = Modifier
) {
    val language = uiState.language

    BackHandler {
        viewModel.setCustomerScreen(CustomerScreen.PROFILE)
    }

    Surface(
        color = FarasLightGreyBg,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.setCustomerScreen(CustomerScreen.PROFILE) },
                    modifier = Modifier.testTag("history_back_btn")
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
                        text = AppStrings.get("trips_history", language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                    Text(
                        text = if (language == AppLanguage.ARABIC) "اضغط على أي مشوار لعرض تفاصيل الإيصال المالي أو تقديم شكوى" else "Tap any trip to view receipt details or report an issue",
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val trips = if (uiState.tripHistory.isEmpty()) {
                listOf(
                    TripOrder(
                        id = "FS-EG-1029",
                        serviceType = ServiceType.RIDE,
                        pickupAddress = "ميدان التحرير، وسط البلد",
                        destinationAddress = "برج القاهرة، الزمالك",
                        pickupXRatio = 0.5f,
                        pickupYRatio = 0.5f,
                        destXRatio = 0.44f,
                        destYRatio = 0.48f,
                        distanceKm = 2.6,
                        durationMin = 8,
                        carDurationMin = 22,
                        baseFare = 21.0,
                        surgeMultiplier = 1.0,
                        finalFare = 21.0,
                        carEstimatedFare = 48.0,
                        comparison = ScooterVsCarComparison(8, 22, 14, 21.0, 48.0, 27.0),
                        paymentMethod = com.example.model.PaymentMethod.WALLET,
                        status = com.example.model.TripStatus.COMPLETED,
                        captain = viewModel.uiState.value.captainsList[0],
                        rating = 5
                    ),
                    TripOrder(
                        id = "FS-EG-8842",
                        serviceType = ServiceType.SEND_PACKAGE,
                        pickupAddress = "المهندسين، شارع جامعة الدول",
                        destinationAddress = "المعادي، شارع 9",
                        pickupXRatio = 0.32f,
                        pickupYRatio = 0.45f,
                        destXRatio = 0.46f,
                        destYRatio = 0.75f,
                        distanceKm = 6.5,
                        durationMin = 16,
                        carDurationMin = 42,
                        baseFare = 32.0,
                        surgeMultiplier = 1.0,
                        finalFare = 32.0,
                        carEstimatedFare = 78.0,
                        comparison = ScooterVsCarComparison(16, 42, 26, 32.0, 78.0, 46.0),
                        paymentMethod = com.example.model.PaymentMethod.CASH,
                        status = com.example.model.TripStatus.COMPLETED,
                        captain = viewModel.uiState.value.captainsList[1],
                        rating = 5
                    )
                )
            } else {
                uiState.tripHistory
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(trips) { trip ->
                    TripReceiptCardLight(
                        trip = trip,
                        language = language,
                        onClick = { viewModel.openTripDetail(trip) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TripReceiptCardLight(
    trip: TripOrder,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(FarasWhite)
            .border(1.dp, FarasCardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
            .testTag("trip_card_${trip.id}")
    ) {
        Column {
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
                            .background(FarasNeonGreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (trip.serviceType) {
                                ServiceType.RIDE -> Icons.Default.ElectricScooter
                                ServiceType.SEND_PACKAGE -> Icons.Default.Inventory2
                                ServiceType.BUY_AND_GET -> Icons.Default.LocalMall
                            },
                            contentDescription = null,
                            tint = FarasNeonGreenDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = when (trip.serviceType) {
                                ServiceType.RIDE -> AppStrings.get("service_ride", language)
                                ServiceType.SEND_PACKAGE -> AppStrings.get("service_send_package", language)
                                ServiceType.BUY_AND_GET -> AppStrings.get("service_buy_and_get", language)
                            },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = FarasTextPrimary
                        )
                        Text(
                            text = trip.id,
                            style = MaterialTheme.typography.labelSmall,
                            color = FarasTextSecondary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${trip.finalFare} ${AppStrings.get("currency", language)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FarasNeonGreenDark
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = "Receipt",
                        tint = FarasTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${trip.pickupAddress} ➔ ${trip.destinationAddress}",
                style = MaterialTheme.typography.bodySmall,
                color = FarasTextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${trip.distanceKm} ${AppStrings.get("km", language)} • ${trip.captain?.name ?: "الكابتن سيف"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = FarasTextSecondary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = FarasAmber,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "5.0",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                }
            }
        }
    }
}
