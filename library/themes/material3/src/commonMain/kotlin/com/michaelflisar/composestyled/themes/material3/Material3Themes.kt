package com.michaelflisar.composestyled.themes.material3

import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.themes.StyledThemeColorsCollection
import com.michaelflisar.composestyled.themes.createStyledThemeColors

@OptIn(InternalComposeStyledApi::class)
object Material3Themes {

    val blue = createStyledThemeColors(
        "blue", "Blue",
        Color(0xFF415F91), Color(0xFFAAC7FF),
        Color(0xFF006A60), Color(0xFF4CD7C5),
        Color(0xFF705575), Color(0xFFE7B8ED),
    )
    val violet = createStyledThemeColors(
        "violet", "Violet",
        Color(0xFF6750A4), Color(0xFFD0BCFF),
        Color(0xFF006A60), Color(0xFF4CD7C5),
        Color(0xFF7E5260), Color(0xFFF0B8C2),
    )
    val green = createStyledThemeColors(
        "green", "Green",
        Color(0xFF386A20), Color(0xFFA1D18A),
        Color(0xFF006A60), Color(0xFF4CD7C5),
        Color(0xFF5B5D8A), Color(0xFFC4C3FF),
    )
    val rose = createStyledThemeColors(
        "rose", "Rose",
        Color(0xFF904A46), Color(0xFFFFB4AB),
        Color(0xFF5B5D8A), Color(0xFFC4C3FF),
        Color(0xFF006A60), Color(0xFF4CD7C5),
    )
    val amber = createStyledThemeColors(
        "amber", "Amber",
        Color(0xFF775A00), Color(0xFFE9C349),
        Color(0xFF415F91), Color(0xFFAAC7FF),
        Color(0xFF006A60), Color(0xFF4CD7C5),
    )
    val teal = createStyledThemeColors(
        "teal", "Teal",
        Color(0xFF006A60), Color(0xFF4CD7C5),
        Color(0xFF415F91), Color(0xFFAAC7FF),
        Color(0xFF775A00), Color(0xFFE9C349),
    )

    val collection = StyledThemeColorsCollection(
        id = "material3",
        name = "Material 3",
        themes = listOf(blue, violet, green, rose, amber, teal),
        matchingPlatforms = setOf(Platform.Android),
    )
}
