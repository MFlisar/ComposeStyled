package com.michaelflisar.composestyledcomponents.classes

import androidx.compose.runtime.Stable
import com.michaelflisar.composestyledcomponents.registry.StyledRegistry

@Stable
data class StyledThemeDefinition(
    val name: String,
    val registry: StyledRegistry,
)