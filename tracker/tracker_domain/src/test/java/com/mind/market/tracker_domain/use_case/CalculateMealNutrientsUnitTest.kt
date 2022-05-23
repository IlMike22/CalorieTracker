package com.mind.market.tracker_domain.use_case

import com.google.common.truth.Truth.assertThat
import com.mind.market.core.data.preferences.Preferences
import com.mind.market.core.domain.models.ActivityLevel
import com.mind.market.core.domain.models.Gender
import com.mind.market.core.domain.models.GoalType
import com.mind.market.core.domain.models.UserInfo
import com.mind.market.tracker_domain.model.MealType
import com.mind.market.tracker_domain.model.TrackedFood
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.random.Random

class CalculateMealNutrientsUnitTest {
    private lateinit var calculateMealNutrients: CalculateMealNutrients

    @Before
    fun setup() {
        val preferences = mockk<Preferences>(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 12,
            weight = 65.0f,
            height = 170,
            activityLevel = ActivityLevel.Low,
            goalType = GoalType.GainWeight,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f
        )

        calculateMealNutrients = CalculateMealNutrients(preferences)
    }

    @Test
    fun `Calories for breakfast properly calculated`() {
        val trackedFoods = (1..30).map { iteration ->
            TrackedFood(
                name = "testName",
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }

        val result = calculateMealNutrients(trackedFoods)
        val breakfastCalories = result.mealNutrients.values.filter { mealNutrients ->
            mealNutrients.mealType is MealType.Breakfast
        }.sumOf { mealNutrients ->
            mealNutrients.calories
        }

        val expectedCalories = trackedFoods.filter { mealNutrients ->
            mealNutrients.mealType is MealType.Breakfast
        }.sumOf { mealNutrients ->
            mealNutrients.calories
        }

        assertThat(breakfastCalories).isEqualTo(expectedCalories)
    }

    @Test
    fun `Carbs for dinner properly calculated`() {
        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "testName",
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }

        val result = calculateMealNutrients(trackedFoods)
        val dinnerCarbs = result.mealNutrients.values.filter { mealNutrients ->
            mealNutrients.mealType is MealType.Dinner
        }.sumOf { mealNutrients ->
            mealNutrients.carbs
        }

        val expectedCarbs = trackedFoods.filter { mealNutrients ->
            mealNutrients.mealType is MealType.Dinner
        }.sumOf { mealNutrients ->
            mealNutrients.carbs
        }

        assertThat(dinnerCarbs).isEqualTo(expectedCarbs)
    }
}