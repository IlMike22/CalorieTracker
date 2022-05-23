package com.mind.market.tracker_presentation.tracker_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.core.domain.preferences.IPreferences
import com.mind.market.core.util.UiEvent
import com.mind.market.tracker_domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackerOverviewViewModel @Inject constructor(
    preferences: IPreferences,
    private val trackerUseCases: TrackerUseCases
) : ViewModel() {

    private var getFoodsForDateJob: Job? = null

    var state by mutableStateOf(TrackerOverviewState())
        private set

    private val _uiEvent = Channel<UiEvent> { }
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        refreshFoods()
        preferences.saveShouldShowOnboarding(false)
    }

    fun onEvent(event: TrackerOverviewEvent) {
        when (event) {
            is TrackerOverviewEvent.OnNextDayClick -> {
                state = state.copy(
                    date = state.date.plusDays(1)
                )

                refreshFoods()
            }
            is TrackerOverviewEvent.OnPreviousDayClick -> {
                state = state.copy(
                    date = state.date.minusDays(1)
                )

                refreshFoods()
            }
            is TrackerOverviewEvent.OnDeleteTrackedFoodClick -> {
                viewModelScope.launch {
                    trackerUseCases.deleteTrackedFood(event.trackedFood)
                    refreshFoods()
                }
            }
            is TrackerOverviewEvent.OnToggleMealClick -> {
                state = state.copy(
                    meals = state.meals.map { meal ->
                        if (meal.name == event.meal.name) {
                            meal.copy(
                                isExpanded = !meal.isExpanded
                            )
                        } else meal
                    }
                )
            }
        }
    }

    /**
     * Update the state since if something has changed (eg deleted some food) we need to
     * get the new (updated) list of food and set our state with the new data.
     */
    private fun refreshFoods() {
        getFoodsForDateJob?.cancel()

        getFoodsForDateJob = trackerUseCases
            .getFoodsForDate(state.date)
            .onEach { foods ->
                val nutrientsResult = trackerUseCases.calculateMealNutrients(foods)
                state = state.copy(
                    totalCarbs = nutrientsResult.totalCarbs,
                    totalProtein = nutrientsResult.totalProtein,
                    totalCalories = nutrientsResult.totalCalories,
                    totalFat = nutrientsResult.totalFat,
                    carbsGoal = nutrientsResult.carbsGoal,
                    proteinGoal = nutrientsResult.proteinGoal,
                    fatGoal = nutrientsResult.fatGoal,
                    caloriesGoal = nutrientsResult.caloriesGoal,
                    trackedFoods = foods,
                    meals = state.meals.map { meal ->
                        val nutrientsMeal = nutrientsResult.mealNutrients[meal.mealType]
                            ?: return@map meal.copy(
                                carbs = 0,
                                fat = 0,
                                calories = 0,
                                protein = 0
                            )
                        meal.copy(
                            carbs = nutrientsMeal.carbs,
                            fat = nutrientsMeal.fat,
                            protein = nutrientsMeal.protein,
                            calories = nutrientsMeal.calories
                        )
                    }
                )
            }
            .launchIn(viewModelScope)
    }
}