package com.mind.market.tracker_data.repository

import com.mind.market.tracker_data.local.ITrackerDao
import com.mind.market.tracker_data.mapper.toTrackableFood
import com.mind.market.tracker_data.mapper.toTrackedFood
import com.mind.market.tracker_data.mapper.toTrackedFoodEntity
import com.mind.market.tracker_data.remote.IOpenFoodApi
import com.mind.market.tracker_domain.model.TrackableFood
import com.mind.market.tracker_domain.model.TrackedFood
import com.mind.market.tracker_domain.repository.ITrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TrackerRepository(
    val api: IOpenFoodApi,
    val dao: ITrackerDao
) : ITrackerRepository {
    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return try {
            val searchDto = api.searchFood(
                query = query,
                page = page,
                pageSize = pageSize
            )
            Result.success(searchDto.products
                .filter { product ->
                    val totalCalories = product.nutriments.carbohydrates100g * 4f
                    +product.nutriments.proteins100g * 4f
                    +product.nutriments.fat100g * 9f

                    val lowerBound = totalCalories * 0.99f
                    val upperBound = totalCalories * 1.01f

                    product.nutriments.energyKcal100g in (lowerBound..upperBound)
                }
                .mapNotNull { product ->
                    product.toTrackableFood()
                })
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure(exception)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        dao.insertTrackedFood(food.toTrackedFoodEntity())
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        dao.deleteTrackedFood(food.toTrackedFoodEntity())
    }

    override fun getFoodsForDate(date: LocalDate): Flow<List<TrackedFood>> {
        return dao.getFoodsForDay(
            day = date.dayOfMonth,
            month = date.monthValue,
            year = date.year
        ).map { trackedFoodEntities ->
            trackedFoodEntities.map { trackedFoodEntity ->
                trackedFoodEntity.toTrackedFood()
            }
        }
    }
}