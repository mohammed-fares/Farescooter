package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import com.example.model.ServiceType
import com.example.model.TripOrder
import com.example.ui.theme.FarasAmber
import com.example.ui.theme.FarasBlack
import com.example.ui.theme.FarasCardBg
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasSurfaceDark
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
        color = FarasBlack,
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
                        tint = FarasWhite
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.get("trips_history", language),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = FarasWhite
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val trips = if (uiState.tripHistory.isEmpty()) {
                listOf(
                    TripOrder(
                        id = "FS-9912",
                        serviceType = ServiceType.RIDE,
                        pickupAddress = "برج خليفة، وسط المدينة",
                        destinationAddress = "مركز دبي المالي DIFC",
                        pickupXRatio = 0.5f,
                        pickupYRatio = 0.5f,
                        destXRatio = 0.7f,
                        destYRatio = 0.3f,
                        distanceKm = 2.9,
                        durationMin = 7,
                        baseFare = 22.0,
                        surgeMultiplier = 1.0,
                        finalFare = 22.0,
                        paymentMethod = com.example.model.PaymentMethod.WALLET,
                        status = com.example.model.TripStatus.COMPLETED,
                        captain = viewModel.uiState.value.captainsList[0],
                        rating = 5
                    ),
                    TripOrder(
                        id = "FS-8814",
                        serviceType = ServiceType.SEND_PACKAGE,
                        pickupAddress = "الخليج التجاري",
                        destinationAddress = "سيتي سنتر ديرة",
                        pickupXRatio = 0.4f,
                        pickupYRatio = 0.6f,
                        destXRatio = 0.8f,
                        destYRatio = 0.2f,
                        distanceKm = 5.2,
                        durationMin = 14,
                        baseFare = 28.0,
                        surgeMultiplier = 1.2,
                        finalFare = 33.6,
                        paymentMethod = com.example.model.PaymentMethod.CREDIT_CARD,
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
                    TripReceiptCard(trip = trip, language = language)
                }
            }
        }
    }
}

@Composable
private fun TripReceiptCard(
    trip: TripOrder,
    language: AppLanguage
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(FarasCardBg)
            .padding(16.dp)
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
                            .background(FarasSurfaceDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (trip.serviceType) {
                                ServiceType.RIDE -> Icons.Default.ElectricScooter
                                ServiceType.SEND_PACKAGE -> Icons.Default.Inventory2
                                ServiceType.BUY_AND_GET -> Icons.Default.LocalMall
                            },
                            contentDescription = null,
                            tint = FarasNeonGreen,
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
                            color = FarasWhite
                        )
                        Text(
                            text = trip.id,
                            style = MaterialTheme.typography.labelSmall,
                            color = FarasTextSecondary
                        )
                    }
                }

                Text(
                    text = "${trip.finalFare} ${AppStrings.get("currency", language)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasNeonGreen
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${trip.pickupAddress} ➔ ${trip.destinationAddress}",
                style = MaterialTheme.typography.bodySmall,
                color = FarasWhite
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
                        color = FarasWhite
                    )
                }
            }
        }
    }
}
