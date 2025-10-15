package com.ben.lincride.presentation.viewmodel

import app.cash.turbine.test
import com.ben.lincride.domain.model.RideEvent
import com.ben.lincride.domain.model.RideState
import com.ben.lincride.domain.repository.RideSimulationEngine
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
class RideViewModelTest {

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
    fun `initial state should be IDLE`() = runTest {
        viewModel.rideState.test {
            val initialState = awaitItem()
            assertEquals(RideEvent.IDLE, initialState.currentEvent)
        }
    }

    @Test
    fun `startRideOffer should call simulation engine`() = runTest {
        viewModel.startRideOffer()
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).startRideOffer()
    }

    @Test
    fun `progressToNextEvent from IDLE should start ride offer`() = runTest {
        rideStateFlow.value = RideState(currentEvent = RideEvent.IDLE)
        
        viewModel.progressToNextEvent()
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).startRideOffer()
    }

    @Test
    fun `progressToNextEvent from OFFER_RIDE_AVAILABLE should accept passengers`() = runTest {
        rideStateFlow.value = RideState(currentEvent = RideEvent.OFFER_RIDE_AVAILABLE)
        
        viewModel.progressToNextEvent()
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).acceptPassengers()
    }

    @Test
    fun `progressToNextEvent from PASSENGERS_ACCEPTED should start get to pickup`() = runTest {
        rideStateFlow.value = RideState(currentEvent = RideEvent.PASSENGERS_ACCEPTED)
        
        viewModel.progressToNextEvent()
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).startGetToPickup()
    }

    @Test
    fun `progressToNextEvent from GET_TO_PICKUP should start pickup confirmation`() = runTest {
        rideStateFlow.value = RideState(currentEvent = RideEvent.GET_TO_PICKUP)
        
        viewModel.progressToNextEvent()
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).startPickupConfirmation()
    }

    @Test
    fun `progressToNextEvent from PICKUP_CONFIRMATION should start heading to dropoff`() = runTest {
        rideStateFlow.value = RideState(currentEvent = RideEvent.PICKUP_CONFIRMATION)
        
        viewModel.progressToNextEvent()
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).startHeadingToDropoff()
    }

    @Test
    fun `progressToNextEvent from HEADING_TO_DROPOFF should complete and end trip`() = runTest {
        rideStateFlow.value = RideState(currentEvent = RideEvent.HEADING_TO_DROPOFF)
        
        viewModel.progressToNextEvent()
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).completeTrip()
        verify(rideSimulationEngine).endTrip()
    }

    @Test
    fun `progressToNextEvent from TRIP_COMPLETED should end trip`() = runTest {
        rideStateFlow.value = RideState(currentEvent = RideEvent.TRIP_COMPLETED)
        
        viewModel.progressToNextEvent()
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).endTrip()
    }

    @Test
    fun `progressToNextEvent from TRIP_ENDED should reset simulation`() = runTest {
        rideStateFlow.value = RideState(currentEvent = RideEvent.TRIP_ENDED)
        
        viewModel.progressToNextEvent()
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).resetSimulation()
    }

    @Test
    fun `confirmPickup should start heading to dropoff`() = runTest {
        viewModel.confirmPickup()
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).startHeadingToDropoff()
    }

    @Test
    fun `handlePassengerNoShow should call simulation engine with passenger ID`() = runTest {
        val passengerId = "passenger-123"
        
        viewModel.handlePassengerNoShow(passengerId)
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).handlePassengerNoShow(passengerId)
    }

    @Test
    fun `resetSimulation should call simulation engine reset`() = runTest {
        viewModel.resetSimulation()
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).resetSimulation()
    }

    @Test
    fun `startNewTrip should reset and start new simulation`() = runTest {
        viewModel.startNewTrip()
        testDispatcher.scheduler.advanceUntilIdle()
        
        verify(rideSimulationEngine).resetSimulation()
        verify(rideSimulationEngine).startRideOffer()
    }

    @Test
    fun `state changes should be reflected in rideState flow`() = runTest {
        val newState = RideState(currentEvent = RideEvent.PASSENGERS_ACCEPTED)
        
        viewModel.rideState.test {
                rideStateFlow.value = newState
            
                val emittedState = awaitItem()
            assertEquals(RideEvent.PASSENGERS_ACCEPTED, emittedState.currentEvent)
        }
    }
}