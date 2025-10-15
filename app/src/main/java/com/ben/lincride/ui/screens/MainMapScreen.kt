package com.ben.lincride.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ben.lincride.R
import com.ben.lincride.core.designsystem.theme.LincRideTheme
import com.ben.lincride.core.designsystem.theme.LincBlue
import com.ben.lincride.ui.components.BottomNavigationBar
import com.ben.lincride.ui.components.CampaignCard
import com.ben.lincride.ui.components.BarIndicator
import com.ben.lincride.domain.model.RideEvent
import com.ben.lincride.domain.model.RideState
import com.ben.lincride.presentation.viewmodel.RideViewModel

@Composable
fun MainMapScreen(
    modifier: Modifier = Modifier,
    viewModel: RideViewModel = hiltViewModel()
) {
    val rideState by viewModel.rideState.collectAsState()
    
    LaunchedEffect(rideState.currentEvent) {
        Log.i("🚗 LincRide_MainMapScreen", "📊 STATE CHANGED: ${rideState.currentEvent}")
        Log.d("🚗 LincRide_MainMapScreen", "isSimulating: ${rideState.isSimulating}")
        Log.d("🚗 LincRide_MainMapScreen", "passengers: ${rideState.passengers.size}")
    }

    MainMapScreenContent(
        modifier = modifier,
        rideState = rideState,
        onOfferRide = { viewModel.startRideOffer() },
        onResetSimulation = { viewModel.resetSimulation() },
        onDidntShow = { passengerId -> viewModel.handlePassengerNoShow(passengerId) },
        onPickedUp = { viewModel.confirmPickup() },
        onProgressToNextEvent = { viewModel.progressToNextEvent() }
    )
}

@Composable
fun MainMapScreenContent(
    modifier: Modifier = Modifier,
    rideState: RideState,
    onOfferRide: () -> Unit,
    onResetSimulation: () -> Unit,
    onDidntShow: (passengerId: String) -> Unit,
    onPickedUp: () -> Unit,
    onProgressToNextEvent: () -> Unit
) {
    var isMapReady by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (LocalInspectionMode.current) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)) {
                Text(
                    text = "Map Placeholder",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            MapScreen(
                onMapReady = { }
            )
        }

        if (rideState.currentEvent == RideEvent.IDLE) {
            HomeScreenOverlay(
                onOfferRide = {
                    Log.i("🚗 LincRide_MainMapScreen", "🔥 USER CLICKED: Offer Ride button pressed!")
                    onOfferRide()
                },
                onJoinRide = { /* Future: Join ride flow */ },
                onWhereToClick = { /* Future: Destination search */ },
                onResetSimulation = onResetSimulation
            )
        }

        when (rideState.currentEvent) {
            RideEvent.OFFER_RIDE_AVAILABLE -> {
                LaunchedEffect(Unit) {
                    Log.d("🚗 LincRide_MainMapScreen", "Auto-progressing from OFFER_RIDE_AVAILABLE...")
                    delay(1000)
                    onProgressToNextEvent()
                }
            }
            
            RideEvent.PASSENGERS_ACCEPTED -> {
                LaunchedEffect(Unit) {
                    Log.d("🚗 LincRide_MainMapScreen", "Auto-progressing from PASSENGERS_ACCEPTED...")
                    delay(500)
                    onProgressToNextEvent()
                }
            }
            
            RideEvent.GET_TO_PICKUP -> {
                GetToPickupBottomSheet(
                    isVisible = true,
                    onDismiss = { },
                    onAnimationComplete = onProgressToNextEvent
                )
            }

            RideEvent.PICKUP_CONFIRMATION -> {
                PickupConfirmationBottomSheet(
                    isVisible = true,
                    onDismiss = { },
                    onDidntShow = {
                        rideState.passengers.firstOrNull()?.let { passenger ->
                            onDidntShow(passenger.id)
                        }
                    },
                    onPickedUp = onPickedUp
                )
            }

            RideEvent.HEADING_TO_DROPOFF -> {
                HeadingToDropOffBottomSheet(
                    isVisible = true,
                    onDismiss = { },
                    onAnimationComplete = onProgressToNextEvent
                )
            }

            RideEvent.TRIP_ENDED -> {
                TripEndedOverlay(
                    isVisible = true,
                    earnings = rideState.earnings,
                    passengers = rideState.passengers,
                    onClose = onResetSimulation,
                    onNewTrip = onResetSimulation,
                    onEarningsHistory = {}
                )
            }

            else -> {
            }
        }
    }
}


@Composable
fun HomeScreenOverlay(
    onOfferRide: () -> Unit,
    onJoinRide: () -> Unit,
    onWhereToClick: () -> Unit,
    onResetSimulation: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF29E892))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_campaign),
                    contentDescription = "Campaign",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Row {
                    Text(
                        text = "1",
                        color = Color(0xFF383838),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = " Active campaign",
                        color = Color(0xFF383838),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BarIndicator(modifier = Modifier.padding(bottom = 8.dp))

            Text(
                text = "Choose your ride mode",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp,
                        top = 16.dp),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                 Button(
                    onClick = onJoinRide,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_join_ride),
                            contentDescription = "Join a Ride",
                            modifier = Modifier.size(32.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text("Join a Ride", fontSize = 14.sp, fontWeight = FontWeight.Medium, maxLines = 1)
                            Text("Book your seat", fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                        }
                    }
                }

                Button(
                    onClick = onOfferRide,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LincBlue),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_offer_ride),
                            contentDescription = "Offer Ride",
                            modifier = Modifier.size(32.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text("Offer Ride", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White, maxLines = 1)
                            Text("Share your trip", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f), maxLines = 1)
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                shape = RoundedCornerShape(20.dp),
                onClick = onWhereToClick,
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(color = Color(0xFFF0F0F0), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_routing),
                            contentDescription = "Routing",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = "Where to?", 
                        fontSize = 14.sp, 
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF383838)
                    )
                }
            }

            BottomNavigationBar(
                selectedTab = 0,
                onTabSelected = { /* Handle tab selection */ }
            )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainMapScreenPreview() {
    LincRideTheme {
        MainMapScreenContent(
            rideState = RideState(currentEvent = RideEvent.IDLE),
            onOfferRide = {},
            onResetSimulation = {},
            onDidntShow = {},
            onPickedUp = {},
            onProgressToNextEvent = {}
        )
    }
}
