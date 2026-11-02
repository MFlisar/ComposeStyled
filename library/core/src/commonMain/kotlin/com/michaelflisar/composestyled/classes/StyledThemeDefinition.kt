package com.michaelflisar.composestyled.classes

import androidx.compose.runtime.Stable
import com.michaelflisar.composestyled.registry.StyledRegistry

/** The resolved theme identity and component registry used by [StyledThemeProvider]. */
@Stable
data class StyledThemeDefinition(
    val name: String,
    val registry: StyledRegistry,
)