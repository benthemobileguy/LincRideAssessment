package com.ben.lincride.ui.components

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

/**
 * Professional pickup location markers with animations
 */
@Composable
fun PickupLocationMarker(
    position: LatLng,
    passengerName: String,
    address: String,
    isActive: Boolean = false,
    isPulsing: Boolean = false,
    onMarkerClick: () -> Unit = {}
) {
    val markerState = remember { MarkerState(position = position) }
    
    val pulseAlpha by animateFloatAsState(
        targetValue = if (isPulsing) 0.6f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pickup_pulse"
    )
    
    Marker(
        state = markerState,
        title = "📍 Pickup: $passengerName",
        snippet = address,
        icon = if (isActive) {
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        } else {
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
        },
        alpha = if (isPulsing) pulseAlpha else 1.0f,
        onClick = {
            onMarkerClick()
            true
        }
    )
}

/**
 * Dropoff location markers
 */
@Composable
fun DropoffLocationMarker(
    position: LatLng,
    passengerName: String,
    address: String,
    isCompleted: Boolean = false,
    onMarkerClick: () -> Unit = {}
) {
    val markerState = remember { MarkerState(position = position) }
    
    Marker(
        state = markerState,
        title = "🏁 Dropoff: $passengerName",
        snippet = address,
        icon = if (isCompleted) {
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        } else {
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        },
        onClick = {
            onMarkerClick()
            true
        }
    )
}

/**
 * Multi-stop route markers with numbered indicators
 */
@Composable
fun MultiStopMarkers(
    stops: List<StopMarkerData>,
    currentStopIndex: Int = 0
) {
    stops.forEachIndexed { index, stop ->
        val isCurrent = index == currentStopIndex
        val isCompleted = index < currentStopIndex
        val isPending = index > currentStopIndex
        
        val markerState = remember(stop.id) { MarkerState(position = stop.position) }
        
        Marker(
            state = markerState,
            title = "${stop.stepNumber}. ${stop.title}",
            snippet = "${stop.address} • ${stop.passengerName ?: "Stop"}",
            icon = when {
                isCompleted -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                isCurrent -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                isPending -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
            },
            alpha = if (isCurrent) 1.0f else 0.8f,
            onClick = {
                stop.onClick()
                true
            }
        )
    }
}

/**
 * Animated waypoint markers that appear sequentially
 */
@Composable
fun AnimatedWaypointMarkers(
    waypoints: List<WaypointData>,
    animationDelay: Int = 500
) {
    waypoints.forEachIndexed { index, waypoint ->
        var isVisible by remember { mutableStateOf(false) }
        
        LaunchedEffect(waypoint.id) {
            kotlinx.coroutines.delay(index * animationDelay.toLong())
            isVisible = true
        }
        
        val scale by animateFloatAsState(
            targetValue = if (isVisible) 1.0f else 0.0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "waypoint_scale"
        )
        
        if (isVisible) {
            val markerState = remember(waypoint.id) { MarkerState(position = waypoint.position) }
            
            Marker(
                state = markerState,
                title = waypoint.title,
                snippet = waypoint.description,
                icon = waypoint.icon,
                alpha = scale
            )
        }
    }
}

/**
 * Interactive marker clusters for multiple passengers
 */
@Composable
fun PassengerClusterMarker(
    position: LatLng,
    passengers: List<PassengerMarkerData>,
    isExpanded: Boolean = false,
    onClusterClick: () -> Unit = {}
) {
    if (!isExpanded) {
        // Clustered marker
        val markerState = remember { MarkerState(position = position) }
        
        Marker(
            state = markerState,
            title = "📍 ${passengers.size} Passengers",
            snippet = passengers.joinToString(", ") { it.name },
            icon = createClusterIcon(passengers.size),
            onClick = {
                onClusterClick()
                true
            }
        )
    } else {
        // Expanded individual markers
        passengers.forEach { passenger ->
            val offsetPosition = calculateOffsetPosition(position, passenger.offsetIndex)
            val markerState = remember(passenger.id) { MarkerState(position = offsetPosition) }
            
            Marker(
                state = markerState,
                title = "👤 ${passenger.name}",
                snippet = passenger.address,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN),
                onClick = {
                    passenger.onClick()
                    true
                }
            )
        }
    }
}

/**
 * Special markers for LincRide branded locations
 */
@Composable
fun LincRideLocationMarkers(
    hubLocation: LatLng? = null,
    serviceArea: List<LatLng> = emptyList(),
    popularPickups: List<PopularLocationData> = emptyList()
) {
    // Main LincRide hub
    hubLocation?.let { hub ->
        val markerState = remember { MarkerState(position = hub) }
        
        Marker(
            state = markerState,
            title = "🏢 LincRide Hub",
            snippet = "Main operation center",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
        )
    }
    
    // Popular pickup locations
    popularPickups.forEach { location ->
        val markerState = remember(location.id) { MarkerState(position = location.position) }
        
        Marker(
            state = markerState,
            title = "⭐ ${location.name}",
            snippet = "${location.pickupCount} pickups this week",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW),
            alpha = 0.7f
        )
    }
}

// Data classes for marker management
data class StopMarkerData(
    val id: String,
    val position: LatLng,
    val stepNumber: Int,
    val title: String,
    val address: String,
    val passengerName: String? = null,
    val onClick: () -> Unit = {}
)

data class WaypointData(
    val id: String,
    val position: LatLng,
    val title: String,
    val description: String,
    val icon: BitmapDescriptor = BitmapDescriptorFactory.defaultMarker()
)

data class PassengerMarkerData(
    val id: String,
    val name: String,
    val address: String,
    val offsetIndex: Int = 0,
    val onClick: () -> Unit = {}
)

data class PopularLocationData(
    val id: String,
    val position: LatLng,
    val name: String,
    val pickupCount: Int
)

// Utility functions
private fun createClusterIcon(count: Int): BitmapDescriptor {
    return when {
        count <= 2 -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        count <= 5 -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
    }
}

private fun calculateOffsetPosition(basePosition: LatLng, offsetIndex: Int): LatLng {
    // Simple offset calculation for cluster expansion
    val offsetDistance = 0.0001 // Small offset in degrees
    val angle = (offsetIndex * 45).toDouble() // 45 degree increments
    val radians = Math.toRadians(angle)
    
    return LatLng(
        basePosition.latitude + offsetDistance * Math.cos(radians),
        basePosition.longitude + offsetDistance * Math.sin(radians)
    )
}

/**
 * Factory for creating Lagos-specific markers
 */
object LagosMarkerFactory {
    
    // Popular Lagos locations
    val LADIPO_OLUWOLE = LatLng(6.5378, 3.3516)
    val COMMUNITY_ROAD = LatLng(6.5198, 3.3441)
    val VICTORIA_ISLAND = LatLng(6.4281, 3.4219)
    val IKEJA = LatLng(6.5958, 3.3371)
    val LEKKI = LatLng(6.4474, 3.5617)
    
    fun createLagosPickupMarkers(): List<StopMarkerData> {
        return listOf(
            StopMarkerData(
                id = "ladipo",
                position = LADIPO_OLUWOLE,
                stepNumber = 1,
                title = "Pickup",
                address = "Ladipo Oluwole Street",
                passengerName = "Darrell Stewart"
            ),
            StopMarkerData(
                id = "community",
                position = COMMUNITY_ROAD,
                stepNumber = 2,
                title = "Dropoff",
                address = "Community Road",
                passengerName = "Darrell Stewart"
            )
        )
    }
    
    fun createLagosPopularLocations(): List<PopularLocationData> {
        return listOf(
            PopularLocationData("vi", VICTORIA_ISLAND, "Victoria Island", 45),
            PopularLocationData("ikeja", IKEJA, "Ikeja", 32),
            PopularLocationData("lekki", LEKKI, "Lekki", 28)
        )
    }
}