package com.michaelflisar.demo.demos

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.classes.CustomColors
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.composestyledcomponents.ui.components.StyledCard
import com.michaelflisar.composestyledcomponents.ui.components.StyledCardDefaults
import com.michaelflisar.composestyledcomponents.ui.components.StyledAlternativeCard
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoCard(baseId: Int, expandState: ExpandState) {
    RegionContainer(
        title = "Card",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Card Variants")
        StyledCard.Variant.entries.forEach { variant ->
            CardForVariant(variant, modifier = Modifier.fillMaxWidth()) {
                StyledText("${variant.name} card")
            }
            val clicks = remember { mutableStateOf(0) }
            CardForVariant(
                variant = variant,
                modifier = Modifier.fillMaxWidth(),
                onClick = { clicks.value++ },
            ) {
                StyledText("Clickable ${variant.name} card: ${clicks.value}")
            }
            CardForVariant(
                variant = variant,
                modifier = Modifier.fillMaxWidth(),
                onClick = {},
                enabled = false,
            ) {
                StyledText("Disabled ${variant.name} card")
            }
        }
        SubTitle("Cards with Customization")
        StyledCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 24.dp, top = 8.dp, end = 12.dp, bottom = 16.dp),
        ) {
            StyledText("Asymmetric content padding (start/end follow layout direction)")
        }
        StyledCard(
            modifier = Modifier.fillMaxWidth(),
            shape = StyledTheme.shapes.large,
            customization = StyledCardDefaults.customize(CustomColors.PrimarySurface),
            applyContentPadding = false,
        ) {
            StyledText("Custom padding", Modifier.padding(8.dp))
        }

        StyledAlternativeCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            enabled = false,
        ) {
            StyledText("Disabled clickable card")
        }
    }
}

@Composable
private fun CardForVariant(
        variant: StyledCard.Variant,
        modifier: Modifier = Modifier,
        shape: androidx.compose.ui.graphics.Shape = StyledCardDefaults.shape,
        contentPadding: PaddingValues = StyledCardDefaults.contentPadding,
        customization: StyledCard.Customization? = null,
        onClick: (() -> Unit)? = null,
        enabled: Boolean = true,
        applyContentPadding: Boolean = true,
        content: @Composable () -> Unit,
    ) {
        if (variant == StyledCard.Variant.Standard) {
            StyledCard(
                modifier = modifier,
                shape = shape,
                contentPadding = contentPadding,
                customization = customization,
                onClick = onClick,
                enabled = enabled,
                applyContentPadding = applyContentPadding,
                content = content,
            )
        } else {
            StyledAlternativeCard(
                modifier = modifier,
                shape = shape,
                contentPadding = contentPadding,
                customization = customization,
                onClick = onClick,
                enabled = enabled,
                applyContentPadding = applyContentPadding,
                content = content,
            )
    }
}
