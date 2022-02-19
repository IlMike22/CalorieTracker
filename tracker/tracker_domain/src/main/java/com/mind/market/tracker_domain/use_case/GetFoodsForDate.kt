package com.mind.market.tracker_domain.use_case

import com.mind.market.tracker_domain.model.TrackedFood
import com.mind.market.tracker_domain.repository.ITrackerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetFoodsForDate(
    private val repository: ITrackerRepository
) {
    operator fun invoke(
        date: LocalDate
    ): Flow<List<TrackedFood>> {
        return repository.getFoodsForDate(date)
    }
}