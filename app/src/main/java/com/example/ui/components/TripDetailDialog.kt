package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.model.PaymentMethod
import com.example.model.TripOrder
import com.example.ui.theme.FarasAmber
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasLightGreyBg
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenDark
import com.example.ui.theme.FarasNeonGreenLight
import com.example.ui.theme.FarasRedAlert
import com.example.ui.theme.FarasTextPrimary
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailDialog(
    language: AppLanguage,
    trip: TripOrder,
    onFileComplaint: (tripId: String, captainName: String) -> Unit,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, FarasCardBorder, RoundedCornerShape(24.dp)),
            color = FarasWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(scrollState)
            ) {
                // Header with Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(FarasNeonGreenLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = null,
                                tint = FarasNeonGreenDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppStrings.get("trip_details_title", language),
                                style = MaterialTheme.typography.titleMedium,
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

                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_trip_detail_btn")) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = FarasTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Captain Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = FarasLightGreyBg),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(FarasNeonGreenLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = FarasNeonGreenDark,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = trip.captain?.name ?: "كابتن سيف الدين المصري",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasTextPrimary
                            )
                            Text(
                                text = "${trip.captain?.scooterModel ?: "Xiaomi Pro Max Ultra"} • ${trip.captain?.plateNumber ?: "ق هـ د 4192"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = FarasTextSecondary
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = FarasAmber, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${trip.captain?.rating ?: 4.96}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasTextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Route details
                Card(
                    colors = CardDefaults.cardColors(containerColor = FarasLightGreyBg),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = FarasNeonGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = AppStrings.get("pickup_location", language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FarasTextSecondary
                                )
                                Text(
                                    text = trip.pickupAddress,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = FarasTextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = FarasAmber, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = AppStrings.get("destination_location", language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FarasTextSecondary
                                )
                                Text(
                                    text = trip.destinationAddress,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = FarasTextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "المسافة: ${trip.distanceKm} كم",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasTextSecondary
                            )
                            Text(
                                text = "المدة: ${trip.durationMin} دقيقة بالسكوتر",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasNeonGreenDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Price Breakdown Receipt (تفصيل القيمة)
                Text(
                    text = "تفصيل الحساب والإيصال البنكي 🧾",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = FarasWhite),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        ReceiptRowItem(label = AppStrings.get("base_fare", language), value = "${trip.baseFare} ج.م")
                        ReceiptRowItem(label = AppStrings.get("distance_fare", language), value = "${String.format(java.util.Locale.US, "%.1f", trip.distanceKm * 3.5)} ج.م")
                        ReceiptRowItem(label = AppStrings.get("time_fare", language), value = "${String.format(java.util.Locale.US, "%.1f", trip.durationMin * 0.6)} ج.م")
                        
                        if (trip.surgeMultiplier > 1.0) {
                            ReceiptRowItem(label = AppStrings.get("surge_fare", language), value = "${trip.surgeMultiplier}x")
                        }

                        if (trip.discountAmount > 0) {
                            ReceiptRowItem(label = AppStrings.get("discount_applied", language), value = "-${trip.discountAmount} ج.م", isDiscount = true)
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = FarasCardBorder
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = AppStrings.get("total_paid", language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasTextPrimary
                            )
                            Text(
                                text = "${trip.finalFare} ${AppStrings.get("currency", language)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = FarasNeonGreenDark
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "طريقة الدفع: ${when(trip.paymentMethod) {
                                PaymentMethod.WALLET -> "محفظة FareScooter الذكية"
                                PaymentMethod.CASH -> "نقداً (كاش للكابتن)"
                                PaymentMethod.CREDIT_CARD -> "بطاقة بنكية أونلاين (فيزا)"
                                PaymentMethod.E_WALLET -> "محفظة إلكترونية (فودافون كاش / إنستاباي)"
                            }}",
                            style = MaterialTheme.typography.labelSmall,
                            color = FarasTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action: File Complaint for this trip
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onFileComplaint(trip.id, trip.captain?.name ?: "كابتن سيف الدين")
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = FarasRedAlert),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("file_trip_complaint_btn")
                ) {
                    Icon(Icons.Default.ReportProblem, contentDescription = null, tint = FarasRedAlert, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.get("file_complaint_btn", language),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = FarasRedAlert
                    )
                }
            }
        }
    }
}

@Composable
private fun ReceiptRowItem(
    label: String,
    value: String,
    isDiscount: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = FarasTextSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isDiscount) FontWeight.Bold else FontWeight.Medium,
            color = if (isDiscount) FarasNeonGreenDark else FarasTextPrimary
        )
    }
}
