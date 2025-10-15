package com.ben.lincride.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ben.lincride.domain.model.RideEvent
import com.ben.lincride.domain.model.RideState
import com.ben.lincride.domain.repository.RideSimulationEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Professional ViewModel implementing MVI pattern for ride management
 */
@HiltViewModel
class RideViewModel @Inject constructor(
    private val rideSimulationEngine: RideSimulationEngine
) : ViewModel() {
    
    companion object {
        private const val TAG = "🚗 LincRide_RideViewModel"
    }
    
    val rideState: StateFlow<RideState> = rideSimulationEngine.rideState
    
    /**
     * Start the complete 5-event ride simulation
     */
    fun startRideSimulation() {
        Log.d(TAG, "🚀 startRideSimulation() called - Beginning full 5-event simulation")
        viewModelScope.launch {
            Log.d(TAG, "📍 Event 1: Starting Offer Ride Available")
            // Event 1: Offer Ride Available
            rideSimulationEngine.startRideOffer()
            delay(3000) // Show for 3 seconds
            
            Log.d(TAG, "✅ Event 2: Auto-accepting passengers")
            // Event 2: Passengers Accepted (automatic)
            rideSimulationEngine.acceptPassengers()
            delay(1000)
            
            Log.d(TAG, "🚗 Event 3: Starting Get to Pickup phase")
            // Event 3: Get to Pickup
            rideSimulationEngine.startGetToPickup()
            delay(4000) // Show pickup progress for 4 seconds
            
            Log.d(TAG, "✋ Event 4: Starting Pickup Confirmation")
            // Event 4: Pickup Confirmation
            rideSimulationEngine.startPickupConfirmation()
            delay(5000) // Show confirmation screen for 5 seconds
            
            Log.d(TAG, "🛣️ Event 5: Starting Heading to Drop-off")
            // Event 5: Heading to Drop-off
            rideSimulationEngine.startHeadingToDropoff()
            delay(4000) // Show journey for 4 seconds
            
            Log.d(TAG, "🏁 Completing Trip")
            // Complete Trip
            rideSimulationEngine.completeTrip()
            delay(1000)
            
            Log.d(TAG, "💰 Ending Trip - Show earnings")
            // End Trip - Show earnings
            rideSimulationEngine.endTrip()
            Log.d(TAG, "✅ Full simulation completed successfully")
        }
    }
    
    /**
     * Event 2: User clicks "Offer Ride" - triggers the ride offer flow.
     */
    fun startRideOffer() {
        Log.i(TAG, "🔥 USER ACTION: startRideOffer() called - User tapped 'Offer Ride' button")
        Log.d(TAG, "Current state before: ${rideState.value.currentEvent}")
        viewModelScope.launch {
            try {
                Log.d(TAG, "Calling rideSimulationEngine.startRideOffer()...")
                rideSimulationEngine.startRideOffer()
                Log.d(TAG, "✅ rideSimulationEngine.startRideOffer() completed")
                Log.d(TAG, "New state after: ${rideState.value.currentEvent}")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error in startRideOffer(): ${e.message}", e)
            }
        }
    }
    
    /**
     * Event progression based on completion triggers
     */
    fun progressToNextEvent() {
        val currentEvent = rideState.value.currentEvent
        Log.i(TAG, "⏭️ progressToNextEvent() called - Current: $currentEvent")
        viewModelScope.launch {
            when (currentEvent) {
                RideEvent.IDLE -> {
                    Log.d(TAG, "IDLE → OFFER_RIDE_AVAILABLE")
                    rideSimulationEngine.startRideOffer()
                }
                RideEvent.OFFER_RIDE_AVAILABLE -> {
                    Log.d(TAG, "OFFER_RIDE_AVAILABLE → PASSENGERS_ACCEPTED")
                    rideSimulationEngine.acceptPassengers()
                }
                RideEvent.PASSENGERS_ACCEPTED -> {
                    Log.d(TAG, "PASSENGERS_ACCEPTED → GET_TO_PICKUP")
                    rideSimulationEngine.startGetToPickup()
                }
                RideEvent.GET_TO_PICKUP -> {
                    Log.d(TAG, "GET_TO_PICKUP → PICKUP_CONFIRMATION")
                    rideSimulationEngine.startPickupConfirmation()
                }
                RideEvent.PICKUP_CONFIRMATION -> {
                    Log.d(TAG, "PICKUP_CONFIRMATION → HEADING_TO_DROPOFF")
                    rideSimulationEngine.startHeadingToDropoff()
                }
                RideEvent.HEADING_TO_DROPOFF -> {
                    Log.d(TAG, "HEADING_TO_DROPOFF → TRIP_COMPLETED → TRIP_ENDED")
                    rideSimulationEngine.completeTrip()
                    delay(500)
                    rideSimulationEngine.endTrip()
                }
                RideEvent.TRIP_COMPLETED -> {
                    Log.d(TAG, "TRIP_COMPLETED → TRIP_ENDED")
                    rideSimulationEngine.endTrip()
                }
                RideEvent.TRIP_ENDED -> {
                    Log.d(TAG, "TRIP_ENDED → IDLE (reset)")
                    rideSimulationEngine.resetSimulation()
                }
            }
            Log.d(TAG, "✅ Event transition completed, new state: ${rideState.value.currentEvent}")
        }
    }
    
    /**
     * Handle pickup confirmation actions
     */
    fun confirmPickup() {
        viewModelScope.launch {
            rideSimulationEngine.startHeadingToDropoff()
        }
    }
    
    /**
     * Handle passenger no-show
     */
    fun handlePassengerNoShow(passengerId: String) {
        viewModelScope.launch {
            rideSimulationEngine.handlePassengerNoShow(passengerId)
        }
    }
    
    /**
     * Reset simulation to beginning
     */
    fun resetSimulation() {
        viewModelScope.launch {
            rideSimulationEngine.resetSimulation()
        }
    }
    
    /**
     * Start new trip after completion
     */
    fun startNewTrip() {
        viewModelScope.launch {
            rideSimulationEngine.resetSimulation()
            delay(500)
            startRideSimulation()
        }
    }
}