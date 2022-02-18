package com.mind.market.onboarding_presentation.nutrient_goal

sealed class NutrientGoalEvent {
    data class OnCarbsRatioEntered(val ratio: String) : NutrientGoalEvent()
    data class OnProteinRatioEntered(val ratio: String) : NutrientGoalEvent()
    data class OnFatRatioEntered(val ratio: String) : NutrientGoalEvent()
    object OnNextClicked:NutrientGoalEvent()
}
