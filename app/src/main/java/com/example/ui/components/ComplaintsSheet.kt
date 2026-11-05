package com.example.ui.components

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.model.ComplaintTicket
import com.example.ui.theme.FarasAmber
import com.example.ui.theme.FarasAmberBg
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasLightGreyBg
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenDark
import com.example.ui.theme.FarasNeonGreenLight
import com.example.ui.theme.FarasRedAlert
import com.example.ui.theme.FarasTextPrimary
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ComplaintsSheet(
    language: AppLanguage,
    tickets: List<ComplaintTicket>,
    initialTripId: String? = null,
    initialCaptainName: String? = null,
    onSubmitComplaint: (ComplaintTicket) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val categories = listOf(
        AppStrings.get("complaint_cat_behavior", language),
        AppStrings.get("complaint_cat_helmet", language),
        AppStrings.get("complaint_cat_fare", language),
        AppStrings.get("complaint_cat_lost", language),
        AppStrings.get("complaint_cat_other", language)
    )

    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var descriptionInput by remember { mutableStateOf("") }
    var showNewForm by remember { mutableStateOf(true) }

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(FarasAmberBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = null,
                            tint = FarasAmber,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = AppStrings.get("support_and_complaints", language),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = FarasTextPrimary
                        )
                        Text(
                            text = if (language == AppLanguage.ARABIC) "فريق دعم عملاء FareScooter في خدمتك 24/7" else "FareScooter Support 24/7",
                            style = MaterialTheme.typography.labelSmall,
                            color = FarasTextSecondary
                        )
                    }
                }

                IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_complaints_btn")) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = FarasTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Toggle between "تقديم شكوى جديدة" and "سجل الشكاوى"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (showNewForm) AppStrings.get("complaint_title", language) else AppStrings.get("my_complaints", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary
                )

                Text(
                    text = if (showNewForm) "${AppStrings.get("my_complaints", language)} (${tickets.size})" else "+ تقديم شكوى جديدة",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasNeonGreenDark,
                    modifier = Modifier
                        .clickable { showNewForm = !showNewForm }
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (showNewForm) {
                // Trip Association Note
                if (initialTripId != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(FarasLightGreyBg)
                            .border(1.dp, FarasCardBorder, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "المشوار المعني: $initialTripId • الكابتن: ${initialCaptainName ?: "كابتن سيف الدين"}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasTextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Category Selection
                Text(
                    text = AppStrings.get("complaint_category", language),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    categories.forEach { cat ->
                        val isSelected = cat == selectedCategory
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) FarasNeonGreenLight else FarasLightGreyBg)
                                .border(1.dp, if (isSelected) FarasNeonGreen else FarasCardBorder, RoundedCornerShape(10.dp))
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) FarasNeonGreenDark else FarasTextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Multiline Description
                OutlinedTextField(
                    value = descriptionInput,
                    onValueChange = { descriptionInput = it },
                    label = { Text(AppStrings.get("complaint_details", language)) },
                    placeholder = {
                        Text(
                            if (language == AppLanguage.ARABIC)
                                "اشرح ما حدث بدقة لنتمكن من مراجعة الرحلة والتواصل معك فوراً..."
                            else
                                "Explain the issue in detail..."
                        )
                    },
                    colors = textFieldColorsLight(),
                    shape = RoundedCornerShape(14.dp),
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("complaint_desc_input")
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (descriptionInput.isNotBlank()) {
                            val newTicket = ComplaintTicket(
                                id = "TKT-${(1000..9999).random()}",
                                tripId = initialTripId ?: "FS-EG-4412",
                                captainName = initialCaptainName ?: "كابتن سيف الدين",
                                category = selectedCategory,
                                description = descriptionInput.trim(),
                                date = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date()),
                                status = "قيد المراجعة من الدعم"
                            )
                            onSubmitComplaint(newTicket)
                            descriptionInput = ""
                            showNewForm = false
                            Toast.makeText(
                                context,
                                AppStrings.get("complaint_submitted", language),
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                if (language == AppLanguage.ARABIC) "يرجى كتابة تفاصيل الشكوى أولاً" else "Please enter complaint details",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FarasRedAlert),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_complaint_btn")
                ) {
                    Icon(Icons.Default.ReportProblem, contentDescription = null, tint = FarasWhite)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.get("submit_complaint_btn", language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FarasWhite
                    )
                }
            } else {
                // List of Tickets
                if (tickets.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "لا توجد شكاوى مسجلة حالياً. نتمنى لك دوماً رحلات سعيدة وآمنة!" else "No complaints filed.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = FarasTextSecondary
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        tickets.forEach { ticket ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = FarasLightGreyBg),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = ticket.id,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = FarasTextPrimary
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(FarasAmberBg)
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = ticket.status,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = FarasAmber
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = ticket.category,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = FarasTextPrimary
                                    )

                                    Text(
                                        text = ticket.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = FarasTextSecondary
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "المشوار: ${ticket.tripId} • ${ticket.date}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = FarasTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
