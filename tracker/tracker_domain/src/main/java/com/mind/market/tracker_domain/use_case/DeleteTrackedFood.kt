package com.mind.market.tracker_domain.use_case

import com.mind.market.tracker_domain.model.TrackedFood
import com.mind.market.tracker_domain.repository.ITrackerRepository

class DeleteTrackedFood(
    private val repository: ITrackerRepository
) {
    suspend operator fun invoke(
        food: TrackedFood
    ) { return repository.deleteTrackedFood(food) }
}