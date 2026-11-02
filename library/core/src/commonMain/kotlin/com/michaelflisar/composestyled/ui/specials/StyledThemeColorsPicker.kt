package com.michaelflisar.composestyled.ui.specials

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.themes.StyledContrastLevel
import com.michaelflisar.composestyled.themes.StyledThemeAppearance
import com.michaelflisar.composestyled.themes.StyledThemeColors
import com.michaelflisar.composestyled.themes.StyledThemeColorsCollection
import com.michaelflisar.composestyled.themes.StyledThemeContrast
import com.michaelflisar.composestyled.themes.StyledThemeVariant
import com.michaelflisar.composestyled.ui.components.StyledDropdown
import com.michaelflisar.composestyled.ui.components.StyledDropdownIndex
import com.michaelflisar.composestyled.ui.components.StyledSegmentedButtonRow
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.primitives.StyledText

/**
 * Lets the user choose theme colors, variant, appearance and contrast.
 * [themeColors] must belong to one of the non-empty [collections].
 */
@Composable
fun StyledThemeColorsPicker(
    themeColors: MutableState<StyledThemeColors>,
    themeVariant: MutableState<StyledThemeVariant>,
    themeAppearance: MutableState<StyledThemeAppearance>,
    themeContrast: MutableState<StyledThemeContrast>,
    modifier: Modifier = Modifier,
    collections: List<StyledThemeColorsCollection>,
) {
    StyledThemeColorsPicker(
        themeColors = themeColors.value,
        themeVariant = themeVariant.value,
        themeAppearance = themeAppearance.value,
        themeContrast = themeContrast.value,
        onThemeColorsChange = { themeColors.value = it },
        onThemeVariantChange = { themeVariant.value = it },
        onThemeAppearanceChange = { themeAppearance.value = it },
        onThemeContrastChange = { themeContrast.value = it },
        modifier = modifier,
        collections = collections,
    )
}

/**
 * Each choice updates only its corresponding value through the matching callback.
 * [themeColors] must belong to one of the non-empty [collections].
 */
@Composable
fun StyledThemeColorsPicker(
    themeColors: StyledThemeColors,
    themeVariant: StyledThemeVariant,
    themeAppearance: StyledThemeAppearance,
    themeContrast: StyledThemeContrast,
    onThemeColorsChange: (StyledThemeColors) -> Unit,
    onThemeVariantChange: (StyledThemeVariant) -> Unit,
    onThemeAppearanceChange: (StyledThemeAppearance) -> Unit,
    onThemeContrastChange: (StyledThemeContrast) -> Unit,
    modifier: Modifier = Modifier,
    collections: List<StyledThemeColorsCollection>,
) {
    require(collections.isNotEmpty()) { "At least one theme-colors collection is required." }
    require(collections.all { it.themes.isNotEmpty() }) {
        "Every theme-colors collection must contain at least one theme."
    }
    val selectedCollectionIndex = collections.indexOfFirst { collection ->
        themeColors in collection.themes
    }
    require(selectedCollectionIndex >= 0) {
        "No theme colors found with id '${themeColors.schemeId}' in the provided collections."
    }
    val selectedCollection = collections[selectedCollectionIndex]
    val selectedThemeIndex = selectedCollection.themes.indexOf(themeColors)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small),
    ) {
        StyledText("Appearance")
        StyledSegmentedButtonRow<StyledThemeAppearance>(
            items = StyledThemeAppearance.entries,
            selected = themeAppearance,
            modifier = Modifier.fillMaxWidth(),
            content = { appearance: StyledThemeAppearance, _: Boolean ->
                StyledText(
                    when (appearance) {
                        StyledThemeAppearance.System -> "System"
                        StyledThemeAppearance.Light -> "Light"
                        StyledThemeAppearance.Dark -> "Dark"
                    },
                )
            },
            onSelectionChange = { _: Int, appearance: StyledThemeAppearance ->
                onThemeAppearanceChange(appearance)
            },
        )

        StyledText("Collection")
        StyledDropdownIndex(
            modifier = Modifier.fillMaxWidth(),
            items = collections.map(StyledThemeColorsCollection::name),
            selectedIndex = selectedCollectionIndex,
            onSelectionChange = { index, _ ->
                onThemeColorsChange(collections[index].themes.first())
            },
        )

        StyledText("Theme colors")
        StyledDropdownIndex(
            modifier = Modifier.fillMaxWidth(),
            items = selectedCollection.themes,
            selectedIndex = selectedThemeIndex,
            content = StyledDropdown.renderContent(
                content = { context ->
                    context.selection?.let { colors ->
                        ThemeColorsOption(colors, themeVariant)
                    }
                },
                dropdownContent = { context ->
                    ThemeColorsOption(context.item, themeVariant)
                },
            ),
            onSelectionChange = { index, _ ->
                onThemeColorsChange(selectedCollection.themes[index])
            },
        )

        StyledText("Variant")
        StyledSegmentedButtonRow<StyledThemeVariant>(
            items = StyledThemeVariant.entries,
            selected = themeVariant,
            modifier = Modifier.fillMaxWidth(),
            content = { variant: StyledThemeVariant, _: Boolean ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    ThemeColorsPreview(themeColors, variant)
                    StyledText(
                        when (variant) {
                            StyledThemeVariant.Monochrome -> "Monochrome"
                            StyledThemeVariant.Colorful -> "Colorful"
                        },
                    )
                }
            },
            onSelectionChange = { _: Int, variant: StyledThemeVariant ->
                onThemeVariantChange(variant)
            },
        )

        StyledText("Contrast")
        StyledDropdownIndex(
            modifier = Modifier.fillMaxWidth(),
            items = StyledThemeContrast.entries.map(StyledThemeContrast::label),
            selectedIndex = StyledThemeContrast.entries.indexOf(themeContrast),
            onSelectionChange = { index, _ ->
                onThemeContrastChange(StyledThemeContrast.entries[index])
            },
        )
    }
}

@Composable
private fun ThemeColorsPreview(themeColors: StyledThemeColors, themeVariant: StyledThemeVariant) {
    val colors = themeColors.resolve(themeVariant, isDark = false, contrast = StyledContrastLevel.Normal)
    Canvas(Modifier.size(16.dp)) {
        val radius = size.minDimension * 0.3f
        drawCircle(
            color = colors.primary,
            radius = radius,
            center = Offset(size.width * 0.35f, size.height * 0.5f),
        )
        drawCircle(
            color = colors.secondary,
            radius = radius,
            center = Offset(size.width * 0.68f, size.height * 0.5f),
        )
    }
}

@Composable
private fun ThemeColorsOption(themeColors: StyledThemeColors, themeVariant: StyledThemeVariant) {
    val colors = themeColors.resolve(themeVariant, isDark = false, contrast = StyledContrastLevel.Normal)
    StyledRow(itemSpacing = StyledTheme.spacing.medium) {
        Canvas(
            Modifier.size(16.dp).border(1.dp, StyledTheme.currentContentColor, CircleShape),
        ) {
            drawCircle(color = colors.primary)
        }
        StyledText(themeColors.name)
    }
}

private val StyledThemeContrast.label: String
    get() = when (this) {
        StyledThemeContrast.System -> "System"
        StyledThemeContrast.Normal -> "Normal"
        StyledThemeContrast.Medium -> "Medium"
        StyledThemeContrast.High -> "High"
    }
