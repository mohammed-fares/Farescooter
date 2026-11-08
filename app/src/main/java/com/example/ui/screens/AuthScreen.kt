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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.model.UserRole
import com.example.ui.theme.FarasAmber
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasLightGreyBg
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenDark
import com.example.ui.theme.FarasRedAlert
import com.example.ui.theme.FarasSurfaceVariant
import com.example.ui.theme.FarasTextPrimary
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite
import kotlinx.coroutines.delay

@Composable
fun AuthScreen(
    language: AppLanguage,
    onCustomerSuccess: (name: String, phone: String, email: String) -> Unit,
    onCaptainSuccess: (name: String, phone: String, email: String, scooterModel: String, plateNumber: String, nationalId: String) -> Unit,
    onAdminLogin: (username: String, pass: String) -> Boolean,
    onSkipGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var selectedRole by remember { mutableStateOf(UserRole.CUSTOMER) }
    var isOtpStep by remember { mutableStateOf(false) }

    // Common Fields
    var nameInput by remember { mutableStateOf("محمد فاروق") }
    var phoneInput by remember { mutableStateOf("01098765432") }
    var emailInput by remember { mutableStateOf("mf.z.hussein.pal@gmail.com") }
    var otpCodeInput by remember { mutableStateOf("") }
    var countdownSec by remember { mutableIntStateOf(60) }

    // Captain Specific Fields
    var scooterModelInput by remember { mutableStateOf("Xiaomi 4 Ultra (500W)") }
    var plateNumberInput by remember { mutableStateOf("ق هـ د 4192") }
    var nationalIdInput by remember { mutableStateOf("29408150102938") }

    // Admin Specific Fields
    var adminUsernameInput by remember { mutableStateOf("admin") }
    var adminPasswordInput by remember { mutableStateOf("admin123") }
    var passwordVisible by remember { mutableStateOf(false) }
    var adminErrorMsg by remember { mutableStateOf<String?>(null) }

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
            Spacer(modifier = Modifier.height(20.dp))

            // Brand Logo
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(FarasNeonGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ElectricScooter,
                    contentDescription = "FareScooter Logo",
                    tint = FarasWhite,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

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

            Spacer(modifier = Modifier.height(18.dp))

            // 1. Role Selection Tabs (Customer vs Captain vs Admin)
            Surface(
                color = FarasWhite,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RoleTabItem(
                        title = AppStrings.get("auth_tab_customer", language),
                        icon = Icons.Default.Person,
                        isSelected = selectedRole == UserRole.CUSTOMER,
                        activeColor = FarasNeonGreen,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedRole = UserRole.CUSTOMER
                            isOtpStep = false
                            adminErrorMsg = null
                        }
                    )
                    RoleTabItem(
                        title = AppStrings.get("auth_tab_captain", language),
                        icon = Icons.Default.TwoWheeler,
                        isSelected = selectedRole == UserRole.CAPTAIN,
                        activeColor = FarasAmber,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedRole = UserRole.CAPTAIN
                            isOtpStep = false
                            adminErrorMsg = null
                        }
                    )
                    RoleTabItem(
                        title = AppStrings.get("auth_tab_admin", language),
                        icon = Icons.Default.AdminPanelSettings,
                        isSelected = selectedRole == UserRole.ADMIN,
                        activeColor = FarasTextPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedRole = UserRole.ADMIN
                            isOtpStep = false
                            adminErrorMsg = null
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Authentication Card
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasWhite),
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (selectedRole) {
                        UserRole.CUSTOMER, UserRole.CAPTAIN -> {
                            if (!isOtpStep) {
                                // Title
                                Text(
                                    text = if (selectedRole == UserRole.CUSTOMER)
                                        AppStrings.get("auth_tab_customer", language)
                                    else
                                        AppStrings.get("auth_tab_captain", language),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = FarasTextPrimary,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = if (selectedRole == UserRole.CUSTOMER)
                                        AppStrings.get("auth_welcome_sub", language)
                                    else
                                        if (language == AppLanguage.ARABIC) "سجّل دراجتك الآن وانضم لأكبر أسطول سكوتر ذكي في مصر" else "Register your scooter & join Egypt's fastest fleet",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FarasTextSecondary,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(16.dp))

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

                                // Email Address (for OTP)
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

                                // Captain specific fields (Vehicle details)
                                if (selectedRole == UserRole.CAPTAIN) {
                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Scooter Model
                                    OutlinedTextField(
                                        value = scooterModelInput,
                                        onValueChange = { scooterModelInput = it },
                                        label = { Text(AppStrings.get("captain_scooter_model", language)) },
                                        leadingIcon = {
                                            Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = FarasAmber)
                                        },
                                        colors = authFieldColors(),
                                        shape = RoundedCornerShape(14.dp),
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("captain_model_field")
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Plate Number
                                    OutlinedTextField(
                                        value = plateNumberInput,
                                        onValueChange = { plateNumberInput = it },
                                        label = { Text(AppStrings.get("captain_plate_number", language)) },
                                        leadingIcon = {
                                            Icon(Icons.Default.Pin, contentDescription = null, tint = FarasAmber)
                                        },
                                        colors = authFieldColors(),
                                        shape = RoundedCornerShape(14.dp),
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("captain_plate_field")
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // National ID
                                    OutlinedTextField(
                                        value = nationalIdInput,
                                        onValueChange = { nationalIdInput = it },
                                        label = { Text(AppStrings.get("captain_national_id", language)) },
                                        leadingIcon = {
                                            Icon(Icons.Default.Badge, contentDescription = null, tint = FarasAmber)
                                        },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        colors = authFieldColors(),
                                        shape = RoundedCornerShape(14.dp),
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("captain_national_id_field")
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(
                                    onClick = {
                                        if (phoneInput.isNotBlank() && emailInput.isNotBlank()) {
                                            isOtpStep = true
                                            Toast.makeText(
                                                context,
                                                "تم إرسال رمز التحقق OTP (7492) إلى $emailInput 📧",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            Toast.makeText(context, "يرجى إدخال رقم الهاتف والبريد الإلكتروني", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedRole == UserRole.CAPTAIN) FarasAmber else FarasNeonGreen
                                    ),
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

                                if (countdownSec > 0) {
                                    Text(
                                        text = "${AppStrings.get("resend_in", language)} $countdownSec ${if (language == AppLanguage.ARABIC) "ثانية" else "seconds"}",
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
                                        if (selectedRole == UserRole.CUSTOMER) {
                                            onCustomerSuccess(
                                                nameInput.ifBlank { "عميل FareScooter" },
                                                phoneInput,
                                                emailInput
                                            )
                                            Toast.makeText(context, "مرحباً بك كعميل في FareScooter 🛵", Toast.LENGTH_SHORT).show()
                                        } else {
                                            onCaptainSuccess(
                                                nameInput.ifBlank { "كابتن سكوتر" },
                                                phoneInput,
                                                emailInput,
                                                scooterModelInput,
                                                plateNumberInput,
                                                nationalIdInput
                                            )
                                            Toast.makeText(context, "تم تسجيلك ككابتن دراجة بنجاح! مرحباً بك 🛵", Toast.LENGTH_LONG).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedRole == UserRole.CAPTAIN) FarasAmber else FarasNeonGreen
                                    ),
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

                        UserRole.ADMIN -> {
                            // Dedicated Admin Login Mechanism (اسم مستخدم وباسورد الادمن)
                            Text(
                                text = AppStrings.get("auth_tab_admin", language),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = FarasTextPrimary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (language == AppLanguage.ARABIC)
                                    "بوابة الإدارة المركزية: التحكم الكامل بالكباتن والأسعار والمظهر"
                                else
                                    "Central Admin Suite: Governance, Captains & Live Controls",
                                style = MaterialTheme.typography.bodySmall,
                                color = FarasTextSecondary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Admin Username
                            OutlinedTextField(
                                value = adminUsernameInput,
                                onValueChange = {
                                    adminUsernameInput = it
                                    adminErrorMsg = null
                                },
                                label = { Text(AppStrings.get("admin_username", language)) },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = FarasTextPrimary)
                                },
                                colors = authFieldColors(),
                                shape = RoundedCornerShape(14.dp),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_username_field")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Admin Password
                            OutlinedTextField(
                                value = adminPasswordInput,
                                onValueChange = {
                                    adminPasswordInput = it
                                    adminErrorMsg = null
                                },
                                label = { Text(AppStrings.get("admin_password", language)) },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = FarasTextPrimary)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = "Toggle password"
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                colors = authFieldColors(),
                                shape = RoundedCornerShape(14.dp),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_password_field")
                            )

                            if (adminErrorMsg != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = adminErrorMsg ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FarasRedAlert,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    val success = onAdminLogin(adminUsernameInput, adminPasswordInput)
                                    if (success) {
                                        Toast.makeText(context, "تم تسجيل دخول الإدارة بنجاح! 🔐", Toast.LENGTH_SHORT).show()
                                    } else {
                                        adminErrorMsg = AppStrings.get("admin_login_error", language)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = FarasTextPrimary),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("admin_submit_btn")
                            ) {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = FarasWhite)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = AppStrings.get("admin_login_btn", language),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = FarasWhite
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Guest Option
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
private fun RoleTabItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    activeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) activeColor else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) FarasWhite else FarasTextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) FarasWhite else FarasTextSecondary,
                maxLines = 1
            )
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
