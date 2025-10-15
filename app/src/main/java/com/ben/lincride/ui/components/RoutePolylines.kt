package com.ben.lincride.ui.components

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.maps.android.compose.Polyline
import kotlinx.coroutines.delay

/**
 * Professional route polylines with smooth animations
 */
@Composable
fun AnimatedRoutePolyline(
    points: List<LatLng>,
    progress: Float = 1f, // 0f to 1f - how much of the route to show
    color: Color = Color.Blue,
    width: Float = 12f,
    isAnimated: Boolean = true,
    animationDurationMs: Int = 2000
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (isAnimated) progress else 1f,
        animationSpec = tween(
            durationMillis = animationDurationMs,
            easing = EaseInOutQuad
        ),
        label = "route_progress"
    )
    
    val visiblePoints = remember(points, animatedProgress) {
        if (animatedProgress >= 1f) {
            points
        } else {
            val targetIndex = (points.size * animatedProgress).toInt().coerceAtMost(points.size - 1)
            if (targetIndex <= 0) {
                if (points.isNotEmpty()) listOf(points[0]) else emptyList()
            } else {
                points.take(targetIndex + 1)
            }
        }
    }
    
    if (visiblePoints.size >= 2) {
        Polyline(
            points = visiblePoints,
            color = color,
            width = width,
            jointType = JointType.ROUND,
            geodesic = true
        )
    }
}

/**
 * Multi-segment route with different colors for different parts
 */
@Composable
fun MultiSegmentRoute(
    segments: List<RouteSegment>,
    currentSegmentIndex: Int = 0,
    progress: Float = 1f
) {
    segments.forEachIndexed { index, segment ->
        val segmentProgress = when {
            index < currentSegmentIndex -> 1f
            index == currentSegmentIndex -> progress
            else -> 0f
        }
        
        AnimatedRoutePolyline(
            points = segment.points,
            progress = segmentProgress,
            color = segment.color,
            width = segment.width,
            isAnimated = true
        )
    }
}

/**
 * Dashed route polyline for planned routes
 */
@Composable
fun DashedRoutePolyline(
    points: List<LatLng>,
    color: Color = Color.Gray,
    width: Float = 8f,
    dashLength: Float = 20f,
    gapLength: Float = 20f
) {
    if (points.size >= 2) {
        Polyline(
            points = points,
            color = color,
            width = width,
            pattern = listOf(
                Dash(dashLength),
                Gap(gapLength)
            ),
            jointType = JointType.ROUND
        )
    }
}

/**
 * Pulsing route polyline for active rides
 */
@Composable
fun PulsingRoutePolyline(
    points: List<LatLng>,
    baseColor: Color = Color.Blue,
    isPulsing: Boolean = true
) {
    val pulseAlpha by animateFloatAsState(
        targetValue = if (isPulsing) 0.3f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    
    val pulseWidth by animateFloatAsState(
        targetValue = if (isPulsing) 16f else 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_width"
    )
    
    if (points.size >= 2) {
        // Background pulse effect
        Polyline(
            points = points,
            color = baseColor.copy(alpha = pulseAlpha),
            width = pulseWidth,
            jointType = JointType.ROUND
        )
        
        // Main route line
        Polyline(
            points = points,
            color = baseColor,
            width = 8f,
            jointType = JointType.ROUND
        )
    }
}

/**
 * Route with distance markers
 */
@Composable
fun RouteWithDistanceMarkers(
    points: List<LatLng>,
    color: Color = Color.Blue,
    showDistanceMarkers: Boolean = true,
    markerIntervalKm: Double = 1.0
) {
    // Main route polyline
    if (points.size >= 2) {
        Polyline(
            points = points,
            color = color,
            width = 12f,
            jointType = JointType.ROUND
        )
    }
    
    // Distance markers (simplified for demo - would need actual distance calculation)
    if (showDistanceMarkers && points.size >= 2) {
        val markerPositions = calculateDistanceMarkerPositions(points, markerIntervalKm)
        markerPositions.forEachIndexed { index, position ->
            DistanceMarker(
                position = position,
                distanceKm = (index + 1) * markerIntervalKm
            )
        }
    }
}

/**
 * LincRide branded route colors and styles
 */
object LincRideRouteStyles {
    val PRIMARY_ROUTE = Color(0xFF00C851) // LincGreen
    val SECONDARY_ROUTE = Color(0xFF2196F3) // Blue
    val PLANNED_ROUTE = Color(0xFF9E9E9E) // Gray
    val COMPLETED_ROUTE = Color(0xFF4CAF50) // Success Green
    val PICKUP_ROUTE = Color(0xFFFF9800) // Orange
    val DROPOFF_ROUTE = Color(0xFFE91E63) // Pink
    
    const val THICK_WIDTH = 16f
    const val MEDIUM_WIDTH = 12f
    const val THIN_WIDTH = 8f
}

data class RouteSegment(
    val points: List<LatLng>,
    val color: Color = LincRideRouteStyles.PRIMARY_ROUTE,
    val width: Float = LincRideRouteStyles.MEDIUM_WIDTH,
    val type: SegmentType = SegmentType.NORMAL
)

enum class SegmentType {
    NORMAL,
    PICKUP,
    DROPOFF,
    COMPLETED
}

@Composable
private fun DistanceMarker(
    position: LatLng,
    distanceKm: Double
) {
    // Simplified distance marker - in real implementation would use custom marker
    // For now, just showing the concept
}

private fun calculateDistanceMarkerPositions(
    points: List<LatLng>, 
    intervalKm: Double
): List<LatLng> {
    // Simplified calculation - in real implementation would calculate actual distances
    // and place markers at specific intervals
    return if (points.size >= 4) {
        listOf(
            points[points.size / 4],
            points[points.size / 2],
            points[3 * points.size / 4]
        )
    } else {
        emptyList()
    }
}

/**
 * Factory for creating common route types
 */
object RouteFactory {
    
    fun createPickupRoute(
        driverLocation: LatLng,
        pickupLocation: LatLng
    ): RouteSegment {
        return RouteSegment(
            points = listOf(driverLocation, pickupLocation),
            color = LincRideRouteStyles.PICKUP_ROUTE,
            width = LincRideRouteStyles.MEDIUM_WIDTH,
            type = SegmentType.PICKUP
        )
    }
    
    fun createDropoffRoute(
        pickupLocation: LatLng,
        dropoffLocation: LatLng
    ): RouteSegment {
        return RouteSegment(
            points = listOf(pickupLocation, dropoffLocation),
            color = LincRideRouteStyles.DROPOFF_ROUTE,
            width = LincRideRouteStyles.MEDIUM_WIDTH,
            type = SegmentType.DROPOFF
        )
    }
    
    fun createMultiStopRoute(
        driverLocation: LatLng,
        pickupLocations: List<LatLng>,
        dropoffLocations: List<LatLng>
    ): List<RouteSegment> {
        val segments = mutableListOf<RouteSegment>()
        
        // Driver to first pickup
        if (pickupLocations.isNotEmpty()) {
            segments.add(createPickupRoute(driverLocation, pickupLocations[0]))
        }
        
        // Between pickups
        for (i in 0 until pickupLocations.size - 1) {
            segments.add(
                RouteSegment(
                    points = listOf(pickupLocations[i], pickupLocations[i + 1]),
                    color = LincRideRouteStyles.PICKUP_ROUTE,
                    type = SegmentType.PICKUP
                )
            )
        }
        
        // Pickups to dropoffs
        if (pickupLocations.isNotEmpty() && dropoffLocations.isNotEmpty()) {
            segments.add(createDropoffRoute(pickupLocations.last(), dropoffLocations[0]))
        }
        
        // Between dropoffs
        for (i in 0 until dropoffLocations.size - 1) {
            segments.add(
                RouteSegment(
                    points = listOf(dropoffLocations[i], dropoffLocations[i + 1]),
                    color = LincRideRouteStyles.DROPOFF_ROUTE,
                    type = SegmentType.DROPOFF
                )
            )
        }
        
        return segments
    }
}