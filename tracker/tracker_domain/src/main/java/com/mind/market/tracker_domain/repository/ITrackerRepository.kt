package com.mind.market.tracker_domain.repository

import com.mind.market.tracker_domain.model.TrackableFood
import com.mind.market.tracker_domain.model.TrackedFood
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ITrackerRepository {
    suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>>

    suspend fun insertTrackedFood(food: TrackedFood)

    suspend fun deleteTrackedFood(food: TrackedFood)

    fun getFoodsForDate(date: LocalDate): Flow<List<TrackedFood>>

}