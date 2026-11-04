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
import com.example.ui.theme.FarasNeonGreen
import com.example.ui.theme.FarasNeonGreenGlow
import com.example.ui.theme.FarasWhite

@Composable
fun InteractiveDarkMap(
    nearbyScooters: List<ScooterLocation>,
    currentTrip: TripOrder?,
    captainProgressRatio: Float = 0f,
    modifier: Modifier = Modifier,
    onMapTapped: (Float, Float) -> Unit = { _, _ -> }
) {
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
        initialValue = 0.6f,
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
            .background(Color(0xFFF1F5F9))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    panOffsetX = (panOffsetX + dragAmount.x).coerceIn(-300f, 300f)
                    panOffsetY = (panOffsetY + dragAmount.y).coerceIn(-300f, 300f)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onMapTapped(offset.x, offset.y)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Draw city grid with Cairo Nile River & clean white/grey layout
            drawCairoCityGrid(width, height, panOffsetX, panOffsetY)

            // 2. Route Line
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

                // Pickup Pin
                drawPickupMarker(startX, startY, pulseRadius, pulseAlpha)

                // Destination Pin
                drawDestinationMarker(endX, destY)

                // Active Captain Scooter Marker
                if (currentTrip.status in listOf(TripStatus.ACCEPTED, TripStatus.ON_THE_WAY, TripStatus.ARRIVED_PICKUP, TripStatus.IN_TRANSIT)) {
                    val currentX = when (currentTrip.status) {
                        TripStatus.IN_TRANSIT -> startX + (endX - startX) * captainProgressRatio
                        TripStatus.ON_THE_WAY -> startX - 120f * (1f - captainProgressRatio)
                        else -> startX
                    }
                    val currentY = when (currentTrip.status) {
                        TripStatus.IN_TRANSIT -> startY + (destY - startY) * captainProgressRatio
                        TripStatus.ON_THE_WAY -> startY - 140f * (1f - captainProgressRatio)
                        else -> startY
                    }

                    drawActiveCaptainScooter(currentX, currentY, pulseRadius, pulseAlpha)
                }
            }

            // 3. Nearby active scooters
            nearbyScooters.forEach { scooter ->
                val sx = scooter.xRatio * width + panOffsetX
                val sy = scooter.yRatio * height + panOffsetY

                if (sx in -40f..(width + 40f) && sy in -40f..(height + 40f)) {
                    drawNeonScooterPin(
                        x = sx,
                        y = sy,
                        pulseRadius = pulseRadius,
                        pulseAlpha = pulseAlpha
                    )
                }
            }
        }

        // Center Location Button
        FloatingActionButton(
            onClick = {
                panOffsetX = 0f
                panOffsetY = 0f
            },
            containerColor = FarasWhite,
            contentColor = FarasNeonGreen,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
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

private fun DrawScope.drawCairoCityGrid(width: Float, height: Float, panX: Float, panY: Float) {
    // Light City Canvas
    drawRect(color = Color(0xFFF6F8FB))

    // Nile River Path (Meandering across Cairo)
    val nilePath = Path().apply {
        moveTo(width * 0.38f + panX * 0.5f, 0f + panY * 0.5f)
        cubicTo(
            width * 0.32f + panX * 0.5f, height * 0.25f + panY * 0.5f,
            width * 0.45f + panX * 0.5f, height * 0.55f + panY * 0.5f,
            width * 0.36f + panX * 0.5f, height + panY * 0.5f
        )
        lineTo(width * 0.48f + panX * 0.5f, height + panY * 0.5f)
        cubicTo(
            width * 0.55f + panX * 0.5f, height * 0.55f + panY * 0.5f,
            width * 0.42f + panX * 0.5f, height * 0.25f + panY * 0.5f,
            width * 0.48f + panX * 0.5f, 0f + panY * 0.5f
        )
        close()
    }
    drawPath(path = nilePath, color = Color(0xFFD6E4F5))

    // Minor streets grid
    val streetColor = Color(0xFFE2E8F0)
    for (x in -200..width.toInt() + 200 step 70) {
        drawLine(
            color = streetColor,
            start = Offset(x.toFloat() + panX, -200f + panY),
            end = Offset(x.toFloat() + panX, height + 200f + panY),
            strokeWidth = 2f
        )
    }

    for (y in -200..height.toInt() + 200 step 70) {
        drawLine(
            color = streetColor,
            start = Offset(-200f + panX, y.toFloat() + panY),
            end = Offset(width + 200f + panX, y.toFloat() + panY),
            strokeWidth = 2f
        )
    }

    // 6th October Bridge & Cairo Ring Road Highways (Major arteries)
    val highwayOuterColor = Color(0xFFCBD5E1)
    val highwayInnerColor = Color(0xFFFFFFFF)

    // 6th October Bridge (East-West diagonal connecting Zamalek & Tahrir)
    drawLine(
        color = highwayOuterColor,
        start = Offset(-50f + panX, height * 0.48f + panY),
        end = Offset(width + 50f + panX, height * 0.44f + panY),
        strokeWidth = 14f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = highwayInnerColor,
        start = Offset(-50f + panX, height * 0.48f + panY),
        end = Offset(width + 50f + panX, height * 0.44f + panY),
        strokeWidth = 8f,
        cap = StrokeCap.Round
    )

    // 26th of July Mehwar
    drawLine(
        color = highwayOuterColor,
        start = Offset(-50f + panX, height * 0.22f + panY),
        end = Offset(width + 50f + panX, height * 0.32f + panY),
        strokeWidth = 12f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = highwayInnerColor,
        start = Offset(-50f + panX, height * 0.22f + panY),
        end = Offset(width + 50f + panX, height * 0.32f + panY),
        strokeWidth = 6f,
        cap = StrokeCap.Round
    )

    // Corniche El Nil (Vertical Boulevard alongside the Nile)
    drawLine(
        color = highwayOuterColor,
        start = Offset(width * 0.49f + panX, -50f + panY),
        end = Offset(width * 0.49f + panX, height + 50f + panY),
        strokeWidth = 10f
    )
    drawLine(
        color = highwayInnerColor,
        start = Offset(width * 0.49f + panX, -50f + panY),
        end = Offset(width * 0.49f + panX, height + 50f + panY),
        strokeWidth = 5f
    )
}

private fun DrawScope.drawGlowRoute(
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float,
    progress: Float
) {
    val midX = (startX + endX) / 2f + (endY - startY) * 0.18f
    val midY = (startY + endY) / 2f - (endX - startX) * 0.15f

    val routePath = Path().apply {
        moveTo(startX, startY)
        quadraticTo(midX, midY, endX, endY)
    }

    // Outer glow
    drawPath(
        path = routePath,
        color = FarasNeonGreenGlow,
        style = Stroke(
            width = 16f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

    // Solid route line
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
    drawCircle(
        color = FarasNeonGreen.copy(alpha = pulseAlpha),
        radius = pulseRadius + 8f,
        center = Offset(x, y)
    )
    drawCircle(
        color = Color(0xFF0F172A),
        radius = 11f,
        center = Offset(x, y)
    )
    drawCircle(
        color = FarasNeonGreen,
        radius = 7f,
        center = Offset(x, y)
    )
}

private fun DrawScope.drawDestinationMarker(x: Float, y: Float) {
    drawCircle(
        color = Color(0x330F172A),
        radius = 14f,
        center = Offset(x, y)
    )
    drawCircle(
        color = Color(0xFF0F172A),
        radius = 9f,
        center = Offset(x, y)
    )
    drawCircle(
        color = FarasWhite,
        radius = 5f,
        center = Offset(x, y)
    )
}

private fun DrawScope.drawNeonScooterPin(
    x: Float,
    y: Float,
    pulseRadius: Float,
    pulseAlpha: Float
) {
    drawCircle(
        color = FarasNeonGreen.copy(alpha = pulseAlpha * 0.4f),
        radius = pulseRadius,
        center = Offset(x, y)
    )
    // Scooter Badge (White circular card with green border)
    drawCircle(
        color = FarasWhite,
        radius = 15f,
        center = Offset(x, y)
    )
    drawCircle(
        color = FarasNeonGreen,
        radius = 15f,
        center = Offset(x, y),
        style = Stroke(width = 2.5f)
    )

    // Mini stylized scooter glyph
    val baseGreen = FarasNeonGreen
    drawCircle(color = baseGreen, radius = 3f, center = Offset(x - 5.5f, y + 4f))
    drawCircle(color = baseGreen, radius = 3f, center = Offset(x + 5.5f, y + 4f))
    drawLine(color = baseGreen, start = Offset(x - 5.5f, y + 3.5f), end = Offset(x + 4f, y + 3.5f), strokeWidth = 2f)
    drawLine(color = baseGreen, start = Offset(x + 4f, y + 3.5f), end = Offset(x + 2f, y - 5f), strokeWidth = 2f)
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
        radius = pulseRadius + 12f,
        center = Offset(x, y)
    )
    drawCircle(
        color = Color(0xFF0F172A),
        radius = 20f,
        center = Offset(x, y)
    )
    drawCircle(
        color = FarasNeonGreen,
        radius = 20f,
        center = Offset(x, y),
        style = Stroke(width = 3.5f)
    )

    // Scooter icon in white
    drawCircle(color = FarasWhite, radius = 4f, center = Offset(x - 7f, y + 5f))
    drawCircle(color = FarasWhite, radius = 4f, center = Offset(x + 7f, y + 5f))
    drawLine(color = FarasNeonGreen, start = Offset(x - 7f, y + 4f), end = Offset(x + 5f, y + 4f), strokeWidth = 3f)
    drawLine(color = FarasNeonGreen, start = Offset(x + 5f, y + 4f), end = Offset(x + 3f, y - 7f), strokeWidth = 3f)
    drawLine(color = FarasWhite, start = Offset(x, y - 7f), end = Offset(x + 6f, y - 7f), strokeWidth = 3f, cap = StrokeCap.Round)
}
