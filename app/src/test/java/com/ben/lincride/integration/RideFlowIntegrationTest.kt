package com.ben.lincride.integration

import app.cash.turbine.test
import com.ben.lincride.domain.model.*
import com.ben.lincride.domain.repository.RideSimulationEngine
import com.ben.lincride.presentation.viewmodel.RideViewModel
import com.google.android.gms.maps.model.LatLng
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class RideFlowIntegrationTest {

    private lateinit var rideSimulationEngine: RideSimulationEngine
    private lateinit var viewModel: RideViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val rideStateFlow = MutableStateFlow(RideState())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        rideSimulationEngine = mock()
        
        whenever(rideSimulationEngine.rideState).thenReturn(rideStateFlow)
        
        viewModel = RideViewModel(rideSimulationEngine)
    }

    @Test
    fun `complete ride flow - from idle to trip ended`() = runTest {
        val stateSequence = listOf(
            RideState(currentEvent = RideEvent.IDLE),
            RideState(currentEvent = RideEvent.OFFER_RIDE_AVAILABLE),
            RideState(currentEvent = RideEvent.PASSENGERS_ACCEPTED, passengers = createTestPassengers()),
            RideState(currentEvent = RideEvent.GET_TO_PICKUP, passengers = createTestPassengers()),
            RideState(currentEvent = RideEvent.PICKUP_CONFIRMATION, passengers = createTestPassengers()),
            RideState(currentEvent = RideEvent.HEADING_TO_DROPOFF, passengers = createTestPassengers()),
            RideState(currentEvent = RideEvent.TRIP_COMPLETED, passengers = createTestPassengers(), earnings = createTestEarnings()),
            RideState(currentEvent = RideEvent.TRIP_ENDED, passengers = createTestPassengers(), earnings = createTestEarnings())
        )

        viewModel.rideState.test {
            stateSequence.forEach { state ->
                rideStateFlow.value = state
                val emittedState = awaitItem()
                assertEquals(state.currentEvent, emittedState.currentEvent)
            }
        }
    }

    @Test
    fun `passenger acceptance flow - adds passengers to ride state`() = runTest {
        val passengers = createTestPassengers()
        val stateWithPassengers = RideState(
            currentEvent = RideEvent.PASSENGERS_ACCEPTED,
            passengers = passengers
        )

        viewModel.rideState.test {
                rideStateFlow.value = stateWithPassengers
            
                val state = awaitItem()
            assertEquals(RideEvent.PASSENGERS_ACCEPTED, state.currentEvent)
            assertEquals(2, state.passengers.size)
            assertEquals("John Doe", state.passengers[0].name)
            assertEquals("Jane Smith", state.passengers[1].name)
        }
    }

    @Test
    fun `pickup confirmation flow - handles passenger actions correctly`() = runTest {
        val passengerId = "passenger-1"
        
        viewModel.handlePassengerNoShow(passengerId)
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).handlePassengerNoShow(passengerId)
    }

    @Test
    fun `earnings calculation flow - displays correct amounts at trip end`() = runTest {
        val earnings = createTestEarnings()
        val stateWithEarnings = RideState(
            currentEvent = RideEvent.TRIP_ENDED,
            earnings = earnings
        )

        viewModel.rideState.test {
                rideStateFlow.value = stateWithEarnings
            
                val state = awaitItem()
            assertEquals(RideEvent.TRIP_ENDED, state.currentEvent)
            assertNotNull(state.earnings)
            assertEquals(1050.0, state.earnings?.total)
            assertEquals("₦", state.earnings?.currency)
        }
    }

    @Test
    fun `route progression flow - tracks progress correctly`() = runTest {
        val routeWithProgress = RideState(
            currentEvent = RideEvent.HEADING_TO_DROPOFF,
            route = createTestRoute(),
            progress = RideProgress(
                currentStep = 2,
                totalSteps = 4,
                progressPercentage = 0.5f,
                timeRemaining = 15,
                distanceRemaining = 5.2f
            )
        )

        viewModel.rideState.test {
                rideStateFlow.value = routeWithProgress
            
                val state = awaitItem()
            assertEquals(RideEvent.HEADING_TO_DROPOFF, state.currentEvent)
            assertEquals(2, state.progress.currentStep)
            assertEquals(0.5f, state.progress.progressPercentage)
            assertEquals(15, state.progress.timeRemaining)
        }
    }

    @Test
    fun `vehicle seat availability - tracks correctly throughout ride`() = runTest {
        val vehicle = Vehicle(
            id = "vehicle-1",
            type = "Sedan",
            licensePlate = "ABC-123",
            currentLocation = LatLng(6.5244, 3.3792),
            availableSeats = 2
        )
        
        val stateWithVehicle = RideState(
            currentEvent = RideEvent.PASSENGERS_ACCEPTED,
            vehicle = vehicle,
            passengers = createTestPassengers()
        )

        viewModel.rideState.test {
                rideStateFlow.value = stateWithVehicle
            
                val state = awaitItem()
            assertEquals(2, state.vehicle?.availableSeats)
            assertEquals(2, state.passengers.size)
        }
    }

    @Test
    fun `simulation reset flow - returns to idle state`() = runTest {
        val completedState = RideState(currentEvent = RideEvent.TRIP_ENDED)
        rideStateFlow.value = completedState

        viewModel.resetSimulation()
        testDispatcher.scheduler.advanceUntilIdle()
        
        rideStateFlow.value = RideState(currentEvent = RideEvent.IDLE)

        viewModel.rideState.test {
                val state = awaitItem()
            assertEquals(RideEvent.IDLE, state.currentEvent)
            assertTrue(state.passengers.isEmpty())
            assertNull(state.earnings)
        }
        
        verify(rideSimulationEngine).resetSimulation()
    }

    @Test
    fun `new trip flow - resets and starts fresh simulation`() = runTest {
        viewModel.startNewTrip()
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).resetSimulation()
        verify(rideSimulationEngine).startRideOffer()
    }

    @Test
    fun `passenger status tracking - maintains status throughout flow`() = runTest {
        val passengers = listOf(
            Passenger(
                id = "passenger-1",
                name = "John Doe",
                initials = "JD",
                rating = 4.5f,
                pickupLocation = Location(
                    coordinates = LatLng(6.5244, 3.3792),
                    address = "Victoria Island, Lagos"
                ),
                dropOffLocation = Location(
                    coordinates = LatLng(6.4281, 3.4219),
                    address = "Ikeja, Lagos"
                ),
                status = PassengerStatus.PICKED_UP
            )
        )
        
        val stateWithPickedUpPassenger = RideState(
            currentEvent = RideEvent.HEADING_TO_DROPOFF,
            passengers = passengers
        )

        viewModel.rideState.test {
                rideStateFlow.value = stateWithPickedUpPassenger
            
                val state = awaitItem()
            assertEquals(PassengerStatus.PICKED_UP, state.passengers[0].status)
        }
    }

    private fun createTestPassengers(): List<Passenger> {
        return listOf(
            Passenger(
                id = "passenger-1",
                name = "John Doe",
                initials = "JD",
                rating = 4.5f,
                pickupLocation = Location(
                    coordinates = LatLng(6.5244, 3.3792),
                    address = "Victoria Island, Lagos"
                ),
                dropOffLocation = Location(
                    coordinates = LatLng(6.4281, 3.4219),
                    address = "Ikeja, Lagos"
                )
            ),
            Passenger(
                id = "passenger-2",
                name = "Jane Smith",
                initials = "JS",
                rating = 4.8f,
                pickupLocation = Location(
                    coordinates = LatLng(6.5244, 3.3792),
                    address = "Victoria Island, Lagos"
                ),
                dropOffLocation = Location(
                    coordinates = LatLng(6.4281, 3.4219),
                    address = "Ikeja, Lagos"
                )
            )
        )
    }

    private fun createTestEarnings(): RideEarnings {
        return RideEarnings(
            baseAmount = 1000.0,
            bonus = 200.0,
            commission = 150.0,
            currency = "₦",
            carbonEmissionAvoided = 1.2
        )
    }

    private fun createTestRoute(): Route {
        return Route(
            startLocation = Location(
                coordinates = LatLng(6.5244, 3.3792),
                address = "Victoria Island, Lagos",
                name = "Start Point"
            ),
            endLocation = Location(
                coordinates = LatLng(6.4281, 3.4219),
                address = "Ikeja, Lagos",
                name = "End Point"
            ),
            waypoints = listOf(
                Location(
                    coordinates = LatLng(6.4698, 3.3909),
                    address = "Yaba, Lagos",
                    name = "Waypoint"
                )
            ),
            estimatedDuration = 45,
            estimatedDistance = 15.5f
        )
    }
}