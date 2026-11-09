package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey

object StyledListItem {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledListItem")
    val DefaultsKey = StyledKey<Defaults>("StyledListItemDefaults")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    data class Defaults(
        val shape: Shape,
        val minHeight: Dp,
        val contentPadding: PaddingValues,
        val spacing: Dp,
    )

    // -------------
    // 3) roles / variants / context
    // -------------

    sealed interface Content {
        class Semantic(
            val headline: @Composable () -> Unit,
            val supporting: (@Composable () -> Unit)?,
            val overline: (@Composable () -> Unit)?,
        ) : Content

        class Custom(val content: @Composable ColumnScope.() -> Unit) : Content
    }

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation>, StyledComponentWithDefaults<Defaults> {
        override val key: StyledKey<Implementation>
            get() = Key

        override val defaultsKey: StyledKey<Defaults>
            get() = DefaultsKey

        @Composable
        fun Render(
            onClick: (() -> Unit)?,
            modifier: Modifier,
            enabled: Boolean,
            interactionSource: MutableInteractionSource?,
            shape: Shape,
            minHeight: Dp,
            contentPadding: PaddingValues,
            spacing: Dp,
            containerColor: Color,
            contentColor: Color,
            supportingColor: Color,
            borderColor: Color,
            leading: (@Composable () -> Unit)?,
            actions: (@Composable RowScope.() -> Unit)?,
            content: Content,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledListItemDefaults {
    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledListItem.DefaultsKey].shape

    val minHeight: Dp
        @Composable get() = LocalStyledRegistry.current[StyledListItem.DefaultsKey].minHeight

    val contentPadding: PaddingValues
        @Composable get() = LocalStyledRegistry.current[StyledListItem.DefaultsKey].contentPadding

    val spacing: Dp
        @Composable get() = LocalStyledRegistry.current[StyledListItem.DefaultsKey].spacing
}

// -------------------------
// public API
// -------------------------

/** Semantic list item. The optional click covers the whole item; action controls handle their own clicks. */
@Composable
fun StyledListItem(
    headline: @Composable () -> Unit,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / colors
    shape: Shape = StyledListItemDefaults.shape,
    minHeight: Dp = StyledListItemDefaults.minHeight,
    contentPadding: PaddingValues = StyledListItemDefaults.contentPadding,
    spacing: Dp = StyledListItemDefaults.spacing,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    supportingColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    // content
    supporting: (@Composable () -> Unit)? = null,
    overline: (@Composable () -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    LocalStyledRegistry.current[StyledListItem.Key].Render(
        onClick, modifier, enabled, interactionSource, shape,
        minHeight, contentPadding, spacing,
        containerColor, contentColor, supportingColor, borderColor, leading, actions,
        StyledListItem.Content.Semantic(headline, supporting, overline),
    )
}

/**
 * Free-content list item; does not infer semantic text roles or line patterns.
 * The optional click covers the whole item; action controls handle their own clicks.
 */
@Composable
fun StyledListItem(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / colors
    shape: Shape = StyledListItemDefaults.shape,
    minHeight: Dp = StyledListItemDefaults.minHeight,
    contentPadding: PaddingValues = StyledListItemDefaults.contentPadding,
    spacing: Dp = StyledListItemDefaults.spacing,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    supportingColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    // content
    leading: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    LocalStyledRegistry.current[StyledListItem.Key].Render(
        onClick, modifier, enabled, interactionSource, shape,
        minHeight, contentPadding, spacing,
        containerColor, contentColor, supportingColor, borderColor,
        leading, actions, StyledListItem.Content.Custom(content),
    )
}
