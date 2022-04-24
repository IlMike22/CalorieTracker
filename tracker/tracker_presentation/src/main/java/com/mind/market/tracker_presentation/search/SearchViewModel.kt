package com.mind.market.tracker_presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.core.domain.use_case.FilterOutDigits
import com.mind.market.core.util.UiEvent
import com.mind.market.core.util.UiText
import com.mind.market.tracker_domain.use_case.TrackerUseCases
import com.mind.market.tracker_presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases,
    private val filterOutDigits: FilterOutDigits
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun invoke(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> {
                state = state.copy(
                    query = event.query
                )
            }
            is SearchEvent.OnAmountForFoodChange -> {
                state = state.copy(
                    trackableFoods = state.trackableFoods.map { uiState ->
                        if (uiState.trackableFood == event.food) {
                            uiState.copy(
                                amount = filterOutDigits(event.amount)
                            )
                        } else {
                            uiState
                        }
                    }
                )

            }
            is SearchEvent.OnSearch -> {
                executeSearch(event)
            }
            is SearchEvent.OnSearchFocusChange -> {
                state = state.copy(
                    isHintVisible = !event.isFocused && state.query.isBlank()
                )
            }
            is SearchEvent.OnTrackFoodClick -> {
                trackFood(event)
            }
            is SearchEvent.OnToggleTrackableFood -> {
                state = state.copy(
                    trackableFoods = state.trackableFoods.map { uiState ->
                        if (uiState.trackableFood == event.food) {
                            uiState.copy(
                                isExpanded = !uiState.isExpanded
                            )
                        } else {
                            uiState
                        }
                    }
                )
            }
        }
    }

    private fun executeSearch(event: SearchEvent.OnSearch) {
        state = state.copy(
            isSearching = true,
            trackableFoods = emptyList()
        )
        viewModelScope.launch {
            trackerUseCases.searchFood(
                query = state.query,
                page = 1,
                pageSize = 30
            )
                .onSuccess { trackableFoods ->
                    state = state.copy(
                        isSearching = false,
                        trackableFoods = trackableFoods.map { food ->
                            TrackableFoodUiState(food)
                        },
                        query = ""
                    )
                }
                .onFailure { error ->
                    state = state.copy(isSearching = false)
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(R.string.error_something_went_wrong)
                        )
                    )
                }
        }
    }

    private fun trackFood(event: SearchEvent.OnTrackFoodClick) {
        viewModelScope.launch {
            val uiState = state.trackableFoods.find { uiState ->
                uiState.trackableFood == event.food
            }

            trackerUseCases.trackFood(
                food = uiState?.trackableFood ?: return@launch,
                amount = uiState.amount.toIntOrNull() ?: return@launch,
                mealType = event.mealType,
                date = event.date
            )

            _uiEvent.send(UiEvent.NavigateUp)
        }
    }
}