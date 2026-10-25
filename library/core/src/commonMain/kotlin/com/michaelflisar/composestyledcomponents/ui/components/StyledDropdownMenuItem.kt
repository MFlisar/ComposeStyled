package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry

object StyledDropdownMenuItem {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledDropdownMenuItem")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    class Defaults(
        val minHeight: Dp,
        val itemPadding: PaddingValues,
        val itemShape: Shape,
    )

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        val defaults: Defaults

        @Composable
        fun Render(
            text: @Composable () -> Unit,
            onClick: () -> Unit,
            modifier: Modifier,
            leadingIcon: (@Composable () -> Unit)?,
            trailingIcon: (@Composable () -> Unit)?,
            enabled: Boolean,
            colors: StyledMenuItemColors,
            contentPadding: PaddingValues,
            interactionSource: MutableInteractionSource?,
            shape: Shape,
            itemPadding: PaddingValues,
            minHeight: Dp,
        )
    }
}

@Immutable
data class StyledMenuItemColors(
    val textColor: Color,
    val leadingIconColor: Color,
    val trailingIconColor: Color,
    val disabledTextColor: Color,
    val disabledLeadingIconColor: Color,
    val disabledTrailingIconColor: Color,
)

// -------------------------
// defaults
// -------------------------

object StyledDropdownMenuItemDefaults {
    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledDropdownMenuItem.Key].defaults.itemShape

    val itemPadding: PaddingValues
        @Composable get() = LocalStyledRegistry.current[StyledDropdownMenuItem.Key].defaults.itemPadding

    val minHeight: Dp
        @Composable get() = LocalStyledRegistry.current[StyledDropdownMenuItem.Key].defaults.minHeight

    @Composable
    fun colors(
        textColor: Color = StyledTheme.currentContentColor,
        leadingIconColor: Color = textColor,
        trailingIconColor: Color = textColor,
        disabledTextColor: Color = textColor.copy(
            alpha = textColor.alpha * StyledTheme.colorScheme.disableFactors.content
        ),
        disabledLeadingIconColor: Color = leadingIconColor.copy(
            alpha = leadingIconColor.alpha * StyledTheme.colorScheme.disableFactors.content
        ),
        disabledTrailingIconColor: Color = trailingIconColor.copy(
            alpha = trailingIconColor.alpha * StyledTheme.colorScheme.disableFactors.content
        ),
    ) = StyledMenuItemColors(
        textColor, leadingIconColor, trailingIconColor,
        disabledTextColor, disabledLeadingIconColor, disabledTrailingIconColor,
    )
}

// -------------------------
// public API
// -------------------------

@Composable
fun StyledDropdownMenuItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    colors: StyledMenuItemColors = StyledDropdownMenuItemDefaults.colors(),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = StyledTheme.padding.medium,
        vertical = StyledTheme.padding.small,
    ),
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = StyledDropdownMenuItemDefaults.shape,
    itemPadding: PaddingValues = StyledDropdownMenuItemDefaults.itemPadding,
    minHeight: Dp = StyledDropdownMenuItemDefaults.minHeight,
) {
    LocalStyledRegistry.current[StyledDropdownMenuItem.Key].Render(
        text, onClick, modifier, leadingIcon, trailingIcon, enabled, colors,
        contentPadding, interactionSource, shape, itemPadding, minHeight,
    )
}
