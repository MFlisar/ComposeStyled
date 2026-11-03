package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.internal.LocalStyledToolbarPopupContext
import com.michaelflisar.composestyled.ui.overlay.StyledPopupContainerScope
import kotlin.math.roundToInt

internal val LocalStyledPopupMenuDismiss =
    staticCompositionLocalOf<(() -> Unit)?> { null }

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
            placement: Placement,
            properties: PopupProperties,
            scrollState: ScrollState,
            style: StyledPopup.Style,
            contentPadding: PaddingValues,
            header: (@Composable () -> Unit)?,
            content: @Composable ColumnScope.() -> Unit,
        )
    }

    /** Placement behavior relative to the measured popup container. */
    enum class Placement {
        /** Opens below or above its anchor, depending on available space. */
        Anchor,
        /** Opens beside its anchor and flips to the other side when needed. */
        Submenu,
    }

    enum class OutsideTapBehavior {
        /** Uses the popup platform's standard outside-tap handling. */
        PlatformDefault,
        /** Uses the full-window overlay when screen coordinates are available. */
        DismissAndConsume,
    }

    /** Width rules for a popup menu relative to its anchor and available content. */
    @Stable
    sealed interface WidthConfig {

        /** Uses the measured content width, optionally capped by `maxWidth`. */
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

        /** Matches the anchor width, capped by `maxWidth` when provided. */
        @Immutable
        data class MatchParent(val maxWidth: Dp? = null) : WidthConfig {
            init {
                require(maxWidth == null || maxWidth.value.isFinite() && maxWidth > 0.dp) {
                    "Menu maximum width must be finite and positive"
                }
            }
        }

        /** Uses the anchor width. */
        @Immutable
        data object MatchAnchor : WidthConfig

        /** Uses at least the anchor width, allowing wider content. */
        @Immutable
        data object AtLeastAnchor : WidthConfig

        /** Uses the menu content's intrinsic width. */
        @Immutable
        data object WrapContent : WidthConfig

        /** Uses a fixed menu width. */
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
        @Composable get() = PaddingValues(
            vertical = if (LocalStyledToolbarPopupContext.current != null) 0.dp
            else StyledTheme.padding.small,
        )

    @Composable
    fun contentColor(containerColor: Color = this.containerColor): Color =
        StyledTheme.contentColorFor(containerColor)
}

// -------------------------
// public API
// -------------------------

/** Shows a menu anchored to this popup-container scope with configurable width and dismissal behavior. */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun StyledPopupContainerScope.StyledPopupMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    popupHeader: @Composable (() -> Unit)? = null,
    properties: PopupProperties = StyledPopupDefaults.properties(),
    widthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenuDefaults.widthConfig,
    offset: DpOffset = StyledPopupMenuDefaults.offset,
    placement: StyledPopupMenu.Placement = StyledPopupMenu.Placement.Anchor,
    scrollState: ScrollState = rememberScrollState(),
    shape: Shape = StyledPopupMenuDefaults.shape,
    containerColor: Color = StyledPopupMenuDefaults.containerColor,
    contentColor: Color = StyledPopupMenuDefaults.contentColor(containerColor),
    shadowElevation: Dp = StyledPopupMenuDefaults.shadowElevation,
    border: BorderStroke? = StyledPopupMenuDefaults.border,
    maxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    contentPadding: PaddingValues = StyledPopupMenuDefaults.contentPadding,
    outsideTapBehavior: StyledPopupMenu.OutsideTapBehavior =
        StyledPopupMenu.OutsideTapBehavior.PlatformDefault,
    content: @Composable ColumnScope.() -> Unit,
) {
    require(maxHeight.value.isFinite() && maxHeight > 0.dp) {
        "Menu maximum height must be finite and positive"
    }
    require(offset.x.value.isFinite() && offset.y.value.isFinite()) {
        "Menu offset must be finite"
    }
    val toolbarContext = LocalStyledToolbarPopupContext.current
    val lastAncestor = toolbarContext?.ancestors?.lastOrNull()
    val animationLevel = lastAncestor?.level ?: toolbarContext?.rootLevel
    val animationId = lastAncestor?.id ?: toolbarContext?.rootId
    val onTransitionFinished: ((Boolean) -> Unit)? =
        remember(animationLevel, animationId) {
            val level = animationLevel
            val id = animationId
            if (level != null && id != null) {
                { finished -> level.setPopupAnimationFinished(id, finished) }
            } else {
                null
            }
        }
    val rootDismiss = LocalStyledPopupMenuDismiss.current ?: onDismissRequest
    val menuContent: @Composable ColumnScope.() -> Unit = {
        CompositionLocalProvider(LocalStyledPopupMenuDismiss provides rootDismiss) {
            content()
        }
    }
    val density = LocalDensity.current
    val windowOrigin = parentWindowOriginOnScreen
    val useWindowOverlay =
        outsideTapBehavior == StyledPopupMenu.OutsideTapBehavior.DismissAndConsume &&
            windowOrigin != null
    if (containerWidth > 0) {
        val anchorWidth = with(density) { containerWidth.toDp() }
        LocalStyledRegistry.current[StyledPopupMenu.Key].Render(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = popupMenuWidthModifier(widthConfig, anchorWidth).then(modifier),
            offset = offset,
            placement = placement,
            properties = properties,
            scrollState = scrollState,
            style = StyledPopup.Style(
                shape = shape,
                containerColor = containerColor,
                contentColor = contentColor,
                shadowElevation = shadowElevation,
                border = border,
                maxHeight = maxHeight,
                windowOverlay = useWindowOverlay,
                onTransitionFinished = onTransitionFinished,
                parentWindowOriginOnScreen = windowOrigin
                    ?.takeIf { useWindowOverlay }
                    ?.let {
                        IntOffset(
                            x = it.x.roundToInt(),
                            y = it.y.roundToInt(),
                        )
                    } ?: IntOffset.Zero,
            ),
            contentPadding = contentPadding,
            header = popupHeader,
            content = menuContent,
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
