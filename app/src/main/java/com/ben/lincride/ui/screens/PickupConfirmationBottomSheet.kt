package com.ben.lincride.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ben.lincride.R
import com.ben.lincride.core.designsystem.theme.LincRideTheme
import com.ben.lincride.core.designsystem.theme.LincGreen
import com.ben.lincride.core.designsystem.theme.LincRideColors
import com.ben.lincride.core.designsystem.theme.PickupBackground
import com.ben.lincride.ui.components.BottomSheetContainer
import kotlinx.coroutines.delay
import androidx.compose.material3.MaterialTheme
import kotlin.math.abs

@Composable
fun PickupConfirmationBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onDidntShow: () -> Unit,
    onPickedUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var timeRemaining by remember { mutableStateOf(285) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            while (timeRemaining > 0) {
                delay(1000)
                timeRemaining--
            }
        }
    }

    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)

    PickupConfirmationBottomSheetContent(
        modifier = modifier,
        isVisible = isVisible,
        onDismiss = onDismiss,
        onDidntShow = onDidntShow,
        onPickedUp = onPickedUp,
        formattedTime = formattedTime,
        passengerName = "Nneka Chukwu",         passengerRating = 4.7f,
        pickupLocation = "Ladipo Oluwole Street",
        availableSeats = 1,
        passengersAccepted = listOf("HC", "DS")
    )
}

@Composable
fun PickupConfirmationBottomSheetContent(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onDidntShow: () -> Unit,
    onPickedUp: () -> Unit,
    formattedTime: String,
    passengerName: String,
    passengerRating: Float,
    pickupLocation: String,
    availableSeats: Int,
    passengersAccepted: List<String>
) {
    BottomSheetContainer(
        isVisible = isVisible,
        onDismiss = onDismiss,
        heightFraction = 0.75f,
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
                    text = "Rider is arriving...", 
                    style = MaterialTheme.typography.headlineMedium, 
                    color = LincRideColors.textPrimary
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formattedTime, 
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        ), 
                        color = Color(0xFF2A2A2A)
                    )
                    Text(
                        text = "Waiting time", 
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        ), 
                        color = Color(0xFF656565)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            PassengerInfoCard(
                name = passengerName,
                rating = passengerRating,
                pickupLocation = pickupLocation,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            SwipeableActionArea(
                onDidntShow = onDidntShow,
                onPickedUp = onPickedUp,
                modifier = Modifier.fillMaxWidth()
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
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "To Pick up",
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
                            modifier = Modifier.size(20.dp),
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
        }
    }
}

@Composable
private fun SwipeableActionArea(
    onDidntShow: () -> Unit,
    onPickedUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var dragOffset by remember { mutableStateOf(0f) }
    val density = LocalDensity.current
    val swipeThreshold = with(density) { 100.dp.toPx() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFF3932),
                        Color(0xFF4A941C)
                    )
                )
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (abs(dragOffset) > swipeThreshold) {
                            if (dragOffset < 0) onDidntShow() else onPickedUp()
                        }
                        dragOffset = 0f
                    }
                ) { _, dragAmount ->
                    dragOffset += dragAmount.x
                }
            }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = "Swipe left",
                    modifier = Modifier.size(42.dp, 24.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Didn't show", 
                    style = MaterialTheme.typography.labelLarge, 
                    color = Color.White
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(
                    text = "Picked up", 
                    style = MaterialTheme.typography.labelLarge, 
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = "Swipe right",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = "Swipe right",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
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
        modifier = modifier
            .size(24.dp)
            .background(Color.Gray, CircleShape),
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
fun PickupConfirmationBottomSheetPreview() {
    LincRideTheme {
        PickupConfirmationBottomSheetContent(
            isVisible = true,
            onDismiss = { },
            onDidntShow = { },
            onPickedUp = { },
            formattedTime = "04:30",
            passengerName = "Nneka Chukwu",             passengerRating = 4.7f,
            pickupLocation = "Ladipo Oluwole Street",
            availableSeats = 1,
            passengersAccepted = listOf("HC", "DS")
        )
    }
}
