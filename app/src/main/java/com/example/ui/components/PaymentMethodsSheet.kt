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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.model.SavedCard
import com.example.model.SavedWallet
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasLightGreyBg
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenDark
import com.example.ui.theme.FarasNeonGreenLight
import com.example.ui.theme.FarasSurfaceVariant
import com.example.ui.theme.FarasTextPrimary
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite

@Composable
fun PaymentMethodsSheet(
    language: AppLanguage,
    savedCards: List<SavedCard>,
    savedWallets: List<SavedWallet>,
    onAddCard: (SavedCard) -> Unit,
    onAddWallet: (SavedWallet) -> Unit,
    onSetDefaultCard: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showAddCardForm by remember { mutableStateOf(false) }
    var cardNumberInput by remember { mutableStateOf("") }
    var cardHolderInput by remember { mutableStateOf("") }
    var cardExpiryInput by remember { mutableStateOf("") }
    var cardCvvInput by remember { mutableStateOf("") }
    var saveCardChecked by remember { mutableStateOf(true) }

    var showAddWalletForm by remember { mutableStateOf(false) }
    var selectedWalletProvider by remember { mutableStateOf("فودافون كاش (Vodafone Cash)") }
    var walletPhoneInput by remember { mutableStateOf("") }

    Surface(
        color = FarasLightGreyBg,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // Header with Back
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("payment_back_btn")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = FarasTextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.get("payment_methods_title", language),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 1: Saved Bank Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.get("saved_cards", language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary
                )
                Text(
                    text = if (showAddCardForm) AppStrings.get("close", language) else AppStrings.get("add_new_card", language),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasNeonGreenDark,
                    modifier = Modifier
                        .clickable { showAddCardForm = !showAddCardForm }
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Add Card Form
            AnimatedVisibility(visible = showAddCardForm) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = FarasWhite),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarasNeonGreen.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "إضافة بطاقة فيزا أو ماستركارد جديدة",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = FarasTextPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = cardNumberInput,
                            onValueChange = { if (it.length <= 19) cardNumberInput = it },
                            label = { Text(AppStrings.get("card_number", language)) },
                            leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null, tint = FarasNeonGreenDark) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = textFieldColorsLight(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = cardHolderInput,
                            onValueChange = { cardHolderInput = it },
                            label = { Text(AppStrings.get("card_holder", language)) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = FarasNeonGreenDark) },
                            colors = textFieldColorsLight(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = cardExpiryInput,
                                onValueChange = { if (it.length <= 5) cardExpiryInput = it },
                                label = { Text(AppStrings.get("card_expiry", language)) },
                                placeholder = { Text("12/28") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = textFieldColorsLight(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = cardCvvInput,
                                onValueChange = { if (it.length <= 4) cardCvvInput = it },
                                label = { Text(AppStrings.get("card_cvv", language)) },
                                placeholder = { Text("•••") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = FarasNeonGreenDark) },
                                colors = textFieldColorsLight(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = saveCardChecked,
                                onCheckedChange = { saveCardChecked = it },
                                colors = CheckboxDefaults.colors(checkedColor = FarasNeonGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.get("save_card_for_future", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = FarasTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (cardNumberInput.length >= 12 && cardHolderInput.isNotBlank()) {
                                    val last4 = if (cardNumberInput.length >= 4) cardNumberInput.takeLast(4) else "4412"
                                    val newCard = SavedCard(
                                        id = "card_${System.currentTimeMillis()}",
                                        cardNumberMasked = "•••• •••• •••• $last4",
                                        cardHolder = cardHolderInput,
                                        expiry = if (cardExpiryInput.isNotBlank()) cardExpiryInput else "09/29",
                                        cardBrand = "Visa",
                                        isDefault = true
                                    )
                                    onAddCard(newCard)
                                    showAddCardForm = false
                                    cardNumberInput = ""
                                    cardHolderInput = ""
                                    cardExpiryInput = ""
                                    cardCvvInput = ""
                                    Toast.makeText(context, "تم حفظ البطاقة البنكية بنجاح للدفع أونلاين 💳", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "يرجى استكمال بيانات البطاقة بشكل صحيح", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = AppStrings.get("save_card_btn", language),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                        }
                    }
                }
            }

            // Cards List
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                savedCards.forEach { card ->
                    SavedCardRow(card = card, onSelectDefault = { onSetDefaultCard(card.id) })
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section 2: Electronic Wallets (فودافون كاش، إنستاباي، وغيرها)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = AppStrings.get("e_wallets", language),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                    Text(
                        text = AppStrings.get("e_wallet_hint", language),
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )
                }

                Text(
                    text = if (showAddWalletForm) AppStrings.get("close", language) else "+ ربط محفظة",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarasNeonGreenDark,
                    modifier = Modifier
                        .clickable { showAddWalletForm = !showAddWalletForm }
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Link Wallet Form
            AnimatedVisibility(visible = showAddWalletForm) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = FarasWhite),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarasNeonGreen.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "ربط محفظة إلكترونية ذكية",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = FarasTextPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Provider selector pills
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("فودافون كاش", "إنستاباي", "أورنج كاش").forEach { prov ->
                                val isSel = selectedWalletProvider.contains(prov)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) FarasNeonGreenLight else FarasLightGreyBg)
                                        .border(1.dp, if (isSel) FarasNeonGreen else FarasCardBorder, RoundedCornerShape(8.dp))
                                        .clickable { selectedWalletProvider = prov }
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = prov,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSel) FarasNeonGreenDark else FarasTextPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = walletPhoneInput,
                            onValueChange = { walletPhoneInput = it },
                            label = { Text(AppStrings.get("wallet_number", language)) },
                            placeholder = { Text("010xxxxxxxx") },
                            leadingIcon = { Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = FarasNeonGreenDark) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = textFieldColorsLight(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (walletPhoneInput.length >= 11) {
                                    val newW = SavedWallet(
                                        id = "w_${System.currentTimeMillis()}",
                                        provider = selectedWalletProvider,
                                        walletNumber = walletPhoneInput,
                                        isDefault = true
                                    )
                                    onAddWallet(newW)
                                    showAddWalletForm = false
                                    walletPhoneInput = ""
                                    Toast.makeText(context, "تم ربط محفظة $selectedWalletProvider بنجاح! 📱", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "يرجى إدخال رقم هاتف محفظة صحيح مكون من 11 رقماً", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Text(
                                text = AppStrings.get("save_wallet_btn", language),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                        }
                    }
                }
            }

            // Wallets list
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                savedWallets.forEach { w ->
                    SavedWalletRow(wallet = w)
                }
            }
        }
    }
}

@Composable
private fun SavedCardRow(
    card: SavedCard,
    onSelectDefault: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(FarasWhite)
            .border(
                1.5.dp,
                if (card.isDefault) FarasNeonGreen else FarasCardBorder,
                RoundedCornerShape(14.dp)
            )
            .clickable { onSelectDefault() }
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
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(FarasNeonGreenLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = null,
                        tint = FarasNeonGreenDark,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "${card.cardBrand} ${card.cardNumberMasked}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                    Text(
                        text = "${card.cardHolder} • ينتهي في ${card.expiry}",
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )
                }
            }

            if (card.isDefault) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(FarasNeonGreenLight)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "الافتراضية ✓",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = FarasNeonGreenDark
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedWalletRow(wallet: SavedWallet) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(FarasWhite)
            .border(1.dp, FarasCardBorder, RoundedCornerShape(14.dp))
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
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(FarasSurfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        tint = FarasNeonGreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = wallet.provider,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                    Text(
                        text = wallet.walletNumber,
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )
                }
            }

            Text(
                text = "جاهزة للدفع",
                style = MaterialTheme.typography.labelSmall,
                color = FarasNeonGreenDark,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
