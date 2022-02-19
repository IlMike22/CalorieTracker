package com.mind.market.tracker_domain.di

import com.mind.market.core.domain.preferences.IPreferences
import com.mind.market.tracker_domain.repository.ITrackerRepository
import com.mind.market.tracker_domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TrackerDomainModule {
    @Provides
    @ViewModelScoped
    fun provideTrackerUseCases(
        trackerRepository: ITrackerRepository,
        preferences: IPreferences
    ): TrackerUseCases {
        return TrackerUseCases(
            calculateMealNutrients = CalculateMealNutrients(preferences),
            trackFood = TrackFood(trackerRepository),
            deleteTrackedFood = DeleteTrackedFood(trackerRepository),
            searchFood = SearchFood(trackerRepository),
            getFoodsForDate = GetFoodsForDate(trackerRepository)
        )
    }
}