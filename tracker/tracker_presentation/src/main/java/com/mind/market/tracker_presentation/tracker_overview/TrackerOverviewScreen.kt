package com.mind.market.tracker_presentation.tracker_overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.mind.market.core.util.UiEvent
import com.mind.market.core_ui.LocalSpacing
import com.mind.market.tracker_presentation.R
import com.mind.market.tracker_presentation.tracker_overview.components.*

@Composable
fun TrackerOverviewScreen(
    onNavigateToSearch: (String, Int, Int, Int) -> Unit,
    viewModel: TrackerOverviewViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = spacing.spaceMedium)
    ) {
        item {
            NutrientsHeader(state = state)

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            TrackerDaySelector(
                date = state.date,
                onPreviousDayClick = { viewModel.onEvent(TrackerOverviewEvent.OnPreviousDayClick) },
                onNextDayClick = { viewModel.onEvent(TrackerOverviewEvent.OnNextDayClick) },
                modifier = Modifier
                    .padding(spacing.spaceMedium)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))
        }

        items(state.meals) { meal ->
            ExpendableMeal(
                meal = meal,
                onToggleClick = {
                    viewModel.onEvent(TrackerOverviewEvent.OnToggleMealClick(meal))
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spacing.spaceSmall)
                    ) {
                        state.trackedFoods.filter { trackedFood ->
                            trackedFood.mealType == meal.mealType
                        }.forEach { food ->
                            TrackedFoodItem(
                                onDeleteClick = {
                                    viewModel.onEvent(
                                        TrackerOverviewEvent.OnDeleteTrackedFoodClick(
                                            trackedFood = food
                                        )
                                    )
                                },
                                trackedFood = food
                            )

                            Spacer(Modifier.height(spacing.spaceMedium))
                        }

                        AddButton(
                            text = stringResource(
                                id = R.string.add_meal,
                                meal.name.asString(context)
                            ),
                            onClick = {
                                onNavigateToSearch(
                                    meal.name.asString(context),
                                    state.date.dayOfMonth,
                                    state.date.monthValue,
                                    state.date.year
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },

                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}