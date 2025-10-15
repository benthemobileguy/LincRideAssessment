package com.ben.lincride.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ben.lincride.core.designsystem.theme.LincRideTheme
import com.ben.lincride.core.designsystem.theme.LincGreen
import com.ben.lincride.core.designsystem.theme.LincRideColors
import com.ben.lincride.domain.model.RideEarnings
import com.ben.lincride.domain.model.Passenger as PassengerData
import com.ben.lincride.ui.components.BarIndicator
import androidx.compose.material3.MaterialTheme

/**
 * Screen 14.7.3: Trip Ended Overlay
 * Half-screen linear gradient overlay with 60% white content area showing trip completion, CO₂ savings, and earnings
 */
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
            // Layer 1: Full-screen gradient background
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
            
            // Layer 2: Gradient content (close button, CO2 indicator, trip complete text)
            TripEndedGradientContent(
                earnings = earnings,
                onClose = onClose,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            
            // Layer 3: White modal overlaying from bottom
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .align(Alignment.BottomCenter),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
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
        
        // Close button - top left, moved down and smaller
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

        Spacer(modifier = Modifier.height(12.dp))

        // CO2 Savings indicator
        CO2SavingsIndicator(
            savings = earnings?.carbonEmissionAvoided?.toFloat() ?: 0.86f, 
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Trip completion message
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
        
        Spacer(modifier = Modifier.height(12.dp))

        // Carbon emission info
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
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Passengers helped message
        Text(
            text = "You helped ${passengers.size} riders get to their destinations.", 
            style = MaterialTheme.typography.labelMedium, 
            color = LincRideColors.textPrimary, 
            textAlign = TextAlign.Center, 
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Rate passengers section
        Text(
            text = "Rate your passengers", 
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), 
            color = LincRideColors.textPrimary, 
            modifier = Modifier.fillMaxWidth(), 
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        // Passenger rating cards
        passengers.forEach {
            PassengerRatingCard(passenger = it, onRate = { /* Handle rating */ })
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Earnings section
        earnings?.let { EarningsSection(earnings = it) }
        }
        
        // Fixed action buttons at bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp), 
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onEarningsHistory, 
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp), // Consistent with other modals
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Earnings History", 
                    style = MaterialTheme.typography.labelLarge,
                    color = LincRideColors.textPrimary
                )
            }
            Button(
                onClick = onNewTrip, 
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp), // Consistent with other modals
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
    Box(
        modifier = modifier.padding(8.dp), 
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LincRideColors.brandGreenDark, CircleShape)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "CO₂", 
                style = MaterialTheme.typography.labelLarge, 
                color = Color.White.copy(alpha = 0.8f)
            )
            Text(
                text = "${savings} kg", 
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), 
                color = Color.White
            )
            Text(
                text = "Saved this month", 
                style = MaterialTheme.typography.labelSmall, 
                color = Color.White.copy(alpha = 0.8f), 
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PassengerRatingCard(passenger: PassengerData, onRate: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(40.dp).background(LincGreen, CircleShape), contentAlignment = Alignment.Center) {
            Text(
                text = passenger.initials, 
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), 
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = passenger.name, 
                style = MaterialTheme.typography.titleMedium, 
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "📍 Pick-up Point: ${passenger.pickupLocation.address}", 
                style = MaterialTheme.typography.labelMedium, 
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        TextButton(onClick = onRate) {
            Text(
                text = "Rate now", 
                style = MaterialTheme.typography.labelMedium, 
                color = LincGreen
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
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
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
                // Fake passenger data for preview
            ),
            onClose = {},
            onNewTrip = {},
            onEarningsHistory = {}
        )
    }
}
