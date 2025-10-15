package com.ben.lincride.domain.model

import org.junit.Test
import org.junit.Assert.*

class RideEarningsTest {

    @Test
    fun `total calculation with positive bonus and commission`() {
        val earnings = RideEarnings(
            baseAmount = 1000.0,
            bonus = 200.0,
            commission = 150.0,
            currency = "₦",
            carbonEmissionAvoided = 1.2
        )

        assertEquals(1050.0, earnings.total, 0.01)
    }

    @Test
    fun `total calculation with no bonus`() {
        val earnings = RideEarnings(
            baseAmount = 1000.0,
            commission = 150.0
        )

        assertEquals(850.0, earnings.total, 0.01)
    }

    @Test
    fun `total calculation with no commission`() {
        val earnings = RideEarnings(
            baseAmount = 1000.0,
            bonus = 200.0
        )

        assertEquals(1200.0, earnings.total, 0.01)
    }

    @Test
    fun `default currency is Nigerian Naira`() {
        val earnings = RideEarnings(baseAmount = 1000.0)

        assertEquals("₦", earnings.currency)
    }

    @Test
    fun `default carbon emission avoided is zero`() {
        val earnings = RideEarnings(baseAmount = 1000.0)

        assertEquals(0.0, earnings.carbonEmissionAvoided, 0.01)
    }

    @Test
    fun `negative commission reduces total`() {
        val earnings = RideEarnings(
            baseAmount = 1000.0,
            commission = 1200.0 // Commission higher than base
        )

        assertEquals(-200.0, earnings.total, 0.01)
    }
}