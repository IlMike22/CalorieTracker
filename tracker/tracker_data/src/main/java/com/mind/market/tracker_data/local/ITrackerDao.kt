package com.mind.market.tracker_data.local

import androidx.room.*
import com.mind.market.tracker_data.local.entity.TrackedFoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ITrackerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackedFood(food: TrackedFoodEntity)

    @Delete
    suspend fun deleteTrackedFood(food: TrackedFoodEntity)

    @Query(
        """ 
            SELECT *
            FROM trackedfoodentity
            WHERE dayOfMonth = :day AND month = :month AND year = :year
        """
    )
    fun getFoodsForDay(day: Int, month: Int, year: Int): Flow<List<TrackedFoodEntity>>
}