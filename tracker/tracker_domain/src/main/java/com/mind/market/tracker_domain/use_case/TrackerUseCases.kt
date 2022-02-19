package com.mind.market.tracker_domain.use_case

data class TrackerUseCases(
    val calculateMealNutrients: CalculateMealNutrients,
    val trackFood: TrackFood,
    val deleteTrackedFood: DeleteTrackedFood,
    val searchFood: SearchFood,
    val getFoodsForDate: GetFoodsForDate
)