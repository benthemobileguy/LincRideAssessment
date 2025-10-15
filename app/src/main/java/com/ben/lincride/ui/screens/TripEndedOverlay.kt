package com.ben.lincride.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI
import com.ben.lincride.R
import com.ben.lincride.core.designsystem.theme.LincRideTheme
import com.ben.lincride.core.designsystem.theme.LincGreen
import com.ben.lincride.core.designsystem.theme.LincRideColors
import com.ben.lincride.core.designsystem.theme.TripEndProgressGreen
import com.ben.lincride.core.designsystem.theme.RidersHelpedTextGreen
import com.ben.lincride.domain.model.RideEarnings
import com.ben.lincride.domain.model.Passenger as PassengerData
import com.ben.lincride.ui.components.BarIndicator
import androidx.compose.material3.MaterialTheme

@Composable
fun TripEndedOverlay(
    isVisible: Boolean,
    earnings: RideEarnings?,
    passengers: List<PassengerData>,
    onClose: () -> Unit,
    onNewTrip: () -> Unit,
    onEarningsHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500))
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                LincRideColors.brandGreenDark,
                                LincRideColors.brandGreenLight
                            )
                        )
                    )
            )
            
            TripEndedGradientContent(
                earnings = earnings,
                onClose = onClose,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                TripEndedWhiteContent(
                    earnings = earnings,
                    passengers = passengers,
                    onNewTrip = onNewTrip,
                    onEarningsHistory = onEarningsHistory
                )
            }
        }
    }
}

@Composable
private fun TripEndedGradientContent(
    earnings: RideEarnings?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(28.dp).background(LincRideColors.surfaceBackground, CircleShape)
            ) {
                Text(
                    text = "✕", 
                    style = MaterialTheme.typography.labelLarge, 
                    color = LincRideColors.textDark
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        CO2SavingsIndicator(
            savings = earnings?.carbonEmissionAvoided?.toFloat() ?: 0.86f, 
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Trip Complete! Thank You.", 
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), 
            color = Color.White, 
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Another successful trip, well done!", 
            style = MaterialTheme.typography.labelMedium, 
            color = Color.White.copy(alpha = 0.9f), 
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
            text = "🌿", 
            style = MaterialTheme.typography.labelLarge
        )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Carbon Emission Avoided: ~${earnings?.carbonEmissionAvoided ?: 1.2} km private car equivalent", 
                style = MaterialTheme.typography.labelMedium, 
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun TripEndedWhiteContent(
    earnings: RideEarnings?,
    passengers: List<PassengerData>,
    onNewTrip: () -> Unit,
    onEarningsHistory: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            text = "You helped ${passengers.size} riders get to their destinations.", 
            style = MaterialTheme.typography.labelMedium, 
            color = RidersHelpedTextGreen, 
            textAlign = TextAlign.Center, 
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "Rate your passengers", 
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), 
            color = LincRideColors.textPrimary, 
            modifier = Modifier.fillMaxWidth(), 
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        passengers.forEach {
            PassengerRatingCard(passenger = it, onRate = { /* Handle rating */ })
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        earnings?.let { EarningsSection(earnings = it) }
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp), 
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onEarningsHistory, 
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Earnings History", 
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                    color = LincRideColors.textPrimary,
                    maxLines = 1
                )
            }
            Button(
                onClick = onNewTrip, 
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LincRideColors.textPrimary), 
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "New Trip", 
                    style = MaterialTheme.typography.labelLarge, 
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun CO2SavingsIndicator(savings: Float, modifier: Modifier = Modifier) {
    val targetProgress = (savings / 2.0f).coerceIn(0f, 1f)
    var startAnimation by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        startAnimation = true
    }
    
    val animatedProgress by animateFloatAsState(
        targetValue = if (startAnimation) targetProgress else 0f,
        animationSpec = tween(durationMillis = 2000, easing = EaseOutCubic, delayMillis = 300),
        label = "co2_progress"
    )
    
    Box(
        modifier = modifier.padding(32.dp), 
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val strokeWidth = 4.dp.toPx()
            val maxRadius = size.minDimension / 2f
            val outerRadius = maxRadius - strokeWidth / 2f
            val innerRadius = maxRadius - strokeWidth
            
            // Inner background circle (fits exactly inside the progress ring)
            drawCircle(
                color = androidx.compose.ui.graphics.Color(0xFF1B5E20),
                radius = innerRadius,
                center = center
            )
            
            // Outer progress track (background arc) - positioned at the outer edge
            drawArc(
                color = androidx.compose.ui.graphics.Color(0xFF2E7D32),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(center.x - outerRadius, center.y - outerRadius),
                size = androidx.compose.ui.geometry.Size(outerRadius * 2, outerRadius * 2),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
            )
            
            // Outer progress arc - positioned at the exact outer edge
            drawArc(
                color = androidx.compose.ui.graphics.Color(TripEndProgressGreen.value),
                startAngle = -90f,
                sweepAngle = animatedProgress * 360f,
                useCenter = false,
                topLeft = Offset(center.x - outerRadius, center.y - outerRadius),
                size = androidx.compose.ui.geometry.Size(outerRadius * 2, outerRadius * 2),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
            )
            
            // Progress thumb - positioned exactly on the progress arc
            val thumbAngle = (animatedProgress * 360f - 90f) * (PI / 180f)
            val thumbX = center.x + outerRadius * cos(thumbAngle).toFloat()
            val thumbY = center.y + outerRadius * sin(thumbAngle).toFloat()
            
            drawCircle(
                color = androidx.compose.ui.graphics.Color(TripEndProgressGreen.value),
                radius = 6.dp.toPx(),
                center = Offset(thumbX, thumbY)
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "CO₂", 
                style = MaterialTheme.typography.titleMedium, 
                color = Color.White.copy(alpha = 0.8f)
            )
            Text(
                text = "0.86 kg", 
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold), 
                color = Color.White
            )
            Text(
                text = "So far this month", 
                style = MaterialTheme.typography.labelMedium, 
                color = Color.White.copy(alpha = 0.8f), 
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PassengerRatingCard(passenger: PassengerData, onRate: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.avatar_profile),
            contentDescription = "Passenger avatar",
            modifier = Modifier.size(40.dp).clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = passenger.name, 
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    ), 
                    color = Color(0xFF2A2A2A)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.verify),
                    contentDescription = "Verified",
                    modifier = Modifier.size(14.dp),
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "Location",
                    modifier = Modifier.size(12.dp),
                    tint = Color(0xFF656565)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Pick-up Point: ${passenger.pickupLocation.address}", 
                    style = MaterialTheme.typography.labelMedium, 
                    color = Color(0xFF656565)
                )
            }
        }
        Button(
            onClick = onRate,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF8F8F8)),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier.height(32.dp)
        ) {
            Text(
                text = "Rate now", 
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                ), 
                color = Color(0xFF2A2A2A)
            )
        }
    }
}

@Composable
private fun EarningsSection(earnings: RideEarnings) {
    val total = earnings.baseAmount + earnings.bonus - earnings.commission
    Column {
        Text(
            text = "Earnings for This Trip", 
            style = MaterialTheme.typography.titleMedium, 
            color = LincRideColors.textPrimary, 
            modifier = Modifier.fillMaxWidth(), 
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        EarningsItem("Net Earnings", earnings.baseAmount, earnings.currency)
        EarningsItem("Bonus", earnings.bonus, earnings.currency)
        EarningsItem("Linc Commission", earnings.commission, earnings.currency, isNegative = true)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Total", 
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), 
                color = LincRideColors.textPrimary
            )
            Text(
                text = "${earnings.currency}${String.format("%.2f", total)}", 
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), 
                color = LincRideColors.textPrimary
            )
        }
    }
}

@Composable
private fun EarningsItem(label: String, amount: Double, currency: String, isNegative: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label, 
            style = MaterialTheme.typography.titleMedium, 
            color = LincRideColors.textPrimary
        )
        Text(
            text = "${if (isNegative) "-" else ""}$currency${String.format("%.2f", amount)}", 
            style = MaterialTheme.typography.titleMedium, 
            color = LincRideColors.textPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TripEndedOverlayPreview() {
    LincRideTheme {
        TripEndedOverlay(
            isVisible = true,
            earnings = RideEarnings(baseAmount = 6500.0, bonus = 500.0, commission = 500.0, carbonEmissionAvoided = 1.2),
            passengers = listOf(
            ),
            onClose = {},
            onNewTrip = {},
            onEarningsHistory = {}
        )
    }
}
