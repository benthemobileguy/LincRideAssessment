package com.ben.lincride.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ben.lincride.R
import com.ben.lincride.core.designsystem.theme.LincRideTheme
import com.ben.lincride.core.designsystem.theme.LincGreen
import com.ben.lincride.core.designsystem.theme.LincRideColors
import com.ben.lincride.ui.components.BottomSheetContainer
import com.ben.lincride.ui.components.GradientProgressBarWithCar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.border
import com.ben.lincride.core.designsystem.theme.PickupBackground
import com.ben.lincride.core.designsystem.theme.FocusBlue
import kotlinx.coroutines.delay

@Composable
fun GetToPickupBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onAnimationComplete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableStateOf(0.3f) }
    var timeRemaining by remember { mutableStateOf("4 min") }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            while (progress < 1.0f) {
                delay(100)
                progress += 0.01f

                val remainingTime = (4 * (1 - progress)).coerceAtLeast(0.0f)
                val remainingMinutes = remainingTime.toInt()
                val remainingSeconds = ((remainingTime % 1) * 60).toInt()
                timeRemaining = if (remainingTime <= 0) "Arrived" else if (remainingMinutes > 0) "$remainingMinutes min" else "${remainingSeconds}s"
            }
            if (progress >= 1.0f) {
                delay(500)
                onAnimationComplete()
            }
        }
    }

    GetToPickupBottomSheetContent(
        modifier = modifier,
        isVisible = isVisible,
        onDismiss = onDismiss,
        progress = progress,
        timeRemaining = timeRemaining,
        passengerName = "Darrell Stewart",
        passengerRating = 4.7f,
        isPassengerVerified = true,
        pickupLocation = "Ladipo Oluwole Street",
        availableSeats = 2,
        passengersAccepted = listOf("DS", "JD")
    )
}

@Composable
fun GetToPickupBottomSheetContent(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    progress: Float,
    timeRemaining: String,
    passengerName: String,
    passengerRating: Float,
    isPassengerVerified: Boolean,
    pickupLocation: String,
    availableSeats: Int,
    passengersAccepted: List<String>
) {
    BottomSheetContainer(
        isVisible = isVisible,
        onDismiss = onDismiss,
        heightFraction = 0.7f,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Get to pickup...",
                    style = MaterialTheme.typography.headlineMedium,
                    color = LincRideColors.textPrimary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFF9EC0FF), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$timeRemaining away",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        ),
                        color = Color(0xFF9EC0FF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            GradientProgressBarWithCar(
                progress = progress,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            PassengerInfoCard(
                name = passengerName,
                rating = passengerRating,
                pickupLocation = pickupLocation,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Available", 
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp
                        ),
                        color = Color(0xFF656565)
                    )
                    Text(
                        text = "Seats", 
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp
                        ),
                        color = Color(0xFF656565)
                    )
                }
                
                Text(
                    text = availableSeats.toString(), 
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(
                        text = "Passengers", 
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp
                        ),
                        color = Color(0xFF656565)
                    )
                        Text(
                            text = "accepted", 
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 10.sp
                            ),
                            color = Color(0xFF656565)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.avatar_offer_ride),
                        contentDescription = "Avatar 1",
                        modifier = Modifier.size(24.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(
                        painter = painterResource(id = R.drawable.avatar_join_ride),
                        contentDescription = "Avatar 2", 
                        modifier = Modifier.size(24.dp).clip(CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { /* Share ride info */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(32.dp),
                    border = BorderStroke(1.dp, Color(0xFF1D1D1D))
                ) {
                    Text(
                        text = "Share Ride Info",
                        style = MaterialTheme.typography.labelLarge,
                        color = LincRideColors.textPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun PassengerInfoCard(
    name: String,
    rating: Float,
    pickupLocation: String,
    modifier: Modifier = Modifier
) {
        Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 4.dp, // Focus ring 4px spread
                color = FocusBlue, // Blue/200 #9EC0FF focus ring color
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = PickupBackground), // #EAFFF6 background
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "To Pick",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp
                ),
                color = Color(0xFF2A2A2A),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar_profile),
                    contentDescription = "Profile Avatar",
                    modifier = Modifier.size(48.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = name, 
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 10.sp
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
                        Text(
                            text = "⭐", 
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = rating.toString(), 
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 10.sp
                            ), 
                            color = Color(0xFF656565)
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFF8F8F8), RoundedCornerShape(17.41.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_message),
                                contentDescription = "Message",
                                modifier = Modifier.size(20.dp), // Bigger and uniform
                                tint = Color(0xFF2A2A2A)
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color.Red, CircleShape)
                                    .align(Alignment.TopEnd)
                                    .offset(x = 2.dp, y = (-2).dp)
                            )
                        }
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFF8F8F8), RoundedCornerShape(17.41.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_call),
                            contentDescription = "Call",
                            modifier = Modifier.size(20.dp), // Bigger and uniform
                            tint = Color(0xFF2A2A2A)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Color.Gray.copy(alpha = 0.3f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(
                                Color(0xFF2A2A2A),
                                CircleShape
                            )
                            .border(
                                1.dp,
                                Color(0xFF2A2A2A),
                                CircleShape
                            )
                    )
                    repeat(3) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(4.dp)
                                .background(Color(0xFF2A2A2A))
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Pick-up point", 
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp
                        ), 
                        color = Color(0xFF656565)
                    )
                    Text(
                        text = pickupLocation, 
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        ), 
                        color = Color(0xFF2A2A2A)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "ETA • 4 mins",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PassengerAvatar(
    initials: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(24.dp).background(Color.Gray, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials, 
            style = MaterialTheme.typography.labelSmall, 
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GetToPickupBottomSheetPreview() {
    LincRideTheme {
        GetToPickupBottomSheetContent(
            isVisible = true,
            onDismiss = {},
            progress = 0.5f,
            timeRemaining = "2 min",
            passengerName = "Darrell Stewart",
            passengerRating = 4.7f,
            isPassengerVerified = true,
            pickupLocation = "Ladipo Oluwole Street",
            availableSeats = 2,
            passengersAccepted = listOf("DS", "JD")
        )
    }
}
