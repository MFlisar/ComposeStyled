package com.michaelflisar.composestyled.themes.cupertino

import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.themes.StyledThemeColorsCollection
import com.michaelflisar.composestyled.themes.createStyledThemeColors

@OptIn(InternalComposeStyledApi::class)
object CupertinoThemes {

    const val themeId = "cupertino"

    val blue = createStyledThemeColors(
        themeId, "blue", "Blue",
        Color(0xFF007AFF), Color(0xFF0A84FF),
        Color(0xFF34C759), Color(0xFF30D158),
        Color(0xFFAF52DE), Color(0xFFBF5AF2),
    )
    val green = createStyledThemeColors(
        themeId, "green", "Green",
        Color(0xFF34C759), Color(0xFF30D158),
        Color(0xFF007AFF), Color(0xFF0A84FF),
        Color(0xFFFF9500), Color(0xFFFF9F0A),
    )
    val orange = createStyledThemeColors(
        themeId, "orange", "Orange",
        Color(0xFFFF9500), Color(0xFFFF9F0A),
        Color(0xFF007AFF), Color(0xFF0A84FF),
        Color(0xFFAF52DE), Color(0xFFBF5AF2),
    )
    val pink = createStyledThemeColors(
        themeId, "pink", "Pink",
        Color(0xFFFF2D55), Color(0xFFFF375F),
        Color(0xFF007AFF), Color(0xFF0A84FF),
        Color(0xFF34C759), Color(0xFF30D158),
    )
    val purple = createStyledThemeColors(
        themeId, "purple", "Purple",
        Color(0xFFAF52DE), Color(0xFFBF5AF2),
        Color(0xFF007AFF), Color(0xFF0A84FF),
        Color(0xFFFF9500), Color(0xFFFF9F0A),
    )
    val graphite = createStyledThemeColors(
        themeId, "graphite", "Graphite",
        Color(0xFF636366), Color(0xFF98989D),
        Color(0xFF007AFF), Color(0xFF0A84FF),
        Color(0xFF34C759), Color(0xFF30D158),
    )

    val collection = StyledThemeColorsCollection(
        id = themeId,
        name = "Cupertino",
        themes = listOf(blue, green, orange, pink, purple, graphite),
        matchingPlatforms = setOf(Platform.IOS),
    )
}
