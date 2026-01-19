package com.michaelflisar.composestyled.demo

import com.michaelflisar.composestyled.components.StyledBanner
import com.michaelflisar.composestyled.components.scaffold.StyledRail
import com.michaelflisar.composestyled.core.classes.ThemeMode
import com.michaelflisar.composestyled.core.tokens.darkStyledColors
import com.michaelflisar.composestyled.core.tokens.lightStyledColors
import com.michaelflisar.composestyled.demo.styles.ThemeWithExtensions
import com.michaelflisar.composestyled.theme.cupertino.StyleCupertino
import com.michaelflisar.composestyled.theme.fluent2.StyleFluent2
import com.michaelflisar.composestyled.theme.material3.StyleMaterial3
import com.michaelflisar.composestyled.theme.wrapper.material3.StyleMaterial3Wrapper

object DemoThemes {

    val themeM3 = ThemeWithExtensions(
        name = "Material3",
        theme = StyleMaterial3,
        extensions = listOf(StyledBanner.Material3, StyledRail.Material3)
    )
    val themeCupertino = ThemeWithExtensions(
        name = "Cupertino",
        theme = StyleCupertino,
        extensions = listOf(StyledBanner.Cupertino, StyledRail.Cupertino)
    )
    val themeFluent2 = ThemeWithExtensions(
        name = "Fluent2",
        theme = StyleFluent2,
        extensions = listOf(StyledBanner.Fluent2, StyledRail.Fluent2)
    )

    val themeM3Wrapper = ThemeWithExtensions(
        name = "Material3 (Wrapper)",
        theme = StyleMaterial3Wrapper,
        extensions = listOf(StyledBanner.Material3, StyledRail.Material3)
    )

    // custom style combining M3 with Cupertino buttons - just for demonstration
    val themeCustom1 = ThemeWithExtensions(
        name = "Custom (M3 + Cupertino Buttons/Extensions)",
        theme = StyleMaterial3.copy(
            components = StyleMaterial3.components.copy(
                button = StyleCupertino.components.button,
            )
        ),
        extensions = listOf(StyledBanner.Cupertino, StyledRail.Cupertino)
    )

    // custom style combining M3 with Fluent2 buttons - just for demonstration
    val themeCustom2 = ThemeWithExtensions(
        name = "Custom (M3 + Fluent2 Buttons/Extensions)",
        theme = StyleMaterial3.copy(
            components = StyleMaterial3.components.copy(
                button = StyleFluent2.components.button,
            )
        ),
        extensions = listOf(StyledBanner.Fluent2, StyledRail.Fluent2)
    )

    val themes = listOf(
        themeM3,
        themeCupertino,
        themeFluent2,
        themeM3Wrapper,
        themeCustom1,
        themeCustom2
    )

    val light = lightStyledColors()
    val dark = darkStyledColors()

    fun selectTheme(
        mode: ThemeMode,
        isDarkMode: Boolean,
    ) = when (mode) {
        ThemeMode.Light -> light
        ThemeMode.Dark -> dark
        ThemeMode.System -> if (isDarkMode) dark else light
    }
}