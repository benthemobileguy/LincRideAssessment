package com.ben.lincride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ben.lincride.core.designsystem.theme.LincRideTheme
import com.ben.lincride.domain.model.PassengerStatus
import com.ben.lincride.domain.model.RideEvent
import com.ben.lincride.domain.model.RideState
import com.ben.lincride.presentation.viewmodel.RideViewModel
import com.ben.lincride.ui.components.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

/**
 * "Smart" composable that connects to the ViewModel.
 */
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    onMapReady: () -> Unit = {},
    viewModel: RideViewModel = hiltViewModel()
) {
    val rideState by viewModel.rideState.collectAsState()

    MapScreenContent(
        modifier = modifier,
        rideState = rideState,
        onMapReady = onMapReady
    )
}

/**
 * "Dumb" composable that displays the UI and is easily previewable.
 */
@Composable
fun MapScreenContent(
    modifier: Modifier = Modifier,
    rideState: RideState,
    onMapReady: () -> Unit
) {
    val lagosCenter = LatLng(6.5244, 3.3792)
    val ladipoOluwole = LatLng(6.5378, 3.3516)
    val communityRoad = LatLng(6.5198, 3.3441)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(lagosCenter, 12f)
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            onMapReady()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (LocalInspectionMode.current) {
            // In preview mode, show a placeholder with fake map content.
            Box(modifier = Modifier.fillMaxSize().background(Color.LightGray)) {
                Text("Map Preview", modifier = Modifier.align(Alignment.Center), color = Color.White)
                // Show fake content based on the ride state to verify logic
                PreviewMapContent(rideState = rideState)
            }
        } else {
            // In a real app, show the actual GoogleMap with its content.
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = false, mapType = MapType.NORMAL),
                uiSettings = MapUiSettings(zoomControlsEnabled = false, compassEnabled = true, myLocationButtonEnabled = false)
            ) {
                RealMapContent(rideState = rideState, lagosCenter = lagosCenter, ladipoOluwole = ladipoOluwole, communityRoad = communityRoad)
            }
        }
    }
}

/**
 * Holds the real GoogleMap content (Markers, Polylines, etc.)
 */
@Composable
fun RealMapContent(rideState: RideState, lagosCenter: LatLng, ladipoOluwole: LatLng, communityRoad: LatLng) {
    when (rideState.currentEvent) {
        RideEvent.GET_TO_PICKUP -> {
            AnimatedCarMarker(currentPosition = lagosCenter, targetPosition = ladipoOluwole, isMoving = true, carIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            AnimatedRoutePolyline(points = listOf(lagosCenter, ladipoOluwole), progress = rideState.progress.progressPercentage, color = Color.Blue, width = 12f)
            PickupLocationMarker(position = ladipoOluwole, passengerName = "Darrell Stewart", address = "Ladipo Oluwole Street", isActive = true, isPulsing = true)
        }
        RideEvent.PICKUP_CONFIRMATION -> {
            PulsingCarMarker(position = ladipoOluwole, isPulsing = true, color = Color.Blue, title = "Waiting for passenger")
            PickupLocationMarker(position = ladipoOluwole, passengerName = "Darrell Stewart", address = "Ladipo Oluwole Street", isActive = true)
        }
        RideEvent.HEADING_TO_DROPOFF -> {
            ProgressCarMarker(startPosition = ladipoOluwole, endPosition = communityRoad, progress = rideState.progress.progressPercentage, title = "En route to dropoff")

            // Draw the route using the data from the ride state
            rideState.route?.let {
                Polyline(
                    points = listOf(it.startLocation.coordinates) + it.waypoints.map { p -> p.coordinates } + listOf(it.endLocation.coordinates),
                    color = Color.Green,
                    width = 12f
                )
            }

            // Draw markers for the passengers being dropped off
            rideState.passengers.forEach { passenger ->
                if (passenger.status == PassengerStatus.PICKED_UP) {
                    DropoffLocationMarker(
                        position = passenger.dropOffLocation.coordinates,
                        passengerName = passenger.name,
                        address = passenger.dropOffLocation.address,
                        isCompleted = false
                    )
                }
            }
        }
        RideEvent.TRIP_COMPLETED, RideEvent.TRIP_ENDED -> {
            AnimatedRoutePolyline(points = listOf(lagosCenter, ladipoOluwole, communityRoad), progress = 1f, color = Color.Green, width = 16f)
            val markerState = remember { MarkerState(position = communityRoad) }
            Marker(state = markerState, title = "Trip Completed", snippet = "₦6,500 earned", icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            DropoffLocationMarker(position = communityRoad, passengerName = "Darrell Stewart", address = "Community Road", isCompleted = true)
        }
        // Idle state
        else -> {
            val markerState = remember { MarkerState(position = lagosCenter) }
            Marker(state = markerState, title = "LincRide Car", snippet = "Available for rides", icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        }
    }
}

/**
 * Holds simple Text representations of map content for previewing.
 */
@Composable
fun BoxScope.PreviewMapContent(rideState: RideState) {
    Column(modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)) {
        Text("PREVIEW MODE", color = Color.White, modifier = Modifier.background(Color.Black))
        Text("Current Event: ${rideState.currentEvent}", color = Color.White, modifier = Modifier.background(Color.Black))
        when (rideState.currentEvent) {
            RideEvent.GET_TO_PICKUP -> {
                Text("Fake Car Marker", color = Color.White)
                Text("Fake Route Polyline", color = Color.White)
                Text("Fake Pickup Marker", color = Color.White)
            }
            RideEvent.PICKUP_CONFIRMATION -> {
                Text("Fake Pulsing Car Marker", color = Color.White)
            }
            RideEvent.HEADING_TO_DROPOFF -> {
                Text("Fake Progress Car Marker", color = Color.White)
                Text("Fake Dropoff Route", color = Color.White)
                Text("Fake Dropoff Markers", color = Color.White)
            }
            RideEvent.TRIP_COMPLETED, RideEvent.TRIP_ENDED -> {
                Text("Fake Completed Route", color = Color.White)
                Text("Fake Dropoff Marker", color = Color.White)
            }
            else -> {
                Text("Fake Car Marker (Idle)", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, name = "Map in Idle State")
@Composable
fun MapScreenIdlePreview() {
    LincRideTheme {
        MapScreenContent(
            rideState = RideState(currentEvent = RideEvent.IDLE),
            onMapReady = {}
        )
    }
}

@Preview(showBackground = true, name = "Map in Get To Pickup State")
@Composable
fun MapScreenGetToPickupPreview() {
    LincRideTheme {
        MapScreenContent(
            rideState = RideState(currentEvent = RideEvent.GET_TO_PICKUP),
            onMapReady = {}
        )
    }
}
