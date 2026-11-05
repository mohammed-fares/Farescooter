package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasLightGreyBg
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenDark
import com.example.ui.theme.FarasNeonGreenLight
import com.example.ui.theme.FarasTextPrimary
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite
import kotlinx.coroutines.delay

@Composable
fun AuthScreen(
    language: AppLanguage,
    onAuthSuccess: (name: String, phone: String, email: String) -> Unit,
    onSkipGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var isOtpStep by remember { mutableStateOf(false) }
    var phoneInput by remember { mutableStateOf("01098765432") }
    var emailInput by remember { mutableStateOf("mf.z.hussein.pal@gmail.com") }
    var nameInput by remember { mutableStateOf("محمد فاروق") }
    var otpCodeInput by remember { mutableStateOf("") }
    var countdownSec by remember { mutableIntStateOf(60) }

    LaunchedEffect(isOtpStep) {
        if (isOtpStep) {
            countdownSec = 60
            while (countdownSec > 0) {
                delay(1000)
                countdownSec--
            }
        }
    }

    Surface(
        color = FarasLightGreyBg,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Brand Logo
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(FarasNeonGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ElectricScooter,
                    contentDescription = "FareScooter Logo",
                    tint = FarasWhite,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = AppStrings.get("app_name", language),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = FarasTextPrimary
            )

            Text(
                text = AppStrings.get("tagline", language),
                style = MaterialTheme.typography.bodyMedium,
                color = FarasTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Main Auth Card
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasWhite),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!isOtpStep) {
                        // Step 1: Input Phone + Email + Name
                        Text(
                            text = AppStrings.get("auth_welcome_title", language),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = FarasTextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = AppStrings.get("auth_welcome_sub", language),
                            style = MaterialTheme.typography.bodySmall,
                            color = FarasTextSecondary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Full Name
                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text(AppStrings.get("full_name", language)) },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = FarasNeonGreenDark)
                            },
                            colors = authFieldColors(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_name_field")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Phone Number
                        OutlinedTextField(
                            value = phoneInput,
                            onValueChange = { phoneInput = it },
                            label = { Text(AppStrings.get("phone_number", language)) },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = FarasNeonGreenDark)
                            },
                            placeholder = { Text(AppStrings.get("phone_placeholder", language)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = authFieldColors(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_phone_field")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Email Address (OTP will be sent here)
                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            label = { Text(AppStrings.get("email_address", language)) },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = FarasNeonGreenDark)
                            },
                            placeholder = { Text(AppStrings.get("email_placeholder", language)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            colors = authFieldColors(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_email_field")
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (phoneInput.isNotBlank() && emailInput.isNotBlank()) {
                                    isOtpStep = true
                                    Toast.makeText(
                                        context,
                                        "تم إرسال رمز التحقق (OTP: 7492) إلى بريدك الإلكتروني بنجاح 📧",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(context, "يرجى إدخال رقم الهاتف والبريد الإلكتروني", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("send_otp_btn")
                        ) {
                            Text(
                                text = AppStrings.get("send_otp_btn", language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                        }
                    } else {
                        // Step 2: OTP Verification
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { isOtpStep = false }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = FarasTextPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.get("otp_title", language),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = FarasTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = AppStrings.get("otp_sent_to", language),
                            style = MaterialTheme.typography.bodySmall,
                            color = FarasTextSecondary,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = emailInput,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasNeonGreenDark,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // OTP Input Field
                        OutlinedTextField(
                            value = otpCodeInput,
                            onValueChange = { if (it.length <= 6) otpCodeInput = it },
                            placeholder = { Text("7492", textAlign = TextAlign.Center) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = FarasNeonGreenDark)
                            },
                            colors = authFieldColors(),
                            shape = RoundedCornerShape(14.dp),
                            textStyle = MaterialTheme.typography.headlineMedium.copy(
                                textAlign = TextAlign.Center,
                                letterSpacing = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = FarasTextPrimary
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("otp_code_field")
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Resend countdown
                        if (countdownSec > 0) {
                            Text(
                                text = "${AppStrings.get("resend_in", language)} $countdownSec ${AppStrings.get("seconds", language).ifEmpty { "ثانية" }}",
                                style = MaterialTheme.typography.labelSmall,
                                color = FarasTextSecondary
                            )
                        } else {
                            Text(
                                text = AppStrings.get("resend_otp", language),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasNeonGreenDark,
                                modifier = Modifier
                                    .clickable {
                                        countdownSec = 60
                                        Toast.makeText(context, "تمت إعادة إرسال رمز التحقق OTP 📧", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                onAuthSuccess(
                                    if (nameInput.isNotBlank()) nameInput else "محمد فاروق",
                                    phoneInput,
                                    emailInput
                                )
                                Toast.makeText(context, "تم تسجيل الدخول بنجاح! مرحباً بك في FareScooter 🛵", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("verify_otp_btn")
                        ) {
                            Text(
                                text = AppStrings.get("verify_otp_btn", language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Skip as guest
            OutlinedButton(
                onClick = onSkipGuest,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("skip_guest_btn")
            ) {
                Text(
                    text = AppStrings.get("or_continue_guest", language),
                    style = MaterialTheme.typography.labelMedium,
                    color = FarasTextSecondary
                )
            }
        }
    }
}

@Composable
fun authFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = FarasNeonGreen,
    unfocusedBorderColor = FarasCardBorder,
    focusedContainerColor = FarasLightGreyBg,
    unfocusedContainerColor = FarasLightGreyBg,
    focusedTextColor = FarasTextPrimary,
    unfocusedTextColor = FarasTextPrimary,
    focusedLabelColor = FarasNeonGreenDark,
    unfocusedLabelColor = FarasTextSecondary
)
