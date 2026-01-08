package com.michaelflisar.composestyled.core.runtime.interaction

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import com.michaelflisar.composestyled.core.classes.StyledInteractionState
import com.michaelflisar.composestyled.core.classes.StyledResolveState

/**
 * Derives a [StyledInteractionState] from a Compose [InteractionSource].
 *
 * Priority (highest first):
 * Error > Pressed > Focused > Hovered > Normal
 *
 * Notes:
 * - If [interactionSource] is null, the result is based only on [isError].
 * - Disabled is intentionally NOT represented here. Use [StyledResolveState.enabled].
 */
@Composable
fun rememberStyledInteractionState(
    interactionSource: InteractionSource?,
    isError: Boolean = false
): StyledInteractionState {
    val pressed = if (interactionSource != null) {
        val v by interactionSource.collectIsPressedAsState()
        v
    } else false
    val focused = if (interactionSource != null) {
        val v by interactionSource.collectIsFocusedAsState()
        v
    } else false
    val hovered = if (interactionSource != null) {
        val v by interactionSource.collectIsHoveredAsState()
        v
    } else false
    return when {
        isError -> StyledInteractionState.Error
        pressed -> StyledInteractionState.Pressed
        focused -> StyledInteractionState.Focused
        hovered -> StyledInteractionState.Hovered
        else -> StyledInteractionState.Normal
    }
}

/**
 * Convenience helper to build a [StyledResolveState] from an [InteractionSource].
 */
@Composable
fun rememberStyledResolveState(
    interactionSource: InteractionSource?,
    enabled: Boolean,
    isError: Boolean = false
): StyledResolveState {
    return StyledResolveState(
        interaction = rememberStyledInteractionState(interactionSource, isError),
        enabled = enabled
    )
}
