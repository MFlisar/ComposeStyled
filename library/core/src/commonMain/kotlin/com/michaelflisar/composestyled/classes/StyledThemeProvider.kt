package com.michaelflisar.composestyled.classes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.michaelflisar.composestyled.ui.icons.StyledIconSet
import com.michaelflisar.composestyled.themes.StyledThemeColors

/**
 * Supplies the native theme root, color palette, icon set and registered components to `StyledTheme`.
 *
 * Implementations should create their [StyledThemeDefinition] inside [definition] so registrations
 * can be remembered in the provider's composition. Implementations must keep their public
 * properties stable for the lifetime of the provider.
 */
@Stable
interface StyledThemeProvider {

    val id: String

    /** The default theme colors used when an app does not choose a preset. */
    val defaultThemeColors: StyledThemeColors

    /** The default semantic icon set registered in the theme registry. */
    val defaultIconSet: StyledIconSet

    /** Applies the native theme inside the already provided Styled token locals. */
    @Composable
    fun Root(content: @Composable () -> Unit)

    /** Called inside [Root], before the registry and theme name are provided. */
    @Composable
    fun definition(): StyledThemeDefinition
}
