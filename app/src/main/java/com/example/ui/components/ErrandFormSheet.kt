package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ListAlt
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.model.ErrandDetails
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenDark
import com.example.ui.theme.FarasTextPrimary
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite

@Composable
fun ErrandFormSheet(
    language: AppLanguage,
    initialDraft: ErrandDetails,
    onBack: () -> Unit,
    onConfirm: (ErrandDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    var storeName by remember { mutableStateOf(initialDraft.storeName) }
    var itemsList by remember { mutableStateOf(initialDraft.itemsList) }
    var budget by remember { mutableStateOf(initialDraft.estimatedBudget.toString()) }

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

            // Store Name
            OutlinedTextField(
                value = storeName,
                onValueChange = { storeName = it },
                label = { Text(AppStrings.get("store_name", language)) },
                leadingIcon = {
                    Icon(Icons.Default.Store, contentDescription = null, tint = FarasNeonGreenDark)
                },
                colors = textFieldColorsLight(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("store_name_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Items List
            OutlinedTextField(
                value = itemsList,
                onValueChange = { itemsList = it },
                label = { Text(AppStrings.get("items_list", language)) },
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

            // Budget in EGP
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
