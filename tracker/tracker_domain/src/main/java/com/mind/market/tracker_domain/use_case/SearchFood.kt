package com.mind.market.tracker_domain.use_case

import com.mind.market.tracker_domain.model.TrackableFood
import com.mind.market.tracker_domain.repository.ITrackerRepository
import kotlinx.coroutines.flow.Flow

class SearchFood(
    private val repository: ITrackerRepository
) {
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        pageSize: Int = 40
    ): Result<List<TrackableFood>> {
        if (query.isBlank()) {
            return Result.success(emptyList())
        }

        return repository.searchFood(
            query = query.trim(),
            page = page,
            pageSize = pageSize
        )
    }
}