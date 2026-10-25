package com.michaelflisar.composestyledcomponents.classes

import androidx.compose.runtime.Composable

interface StyledThemeProvider {

    /** Applies the native theme inside the already provided Styled token locals. */
    @Composable
    fun Root(content: @Composable () -> Unit)

    /** Called inside [Root], before the registry and theme name are provided. */
    @Composable
    fun definition(): StyledThemeDefinition
}
