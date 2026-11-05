package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.model.ContactInvite
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasLightGreyBg
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenDark
import com.example.ui.theme.FarasNeonGreenLight
import com.example.ui.theme.FarasSurfaceVariant
import com.example.ui.theme.FarasTextPrimary
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferralDialog(
    language: AppLanguage,
    referralCode: String,
    contactsList: List<ContactInvite>,
    onInviteContact: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, FarasCardBorder, RoundedCornerShape(24.dp)),
            color = FarasWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
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
                                imageVector = Icons.Default.Redeem,
                                contentDescription = null,
                                tint = FarasNeonGreenDark,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = AppStrings.get("referral_title", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarasTextPrimary
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = FarasTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = AppStrings.get("referral_desc", language),
                    style = MaterialTheme.typography.bodySmall,
                    color = FarasTextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Referral Code Box with Copy Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(FarasNeonGreenLight)
                        .border(1.5.dp, FarasNeonGreen.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = AppStrings.get("your_referral_code", language),
                                style = MaterialTheme.typography.labelSmall,
                                color = FarasTextSecondary
                            )
                            Text(
                                text = referralCode,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasNeonGreenDark
                            )
                        }

                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("FareScooter Code", referralCode)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, AppStrings.get("code_copied", language), Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = FarasWhite, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.get("copy_code", language),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Quick Share Buttons (WhatsApp, SMS, Facebook)
                Text(
                    text = AppStrings.get("share_via", language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SocialSharePill(
                        title = AppStrings.get("share_whatsapp", language),
                        bgColor = Color(0xFF25D366),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            Toast.makeText(context, "جاري فتح واتساب لمشاركة كود $referralCode ...", Toast.LENGTH_SHORT).show()
                        }
                    )
                    SocialSharePill(
                        title = AppStrings.get("share_sms", language),
                        bgColor = Color(0xFF2563EB),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            Toast.makeText(context, "جاري فتح الرسائل النصية SMS...", Toast.LENGTH_SHORT).show()
                        }
                    )
                    SocialSharePill(
                        title = AppStrings.get("share_facebook", language),
                        bgColor = Color(0xFF1877F2),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            Toast.makeText(context, "جاري المشاركة عبر فيسبوك...", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Invite Directly from Contacts
                Text(
                    text = AppStrings.get("invite_from_contacts", language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = FarasTextPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(contactsList) { contact ->
                        ContactInviteRow(
                            contact = contact,
                            language = language,
                            onInvite = {
                                onInviteContact(contact.id)
                                Toast.makeText(context, "تم إرسال دعوة FareScooter إلى ${contact.name} 🎁", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SocialSharePill(
    title: String,
    bgColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = FarasWhite
        )
    }
}

@Composable
private fun ContactInviteRow(
    contact: ContactInvite,
    language: AppLanguage,
    onInvite: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(FarasLightGreyBg)
            .border(1.dp, FarasCardBorder, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
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
                        .background(FarasSurfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = FarasTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = FarasTextPrimary
                    )
                    Text(
                        text = contact.phoneNumber,
                        style = MaterialTheme.typography.labelSmall,
                        color = FarasTextSecondary
                    )
                }
            }

            if (contact.isInvited) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FarasNeonGreenLight)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = AppStrings.get("invited_btn", language),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = FarasNeonGreenDark
                    )
                }
            } else {
                Button(
                    onClick = onInvite,
                    colors = ButtonDefaults.buttonColors(containerColor = FarasNeonGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = AppStrings.get("invite_btn", language),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = FarasWhite
                    )
                }
            }
        }
    }
}
