package com.ben.lincride.core.network.datasource

import com.ben.lincride.core.common.dispatcher.Dispatcher
import com.ben.lincride.core.common.dispatcher.LincRideDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Simulates network operations for the ride-sharing app.
 * In a real app, this would be replaced with actual API calls.
 */
@Singleton
class SimulationDataSource @Inject constructor(
    @Dispatcher(LincRideDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {
    
    suspend fun simulateNetworkDelay(delayMs: Long = 1000): Unit = withContext(ioDispatcher) {
        delay(delayMs)
    }
    
    suspend fun simulateApiCall(): Boolean = withContext(ioDispatcher) {
        delay(500) // Simulate network latency
        true // Simulate successful API response
    }
}