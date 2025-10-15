package com.ben.lincride.ui.components

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.delay
import kotlin.math.*

/**
 * Professional car marker with smooth animation between locations
 */
@Composable
fun AnimatedCarMarker(
    currentPosition: LatLng,
    targetPosition: LatLng,
    isMoving: Boolean = false,
    carIcon: BitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
    animationDurationMs: Int = 2000,
    onAnimationEnd: () -> Unit = {}
) {
    var animatedPosition by remember { mutableStateOf(currentPosition) }
    val animationProgress by animateFloatAsState(
        targetValue = if (isMoving) 1f else 0f,
        animationSpec = tween(
            durationMillis = animationDurationMs,
            easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f) // Material motion curve
        ),
        finishedListener = { onAnimationEnd() },
        label = "car_movement"
    )
    
    LaunchedEffect(currentPosition, targetPosition, isMoving) {
        if (isMoving) {
            // Animate car movement with smooth interpolation
            while (animationProgress < 1f) {
                val progress = animationProgress
                animatedPosition = interpolateLatLng(currentPosition, targetPosition, progress)
                delay(16) // ~60 FPS
            }
        } else {
            animatedPosition = currentPosition
        }
    }
    
    val markerState = remember { MarkerState(position = animatedPosition) }
    
    // Update marker state
    LaunchedEffect(animatedPosition) {
        markerState.position = animatedPosition
    }
    
    Marker(
        state = markerState,
        title = "LincRide Car",
        snippet = if (isMoving) "En route..." else "Available",
        icon = carIcon,
        rotation = if (isMoving) calculateBearing(currentPosition, targetPosition) else 0f,
        flat = true
    )
}

/**
 * Pulsing car marker for stationary states (waiting, pickup)
 */
@Composable
fun PulsingCarMarker(
    position: LatLng,
    isPulsing: Boolean = true,
    color: Color = Color.Blue,
    title: String = "LincRide Car"
) {
    val pulseScale by animateFloatAsState(
        targetValue = if (isPulsing) 1.2f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_animation"
    )
    
    val markerState = remember { MarkerState(position = position) }
    
    Marker(
        state = markerState,
        title = title,
        snippet = "Waiting...",
        alpha = if (isPulsing) 0.7f + (pulseScale - 1f) * 0.3f else 1.0f,
        icon = createColoredCarIcon(color)
    )
}

/**
 * Car marker with progress indicator for ongoing rides
 */
@Composable
fun ProgressCarMarker(
    startPosition: LatLng,
    endPosition: LatLng,
    progress: Float, // 0f to 1f
    title: String = "En route"
) {
    val currentPosition = interpolateLatLng(startPosition, endPosition, progress.coerceIn(0f, 1f))
    val bearing = calculateBearing(startPosition, endPosition)
    
    val markerState = remember { MarkerState(position = currentPosition) }
    
    LaunchedEffect(currentPosition) {
        markerState.position = currentPosition
    }
    
    Marker(
        state = markerState,
        title = title,
        snippet = "${(progress * 100).toInt()}% complete",
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
        rotation = bearing,
        flat = true
    )
}

/**
 * Multiple car markers for fleet management
 */
@Composable
fun CarFleetMarkers(
    cars: List<CarMarkerData>,
    selectedCarId: String? = null
) {
    cars.forEach { car ->
        val isSelected = car.id == selectedCarId
        val markerState = remember(car.id) { MarkerState(position = car.position) }
        
        LaunchedEffect(car.position) {
            markerState.position = car.position
        }
        
        Marker(
            state = markerState,
            title = car.title,
            snippet = car.status,
            icon = if (isSelected) {
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            } else {
                createCarIconByStatus(car.status)
            },
            rotation = car.bearing,
            alpha = if (isSelected) 1.0f else 0.8f
        )
    }
}

data class CarMarkerData(
    val id: String,
    val position: LatLng,
    val title: String,
    val status: String,
    val bearing: Float = 0f
)

/**
 * Utility functions for car marker animations
 */
private fun interpolateLatLng(start: LatLng, end: LatLng, fraction: Float): LatLng {
    val lat = start.latitude + (end.latitude - start.latitude) * fraction
    val lng = start.longitude + (end.longitude - start.longitude) * fraction
    return LatLng(lat, lng)
}

private fun calculateBearing(start: LatLng, end: LatLng): Float {
    val startLat = Math.toRadians(start.latitude)
    val startLng = Math.toRadians(start.longitude)
    val endLat = Math.toRadians(end.latitude)
    val endLng = Math.toRadians(end.longitude)
    
    val deltaLng = endLng - startLng
    val y = sin(deltaLng) * cos(endLat)
    val x = cos(startLat) * sin(endLat) - sin(startLat) * cos(endLat) * cos(deltaLng)
    
    val bearing = Math.toDegrees(atan2(y, x))
    return ((bearing + 360) % 360).toFloat()
}

private fun createColoredCarIcon(color: Color): BitmapDescriptor {
    return when (color) {
        Color.Red -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        Color.Green -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        Color.Blue -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        Color.Yellow -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
        else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
    }
}

private fun createCarIconByStatus(status: String): BitmapDescriptor {
    return when (status.lowercase()) {
        "available" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        "en route" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        "waiting" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
        "busy" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
    }
}