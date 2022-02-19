package com.mind.market.tracker_domain.use_case

import com.mind.market.tracker_domain.model.MealType
import com.mind.market.tracker_domain.model.TrackableFood
import com.mind.market.tracker_domain.model.TrackedFood
import com.mind.market.tracker_domain.repository.ITrackerRepository
import java.time.LocalDate
import kotlin.math.roundToInt

class TrackFood(
    private val repository: ITrackerRepository
) {
    suspend operator fun invoke(
        food: TrackableFood,
        amount: Int,
        mealType: MealType,
        date: LocalDate
    ) {
        return repository.insertTrackedFood(
            food = TrackedFood(
                name = food.name,
                imageUrl = food.imageUrl,
                protein =  ((food.proteinsPer100g / 100f) * amount).roundToInt(),
                carbs = ((food.carbsPer100g / 100f) * amount).roundToInt(),
                fat =  ((food.fatPer100g / 100f) * amount).roundToInt(),
                mealType = mealType,
                date = date,
                amount = amount,
                calories =  ((food.caloriesPer100g / 100f) * amount).roundToInt(),
            )
        )
    }
}