package com.ben.lincride.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ben.lincride.R
import com.ben.lincride.core.designsystem.theme.LincGreen
import com.ben.lincride.core.designsystem.theme.LincRideTheme
import com.ben.lincride.core.designsystem.theme.LincRideColors
import com.ben.lincride.core.designsystem.theme.StopGreen
import com.ben.lincride.core.designsystem.theme.StopOrange
import com.ben.lincride.core.designsystem.theme.StopTextGray
import com.ben.lincride.core.designsystem.theme.StopDottedGray
import com.ben.lincride.core.designsystem.theme.StopDestination
import com.ben.lincride.ui.components.BarIndicator
import com.ben.lincride.ui.components.BottomSheetContainer
import com.ben.lincride.ui.components.CarProgressBar
import kotlinx.coroutines.delay

data class RouteStop(
    val type: StopType,
    val location: String,
    val passengerName: String? = null,
    val passengerInitials: String? = null,
    val isCompleted: Boolean = false,
    val isCurrent: Boolean = false,
    val stopNumber: Int? = null // For Drop-off 1, Drop-off 2, etc.
)

enum class StopType {
    STARTING_POINT,
    DROP_OFF_1,
    DROP_OFF_2, 
    DESTINATION
}

/**
 * "Smart" composable for Screen 14.4.1: Heading to Drop Off Bottom Sheet
 * Handles the progress simulation logic.
 */
@Composable
fun HeadingToDropOffBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onAnimationComplete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableStateOf(0.4f) }

    // Simulate journey progress
    LaunchedEffect(isVisible) {
        if (isVisible) {
            while (progress < 1.0f) {
                delay(150)
                progress += 0.005f
            }
            if (progress >= 1.0f) {
                delay(500) // Brief pause
                onAnimationComplete()
            }
        }
    }

    val routeStops = getSampleRouteStops(progress)

    HeadingToDropOffBottomSheetContent(
        modifier = modifier,
        isVisible = isVisible,
        onDismiss = onDismiss,
        progress = progress,
        routeStops = routeStops,
        nextStopLocation = "Community Road",
        passengers = listOf("DS", "HC")
    )
}

/**
 * "Dumb" composable that only displays the UI, making it previewable.
 */
@Composable
fun HeadingToDropOffBottomSheetContent(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    progress: Float,
    routeStops: List<RouteStop>,
    nextStopLocation: String,
    passengers: List<String>
) {
    BottomSheetContainer(
        isVisible = isVisible,
        onDismiss = onDismiss,
        heightFraction = 0.8f,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with margins
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Heading to", 
                    style = MaterialTheme.typography.headlineMedium,
                    color = LincRideColors.textPrimary
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = nextStopLocation, 
                        style = MaterialTheme.typography.labelMedium.copy(
                            lineHeight = 18.sp, // 150% of 12sp as per Figma
                            textAlign = TextAlign.Right
                        ),
                        color = Color(0xFF383838)
                    )
                    Text(
                        text = "To drop off 🏠", 
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress bar with car - full width NO MARGINS
            ProgressBarWithCar(
                progress = progress,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Route visualization with margins
            RouteVisualization(
                stops = routeStops, 
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Available seats row - CONSISTENT with other modals (text broken into lines)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: "Available" + "Seats" (broken lines)
                Column {
                    Text(
                        text = "Available", 
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Seats", 
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Center: Big number "1"
                Text(
                    text = "1", 
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // Right side: "Passengers" + "accepted" + avatars (SAME ROW)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(
                            text = "Passengers", 
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "accepted", 
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // Use actual avatar images like other modals
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

            // Share Ride Info button - proper spacing and margins
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
private fun RouteVisualization(
    stops: List<RouteStop>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        stops.forEachIndexed { index, stop ->
            var isVisible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                delay(index * 150L)
                isVisible = true
            }
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { 50 }, animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f)) + fadeIn(animationSpec = tween(400))
            ) {
                RouteStopItem(stop = stop, isLast = index == stops.lastIndex)
            }
            if (index < stops.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun RouteStopItem(stop: RouteStop, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Circle indicator based on Figma specs
            when (stop.type) {
                StopType.STARTING_POINT -> {
                    // White circle with border as per Figma
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(Color.White, CircleShape)
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(StopTextGray, CircleShape)
                        )
                    }
                }
                StopType.DROP_OFF_1 -> {
                    // Avatar with green border
                    stop.passengerInitials?.let { initials ->
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(StopGreen, CircleShape)
                                .padding(1.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.avatar_offer_ride),
                                contentDescription = "Passenger avatar",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }
                    }
                }
                StopType.DROP_OFF_2 -> {
                    // Avatar with orange border
                    stop.passengerInitials?.let { initials ->
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(StopOrange, CircleShape)
                                .padding(1.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.avatar_join_ride),
                                contentDescription = "Passenger avatar",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }
                    }
                }
                StopType.DESTINATION -> {
                    // Solid filled circle
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(StopDestination, CircleShape)
                    )
                }
            }
            
            // Connecting lines based on Figma specs
            if (!isLast) {
                val nextStop = when (stop.type) {
                    StopType.STARTING_POINT -> StopType.DROP_OFF_1
                    StopType.DROP_OFF_1 -> StopType.DROP_OFF_2
                    StopType.DROP_OFF_2 -> StopType.DESTINATION
                    StopType.DESTINATION -> null
                }
                
                when (nextStop) {
                    StopType.DROP_OFF_1 -> {
                        // Green dots to Drop-off 1
                        repeat(10) { index ->
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(2.dp)
                                    .background(StopGreen, CircleShape)
                            )
                            if (index < 9) {
                                Spacer(modifier = Modifier.height(2.dp))
                            }
                        }
                    }
                    StopType.DROP_OFF_2 -> {
                        // Orange lines to Drop-off 2
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(20.dp)
                                .background(StopOrange)
                        )
                    }
                    StopType.DESTINATION -> {
                        // Dotted gray lines to Destination
                        repeat(6) { index ->
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(4.dp)
                                    .background(StopDottedGray)
                            )
                            if (index < 5) {
                                Spacer(modifier = Modifier.height(2.dp))
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
        
        Spacer(modifier = Modifier.width(10.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = when (stop.type) {
                        StopType.STARTING_POINT -> "Starting Point"
                        StopType.DROP_OFF_1 -> "Drop-off 1"
                        StopType.DROP_OFF_2 -> "Drop-off 2"
                        StopType.DESTINATION -> "Destination"
                    },
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = when (stop.type) {
                        StopType.STARTING_POINT, StopType.DESTINATION -> StopTextGray
                        StopType.DROP_OFF_1 -> StopGreen
                        StopType.DROP_OFF_2 -> StopOrange
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = stop.location,
                style = MaterialTheme.typography.titleMedium,
                color = LincRideColors.textPrimary
            )
            
            // Route info card
            if ((stop.type == StopType.DROP_OFF_1 || stop.type == StopType.DROP_OFF_2) && stop.passengerName != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text(
                        text = "Through Aromire Str. • 4 min",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
        
        if (stop.isCurrent) {
            Text(
                text = "4 min",
                style = MaterialTheme.typography.labelMedium,
                color = LincRideColors.textPrimary
            )
        }
    }
}

@Composable
private fun PassengerAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 24.dp
) {
    Box(
        modifier = modifier.size(size).background(Color.Gray, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials, 
            fontSize = (size.value * 0.4f).sp, 
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

fun getSampleRouteStops(progress: Float = 0.4f): List<RouteStop> {
    return listOf(
        RouteStop(type = StopType.STARTING_POINT, location = "Ladipo Oluwole Street", isCompleted = true),
        RouteStop(type = StopType.DROP_OFF_1, location = "Community Road", passengerName = "Drop off Darrell Stewart", passengerInitials = "DS", isCompleted = progress > 0.6f, isCurrent = progress <= 0.6f, stopNumber = 1),
        RouteStop(type = StopType.DROP_OFF_2, location = "Community Road", passengerName = "Drop off Hinata Chukwu", passengerInitials = "HC", isCompleted = progress >= 1.0f, isCurrent = progress in 0.6f..0.99f, stopNumber = 2),
        RouteStop(type = StopType.DESTINATION, location = "Community Road", isCompleted = progress >= 1.0f)
    )
}

@Composable
private fun ProgressBarWithCar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp), // Height to accommodate car above progress bar
        contentAlignment = Alignment.CenterStart
    ) {
        // Layer 1 (Bottom): Background track - 8px height, no radius
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .align(Alignment.CenterStart)
                .background(Color(0xFFE8ECF0))
        )
        
        // Layer 2 (Middle): Progress fill - 8px height, no radius
        val animatedProgress by animateFloatAsState(
            targetValue = progress.coerceIn(0f, 1f),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "progress_animation"
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(8.dp)
                .align(Alignment.CenterStart)
                .background(LincRideColors.progressGreenHeading)
        )
        
        // Layer 3 (Top): Car icon stacked on top
        if (progress > 0f) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                val carXPosition = (maxWidth - 56.dp) * animatedProgress
                
                Image(
                    painter = painterResource(id = R.drawable.ic_car),
                    contentDescription = "Car",
                    modifier = Modifier
                        .size(56.dp)
                        .offset(x = carXPosition)
                        .align(Alignment.CenterStart)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeadingToDropOffBottomSheetPreview() {
    LincRideTheme {
        HeadingToDropOffBottomSheetContent(
            isVisible = true,
            onDismiss = {},
            progress = 0.5f,
            routeStops = getSampleRouteStops(0.5f),
            nextStopLocation = "Community Road",
            passengers = listOf("DS", "HC")
        )
    }
}
