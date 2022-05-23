package com.plcoding.calorytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mind.market.core.domain.preferences.IPreferences
import com.plcoding.calorytracker.navigation.Route
import com.mind.market.onboarding_presentation.activity_level.ActivityLevelScreen
import com.mind.market.onboarding_presentation.age.AgeScreen
import com.mind.market.onboarding_presentation.gender.GenderScreen
import com.mind.market.onboarding_presentation.goal.GoalScreen
import com.mind.market.onboarding_presentation.height.HeightScreen
import com.mind.market.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.mind.market.onboarding_presentation.weight.WeightScreen
import com.mind.market.onboarding_presentation.welcome.WelcomeScreen
import com.mind.market.tracker_presentation.search.SearchScreen
import com.mind.market.tracker_presentation.tracker_overview.TrackerOverviewScreen
import com.plcoding.calorytracker.ui.theme.CaloryTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var preferences: IPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val showOnboarding = preferences.loadShouldShowOnboarding()

        setContent {
            CaloryTrackerTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = if (showOnboarding) {
                            Route.WELCOME
                        } else Route.TRACKER_OVERVIEW
                    ) {
                        composable(Route.WELCOME) {
                            WelcomeScreen(
                                onNextClick = {
                                    navController.navigate(Route.GENDER)
                                }
                            )
                        }
                        composable(Route.GENDER) {
                            GenderScreen(onNextClick = {
                                navController.navigate(Route.AGE)
                            })
                        }
                        composable(Route.AGE) {
                            AgeScreen(
                                onNextClick = {
                                    navController.navigate(Route.HEIGHT)
                                },
                                scaffoldState = scaffoldState
                            )
                        }
                        composable(Route.HEIGHT) {
                            HeightScreen(
                                onNextClick = {
                                    navController.navigate(Route.WEIGHT)
                                },
                                scaffoldState = scaffoldState
                            )
                        }
                        composable(Route.WEIGHT) {
                            WeightScreen(
                                onNextClick = {
                                    navController.navigate(Route.ACTIVITY)
                                },
                                scaffoldState = scaffoldState
                            )
                        }
                        composable(Route.ACTIVITY) {
                            ActivityLevelScreen(onNextClick = {
                                navController.navigate(Route.GOAL)
                            })
                        }
                        composable(Route.GOAL) {
                            GoalScreen(onNextClick = {
                                navController.navigate(Route.NUTRIENT_GOAL)
                            })
                        }
                        composable(Route.NUTRIENT_GOAL) {
                            NutrientGoalScreen(
                                onNextClick = { navController.navigate(Route.TRACKER_OVERVIEW) },
                                scaffoldState = scaffoldState
                            )
                        }
                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen(
                                onNavigateToSearch = { mealName, day, month, year ->
                                    navController.navigate(
                                        route = Route.SEARCH + "/$mealName/$day/$month/$year"
                                    )
                                }
                            )
                        }
                        composable(
                            route = Route.SEARCH + "/{mealName}/{dayOfMonth}/{month}/{year}",
                            arguments = listOf(
                                navArgument("mealName") {
                                    type = NavType.StringType
                                },
                                navArgument("dayOfMonth") {
                                    type = NavType.IntType
                                },
                                navArgument("month") {
                                    type = NavType.IntType
                                },
                                navArgument("year") {
                                    type = NavType.IntType
                                },
                            )
                        ) {
                            val mealName = it.arguments?.getString("mealName")!!
                            val dayOfMonth = it.arguments?.getInt("dayOfMonth")!!
                            val month = it.arguments?.getInt("month")!!
                            val year = it.arguments?.getInt("year")!!
                            SearchScreen(
                                scaffoldState = scaffoldState,
                                mealName = mealName,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}