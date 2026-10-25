package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey
import com.michaelflisar.composestyledcomponents.ui.overlay.StyledPopupContainerScope

object StyledPopupMenu {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledPopupMenu")

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            expanded: Boolean,
            onDismissRequest: () -> Unit,
            modifier: Modifier,
            offset: DpOffset,
            properties: PopupProperties,
            scrollState: ScrollState,
            style: StyledPopup.Style,
            contentPadding: PaddingValues,
            header: (@Composable () -> Unit)?,
            content: @Composable ColumnScope.() -> Unit,
        )
    }

    @Stable
    sealed interface WidthConfig {

        @Immutable
        data class Auto(
            val maxWidth: Dp? = StyledPopupMenuDefaults.maxWidth,
            val limitToContainerWidth: Boolean = false,
        ) : WidthConfig {
            init {
                require(maxWidth == null || maxWidth.value.isFinite() && maxWidth > 0.dp) {
                    "Menu maximum width must be finite and positive"
                }
            }
        }

        /** Matches the anchor width, capped by [maxWidth] when provided. */
        @Immutable
        data class MatchParent(val maxWidth: Dp? = null) : WidthConfig {
            init {
                require(maxWidth == null || maxWidth.value.isFinite() && maxWidth > 0.dp) {
                    "Menu maximum width must be finite and positive"
                }
            }
        }

        @Immutable
        data object MatchAnchor : WidthConfig

        @Immutable
        data object AtLeastAnchor : WidthConfig

        @Immutable
        data object WrapContent : WidthConfig

        @Immutable
        data class Fixed(val width: Dp) : WidthConfig {
            init {
                require(width.value.isFinite() && width > 0.dp) {
                    "Popup width must be finite and positive"
                }
            }
        }
    }
}

// -------------------------
// defaults
// -------------------------

object StyledPopupMenuDefaults {
    val maxWidth: Dp = 280.dp
    val maxHeight: Dp = 320.dp
    val shadowElevation: Dp = 8.dp
    val offset: DpOffset = DpOffset(0.dp, 0.dp)
    val border: BorderStroke? = null
    val widthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchParent()

    val shape: Shape
        @Composable get() = StyledTheme.shapes.small

    val containerColor: Color
        @Composable get() = StyledTheme.colorScheme.surface

    val contentPadding: PaddingValues
        @Composable get() = PaddingValues(vertical = StyledTheme.padding.small)

    @Composable
    fun contentColor(containerColor: Color = this.containerColor): Color =
        StyledTheme.contentColorFor(containerColor)
}

// -------------------------
// public API
// -------------------------

@Composable
fun StyledPopupContainerScope.StyledPopupMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    popupHeader: @Composable (() -> Unit)? = null,
    properties: PopupProperties = StyledPopupDefaults.properties(),
    widthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenuDefaults.widthConfig,
    offset: DpOffset = StyledPopupMenuDefaults.offset,
    scrollState: ScrollState = rememberScrollState(),
    shape: Shape = StyledPopupMenuDefaults.shape,
    containerColor: Color = StyledPopupMenuDefaults.containerColor,
    contentColor: Color = StyledPopupMenuDefaults.contentColor(containerColor),
    shadowElevation: Dp = StyledPopupMenuDefaults.shadowElevation,
    border: BorderStroke? = StyledPopupMenuDefaults.border,
    maxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    contentPadding: PaddingValues = StyledPopupMenuDefaults.contentPadding,
    content: @Composable ColumnScope.() -> Unit,
) {
    require(maxHeight.value.isFinite() && maxHeight > 0.dp) {
        "Menu maximum height must be finite and positive"
    }
    require(offset.x.value.isFinite() && offset.y.value.isFinite()) {
        "Menu offset must be finite"
    }
    val density = LocalDensity.current
    if (containerWidth > 0) {
        val anchorWidth = with(density) { containerWidth.toDp() }
        LocalStyledRegistry.current[StyledPopupMenu.Key].Render(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = popupMenuWidthModifier(widthConfig, anchorWidth).then(modifier),
            offset = offset,
            properties = properties,
            scrollState = scrollState,
            style = StyledPopup.Style(
                shape = shape,
                containerColor = containerColor,
                contentColor = contentColor,
                shadowElevation = shadowElevation,
                border = border,
                maxHeight = maxHeight,
            ),
            contentPadding = contentPadding,
            header = popupHeader,
            content = content,
        )
    }
}

private fun popupMenuWidthModifier(
    config: StyledPopupMenu.WidthConfig,
    anchorWidth: Dp,
): Modifier = when (config) {
    StyledPopupMenu.WidthConfig.MatchAnchor -> Modifier.width(anchorWidth)
    StyledPopupMenu.WidthConfig.AtLeastAnchor -> Modifier.widthIn(min = anchorWidth)
    StyledPopupMenu.WidthConfig.WrapContent -> Modifier
    is StyledPopupMenu.WidthConfig.Fixed -> Modifier.width(config.width)
    is StyledPopupMenu.WidthConfig.Auto -> {
        val maximum = when {
            config.limitToContainerWidth -> minOf(config.maxWidth ?: anchorWidth, anchorWidth)
            else -> config.maxWidth
        }
        if (maximum != null) Modifier.widthIn(max = maximum) else Modifier
    }

    is StyledPopupMenu.WidthConfig.MatchParent -> {
        val width = config.maxWidth?.let { minOf(anchorWidth, it) } ?: anchorWidth
        Modifier.width(width)
    }
}
