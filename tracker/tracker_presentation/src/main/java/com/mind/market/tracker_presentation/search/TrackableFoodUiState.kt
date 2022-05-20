package com.mind.market.tracker_presentation.search

import com.mind.market.tracker_domain.model.TrackableFood

data class TrackableFoodUiState(
    val food: TrackableFood,
    val amount: String = "",
    val isExpanded: Boolean = false
) {
}