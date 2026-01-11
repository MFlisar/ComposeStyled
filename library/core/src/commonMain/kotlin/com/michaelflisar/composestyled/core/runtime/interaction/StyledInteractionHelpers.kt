package com.michaelflisar.composestyled.core.runtime.interaction

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.input.InputModeManager
import androidx.compose.ui.platform.LocalInputModeManager
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
internal fun rememberStyledInteractionState(
    interactionSource: InteractionSource?,
    isError: Boolean = false,
): StyledInteractionState {
    val isKeyboard = LocalInputModeManager.current.inputMode == InputMode.Keyboard
    val pressed = interactionSource?.collectIsPressedAsState()?.value ?: false
    val focused = interactionSource?.collectIsFocusedAsState()?.value ?: false
    val hovered = interactionSource?.collectIsHoveredAsState()?.value ?: false
    return when {
        isError -> StyledInteractionState.Error
        pressed -> StyledInteractionState.Pressed
        isKeyboard && focused -> StyledInteractionState.Focused
        hovered -> StyledInteractionState.Hovered
        else -> StyledInteractionState.Normal
    }
}

/**
 * Convenience helper to build a [StyledResolveState] from an [InteractionSource].
 */
@Composable
internal fun rememberStyledResolveState(
    interactionSource: InteractionSource?,
    enabled: Boolean,
    isError: Boolean = false,
): StyledResolveState {
    return StyledResolveState(
        interaction = rememberStyledInteractionState(interactionSource, isError),
        enabled = enabled
    )
}