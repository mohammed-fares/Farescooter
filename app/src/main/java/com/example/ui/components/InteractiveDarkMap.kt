package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.model.ScooterLocation
import com.example.model.TripOrder
import com.example.model.TripStatus
import com.example.ui.theme.FarasBlack
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenGlow
import com.example.ui.theme.FarasSurfaceDark
import com.example.ui.theme.FarasWhite
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InteractiveDarkMap(
    nearbyScooters: List<ScooterLocation>,
    currentTrip: TripOrder?,
    captainProgressRatio: Float = 0f,
    modifier: Modifier = Modifier,
    onMapTapped: (Float, Float) -> Unit = { _, _ -> }
) {
    // Pulse animation for neon scooter markers
    val infiniteTransition = rememberInfiniteTransition(label = "scooter_pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 26f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .background(FarasBlack)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    panOffsetX = (panOffsetX + dragAmount.x).coerceIn(-300f, 300f)
                    panOffsetY = (panOffsetY + dragAmount.y).coerceIn(-300f, 300f)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    // Calculate normalized ratio
                    onMapTapped(offset.x, offset.y)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Draw city blocks & dark background grid
            drawDarkCityGrid(width, height, panOffsetX, panOffsetY)

            // 2. If trip active or destination selected, draw the neon route line
            if (currentTrip != null) {
                val startX = currentTrip.pickupXRatio * width + panOffsetX
                val startY = currentTrip.pickupYRatio * height + panOffsetY
                val endX = currentTrip.destXRatio * width + panOffsetX
                val destY = currentTrip.destYRatio * height + panOffsetY

                drawGlowRoute(
                    startX = startX,
                    startY = startY,
                    endX = endX,
                    endY = destY,
                    progress = if (currentTrip.status == TripStatus.IN_TRANSIT) captainProgressRatio else 0f
                )

                // Draw Pickup Marker
                drawPickupMarker(startX, startY, pulseRadius, pulseAlpha)

                // Draw Destination Marker
                drawDestinationMarker(endX, destY)

                // If in transit or rider on the way, draw Captain scooter marker
                if (currentTrip.status in listOf(TripStatus.ACCEPTED, TripStatus.ON_THE_WAY, TripStatus.ARRIVED_PICKUP, TripStatus.IN_TRANSIT)) {
                    val currentX = when (currentTrip.status) {
                        TripStatus.IN_TRANSIT -> startX + (endX - startX) * captainProgressRatio
                        TripStatus.ON_THE_WAY -> {
                            // Approaching pickup
                            val approachX = startX - 120f * (1f - captainProgressRatio)
                            approachX
                        }
                        else -> startX
                    }
                    val currentY = when (currentTrip.status) {
                        TripStatus.IN_TRANSIT -> startY + (destY - startY) * captainProgressRatio
                        TripStatus.ON_THE_WAY -> {
                            val approachY = startY - 140f * (1f - captainProgressRatio)
                            approachY
                        }
                        else -> startY
                    }

                    drawActiveCaptainScooter(currentX, currentY, pulseRadius, pulseAlpha)
                }
            }

            // 3. Draw nearby active scooters
            nearbyScooters.forEach { scooter ->
                val sx = scooter.xRatio * width + panOffsetX
                val sy = scooter.yRatio * height + panOffsetY

                // Don't draw if outside viewport
                if (sx in -40f..(width + 40f) && sy in -40f..(height + 40f)) {
                    drawNeonScooterPin(
                        x = sx,
                        y = sy,
                        heading = scooter.heading,
                        pulseRadius = pulseRadius,
                        pulseAlpha = pulseAlpha
                    )
                }
            }
        }

        // Map Control Floating Buttons (Reset Pan / Recenter)
        FloatingActionButton(
            onClick = {
                panOffsetX = 0f
                panOffsetY = 0f
            },
            containerColor = FarasSurfaceDark,
            contentColor = FarasNeonGreen,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 80.dp, end = 16.dp)
                .size(46.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "Center Location",
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

private fun DrawScope.drawDarkCityGrid(width: Float, height: Float, panX: Float, panY: Float) {
    // Base dark background
    drawRect(color = Color(0xFF0D1015))

    // Water body (e.g. Dubai Creek / waterfront area)
    val waterPath = Path().apply {
        moveTo(0f + panX * 0.5f, height * 0.18f + panY * 0.5f)
        cubicTo(
            width * 0.35f + panX * 0.5f, height * 0.12f + panY * 0.5f,
            width * 0.65f + panX * 0.5f, height * 0.28f + panY * 0.5f,
            width + panX * 0.5f, height * 0.20f + panY * 0.5f
        )
        lineTo(width + panX * 0.5f, height * 0.32f + panY * 0.5f)
        cubicTo(
            width * 0.7f + panX * 0.5f, height * 0.38f + panY * 0.5f,
            width * 0.3f + panX * 0.5f, height * 0.22f + panY * 0.5f,
            0f + panX * 0.5f, height * 0.28f + panY * 0.5f
        )
        close()
    }
    drawPath(
        path = waterPath,
        color = Color(0xFF111D2B)
    )

    // Minor streets grid
    val minorRoadColor = Color(0xFF181D26)
    val minorRoadStroke = 2.5f

    for (x in -200..width.toInt() + 200 step 70) {
        drawLine(
            color = minorRoadColor,
            start = Offset(x.toFloat() + panX, -200f + panY),
            end = Offset(x.toFloat() + panX, height + 200f + panY),
            strokeWidth = minorRoadStroke
        )
    }

    for (y in -200..height.toInt() + 200 step 70) {
        drawLine(
            color = minorRoadColor,
            start = Offset(-200f + panX, y.toFloat() + panY),
            end = Offset(width + 200f + panX, y.toFloat() + panY),
            strokeWidth = minorRoadStroke
        )
    }

    // Major Avenues (Arterial Highways with dark gray tint)
    val majorRoadColor = Color(0xFF262F3E)
    val highwayColor = Color(0xFF333F52)

    // Highway diagonal
    drawLine(
        color = highwayColor,
        start = Offset(-50f + panX, height * 0.85f + panY),
        end = Offset(width + 50f + panX, height * 0.15f + panY),
        strokeWidth = 14f,
        cap = StrokeCap.Round
    )

    // Inner highway glow center line
    drawLine(
        color = Color(0xFF1E2530),
        start = Offset(-50f + panX, height * 0.85f + panY),
        end = Offset(width + 50f + panX, height * 0.15f + panY),
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )

    // Horizontal major avenue
    drawLine(
        color = majorRoadColor,
        start = Offset(-50f + panX, height * 0.52f + panY),
        end = Offset(width + 50f + panX, height * 0.52f + panY),
        strokeWidth = 9f
    )

    // Vertical major boulevard
    drawLine(
        color = majorRoadColor,
        start = Offset(width * 0.42f + panX, -50f + panY),
        end = Offset(width * 0.42f + panX, height + 50f + panY),
        strokeWidth = 9f
    )

    // Secondary boulevard
    drawLine(
        color = majorRoadColor,
        start = Offset(width * 0.78f + panX, -50f + panY),
        end = Offset(width * 0.78f + panX, height + 50f + panY),
        strokeWidth = 8f
    )
}

private fun DrawScope.drawGlowRoute(
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float,
    progress: Float
) {
    // Waypoint curve
    val midX = (startX + endX) / 2f + (endY - startY) * 0.2f
    val midY = (startY + endY) / 2f - (endX - startX) * 0.15f

    val routePath = Path().apply {
        moveTo(startX, startY)
        quadraticTo(midX, midY, endX, endY)
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

    // Solid Neon Green Route Line
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
    // Pulse halo
    drawCircle(
        color = FarasNeonGreen.copy(alpha = pulseAlpha),
        radius = pulseRadius + 8f,
        center = Offset(x, y)
    )

    // Outer dark ring
    drawCircle(
        color = FarasBlack,
        radius = 11f,
        center = Offset(x, y)
    )

    // Center Neon dot
    drawCircle(
        color = FarasNeonGreen,
        radius = 7f,
        center = Offset(x, y)
    )
}

private fun DrawScope.drawDestinationMarker(x: Float, y: Float) {
    // Square / flag pin for destination
    drawCircle(
        color = FarasWhite.copy(alpha = 0.3f),
        radius = 14f,
        center = Offset(x, y)
    )
    drawCircle(
        color = FarasBlack,
        radius = 10f,
        center = Offset(x, y)
    )
    drawCircle(
        color = FarasWhite,
        radius = 6f,
        center = Offset(x, y)
    )
}

private fun DrawScope.drawNeonScooterPin(
    x: Float,
    y: Float,
    heading: Float,
    pulseRadius: Float,
    pulseAlpha: Float
) {
    // Subtle beacon glow
    drawCircle(
        color = FarasNeonGreen.copy(alpha = pulseAlpha * 0.5f),
        radius = pulseRadius,
        center = Offset(x, y)
    )

    // Badge circle background (Black pill/circle)
    drawCircle(
        color = Color(0xFF141920),
        radius = 15f,
        center = Offset(x, y)
    )
    drawCircle(
        color = FarasNeonGreen,
        radius = 15f,
        center = Offset(x, y),
        style = Stroke(width = 2.5f)
    )

    // Scooter icon silhouette (mini stylized wheels & handlebar)
    val wheelRadius = 3f
    val baseGreen = FarasNeonGreen

    // Back wheel
    drawCircle(
        color = baseGreen,
        radius = wheelRadius,
        center = Offset(x - 5.5f, y + 4f)
    )
    // Front wheel
    drawCircle(
        color = baseGreen,
        radius = wheelRadius,
        center = Offset(x + 5.5f, y + 4f)
    )
    // Deck
    drawLine(
        color = baseGreen,
        start = Offset(x - 5.5f, y + 3.5f),
        end = Offset(x + 4f, y + 3.5f),
        strokeWidth = 2f
    )
    // Stem & handlebar
    drawLine(
        color = baseGreen,
        start = Offset(x + 4f, y + 3.5f),
        end = Offset(x + 2f, y - 5f),
        strokeWidth = 2f
    )
    drawLine(
        color = FarasWhite,
        start = Offset(x - 0.5f, y - 5f),
        end = Offset(x + 4.5f, y - 5f),
        strokeWidth = 2f,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawActiveCaptainScooter(
    x: Float,
    y: Float,
    pulseRadius: Float,
    pulseAlpha: Float
) {
    // Large prominent pulse
    drawCircle(
        color = FarasNeonGreen.copy(alpha = pulseAlpha * 0.8f),
        radius = pulseRadius + 14f,
        center = Offset(x, y)
    )

    // Main captain badge
    drawCircle(
        color = FarasBlack,
        radius = 20f,
        center = Offset(x, y)
    )
    drawCircle(
        color = FarasNeonGreen,
        radius = 20f,
        center = Offset(x, y),
        style = Stroke(width = 3.5f)
    )

    // Scooter glyph in bright white & neon
    drawCircle(
        color = FarasWhite,
        radius = 4f,
        center = Offset(x - 7f, y + 5f)
    )
    drawCircle(
        color = FarasWhite,
        radius = 4f,
        center = Offset(x + 7f, y + 5f)
    )
    drawLine(
        color = FarasNeonGreen,
        start = Offset(x - 7f, y + 4f),
        end = Offset(x + 5f, y + 4f),
        strokeWidth = 3f
    )
    drawLine(
        color = FarasNeonGreen,
        start = Offset(x + 5f, y + 4f),
        end = Offset(x + 3f, y - 7f),
        strokeWidth = 3f
    )
    drawLine(
        color = FarasWhite,
        start = Offset(x, y - 7f),
        end = Offset(x + 6f, y - 7f),
        strokeWidth = 3f,
        cap = StrokeCap.Round
    )
}
