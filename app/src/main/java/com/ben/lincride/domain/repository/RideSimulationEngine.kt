package com.ben.lincride.domain.repository

import android.util.Log
import com.ben.lincride.domain.model.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RideSimulationEngine @Inject constructor() {

    companion object {
        private const val TAG = "🚗 LincRide_SimulationEngine"
    }

    private val _rideState = MutableStateFlow(createInitialState())
    val rideState: StateFlow<RideState> = _rideState.asStateFlow()

    private val lagosCenter = LatLng(6.5244, 3.3792)
    private val ladipoOluwoleStreet = LatLng(6.5378, 3.3516)
    private val aromireStreet = LatLng(6.5287, 3.3478) // First drop-off point  
    private val communityRoad = LatLng(6.5198, 3.3441) // Second drop-off point

    suspend fun startRideOffer() {
        Log.i(TAG, "🎯 startRideOffer() - Starting simulation engine")
        Log.d(TAG, "State before: ${_rideState.value.currentEvent}")
        
        val passengers = createSamplePassengers()
        val route = createSampleRoute()
        Log.d(TAG, "Created ${passengers.size} passengers and route")

        val newState = _rideState.value.copy(
            currentEvent = RideEvent.OFFER_RIDE_AVAILABLE,
            passengers = passengers,
            route = route,
            driver = createSampleDriver(),
            vehicle = createSampleVehicle(),
            isSimulating = true
        )
        
        _rideState.value = newState
        Log.i(TAG, "✅ State updated to: ${newState.currentEvent}")
        Log.d(TAG, "Passengers: ${passengers.map { "${it.name} (${it.initials})" }}")
        Log.d(TAG, "isSimulating: ${newState.isSimulating}")
    }

    suspend fun acceptPassengers() {
        _rideState.value = _rideState.value.copy(
            currentEvent = RideEvent.PASSENGERS_ACCEPTED,
            passengers = _rideState.value.passengers.map {
                it.copy(status = PassengerStatus.ACCEPTED)
            }
        )
    }

    suspend fun startGetToPickup() {
        _rideState.value = _rideState.value.copy(
            currentEvent = RideEvent.GET_TO_PICKUP,
            progress = RideProgress(
                currentStep = 1,
                totalSteps = 4,
                progressPercentage = 0.3f,
                timeRemaining = 240,
                distanceRemaining = 2.1f
            )
        )
    }

    suspend fun startPickupConfirmation() {
        _rideState.value = _rideState.value.copy(
            currentEvent = RideEvent.PICKUP_CONFIRMATION,
            progress = RideProgress(
                currentStep = 2,
                totalSteps = 4,
                progressPercentage = 0.5f,
                timeRemaining = 285,
                distanceRemaining = 0f
            )
        )
    }

    /**
     * Event 5: Heading to Drop-off. Called when driver confirms pickup.
     */
    suspend fun startHeadingToDropoff() {
        val updatedPassengers = _rideState.value.passengers.map { passenger ->
            // Mark all accepted passengers as picked up.
            if (passenger.status == PassengerStatus.ACCEPTED) {
                passenger.copy(status = PassengerStatus.PICKED_UP)
            } else {
                passenger
            }
        }
        proceedToHeadingToDropOff(updatedPassengers, _rideState.value.vehicle)
    }

    /**
     * Trip Completed - Calculate earnings
     */
    suspend fun completeTrip() {
        _rideState.value = _rideState.value.copy(
            currentEvent = RideEvent.TRIP_COMPLETED,
            passengers = _rideState.value.passengers.map {
                it.copy(status = PassengerStatus.DROPPED_OFF)
            },
            progress = RideProgress(
                currentStep = 4,
                totalSteps = 4,
                progressPercentage = 1.0f,
                timeRemaining = 0,
                distanceRemaining = 0f
            ),
            earnings = RideEarnings(
                baseAmount = 6500.0,
                bonus = 500.0,
                commission = 500.0,
                carbonEmissionAvoided = 1.2
            )
        )
    }

    /**
     * Trip Ended - Show final summary
     */
    suspend fun endTrip() {
        _rideState.value = _rideState.value.copy(
            currentEvent = RideEvent.TRIP_ENDED
        )
    }

    /**
     * Reset simulation to idle state
     */
    suspend fun resetSimulation() {
        _rideState.value = createInitialState()
    }

    /**
     * Handle passenger no-show scenario.
     * As per the assessment, this should lead to the "Heading to Destination" state.
     */
    suspend fun handlePassengerNoShow(passengerId: String) {
        val currentState = _rideState.value
        val updatedPassengers = currentState.passengers.map { passenger ->
            when {
                passenger.id == passengerId -> passenger.copy(status = PassengerStatus.NO_SHOW)
                // The other accepted passengers are now considered picked up
                passenger.status == PassengerStatus.ACCEPTED -> passenger.copy(status = PassengerStatus.PICKED_UP)
                else -> passenger
            }
        }

        // A seat is freed up because of the no-show
        val updatedVehicle = currentState.vehicle?.copy(
            availableSeats = (currentState.vehicle.availableSeats + 1).coerceAtMost(4)
        )

        proceedToHeadingToDropOff(updatedPassengers, updatedVehicle)
    }

    private fun proceedToHeadingToDropOff(passengers: List<Passenger>, vehicle: Vehicle?) {
        _rideState.value = _rideState.value.copy(
            currentEvent = RideEvent.HEADING_TO_DROPOFF,
            passengers = passengers,
            vehicle = vehicle,
            progress = RideProgress(
                currentStep = 3,
                totalSteps = 4,
                progressPercentage = 0.75f,
                timeRemaining = 480, // 8 minutes
                distanceRemaining = 3.2f
            )
        )
    }

    private fun createInitialState(): RideState {
        return RideState(
            currentEvent = RideEvent.IDLE,
            isSimulating = false
        )
    }

    private fun createSampleDriver(): Driver {
        return Driver(
            id = "driver_001",
            name = "Current Driver",
            rating = 4.8f,
            vehicleId = "vehicle_001",
            currentLocation = lagosCenter
        )
    }

    private fun createSampleVehicle(): Vehicle {
        return Vehicle(
            id = "vehicle_001",
            type = "Sedan",
            licensePlate = "ABC-123-XY",
            currentLocation = lagosCenter,
            availableSeats = 2
        )
    }

    private fun createSamplePassengers(): List<Passenger> {
        // Create 2 passengers with different drop-off locations for true multi-stop journey
        return listOf(
            Passenger(
                id = "passenger_001",
                name = "Darrell Stewart",
                initials = "DS",
                rating = 4.7f,
                pickupLocation = Location(
                    coordinates = ladipoOluwoleStreet,
                    address = "Ladipo Oluwole Street",
                    name = "Ladipo Oluwole Street"
                ),
                dropOffLocation = Location(
                    coordinates = aromireStreet,
                    address = "Aromire Street",
                    name = "Aromire Street"
                )
            ),
            Passenger(
                id = "passenger_002",
                name = "Hinata Chukwu",
                initials = "HC",
                rating = 4.7f,
                pickupLocation = Location(
                    coordinates = ladipoOluwoleStreet,
                    address = "Ladipo Oluwole Street",
                    name = "Ladipo Oluwole Street"
                ),
                dropOffLocation = Location(
                    coordinates = communityRoad,
                    address = "Community Road",
                    name = "Community Road"
                )
            )
        )
    }

    private fun createSampleRoute(): Route {
        return Route(
            startLocation = Location(
                coordinates = lagosCenter,
                address = "Current Location",
                name = "Driver Location"
            ),
            endLocation = Location(
                coordinates = communityRoad,
                address = "Community Road",
                name = "Final Destination"
            ),
            waypoints = listOf(
                Location(
                    coordinates = ladipoOluwoleStreet,
                    address = "Ladipo Oluwole Street",
                    name = "Pickup Point"
                ),
                Location(
                    coordinates = aromireStreet,
                    address = "Aromire Street",
                    name = "First Drop-off (Darrell Stewart)"
                )
            ),
            estimatedDuration = 15,
            estimatedDistance = 5.3f
        )
    }
}
