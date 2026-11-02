package com.michaelflisar.composestyled.themes.fluent2

import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.themes.StyledThemeColorsCollection
import com.michaelflisar.composestyled.themes.createStyledThemeColors

@OptIn(InternalComposeStyledApi::class)
object Fluent2Themes {

    const val themeId = "fluent2"

    val blue = createStyledThemeColors(
        themeId, "blue", "Blue",
        Color(0xFF0078D4), Color(0xFF4CC2FF),
        Color(0xFF008272), Color(0xFF00CC6A),
        Color(0xFF8764B8), Color(0xFFB4A0FF),
    )
    val purple = createStyledThemeColors(
        themeId, "purple", "Purple",
        Color(0xFF5C2D91), Color(0xFFB4A0FF),
        Color(0xFF0078D4), Color(0xFF4CC2FF),
        Color(0xFFCA5010), Color(0xFFFCE100),
    )
    val teal = createStyledThemeColors(
        themeId, "teal", "Teal",
        Color(0xFF006A6A), Color(0xFF4CD7C5),
        Color(0xFF0078D4), Color(0xFF4CC2FF),
        Color(0xFF8E562E), Color(0xFFD7A07A),
    )
    val green = createStyledThemeColors(
        themeId, "green", "Green",
        Color(0xFF107C10), Color(0xFF6CCB5F),
        Color(0xFF0078D4), Color(0xFF4CC2FF),
        Color(0xFF8764B8), Color(0xFFB4A0FF),
    )
    val orange = createStyledThemeColors(
        themeId, "orange", "Orange",
        Color(0xFFC65911), Color(0xFFF7630C),
        Color(0xFF0078D4), Color(0xFF4CC2FF),
        Color(0xFF008272), Color(0xFF00CC6A),
    )
    val rose = createStyledThemeColors(
        themeId, "rose", "Rose",
        Color(0xFFB3265E), Color(0xFFFF99A4),
        Color(0xFF0078D4), Color(0xFF4CC2FF),
        Color(0xFF008272), Color(0xFF00CC6A),
    )

    val collection = StyledThemeColorsCollection(
        id = themeId,
        name = "Fluent 2",
        themes = listOf(blue, purple, teal, green, orange, rose),
        matchingPlatforms = setOf(Platform.JvmWindows),
    )
}
