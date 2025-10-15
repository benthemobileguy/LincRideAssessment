package com.ben.lincride.core.data.model

import com.google.android.gms.maps.model.LatLng

data class Passenger(
    val id: String,
    val name: String,
    val rating: Float,
    val profileImageUrl: String = ""
)

data class RideLocation(
    val address: String,
    val coordinates: LatLng
)

data class RouteProgress(
    val currentPosition: LatLng,
    val progress: Float, // 0.0 to 1.0
    val estimatedTimeMinutes: Int
)

enum class RideState {
    IDLE,
    OFFERING_RIDE,
    GETTING_TO_PICKUP,
    PICKUP_CONFIRMATION,
    HEADING_TO_DESTINATION,
    TRIP_COMPLETED
}

enum class PickupAction {
    PICKED_UP,
    DIDNT_SHOW
}

data class TripEarnings(
    val netEarnings: String,
    val bonus: String,
    val commission: String
)

data class RideSession(
    val id: String,
    val state: RideState,
    val passengers: List<Passenger>,
    val pickupLocation: RideLocation?,
    val destinationLocation: RideLocation?,
    val routeProgress: RouteProgress?,
    val availableSeats: Int,
    val acceptedPassengers: Int,
    val waitingTimeMinutes: Int,
    val earnings: TripEarnings?,
    val co2Saved: String
)