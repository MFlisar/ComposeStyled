package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbarData
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbarDuration
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbarHost
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbarHostState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@InternalComposeStyledApi
class FoundationSnackbarHost : StyledSnackbarHost.Implementation {

    @Composable
    override fun Render(
        hostState: StyledSnackbarHostState,
        modifier: Modifier,
        snackbarPaneTitle: String,
        snackbar: @Composable (StyledSnackbarData) -> Unit,
    ) {
        val data = hostState.currentSnackbarData
        val accessibilityManager = LocalAccessibilityManager.current
        LaunchedEffect(data, accessibilityManager) {
            if (data != null && data.duration != StyledSnackbarDuration.Indefinite) {
                val timeout = when (data.duration) {
                    StyledSnackbarDuration.Short -> 4_000L
                    StyledSnackbarDuration.Long -> 10_000L
                    StyledSnackbarDuration.Indefinite -> error("Indefinite snackbar has no timeout")
                }
                val accessibleTimeout = accessibilityManager?.calculateRecommendedTimeoutMillis(
                    originalTimeoutMillis = timeout,
                    containsIcons = true,
                    containsText = true,
                    containsControls = data.actionLabel != null || data.withDismissAction,
                ) ?: timeout
                delay(accessibleTimeout.milliseconds)
                data.dismiss()
            }
        }
        AnimatedContent(
            targetState = data,
            modifier = modifier,
            transitionSpec = {
                val delay = if (initialState != null) 75 else 0
                val enter = fadeIn(tween(150, delay, LinearEasing)) +
                    scaleIn(tween(150, delay, FastOutSlowInEasing), initialScale = 0.8f)
                val exit = fadeOut(tween(75, easing = LinearEasing)) +
                    scaleOut(tween(75, easing = FastOutSlowInEasing), targetScale = 0.8f)
                (enter togetherWith exit).using(null)
            },
            label = "StyledSnackbar",
        ) { visibleData ->
            if (visibleData != null) {
                Box(
                    Modifier.padding(StyledTheme.padding.content).semantics {
                        liveRegion = LiveRegionMode.Polite
                        paneTitle = snackbarPaneTitle
                        dismiss {
                            visibleData.dismiss()
                            true
                        }
                    }
                ) {
                    snackbar(visibleData)
                }
            }
        }
    }
}
