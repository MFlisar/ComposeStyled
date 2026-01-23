package com.michaelflisar.composestyled.demo.styles

import androidx.compose.runtime.Stable
import com.michaelflisar.composestyled.core.classes.StyledThemeDefinition
import com.michaelflisar.composestyled.core.renderer.ExtensionRenderer

@Stable
class ThemeWithExtensions(
    val name: String,
    val shortName: String,
    val theme: StyledThemeDefinition<*>,
    val extensions: List<ExtensionRenderer<*>>,
)