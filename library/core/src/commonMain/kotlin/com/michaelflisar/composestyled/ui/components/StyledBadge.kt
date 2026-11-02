package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext

object StyledBadge {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledBadge")
    val DefaultsKey = StyledKey<Defaults>("StyledBadgeDefaults")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    data class Defaults(
        val minSize: Dp,
        val dotSize: Dp,
        val textStyle: TextStyle,
        val iconSize: Dp,
        val contentPadding: PaddingValues,
    )

    // -------------
    // 3) roles / variants / context
    // -------------

    enum class Context : StyledContext {
        BadgeContent,
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
            badge: (@Composable () -> Unit)?,
            content: @Composable () -> Unit,
            alignment: Alignment,
            minSize: Dp,
            dotSize: Dp,
            textStyle: TextStyle,
            contentPadding: PaddingValues,
            modifier: Modifier,
            offset: DpOffset,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledBadgeDefaults {
    val minSize: Dp
        @Composable get() = LocalStyledRegistry.current[StyledBadge.DefaultsKey].minSize
    val dotSize: Dp
        @Composable get() = LocalStyledRegistry.current[StyledBadge.DefaultsKey].dotSize
    val textStyle: TextStyle
        @Composable get() = LocalStyledRegistry.current[StyledBadge.DefaultsKey].textStyle
    val iconSize: Dp
        @Composable get() = LocalStyledRegistry.current[StyledBadge.DefaultsKey].iconSize
    val contentPadding: PaddingValues
        @Composable get() = LocalStyledRegistry.current[StyledBadge.DefaultsKey].contentPadding
}

// -------------------------
// public API
// -------------------------

/**
 * Centers the badge on [alignment] within [content], without reserving space for its overflow.
 * Padding on [modifier] adds outer clearance without changing the anchor point.
 * [offset] moves only the badge: positive x points toward end, positive y points downward.
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun StyledBadge(
    badge: (@Composable () -> Unit)?,
    content: @Composable () -> Unit,
    alignment: Alignment = Alignment.TopEnd,
    minSize: Dp = StyledBadgeDefaults.minSize,
    dotSize: Dp = StyledBadgeDefaults.dotSize,
    textStyle: TextStyle = StyledBadgeDefaults.textStyle,
    contentPadding: PaddingValues = StyledBadgeDefaults.contentPadding,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset.Zero,
) {
    val scopedBadge: (@Composable () -> Unit)? = badge?.let { slot ->
        {
            CompositionLocalProvider(
                LocalStyledContext provides StyledBadge.Context.BadgeContent,
                content = slot,
            )
        }
    }
    LocalStyledRegistry.current[StyledBadge.Key].Render(
        badge = scopedBadge,
        content = content,
        alignment = alignment,
        minSize = minSize,
        dotSize = dotSize,
        textStyle = textStyle,
        contentPadding = contentPadding,
        modifier = modifier,
        offset = offset,
    )
}
