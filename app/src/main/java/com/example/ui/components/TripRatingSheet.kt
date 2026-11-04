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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.model.TripOrder
import com.example.ui.theme.FarasAmber
import com.example.ui.theme.FarasBlack
import com.example.ui.theme.FarasCardBg
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasSurfaceDark
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite

@Composable
fun TripRatingSheet(
    language: AppLanguage,
    trip: TripOrder?,
    onSubmit: (Int, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedStars by remember { mutableIntStateOf(5) }
    var selectedTip by remember { mutableDoubleStateOf(5.0) }

    Surface(
        color = FarasSurfaceDark,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Success Check Badge
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(FarasNeonGreen.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = FarasNeonGreen,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = AppStrings.get("trip_completed_title", language),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = FarasWhite
            )

            Text(
                text = "${trip?.finalFare ?: 28.0} ${AppStrings.get("currency", language)} • ${trip?.captain?.name ?: "الكابتن سيف"}",
                style = MaterialTheme.typography.bodyMedium,
                color = FarasNeonGreen
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 5 Star Rating Bar
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (star in 1..5) {
                    Icon(
                        imageVector = if (star <= selectedStars) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Star $star",
                        tint = if (star <= selectedStars) FarasAmber else FarasCardBorder,
                        modifier = Modifier
                            .size(38.dp)
                            .clickable { selectedStars = star }
                            .testTag("star_rating_$star")
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Compliments chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val compliment1 = if (language == AppLanguage.ARABIC) "سائق محترف" else "Pro Rider"
                val compliment2 = if (language == AppLanguage.ARABIC) "خوذة معقمة ونظيفة" else "Sanitized Helmet"
                val compliment3 = if (language == AppLanguage.ARABIC) "تجاوز سريع للزحام" else "Skipped Traffic"

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FarasCardBg)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = compliment1,
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1.2f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FarasCardBg)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = compliment2,
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasNeonGreen,
                        textAlign = TextAlign.Center
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1.1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FarasCardBg)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = compliment3,
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tip Selection
            Text(
                text = AppStrings.get("tip_captain", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = FarasWhite
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val tipOptions = listOf(0.0, 3.0, 5.0, 10.0)
                tipOptions.forEach { tipVal ->
                    val isSelected = selectedTip == tipVal
                    val label = if (tipVal == 0.0) AppStrings.get("no_tip", language) else "+${tipVal.toInt()} ${AppStrings.get("currency", language)}"

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) FarasNeonGreen else FarasCardBg)
                            .border(1.dp, if (isSelected) FarasNeonGreen else FarasCardBorder, RoundedCornerShape(12.dp))
                            .clickable { selectedTip = tipVal }
                            .padding(vertical = 10.dp)
                            .testTag("tip_option_${tipVal.toInt()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) FarasBlack else FarasWhite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = { onSubmit(selectedStars, selectedTip) },
                colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_rating_btn")
            ) {
                Text(
                    text = AppStrings.get("finish_and_submit", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasBlack
                )
            }
        }
    }
}
