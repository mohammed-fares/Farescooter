package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.AppStrings
import com.example.model.ScooterLocation
import com.example.model.TripOrder
import com.example.model.TripStatus
import com.example.ui.theme.FarasAmber
import com.example.ui.theme.FarasCardBorder
import com.example.ui.theme.FarasLightGreyBg
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenDark
import com.example.ui.theme.FarasNeonGreenGlow
import com.example.ui.theme.FarasNeonGreenLight
import com.example.ui.theme.FarasRedAlert
import com.example.ui.theme.FarasTextPrimary
import com.example.ui.theme.FarasTextSecondary
import com.example.ui.theme.FarasWhite
import com.example.viewmodel.MapPinTarget
import kotlin.math.roundToInt

@Composable
fun InteractiveDarkMap(
    nearbyScooters: List<ScooterLocation>,
    currentTrip: TripOrder?,
    captainProgressRatio: Float = 0f,
    language: AppLanguage = AppLanguage.ARABIC,
    exactPickupAddress: String = "ميدان التحرير، وسط البلد",
    exactPickupXRatio: Float = 0.49f,
    exactPickupYRatio: Float = 0.52f,
    isMapPinningActive: Boolean = false,
    mapPinTarget: MapPinTarget = MapPinTarget.PICKUP,
    pinnedLocationName: String = "",
    pinnedXRatio: Float = 0.49f,
    pinnedYRatio: Float = 0.52f,
    selectedScooterPreview: ScooterLocation? = null,
    onMapLocationTapped: (Float, Float) -> Unit = { _, _ -> },
    onConfirmPinnedLocation: () -> Unit = {},
    onSetMapPinTarget: (MapPinTarget) -> Unit = {},
    onSelectNearbyScooter: (ScooterLocation?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Pulse animation for beacon/markers
    val infiniteTransition = rememberInfiniteTransition(label = "map_pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    // Animated dash offset for route line
    val dashPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dashPhase"
    )

    // Interactive Zoom and Pan state
    var scale by remember { mutableFloatStateOf(1.0f) }
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .background(Color(0xFFE2E8F0))
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.75f, 2.2f)
                    panOffsetX = (panOffsetX + pan.x).coerceIn(-600f, 600f)
                    panOffsetY = (panOffsetY + pan.y).coerceIn(-600f, 600f)
                }
            }
            .pointerInput(scale, panOffsetX, panOffsetY) {
                detectTapGestures { tapOffset ->
                    val width = size.width
                    val height = size.height
                    if (width > 0 && height > 0) {
                        // Calculate normalized (0..1) map coordinates taking scale and pan into account
                        val centerX = width / 2f
                        val centerY = height / 2f
                        val unscaledX = (tapOffset.x - centerX - panOffsetX) / scale + centerX
                        val unscaledY = (tapOffset.y - centerY - panOffsetY) / scale + centerY
                        val normX = (unscaledX / width).coerceIn(0.05f, 0.95f)
                        val normY = (unscaledY / height).coerceIn(0.05f, 0.95f)

                        // Check if a nearby scooter was tapped
                        var scooterTapped: ScooterLocation? = null
                        nearbyScooters.forEach { sc ->
                            val scCanvasX = centerX + (sc.xRatio * width - centerX) * scale + panOffsetX
                            val scCanvasY = centerY + (sc.yRatio * height - centerY) * scale + panOffsetY
                            val dist = (tapOffset.x - scCanvasX) * (tapOffset.x - scCanvasX) + (tapOffset.y - scCanvasY) * (tapOffset.y - scCanvasY)
                            if (dist < 1600f) { // 40dp radius
                                scooterTapped = sc
                            }
                        }

                        if (scooterTapped != null) {
                            onSelectNearbyScooter(scooterTapped)
                        } else {
                            onSelectNearbyScooter(null)
                            onMapLocationTapped(normX, normY)
                        }
                    }
                }
            }
    ) {
        // The Interactive Cairo Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2f
            val centerY = height / 2f

            // Helper to transform normalized map coordinates (0..1) into canvas pixels
            fun toCanvasOffset(nx: Float, ny: Float): Offset {
                val cx = centerX + (nx * width - centerX) * scale + panOffsetX
                val cy = centerY + (ny * height - centerY) * scale + panOffsetY
                return Offset(cx, cy)
            }

            // 1. Draw Real Cairo Geography (Nile, Islands, Districts, Bridges & Highways)
            drawRealisticCairoMap(
                width = width,
                height = height,
                scale = scale,
                panX = panOffsetX,
                panY = panOffsetY,
                language = language
            )

            // 2. Active Trip Route Line
            if (currentTrip != null) {
                val start = toCanvasOffset(currentTrip.pickupXRatio, currentTrip.pickupYRatio)
                val end = toCanvasOffset(currentTrip.destXRatio, currentTrip.destYRatio)

                drawGlowRouteCairo(
                    start = start,
                    end = end,
                    progress = if (currentTrip.status == TripStatus.IN_TRANSIT) captainProgressRatio else 0f,
                    dashPhase = dashPhase
                )

                // Pickup Marker
                drawPickupMarker(start.x, start.y, pulseRadius, pulseAlpha)

                // Destination Marker
                drawDestinationMarker(end.x, end.y)

                // Live Moving Scooter of Captain during Trip
                if (currentTrip.status in listOf(TripStatus.ACCEPTED, TripStatus.ON_THE_WAY, TripStatus.ARRIVED_PICKUP, TripStatus.IN_TRANSIT)) {
                    val currentPos = when (currentTrip.status) {
                        TripStatus.IN_TRANSIT -> Offset(
                            start.x + (end.x - start.x) * captainProgressRatio,
                            start.y + (end.y - start.y) * captainProgressRatio
                        )
                        TripStatus.ON_THE_WAY -> Offset(
                            start.x - 90f * (1f - captainProgressRatio) * scale,
                            start.y - 110f * (1f - captainProgressRatio) * scale
                        )
                        else -> start
                    }
                    drawActiveCaptainScooter(currentPos.x, currentPos.y, pulseRadius, pulseAlpha)
                }
            } else {
                // Draw current saved pickup point marker
                val pickupPos = toCanvasOffset(exactPickupXRatio, exactPickupYRatio)
                drawPickupMarker(pickupPos.x, pickupPos.y, pulseRadius, pulseAlpha)

                // If pinning is active and placed elsewhere, draw drop pin
                if (isMapPinningActive) {
                    val pinnedPos = toCanvasOffset(pinnedXRatio, pinnedYRatio)
                    if (mapPinTarget == MapPinTarget.PICKUP) {
                        drawInteractivePin(pinnedPos.x, pinnedPos.y, FarasNeonGreen, pulseRadius, pulseAlpha)
                    } else {
                        drawInteractivePin(pinnedPos.x, pinnedPos.y, FarasRedAlert, pulseRadius, pulseAlpha)
                    }
                }
            }

            // 3. Ambient Nearby Scooters
            nearbyScooters.forEach { sc ->
                val pos = toCanvasOffset(sc.xRatio, sc.yRatio)
                if (pos.x in -50f..(width + 50f) && pos.y in -50f..(height + 50f)) {
                    drawNeonScooterPin(
                        x = pos.x,
                        y = pos.y,
                        pulseRadius = pulseRadius,
                        pulseAlpha = pulseAlpha,
                        isSelected = selectedScooterPreview?.id == sc.id
                    )
                }
            }
        }

        // Top Floating Map Target Switcher: [ 🟢 مكان الانطلاق ] | [ 🔴 مكان الوصول ]
        if (currentTrip == null) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, FarasCardBorder, RoundedCornerShape(20.dp)),
                color = FarasWhite,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    MapModePill(
                        title = AppStrings.get("pin_pickup_mode", language),
                        isSelected = mapPinTarget == MapPinTarget.PICKUP,
                        activeColor = FarasNeonGreen,
                        onClick = { onSetMapPinTarget(MapPinTarget.PICKUP) }
                    )
                    MapModePill(
                        title = AppStrings.get("pin_dest_mode", language),
                        isSelected = mapPinTarget == MapPinTarget.DESTINATION,
                        activeColor = FarasRedAlert,
                        onClick = { onSetMapPinTarget(MapPinTarget.DESTINATION) }
                    )
                }
            }
        }

        // Floating Action Buttons for Map Controls (Zoom in, Zoom out, My Location)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 80.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // My Location (Center on Cairo / Pickup)
            FloatingActionButton(
                onClick = {
                    scale = 1.1f
                    panOffsetX = 0f
                    panOffsetY = 0f
                },
                containerColor = FarasWhite,
                contentColor = FarasNeonGreenDark,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 3.dp),
                shape = CircleShape,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Center", modifier = Modifier.size(20.dp))
            }

            // Zoom In
            SmallFloatingActionButton(
                onClick = { scale = (scale * 1.25f).coerceAtMost(2.2f) },
                containerColor = FarasWhite,
                contentColor = FarasTextPrimary,
                shape = CircleShape,
                modifier = Modifier.size(38.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom in", modifier = Modifier.size(18.dp))
            }

            // Zoom Out
            SmallFloatingActionButton(
                onClick = { scale = (scale / 1.25f).coerceAtLeast(0.75f) },
                containerColor = FarasWhite,
                contentColor = FarasTextPrimary,
                shape = CircleShape,
                modifier = Modifier.size(38.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom out", modifier = Modifier.size(18.dp))
            }
        }

        // Pinned Location Floating Confirmation Card (when user taps map to select location)
        AnimatedVisibility(
            visible = isMapPinningActive && currentTrip == null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 70.dp, start = 20.dp, end = 20.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = FarasWhite),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, if (mapPinTarget == MapPinTarget.PICKUP) FarasNeonGreen else FarasRedAlert),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (mapPinTarget == MapPinTarget.PICKUP) FarasNeonGreenLight else Color(0xFFFEE2E2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (mapPinTarget == MapPinTarget.PICKUP) FarasNeonGreenDark else FarasRedAlert,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (mapPinTarget == MapPinTarget.PICKUP) AppStrings.get("pickup_location", language) else AppStrings.get("destination_location", language),
                                style = MaterialTheme.typography.labelSmall,
                                color = FarasTextSecondary
                            )
                            Text(
                                text = pinnedLocationName.ifBlank { "موقع على الخريطة" },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarasTextPrimary,
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onConfirmPinnedLocation,
                        colors = ButtonDefaults.buttonColors(containerColor = if (mapPinTarget == MapPinTarget.PICKUP) FarasNeonGreen else FarasRedAlert),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = FarasWhite, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = AppStrings.get("confirm_pinned_location", language),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = FarasWhite
                        )
                    }
                }
            }
        }

        // Scooter Preview Mini Card (when a nearby scooter is tapped)
        selectedScooterPreview?.let { scooter ->
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 290.dp, start = 16.dp, end = 16.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = FarasWhite),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FarasCardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
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
                                    imageVector = Icons.Default.ElectricScooter,
                                    contentDescription = null,
                                    tint = FarasNeonGreenDark,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = scooter.captainName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = FarasTextPrimary
                                )
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "سكوتر سريع • يبعد 2 دقيقة • خوذة معقمة" else "Quick Scooter • 2 min away • Sanitized",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FarasNeonGreenDark
                                )
                            }
                        }

                        IconButton(onClick = { onSelectNearbyScooter(null) }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = FarasTextSecondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MapModePill(
    title: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) activeColor else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) FarasWhite else FarasTextSecondary
        )
    }
}

private fun DrawScope.drawRealisticCairoMap(
    width: Float,
    height: Float,
    scale: Float,
    panX: Float,
    panY: Float,
    language: AppLanguage
) {
    val centerX = width / 2f
    val centerY = height / 2f

    fun toPx(nx: Float, ny: Float): Offset {
        val cx = centerX + (nx * width - centerX) * scale + panX
        val cy = centerY + (ny * height - centerY) * scale + panY
        return Offset(cx, cy)
    }

    // 1. Light Urban Map Background
    drawRect(color = Color(0xFFF1F5F9))

    // 2. City Blocks & Green Parks
    val parkColor = Color(0xFFE2F0D9)
    // Al-Azhar Park (East Cairo)
    val azharTopLeft = toPx(0.62f, 0.48f)
    drawRoundRect(
        color = parkColor,
        topLeft = azharTopLeft,
        size = androidx.compose.ui.geometry.Size(90f * scale, 120f * scale),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f * scale, 16f * scale)
    )

    // Gezira Sporting Club & Cairo Tower Park (Inside Zamalek)
    val geziraParkTopLeft = toPx(0.40f, 0.40f)
    drawRoundRect(
        color = parkColor,
        topLeft = geziraParkTopLeft,
        size = androidx.compose.ui.geometry.Size(60f * scale, 140f * scale),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f * scale, 20f * scale)
    )

    // Cairo University Campus (Giza)
    val uniTopLeft = toPx(0.33f, 0.60f)
    drawRoundRect(
        color = parkColor,
        topLeft = uniTopLeft,
        size = androidx.compose.ui.geometry.Size(70f * scale, 70f * scale),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f * scale, 12f * scale)
    )

    // 3. Nile River (Main Branch & Zamalek Channel)
    val waterColor = Color(0xFFBFDBFE)
    val waterBorderColor = Color(0xFF93C5FD)

    // West Nile Channel (Separating Dokki/Agouza from Zamalek)
    val westChannel = Path().apply {
        val p1 = toPx(0.37f, -0.2f)
        val p2 = toPx(0.36f, 0.30f)
        val p3 = toPx(0.40f, 0.60f)
        val p4 = toPx(0.42f, 1.2f)
        moveTo(p1.x, p1.y)
        cubicTo(p2.x, p2.y, p3.x, p3.y, p4.x, p4.y)
    }
    drawPath(path = westChannel, color = waterColor, style = Stroke(width = 30f * scale, cap = StrokeCap.Round))

    // East Nile Main Stream (Separating Zamalek/Gezira from Downtown/Corniche)
    val eastChannel = Path().apply {
        val p1 = toPx(0.48f, -0.2f)
        val p2 = toPx(0.47f, 0.30f)
        val p3 = toPx(0.46f, 0.60f)
        val p4 = toPx(0.45f, 1.2f)
        moveTo(p1.x, p1.y)
        cubicTo(p2.x, p2.y, p3.x, p3.y, p4.x, p4.y)
    }
    drawPath(path = eastChannel, color = waterColor, style = Stroke(width = 44f * scale, cap = StrokeCap.Round))
    drawPath(path = eastChannel, color = waterBorderColor, style = Stroke(width = 4f * scale))

    // Gezira Island Body (Zamalek)
    val geziraIsland = Path().apply {
        val top = toPx(0.42f, 0.24f)
        val right = toPx(0.46f, 0.44f)
        val bot = toPx(0.44f, 0.62f)
        val left = toPx(0.39f, 0.44f)
        moveTo(top.x, top.y)
        cubicTo(right.x, top.y, right.x, bot.y, bot.x, bot.y)
        cubicTo(left.x, bot.y, left.x, top.y, top.x, top.y)
        close()
    }
    drawPath(path = geziraIsland, color = Color(0xFFF8FAFC))
    drawPath(path = geziraIsland, color = Color(0xFFCBD5E1), style = Stroke(width = 2f * scale))

    // 4. Street Grid (Minor residential roads in light slate)
    val streetColor = Color(0xFFE2E8F0)
    for (i in -4..14) {
        val yPos = toPx(0f, i * 0.10f).y
        drawLine(
            color = streetColor,
            start = Offset(-200f, yPos),
            end = Offset(width + 200f, yPos),
            strokeWidth = 2f * scale
        )
    }
    for (i in -4..14) {
        val xPos = toPx(i * 0.10f, 0f).x
        drawLine(
            color = streetColor,
            start = Offset(xPos, -200f),
            end = Offset(xPos, height + 200f),
            strokeWidth = 2f * scale
        )
    }

    // 5. Major Cairo Highways & Bridges (Elevated & High Contrast)
    val highwayBed = Color(0xFF94A3B8)
    val highwaySurface = Color(0xFFFFFFFF)

    // A) 6th of October Bridge (Transits Agouza -> Zamalek -> Tahrir / Downtown -> Ramses)
    val octStart = toPx(0.24f, 0.44f)
    val octEnd = toPx(0.68f, 0.42f)
    drawLine(color = highwayBed, start = octStart, end = octEnd, strokeWidth = 16f * scale, cap = StrokeCap.Round)
    drawLine(color = highwaySurface, start = octStart, end = octEnd, strokeWidth = 9f * scale, cap = StrokeCap.Round)

    // B) Qasr El Nil Bridge (Tahrir Sq <--> Opera / Gezira)
    val qasrStart = toPx(0.43f, 0.53f)
    val qasrEnd = toPx(0.50f, 0.52f)
    drawLine(color = Color(0xFF64748B), start = qasrStart, end = qasrEnd, strokeWidth = 14f * scale, cap = StrokeCap.Square)
    drawLine(color = Color(0xFFF1F5F9), start = qasrStart, end = qasrEnd, strokeWidth = 7f * scale, cap = StrokeCap.Square)

    // C) 15th of May Bridge (Mohandessin -> Zamalek -> Downtown)
    val mayStart = toPx(0.25f, 0.32f)
    val mayEnd = toPx(0.60f, 0.30f)
    drawLine(color = highwayBed, start = mayStart, end = mayEnd, strokeWidth = 14f * scale, cap = StrokeCap.Round)
    drawLine(color = highwaySurface, start = mayStart, end = mayEnd, strokeWidth = 8f * scale, cap = StrokeCap.Round)

    // D) Corniche El Nil (North-South Avenue)
    val cornicheP1 = toPx(0.50f, -0.2f)
    val cornicheP2 = toPx(0.49f, 0.40f)
    val cornicheP3 = toPx(0.48f, 0.70f)
    val cornicheP4 = toPx(0.47f, 1.2f)
    val cornichePath = Path().apply {
        moveTo(cornicheP1.x, cornicheP1.y)
        cubicTo(cornicheP2.x, cornicheP2.y, cornicheP3.x, cornicheP3.y, cornicheP4.x, cornicheP4.y)
    }
    drawPath(path = cornichePath, color = highwayBed, style = Stroke(width = 12f * scale, cap = StrokeCap.Round))
    drawPath(path = cornichePath, color = highwaySurface, style = Stroke(width = 6f * scale, cap = StrokeCap.Round))

    // E) Gameat El Dowal St (Mohandessin diagonal)
    val gameatStart = toPx(0.22f, 0.50f)
    val gameatEnd = toPx(0.38f, 0.40f)
    drawLine(color = highwayBed, start = gameatStart, end = gameatEnd, strokeWidth = 11f * scale, cap = StrokeCap.Round)
    drawLine(color = highwaySurface, start = gameatStart, end = gameatEnd, strokeWidth = 5f * scale, cap = StrokeCap.Round)

    // 6. District Labels & Landmark Badges rendered via Android Native Canvas
    drawContext.canvas.nativeCanvas.apply {
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#334155")
            textSize = 28f * scale.coerceIn(0.9f, 1.4f)
            isFakeBoldText = true
            textAlign = android.graphics.Paint.Align.CENTER
            setShadowLayer(4f, 0f, 2f, android.graphics.Color.WHITE)
        }

        // Tahrir / Downtown
        val tahrirLoc = toPx(0.51f, 0.50f)
        drawText(if (language == AppLanguage.ARABIC) "ميدان التحرير 🏛️" else "Tahrir Sq 🏛️", tahrirLoc.x, tahrirLoc.y, paint)

        // Zamalek / Cairo Tower
        val towerLoc = toPx(0.42f, 0.46f)
        drawText(if (language == AppLanguage.ARABIC) "برج القاهرة 🗼" else "Cairo Tower 🗼", towerLoc.x, towerLoc.y, paint)

        // Dokki
        val dokkiLoc = toPx(0.34f, 0.54f)
        drawText(if (language == AppLanguage.ARABIC) "حي الدقي" else "Dokki", dokkiLoc.x, dokkiLoc.y, paint)

        // Mohandessin
        val mohandLoc = toPx(0.28f, 0.42f)
        drawText(if (language == AppLanguage.ARABIC) "المهندسين" else "Mohandessin", mohandLoc.x, mohandLoc.y, paint)

        // Nile Corniche
        val cornicheLoc = toPx(0.52f, 0.65f)
        drawText(if (language == AppLanguage.ARABIC) "كورنيش النيل ⛵" else "Nile Corniche ⛵", cornicheLoc.x, cornicheLoc.y, paint)

        // Giza
        val gizaLoc = toPx(0.26f, 0.68f)
        drawText(if (language == AppLanguage.ARABIC) "الأهرامات والجيزة 🔺" else "Giza & Pyramids 🔺", gizaLoc.x, gizaLoc.y, paint)

        // 6th October Bridge Tag
        paint.textSize = 20f * scale.coerceIn(0.8f, 1.2f)
        paint.color = android.graphics.Color.parseColor("#475569")
        val octLabel = toPx(0.46f, 0.41f)
        drawText(if (language == AppLanguage.ARABIC) "كوبري 6 أكتوبر" else "6th October Bridge", octLabel.x, octLabel.y - 12f * scale, paint)
    }
}

private fun DrawScope.drawGlowRouteCairo(
    start: Offset,
    end: Offset,
    progress: Float,
    dashPhase: Float
) {
    val midX = (start.x + end.x) / 2f + (end.y - start.y) * 0.15f
    val midY = (start.y + end.y) / 2f - (end.x - start.x) * 0.12f

    val routePath = Path().apply {
        moveTo(start.x, start.y)
        quadraticTo(midX, midY, end.x, end.y)
    }

    // Outer Neon Glow
    drawPath(
        path = routePath,
        color = FarasNeonGreenGlow,
        style = Stroke(
            width = 16f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

    // Main Green Route Polyline
    drawPath(
        path = routePath,
        color = FarasNeonGreen,
        style = Stroke(
            width = 6f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

private fun DrawScope.drawPickupMarker(x: Float, y: Float, pulseRadius: Float, pulseAlpha: Float) {
    // Pulse ring
    drawCircle(
        color = FarasNeonGreen.copy(alpha = pulseAlpha),
        radius = pulseRadius + 8f,
        center = Offset(x, y)
    )
    // Dark outer border
    drawCircle(
        color = Color(0xFF0F172A),
        radius = 12f,
        center = Offset(x, y)
    )
    // Green center
    drawCircle(
        color = FarasNeonGreen,
        radius = 8f,
        center = Offset(x, y)
    )
    // White core
    drawCircle(
        color = FarasWhite,
        radius = 3.5f,
        center = Offset(x, y)
    )
}

private fun DrawScope.drawDestinationMarker(x: Float, y: Float) {
    drawCircle(
        color = Color(0x33DC2626),
        radius = 16f,
        center = Offset(x, y)
    )
    drawCircle(
        color = FarasRedAlert,
        radius = 11f,
        center = Offset(x, y)
    )
    drawCircle(
        color = FarasWhite,
        radius = 5f,
        center = Offset(x, y)
    )
}

private fun DrawScope.drawInteractivePin(
    x: Float,
    y: Float,
    pinColor: Color,
    pulseRadius: Float,
    pulseAlpha: Float
) {
    // Ground ripple
    drawCircle(
        color = pinColor.copy(alpha = pulseAlpha * 0.5f),
        radius = pulseRadius + 10f,
        center = Offset(x, y)
    )
    // Drop shadow
    drawCircle(
        color = Color(0x40000000),
        radius = 8f,
        center = Offset(x, y + 4f)
    )
    // Pin head circle
    drawCircle(
        color = pinColor,
        radius = 14f,
        center = Offset(x, y - 24f)
    )
    drawCircle(
        color = FarasWhite,
        radius = 6f,
        center = Offset(x, y - 24f)
    )
    // Pin needle stem
    val pinPath = Path().apply {
        moveTo(x - 6f, y - 18f)
        lineTo(x, y)
        lineTo(x + 6f, y - 18f)
        close()
    }
    drawPath(path = pinPath, color = pinColor)
}

private fun DrawScope.drawNeonScooterPin(
    x: Float,
    y: Float,
    pulseRadius: Float,
    pulseAlpha: Float,
    isSelected: Boolean
) {
    if (isSelected) {
        drawCircle(
            color = FarasNeonGreen.copy(alpha = 0.5f),
            radius = 24f,
            center = Offset(x, y)
        )
    }

    drawCircle(
        color = FarasNeonGreen.copy(alpha = pulseAlpha * 0.4f),
        radius = pulseRadius,
        center = Offset(x, y)
    )

    // White circle with Green border
    drawCircle(
        color = if (isSelected) FarasNeonGreenLight else FarasWhite,
        radius = 16f,
        center = Offset(x, y)
    )
    drawCircle(
        color = if (isSelected) FarasNeonGreenDark else FarasNeonGreen,
        radius = 16f,
        center = Offset(x, y),
        style = Stroke(width = if (isSelected) 3.5f else 2.5f)
    )

    // Mini scooter icon in green
    val g = if (isSelected) FarasNeonGreenDark else FarasNeonGreen
    drawCircle(color = g, radius = 3f, center = Offset(x - 5.5f, y + 4f))
    drawCircle(color = g, radius = 3f, center = Offset(x + 5.5f, y + 4f))
    drawLine(color = g, start = Offset(x - 5.5f, y + 3.5f), end = Offset(x + 4f, y + 3.5f), strokeWidth = 2f)
    drawLine(color = g, start = Offset(x + 4f, y + 3.5f), end = Offset(x + 2f, y - 5f), strokeWidth = 2f)
    drawLine(color = Color(0xFF0F172A), start = Offset(x - 0.5f, y - 5f), end = Offset(x + 4.5f, y - 5f), strokeWidth = 2f, cap = StrokeCap.Round)
}

private fun DrawScope.drawActiveCaptainScooter(
    x: Float,
    y: Float,
    pulseRadius: Float,
    pulseAlpha: Float
) {
    drawCircle(
        color = FarasNeonGreen.copy(alpha = pulseAlpha * 0.7f),
        radius = pulseRadius + 14f,
        center = Offset(x, y)
    )
    drawCircle(
        color = Color(0xFF0F172A),
        radius = 22f,
        center = Offset(x, y)
    )
    drawCircle(
        color = FarasNeonGreen,
        radius = 22f,
        center = Offset(x, y),
        style = Stroke(width = 3.5f)
    )

    // White scooter icon
    drawCircle(color = FarasWhite, radius = 4f, center = Offset(x - 7f, y + 5f))
    drawCircle(color = FarasWhite, radius = 4f, center = Offset(x + 7f, y + 5f))
    drawLine(color = FarasNeonGreen, start = Offset(x - 7f, y + 4f), end = Offset(x + 5f, y + 4f), strokeWidth = 3f)
    drawLine(color = FarasNeonGreen, start = Offset(x + 5f, y + 4f), end = Offset(x + 3f, y - 7f), strokeWidth = 3f)
    drawLine(color = FarasWhite, start = Offset(x, y - 7f), end = Offset(x + 6f, y - 7f), strokeWidth = 3f, cap = StrokeCap.Round)
}
