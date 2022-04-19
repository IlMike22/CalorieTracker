package com.mind.market.core.data.preferences

import android.content.SharedPreferences
import com.mind.market.core.domain.models.ActivityLevel
import com.mind.market.core.domain.models.Gender
import com.mind.market.core.domain.models.GoalType
import com.mind.market.core.domain.models.UserInfo
import com.mind.market.core.domain.preferences.IPreferences

class Preferences(
    private val sharedPref: SharedPreferences
) : IPreferences {
    override fun saveGender(gender: Gender) {
        sharedPref.edit()
            .putString(IPreferences.KEY_GENDER, gender.name)
            .apply()
    }

    override fun saveAge(age: Int) {
        sharedPref.edit()
            .putInt(IPreferences.KEY_AGE, age)
            .apply()
    }

    override fun saveWeight(weight: Float) {
        sharedPref.edit()
            .putFloat(IPreferences.KEY_WEIGHT, weight)
            .apply()
    }

    override fun saveHeight(height: Int) {
        sharedPref.edit()
            .putInt(IPreferences.KEY_HEIGHT, height)
            .apply()
    }

    override fun saveActivityLevel(level: ActivityLevel) {
        sharedPref.edit()
            .putString(IPreferences.KEY_ACTIVITY_LEVEL, level.name)
            .apply()
    }

    override fun saveGoalType(type: GoalType) {
        sharedPref.edit()
            .putString(IPreferences.KEY_GOAL_TYPE, type.name)
            .apply()
    }

    override fun saveCarbRatio(ratio: Float) {
        sharedPref.edit()
            .putFloat(IPreferences.KEY_CARB_RATIO, ratio)
            .apply()
    }

    override fun saveProteinRatio(ratio: Float) {
        sharedPref.edit()
            .putFloat(IPreferences.KEY_PROTEIN_RATIO, ratio)
            .apply()
    }

    override fun saveFatRatio(ratio: Float) {
        sharedPref.edit()
            .putFloat(IPreferences.KEY_FAT_RATIO, ratio)
            .apply()
    }

    override fun loadUserInfo(): UserInfo {
        val age = sharedPref.getInt(IPreferences.KEY_AGE, -1)
        val weight = sharedPref.getFloat(IPreferences.KEY_WEIGHT, -1f)
        val height = sharedPref.getInt(IPreferences.KEY_HEIGHT, -1)
        val gender = sharedPref.getString(IPreferences.KEY_GENDER, null)
        val activityLevel = sharedPref.getString(IPreferences.KEY_ACTIVITY_LEVEL, null)
        val goalType = sharedPref.getString(IPreferences.KEY_GOAL_TYPE, null)
        val proteinRatio = sharedPref.getFloat(IPreferences.KEY_PROTEIN_RATIO, -1f)
        val carbRatio = sharedPref.getFloat(IPreferences.KEY_CARB_RATIO, -1f)
        val fatRatio = sharedPref.getFloat(IPreferences.KEY_FAT_RATIO, -1f)

        return UserInfo(
            gender = Gender.fromString(gender ?: "male"),
            age = age,
            weight = weight,
            height = height,
            activityLevel = ActivityLevel.fromString(activityLevel ?: "medium"),
            goalType = GoalType.fromString(goalType ?: "keep_weight"),
            proteinRatio = proteinRatio,
            carbRatio = carbRatio,
            fatRatio = fatRatio
        )
    }

    override fun saveShouldShowOnboarding(showOnboarding: Boolean) {
        sharedPref.edit()
            .putBoolean(IPreferences.KEY_SHOULD_SHOW_ONBOARDING, showOnboarding)
            .apply()
    }

    override fun loadShouldShowOnboarding(): Boolean =
        sharedPref.getBoolean(IPreferences.KEY_SHOULD_SHOW_ONBOARDING, true)

}