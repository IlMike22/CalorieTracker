package com.mind.market.tracker_domain.use_case

import com.mind.market.core.data.preferences.Preferences
import com.mind.market.core.domain.models.ActivityLevel
import com.mind.market.core.domain.models.Gender
import com.mind.market.core.domain.models.GoalType
import com.mind.market.core.domain.models.UserInfo
import com.mind.market.core.domain.preferences.IPreferences
import com.mind.market.tracker_domain.model.MealType
import com.mind.market.tracker_domain.model.TrackedFood
import kotlin.math.roundToInt

class CalculateMealNutrients(
    val preferences: IPreferences
) {
    operator fun invoke(trackedFoods: List<TrackedFood>): Result {
        val allNutrients = trackedFoods.groupBy { trackedFood ->
            trackedFood.mealType
        }.mapValues { entry ->
            val mealType = entry.key
            val trackedFoods = entry.value
            MealNutrients(
                carbs = trackedFoods.sumOf { trackedFood -> trackedFood.carbs },
                protein = trackedFoods.sumOf { trackedFood -> trackedFood.protein },
                fat = trackedFoods.sumOf { trackedFood -> trackedFood.fat },
                calories = trackedFoods.sumOf { trackedFood -> trackedFood.calories },
                mealType = mealType
            )
        }
        val totalCarbs = allNutrients.values.sumOf { mealNutrients -> mealNutrients.carbs }
        val totalProtein = allNutrients.values.sumOf { mealNutrients -> mealNutrients.protein }
        val totalFat = allNutrients.values.sumOf { mealNutrients -> mealNutrients.fat }
        val totalCalories = allNutrients.values.sumOf { mealNutrients -> mealNutrients.calories }

        val userInfo = preferences.loadUserInfo()

        val caloriesGoal = dailyCaloryRequirement(userInfo)
        val carbsGoal = ((caloriesGoal * userInfo.carbRatio) / 4f).roundToInt()
        val proteinGoal = ((caloriesGoal * userInfo.proteinRatio) / 4f).roundToInt()
        val fatGoal = ((caloriesGoal * userInfo.fatRatio) / 9f).roundToInt()

        return Result(
            carbsGoal = carbsGoal,
            proteinGoal = proteinGoal,
            fatGoal = fatGoal,
            caloriesGoal = caloriesGoal,
            totalCarbs = totalCarbs,
            totalFat = totalFat,
            totalProtein = totalProtein,
            totalCalories = totalCalories,
            mealNutrients = allNutrients
        )
    }

    private fun bmr(userInfo: UserInfo): Int {
        return when (userInfo.gender) {
            is Gender.Male -> {
                (66.47f + 13.75f * userInfo.weight +
                        5f * userInfo.height - 6.75f * userInfo.age).roundToInt()
            }
            is Gender.Female -> {
                (665.09f + 9.56f * userInfo.weight +
                        1.84f * userInfo.height - 4.67 * userInfo.age).roundToInt()
            }
        }
    }

    private fun dailyCaloryRequirement(userInfo: UserInfo): Int {
        val activityFactor = when (userInfo.activityLevel) {
            is ActivityLevel.Low -> 1.2f
            is ActivityLevel.Medium -> 1.3f
            is ActivityLevel.High -> 1.4f
        }
        val caloryExtra = when (userInfo.goalType) {
            is GoalType.LoseWeight -> -500
            is GoalType.KeepWeight -> 0
            is GoalType.GainWeight -> 500
        }
        return (bmr(userInfo) * activityFactor + caloryExtra).roundToInt()
    }

    data class MealNutrients(
        val carbs: Int,
        val protein: Int,
        val fat: Int,
        val calories: Int,
        val mealType: MealType
    )

    data class Result(
        val carbsGoal: Int,
        val proteinGoal: Int,
        val fatGoal: Int,
        val caloriesGoal: Int,
        val totalCarbs: Int,
        val totalFat: Int,
        val totalProtein: Int,
        val totalCalories: Int,
        val mealNutrients: Map<MealType, MealNutrients>
    )
}