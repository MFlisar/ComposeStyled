package com.michaelflisar.composestyled.themes.nature

import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.themes.StyledThemeColorsCollection
import com.michaelflisar.composestyled.themes.createStyledThemeColors

@OptIn(InternalComposeStyledApi::class)
object NatureThemes {

    const val themeId = "nature"

    val forest = createStyledThemeColors(
        themeId, "forest", "Forest",
        Color(0xFF356B45), Color(0xFF8BD5A2),
        Color(0xFF397B8C), Color(0xFF82D2E2),
        Color(0xFF80602F), Color(0xFFD8B575),
    )
    val ocean = createStyledThemeColors(
        themeId, "ocean", "Ocean",
        Color(0xFF176B8A), Color(0xFF75C9E8),
        Color(0xFF386A63), Color(0xFF8DD0B7),
        Color(0xFF5A5FA1), Color(0xFFAEB4F5),
    )
    val meadow = createStyledThemeColors(
        themeId, "meadow", "Meadow",
        Color(0xFF56752D), Color(0xFFB2D57D),
        Color(0xFF387568), Color(0xFF82D1B6),
        Color(0xFF91623D), Color(0xFFD9A878),
    )
    val sunset = createStyledThemeColors(
        themeId, "sunset", "Sunset",
        Color(0xFFB04D31), Color(0xFFFFA883),
        Color(0xFF8E4E78), Color(0xFFE8A4D0),
        Color(0xFF88631D), Color(0xFFE2C05A),
    )
    val earth = createStyledThemeColors(
        themeId, "earth", "Earth",
        Color(0xFF795438), Color(0xFFD5A77A),
        Color(0xFF56752D), Color(0xFFB2D57D),
        Color(0xFF176B8A), Color(0xFF75C9E8),
    )
    val blossom = createStyledThemeColors(
        themeId, "blossom", "Blossom",
        Color(0xFFA54B73), Color(0xFFF0A1BB),
        Color(0xFF76579B), Color(0xFFC2A5E8),
        Color(0xFF5C783D), Color(0xFFB6D18A),
    )
    val stone = createStyledThemeColors(
        themeId, "stone", "Stone",
        Color(0xFF68737D), Color(0xFFBDC7D0),
        Color(0xFF84796B), Color(0xFFD1C4B5),
        Color(0xFF6D8475), Color(0xFFBBD0C0),
    )

    val collection = StyledThemeColorsCollection(
        id = themeId,
        name = "Nature",
        themes = listOf(forest, ocean, meadow, sunset, earth, blossom, stone),
        matchingPlatforms = Platform.entries.toSet(),
    )
}
