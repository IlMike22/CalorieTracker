package com.mind.market.tracker_data.mapper

import com.mind.market.tracker_data.local.entity.TrackedFoodEntity
import com.mind.market.tracker_domain.model.MealType
import com.mind.market.tracker_domain.model.TrackedFood
import java.time.LocalDate

fun TrackedFoodEntity.toTrackedFood(): TrackedFood {
    return TrackedFood(
        name = name,
        imageUrl = imageUrl,
        protein = protein,
        carbs = carbs,
        fat = fat,
        mealType = MealType.fromString(type),
        date = convertToLocalDate(dayOfMonth, month, year),
        amount = amount,
        calories = calories,
        id = id
    )
}

fun TrackedFood.toTrackedFoodEntity(): TrackedFoodEntity {
    return TrackedFoodEntity(
        name = name,
        imageUrl = imageUrl,
        amount = amount,
        calories = calories,
        protein = protein,
        carbs = carbs,
        fat = fat,
        type = mealType.name,
        dayOfMonth = date.dayOfMonth,
        month = date.monthValue,
        year = date.year,
        id = id
    )
}

fun convertToLocalDate(day: Int, month: Int, year: Int): LocalDate = LocalDate.of(year, month, day)
