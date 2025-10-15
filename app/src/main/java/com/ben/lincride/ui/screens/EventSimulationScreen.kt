package com.ben.lincride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ben.lincride.core.designsystem.theme.LincRideTheme
import com.ben.lincride.core.designsystem.theme.LincGreen
import com.ben.lincride.domain.model.RideEvent
import com.ben.lincride.presentation.viewmodel.RideViewModel

/**
 * Professional event simulation screen for development and testing
 */
@Composable
fun EventSimulationScreen(
    modifier: Modifier = Modifier,
    viewModel: RideViewModel = hiltViewModel()
) {
    val rideState by viewModel.rideState.collectAsState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "🚗 LincRide Event Simulation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LincGreen
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Senior Android Engineer Assessment",
            fontSize = 14.sp,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Current State Card
        CurrentStateCard(
            currentEvent = rideState.currentEvent,
            isSimulating = rideState.isSimulating,
            passengersCount = rideState.passengers.size,
            progress = rideState.progress
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Control Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.startRideSimulation() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = LincGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (rideState.isSimulating) "Restart" else "Start Simulation",
                    fontSize = 14.sp
                )
            }
            
            OutlinedButton(
                onClick = { viewModel.progressToNextEvent() },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Next Event",
                    fontSize = 14.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { viewModel.resetSimulation() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Reset Simulation",
                fontSize = 14.sp,
                color = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Event Timeline
        EventTimeline()
    }
}

@Composable
private fun CurrentStateCard(
    currentEvent: RideEvent,
    isSimulating: Boolean,
    passengersCount: Int,
    progress: com.ben.lincride.domain.model.RideProgress
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = LincGreen.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Current Event",
                fontSize = 12.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = currentEvent.name.replace("_", " "),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = LincGreen
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusItem(
                    label = "Status",
                    value = if (isSimulating) "Running" else "Idle"
                )
                
                StatusItem(
                    label = "Passengers",
                    value = passengersCount.toString()
                )
                
                StatusItem(
                    label = "Progress",
                    value = "${(progress.progressPercentage * 100).toInt()}%"
                )
            }
        }
    }
}

@Composable
private fun StatusItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun EventTimeline() {
    val events = listOf(
        "1. OFFER_RIDE_AVAILABLE" to "Driver sees ride request",
        "2. GET_TO_PICKUP" to "Navigate to pickup location",
        "3. PICKUP_CONFIRMATION" to "Confirm passenger arrival",
        "4. HEADING_TO_DROPOFF" to "Journey to destination", 
        "5. TRIP_ENDED" to "Show earnings and summary"
    )
    
    Column {
        Text(
            text = "Event Flow Timeline",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(events) { (event, description) ->
                TimelineItem(
                    event = event,
                    description = description
                )
            }
        }
    }
}

@Composable
private fun TimelineItem(
    event: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(LincGreen, shape = RoundedCornerShape(4.dp))
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = event,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = LincGreen
            )
            Text(
                text = description,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventSimulationScreenPreview() {
    LincRideTheme {
        EventSimulationScreen()
    }
}