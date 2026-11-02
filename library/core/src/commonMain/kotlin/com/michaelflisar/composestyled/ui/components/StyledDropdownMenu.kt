package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.primitives.StyledHorizontalDivider

object StyledDropdownMenuItem {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledDropdownMenuItem")
    val DefaultsKey = StyledKey<Defaults>("StyledDropdownMenuItemDefaults")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    data class Defaults(
        val minHeight: Dp,
        val itemPadding: PaddingValues,
        val itemShape: Shape,
    )

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation>,
        StyledComponentWithDefaults<Defaults> {
        override val key: StyledKey<Implementation>
            get() = Key

        override val defaultsKey: StyledKey<Defaults>
            get() = DefaultsKey

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

        @Composable
        fun Render(
            content: @Composable RowScope.() -> Unit,
            onClick: (() -> Unit)?,
            modifier: Modifier,
            enabled: Boolean,
            colors: StyledMenuItemColors,
            contentPadding: PaddingValues,
            interactionSource: MutableInteractionSource?,
            shape: Shape,
            itemPadding: PaddingValues,
            minHeight: Dp,
        ) {
            val effectiveEnabled = enabled && onClick != null
            val renderColors = if (enabled && onClick == null) {
                colors.copy(
                    disabledTextColor = colors.textColor,
                    disabledLeadingIconColor = colors.leadingIconColor,
                    disabledTrailingIconColor = colors.trailingIconColor,
                )
            } else {
                colors
            }
            Render(
                text = { Row(content = content) },
                onClick = onClick ?: {},
                modifier = modifier,
                leadingIcon = null,
                trailingIcon = null,
                enabled = effectiveEnabled,
                colors = renderColors,
                contentPadding = contentPadding,
                interactionSource = interactionSource,
                shape = shape,
                itemPadding = itemPadding,
                minHeight = minHeight,
            )
        }
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
        @Composable get() = LocalStyledRegistry.current[StyledDropdownMenuItem.DefaultsKey].itemShape

    val itemPadding: PaddingValues
        @Composable get() = LocalStyledRegistry.current[StyledDropdownMenuItem.DefaultsKey].itemPadding

    val minHeight: Dp
        @Composable get() = LocalStyledRegistry.current[StyledDropdownMenuItem.DefaultsKey].minHeight

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

/** Renders a clickable menu row with optional leading and trailing content. */
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

/** Renders a custom menu row with the same interaction and styling as a menu item. */
@Composable
fun StyledDropdownMenuContent(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    // defaults / customisation
    colors: StyledMenuItemColors = StyledDropdownMenuItemDefaults.colors(),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = StyledTheme.padding.medium,
        vertical = StyledTheme.padding.small,
    ),
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = StyledDropdownMenuItemDefaults.shape,
    itemPadding: PaddingValues = StyledDropdownMenuItemDefaults.itemPadding,
    minHeight: Dp = StyledDropdownMenuItemDefaults.minHeight,
    // content
    content: @Composable RowScope.() -> Unit,
) {
    LocalStyledRegistry.current[StyledDropdownMenuItem.Key].Render(
        content, onClick, modifier, enabled, colors, contentPadding,
        interactionSource, shape, itemPadding, minHeight,
    )
}


/**
 * Adds a consistently styled, non-interactive heading between dropdown menu item groups.
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun StyledDropdownMenuSectionHeader(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = StyledTheme.typography.labelLarge,
    contentColor: Color = StyledTheme.colorScheme.primary,
    itemPadding: PaddingValues = StyledDropdownMenuItemDefaults.itemPadding,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = StyledTheme.padding.medium,
        vertical = StyledTheme.padding.small,
    ),
) {
    val layoutDirection = LocalLayoutDirection.current
    UpdateStyledThemeLocals(
        contentColor = contentColor,
        textStyle = textStyle,
        content = {
            Box(
                modifier = modifier
                    .semantics { heading() }
                    .padding(
                        start = itemPadding.calculateStartPadding(layoutDirection) +
                                contentPadding.calculateStartPadding(layoutDirection),
                        top = contentPadding.calculateTopPadding(),
                        bottom = contentPadding.calculateBottomPadding(),
                    ),
                content = { text() },
            )
        },
    )
}

/**
 * Adds a horizontal divider with vertical spacing between dropdown menu groups.
 */
@Composable
fun StyledDropdownMenuDivider(
    modifier: Modifier = Modifier,
) {
    StyledHorizontalDivider(
        modifier = modifier.padding(vertical = StyledTheme.spacing.small),
    )
}