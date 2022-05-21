package com.mind.market.tracker_presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.mind.market.core.util.UiEvent
import com.mind.market.core_ui.LocalSpacing
import com.mind.market.tracker_domain.model.MealType
import com.mind.market.tracker_presentation.R
import com.mind.market.tracker_presentation.search.components.SearchTextField
import com.mind.market.tracker_presentation.search.components.TrackableFoodItem
import java.time.LocalDate

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    scaffoldState: ScaffoldState,
    onNavigateUp: () -> Unit,
    mealName: String,
    dayOfMonth: Int,
    month: Int,
    year: Int,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val state = viewModel.state
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = keyboardController) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(uiEvent.message.asString(context))
                    keyboardController?.hide()
                }
                is UiEvent.NavigateUp -> {
                    onNavigateUp()
                }
                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceMedium)
    ) {
        Text(
            text = stringResource(id = R.string.add_meal, mealName),
            style = MaterialTheme.typography.h2
        )

        Spacer(modifier = Modifier.height(spacing.spaceMedium))

        SearchTextField(
            text = state.query,
            onValueChange = { query ->
                viewModel.invoke(SearchEvent.OnQueryChange(query))
            },
            onSearch = {
                keyboardController?.hide()
                viewModel.invoke(SearchEvent.OnSearch)
            },
            onFocusChanged = { focusState ->
                viewModel.invoke(SearchEvent.OnSearchFocusChange(focusState.isFocused))
            },
            isHintVisible = state.isHintVisible
        )

        Spacer(modifier = Modifier.height(spacing.spaceMedium))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.trackableFoods) { trackableFood ->
                TrackableFoodItem(
                    trackableFoodUiState = trackableFood,
                    onClick = {
                        viewModel.invoke(
                            SearchEvent.OnToggleTrackableFood(
                                food = trackableFood.food
                            )
                        )
                    },
                    onAmountChange = { newAmount ->
                        viewModel.invoke(
                            SearchEvent.OnAmountForFoodChange(
                                food = trackableFood.food,
                                amount = newAmount
                            )
                        )
                    },
                    onTrack = {
                        keyboardController?.hide()

                        viewModel.invoke(
                            SearchEvent.OnTrackFoodClick(
                                food = trackableFood.food,
                                mealType = MealType.fromString(mealName),
                                date = LocalDate.of(year, month, dayOfMonth)
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isSearching -> CircularProgressIndicator()
            state.trackableFoods.isEmpty() -> {
                Text(
                    text = stringResource(id = R.string.no_results),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}