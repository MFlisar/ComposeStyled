package com.michaelflisar.composestyledcomponents.tokens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.classes.StyledLocalMissingException

/**
 * Defines the padding values used throughout the application.
 *
 * @property small The small padding value.
 * @property medium The medium padding value.
 * @property large The large padding value.
 * @property content The padding value for content areas (e.g. scaffolds or root containers).
 */
@Stable
data class StyledPadding(
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val content: PaddingValues
)

internal val LocalStyledPadding = staticCompositionLocalOf<StyledPadding> { throw StyledLocalMissingException("LocalStyledPadding") }
