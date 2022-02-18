package com.mind.market.tracker_data.mapper

import com.mind.market.tracker_data.remote.dto.Product
import com.mind.market.tracker_domain.model.TrackableFood
import kotlin.math.roundToInt

fun Product.toTrackableFood(): TrackableFood? {
    val carbsPer100g: Int = nutriments.carbohydrates100g.roundToInt()
    val fatPer100g: Int = nutriments.fat100g.roundToInt()
    val proteinPer100g: Int = nutriments.proteins100g.roundToInt()
    val caloriesPer100g: Int = nutriments.energyKcal100g.roundToInt()
    return TrackableFood(
        name = productName ?: return null,
        imageUrl = imageFrontThumbUrl,
        carbsPer100g = carbsPer100g,
        fatPer100g = fatPer100g,
        proteinsPer100g = proteinPer100g,
        caloriesPer100g = caloriesPer100g
    )
}