package com.michaelflisar.demo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.themes.StyledThemeAppearance
import com.michaelflisar.composestyled.themes.StyledThemeColors
import com.michaelflisar.composestyled.themes.StyledThemeColorsCollection
import com.michaelflisar.composestyled.themes.StyledThemeContrast
import com.michaelflisar.composestyled.themes.StyledThemeVariant
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdownIndex
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.specials.StyledThemeColorsPicker
import com.michaelflisar.demo.helper.SubTitle

@Composable
fun DemoSetup(
    platforms: List<String>,
    implementations: List<String>,
    themeCollections: List<StyledThemeColorsCollection>,
    selectedPlatformIndex: MutableState<Int>,
    selectedImplementationIndex: MutableState<Int>,
    themeColors: MutableState<StyledThemeColors>,
    themeVariant: MutableState<StyledThemeVariant>,
    themeAppearance: MutableState<StyledThemeAppearance>,
    themeContrast: MutableState<StyledThemeContrast>,
) {
    SubTitle("Platform / Implementation")
    StyledRow(
        itemSpacing = StyledTheme.spacing.medium
    ) {
        StyledPopupDropdownIndex(
            modifier = Modifier.weight(1f),
            items = platforms,
            selectedIndex = selectedPlatformIndex
        )
        StyledPopupDropdownIndex(
            modifier = Modifier.weight(1f),
            items = implementations,
            selectedIndex = selectedImplementationIndex,
        )
    }

    SubTitle("Theme colors")
    StyledThemeColorsPicker(
        themeColors = themeColors,
        themeVariant = themeVariant,
        themeAppearance = themeAppearance,
        themeContrast = themeContrast,
        modifier = Modifier.fillMaxWidth(),
        collections = themeCollections,
    )

    SubTitle("Theme Name")
    StyledText(
        text = "${platforms[selectedPlatformIndex.value]} => ${StyledTheme.themeName}"
    )
}