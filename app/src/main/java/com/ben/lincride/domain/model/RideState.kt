package com.ben.lincride.domain.model

import com.google.android.gms.maps.model.LatLng

data class RideState(
    val currentEvent: RideEvent = RideEvent.IDLE,
    val driver: Driver? = null,
    val passengers: List<Passenger> = emptyList(),
    val route: Route? = null,
    val vehicle: Vehicle? = null,
    val progress: RideProgress = RideProgress(),
    val earnings: RideEarnings? = null,
    val isSimulating: Boolean = false
)

enum class RideEvent {
    IDLE,
    OFFER_RIDE_AVAILABLE,
    PASSENGERS_ACCEPTED,
    GET_TO_PICKUP,
    PICKUP_CONFIRMATION,
    HEADING_TO_DROPOFF,
    TRIP_COMPLETED,
    TRIP_ENDED
}

data class Driver(
    val id: String,
    val name: String,
    val rating: Float,
    val vehicleId: String,
    val currentLocation: LatLng
)

data class Passenger(
    val id: String,
    val name: String,
    val initials: String,
    val rating: Float,
    val pickupLocation: Location,
    val dropOffLocation: Location,
    val status: PassengerStatus = PassengerStatus.ACCEPTED
)

enum class PassengerStatus {
    PENDING,
    ACCEPTED,
    PICKED_UP,
    DROPPED_OFF,
    NO_SHOW
}

data class Location(
    val coordinates: LatLng,
    val address: String,
    val name: String? = null
)

data class Route(
    val startLocation: Location,
    val endLocation: Location,
    val waypoints: List<Location> = emptyList(),
    val estimatedDuration: Int, // in minutes
    val estimatedDistance: Float // in kilometers
)

data class Vehicle(
    val id: String,
    val type: String,
    val licensePlate: String,
    val currentLocation: LatLng,
    val availableSeats: Int = 4
)

data class RideProgress(
    val currentStep: Int = 0,
    val totalSteps: Int = 0,
    val progressPercentage: Float = 0f,
    val timeRemaining: Int = 0,
    val distanceRemaining: Float = 0f
)

data class RideEarnings(
    val baseAmount: Double,
    val bonus: Double = 0.0,
    val commission: Double = 0.0,
    val currency: String = "₦",
    val carbonEmissionAvoided: Double = 0.0
) {
    val total: Double get() = baseAmount + bonus - commission
}
