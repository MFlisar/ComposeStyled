package com.michaelflisar.composestyled.classes

import androidx.compose.runtime.Stable
import com.michaelflisar.composestyled.registry.StyledRegistry

@Stable
data class StyledThemeDefinition(
    val name: String,
    val registry: StyledRegistry,
)