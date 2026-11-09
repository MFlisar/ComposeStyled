package com.michaelflisar.composestyled.themes.pastel

import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.themes.StyledThemeColorsCollection
import com.michaelflisar.composestyled.themes.createStyledThemeColors

@OptIn(InternalComposeStyledApi::class)
object PastelThemes {

    const val themeId = "pastel"

    val mint = createStyledThemeColors(
        themeId, "mint", "Mint",
        Color(0xFF4D9C85), Color(0xFF78D7B5),
        Color(0xFF6486C2), Color(0xFF9CB8F0),
        Color(0xFFB36B86), Color(0xFFE99AB4),
    )
    val rose = createStyledThemeColors(
        themeId, "rose", "Rose",
        Color(0xFFB45F78), Color(0xFFF29BB0),
        Color(0xFF6686BC), Color(0xFF9DB9EE),
        Color(0xFF9A7652), Color(0xFFD8B98D),
    )
    val lavender = createStyledThemeColors(
        themeId, "lavender", "Lavender",
        Color(0xFF7963B5), Color(0xFFB6A1F0),
        Color(0xFF4D9C85), Color(0xFF78D7B5),
        Color(0xFFB36B86), Color(0xFFE99AB4),
    )
    val peach = createStyledThemeColors(
        themeId, "peach", "Peach",
        Color(0xFFB56645), Color(0xFFF0A47F),
        Color(0xFF6686BC), Color(0xFF9DB9EE),
        Color(0xFF7963B5), Color(0xFFB6A1F0),
    )
    val sky = createStyledThemeColors(
        themeId, "sky", "Sky",
        Color(0xFF4D7FAE), Color(0xFF8FC4F2),
        Color(0xFF4D9C85), Color(0xFF78D7B5),
        Color(0xFFB36B86), Color(0xFFE99AB4),
    )
    val lemon = createStyledThemeColors(
        themeId, "lemon", "Lemon",
        Color(0xFF8A761F), Color(0xFFD9C45D),
        Color(0xFF4D7FAE), Color(0xFF8FC4F2),
        Color(0xFFB56645), Color(0xFFF0A47F),
    )
    val cloud = createStyledThemeColors(
        themeId, "cloud", "Cloud",
        Color(0xFF68727F), Color(0xFFC1C8D1),
        Color(0xFF6F8795), Color(0xFFB8CED8),
        Color(0xFF88796E), Color(0xFFD4C1B5),
    )

    val collection = StyledThemeColorsCollection(
        id = themeId,
        name = "Pastel",
        themes = listOf(mint, rose, lavender, peach, sky, lemon, cloud),
        matchingPlatforms = Platform.entries.toSet(),
    )
}
