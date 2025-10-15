package com.ben.lincride.domain.model

import com.google.android.gms.maps.model.LatLng
import org.junit.Test
import org.junit.Assert.*

class RideStateTest {

    @Test
    fun `default ride state has idle event`() {
        val rideState = RideState()

        assertEquals(RideEvent.IDLE, rideState.currentEvent)
        assertTrue(rideState.passengers.isEmpty())
        assertNull(rideState.driver)
        assertNull(rideState.route)
        assertFalse(rideState.isSimulating)
    }

    @Test
    fun `ride state with passengers`() {
        val passenger = createTestPassenger()
        val rideState = RideState(
            currentEvent = RideEvent.PASSENGERS_ACCEPTED,
            passengers = listOf(passenger)
        )

        assertEquals(RideEvent.PASSENGERS_ACCEPTED, rideState.currentEvent)
        assertEquals(1, rideState.passengers.size)
        assertEquals("John Doe", rideState.passengers.first().name)
    }

    @Test
    fun `passenger status transitions`() {
        val passenger = createTestPassenger()

        assertEquals(PassengerStatus.ACCEPTED, passenger.status)
    }

    @Test
    fun `ride progress calculations`() {
        val progress = RideProgress(
            currentStep = 2,
            totalSteps = 4,
            progressPercentage = 0.5f,
            timeRemaining = 15,
            distanceRemaining = 5.2f
        )

        assertEquals(2, progress.currentStep)
        assertEquals(4, progress.totalSteps)
        assertEquals(0.5f, progress.progressPercentage, 0.01f)
        assertEquals(15, progress.timeRemaining)
        assertEquals(5.2f, progress.distanceRemaining, 0.01f)
    }

    @Test
    fun `vehicle default seats is 4`() {
        val vehicle = Vehicle(
            id = "vehicle-1",
            type = "Sedan",
            licensePlate = "ABC-123",
            currentLocation = LatLng(6.5244, 3.3792)
        )

        assertEquals(4, vehicle.availableSeats)
    }

    @Test
    fun `route with waypoints`() {
        val startLocation = Location(
            coordinates = LatLng(6.5244, 3.3792),
            address = "Victoria Island, Lagos",
            name = "Start Point"
        )
        val endLocation = Location(
            coordinates = LatLng(6.4281, 3.4219),
            address = "Ikeja, Lagos",
            name = "End Point"
        )
        val waypoint = Location(
            coordinates = LatLng(6.4698, 3.3909),
            address = "Yaba, Lagos",
            name = "Waypoint"
        )

        val route = Route(
            startLocation = startLocation,
            endLocation = endLocation,
            waypoints = listOf(waypoint),
            estimatedDuration = 45,
            estimatedDistance = 15.5f
        )

        assertEquals("Victoria Island, Lagos", route.startLocation.address)
        assertEquals("Ikeja, Lagos", route.endLocation.address)
        assertEquals(1, route.waypoints.size)
        assertEquals("Yaba, Lagos", route.waypoints.first().address)
        assertEquals(45, route.estimatedDuration)
        assertEquals(15.5f, route.estimatedDistance, 0.01f)
    }

    private fun createTestPassenger() = Passenger(
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
    )
}