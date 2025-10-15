package com.ben.lincride.core.data.repository

import com.ben.lincride.core.common.result.Result
import com.ben.lincride.core.data.model.RideSession
import com.ben.lincride.core.data.model.RouteProgress
import kotlinx.coroutines.flow.Flow

interface RideRepository {
    fun getCurrentRideSession(): Flow<Result<RideSession>>
    suspend fun startRideSession(): Result<RideSession>
    suspend fun updateRouteProgress(progress: RouteProgress): Result<Unit>
    suspend fun completePickup(): Result<Unit>
    suspend fun completeTrip(): Result<Unit>
    suspend fun resetSession(): Result<Unit>
}