package com.mind.market.onboarding_presentation.activity_level

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.mind.market.core.R
import com.mind.market.core.domain.models.ActivityLevel
import com.mind.market.core.domain.models.Gender
import com.mind.market.core.util.UiEvent
import com.mind.market.core_ui.LocalSpacing
import com.mind.market.onboarding_presentation.components.ActionButton
import com.mind.market.onboarding_presentation.components.SelectableButton
import kotlinx.coroutines.flow.collect

@Composable
fun ActivityLevelScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ActivityLevelViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceLarge)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = stringResource(id = R.string.whats_your_activity_level),
                style = MaterialTheme.typography.h3
            )

            Spacer(Modifier.height(spacing.spaceMedium))

            Row {
                SelectableButton(
                    onClick = {
                        viewModel.onActivityLevelClicked(ActivityLevel.Low)
                    },
                    text = stringResource(id = R.string.low),
                    isSelected = viewModel.selectedActivityLevel == ActivityLevel.Low,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(Modifier.width(spacing.spaceMedium))

                SelectableButton(
                    onClick = {
                        viewModel.onActivityLevelClicked(ActivityLevel.Medium)
                    },
                    text = stringResource(id = R.string.medium),
                    isSelected = viewModel.selectedActivityLevel == ActivityLevel.Medium,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(Modifier.width(spacing.spaceMedium))

                SelectableButton(
                    onClick = {
                        viewModel.onActivityLevelClicked(ActivityLevel.High)
                    },
                    text = stringResource(id = R.string.high),
                    isSelected = viewModel.selectedActivityLevel == ActivityLevel.High,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = viewModel::onNextClicked,
            modifier = Modifier.align(BottomEnd)
        )
    }
}