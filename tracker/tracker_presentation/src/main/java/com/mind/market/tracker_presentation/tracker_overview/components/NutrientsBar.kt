package com.mind.market.tracker_presentation.tracker_overview.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import com.mind.market.core_ui.CarbColor
import com.mind.market.core_ui.FatColor
import com.mind.market.core_ui.ProteinColor

@Composable
fun NutrientsBar(
    carbs: Int,
    proteins: Int,
    fat: Int,
    calories: Int,
    caloryGoal: Int,
    modifier: Modifier = Modifier
) {
    val backgroundColor = MaterialTheme.colors.background
    val caloriesExceededColor = MaterialTheme.colors.error
    val carbWidthRatio = remember {
        Animatable(0f)
    }
    val proteinWidthRatio = remember {
        Animatable(0f)
    }
    val fatWidthRatio = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = carbs) {
        carbWidthRatio.animateTo(
            targetValue = (carbs * 4f) / caloryGoal
        )
    }

    LaunchedEffect(key1 = proteins) {
        proteinWidthRatio.animateTo(
            targetValue = (proteins * 4f) / caloryGoal
        )
    }

    LaunchedEffect(key1 = fat) {
        fatWidthRatio.animateTo(
            targetValue = (fat * 9f) / caloryGoal
        )
    }

    Canvas(modifier = modifier) {
        if (calories <= caloryGoal) {
            val carbsWidth = carbWidthRatio.value * size.width
            val proteinWidth = proteinWidthRatio.value * size.width
            val fatWidth = fatWidthRatio.value * size.width

            /**
             * Now drawing the four recs to the canvas.
             * The first one is the whole white one with the complete width.
             * The other three rects are for our three values we want to display.
             */

            drawRoundRect(
                color = backgroundColor,
                size = size,
                cornerRadius = CornerRadius(100f)
            )

            /**
             * since we draw the fat bar all over the other two bars
             * we need to draw the fat bar with the complete size of all
             * the three bars. Therefore width = carbs plus protein plus fat
             * width.
             */
            drawRoundRect(
                color = FatColor,
                size = Size(
                    width = carbsWidth + proteinWidth + fatWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )

            drawRoundRect(
                color = ProteinColor,
                size = Size(
                    width = carbsWidth + proteinWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )

            drawRoundRect(
                color = CarbColor,
                size = Size(
                    width = carbsWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )
        } else {
            drawRoundRect(
                color = caloriesExceededColor,
                size = size,
                cornerRadius = CornerRadius(100f)
            )
        }
    }
}