package com.mind.market.tracker_domain.model

import java.time.LocalDate

data class TrackedFood(
    val name: String,
    val imageUrl: String?,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val mealType: MealType,
    val date: LocalDate,
    val amount: Int,
    val calories: Int,
    val id: Int? = null
)
