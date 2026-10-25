package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledPopup {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledPopup")

    // -------------
    // 5) implementation
    // -------------

    @Stable
    data class Style(
        val shape: Shape,
        val containerColor: Color,
        val contentColor: Color,
        val shadowElevation: Dp,
        val border: BorderStroke?,
        val maxHeight: Dp,
    ) {
        init {
            require(maxHeight.value.isFinite() && maxHeight > 0.dp) {
                "Popup maximum height must be finite and positive"
            }
        }
    }

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            visible: Boolean,
            onDismissRequest: () -> Unit,
            modifier: Modifier,
            popupPositionProvider: PopupPositionProvider,
            properties: PopupProperties,
            style: Style,
            content: @Composable () -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledPopupDefaults {

    @Composable
    fun properties(
        focusable: Boolean = true,
        dismissOnBackPress: Boolean = true,
        dismissOnClickOutside: Boolean = true,
        clippingEnabled: Boolean = true,
        usePlatformDefaultWidth: Boolean = true,
    ) = PopupProperties(
        focusable = focusable,
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
        clippingEnabled = clippingEnabled,
        usePlatformDefaultWidth = usePlatformDefaultWidth,
    )

    @Composable
    fun style(
        shape: Shape = StyledTheme.shapes.small,
        containerColor: Color = StyledTheme.colorScheme.surface,
        contentColor: Color = StyledTheme.colorScheme.onSurface,
        shadowElevation: Dp = 8.dp,
        border: BorderStroke? = null,
        maxHeight: Dp = 320.dp,
    ) = StyledPopup.Style(
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        shadowElevation = shadowElevation,
        border = border,
        maxHeight = maxHeight,
    )

    @Composable
    fun positionProvider(
        offset: DpOffset = DpOffset(0.dp, 4.dp),
    ): PopupPositionProvider {
        val density = LocalDensity.current
        return remember(density, offset) {
            AnchoredPopupPositionProvider(
                with(density) {
                    IntOffset(offset.x.roundToPx(), offset.y.roundToPx())
                },
            )
        }
    }
}

// -------------------------
// public API
// -------------------------

@Composable
fun StyledPopup(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    popupPositionProvider: PopupPositionProvider = StyledPopupDefaults.positionProvider(),
    properties: PopupProperties = StyledPopupDefaults.properties(),
    style: StyledPopup.Style = StyledPopupDefaults.style(),
    content: @Composable () -> Unit,
) {
    LocalStyledRegistry.current[StyledPopup.Key].Render(
        visible = visible,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        popupPositionProvider = popupPositionProvider,
        properties = properties,
        style = style,
        content = content,
    )
}

@Composable
fun StyledPopup(
    visible: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    popupPositionProvider: PopupPositionProvider = StyledPopupDefaults.positionProvider(),
    properties: PopupProperties = StyledPopupDefaults.properties(),
    style: StyledPopup.Style = StyledPopupDefaults.style(),
    content: @Composable () -> Unit,
) {
    StyledPopup(
        visible = visible.value,
        onDismissRequest = { visible.value = false },
        modifier = modifier,
        popupPositionProvider = popupPositionProvider,
        properties = properties,
        style = style,
        content = content,
    )
}

/**
 * Positions the popup relative to the anchor and clamps it to the window.
 */
private class AnchoredPopupPositionProvider(
    private val offset: IntOffset,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val rtl = layoutDirection == LayoutDirection.Rtl
        val dx = if (rtl) -offset.x else offset.x
        val start =
            (if (rtl) anchorBounds.right - popupContentSize.width else anchorBounds.left) + dx
        val end =
            (if (rtl) anchorBounds.left else anchorBounds.right - popupContentSize.width) + dx
        val maxX = (windowSize.width - popupContentSize.width).coerceAtLeast(0)
        val x = when {
            start in 0..maxX -> start
            end in 0..maxX -> end
            else -> start.coerceIn(0, maxX)
        }
        val below = anchorBounds.bottom + offset.y
        val above = anchorBounds.top - popupContentSize.height - offset.y
        val maxY = (windowSize.height - popupContentSize.height).coerceAtLeast(0)
        val y = when {
            below in 0..maxY -> below
            above in 0..maxY -> above
            else -> below.coerceIn(0, maxY)
        }
        return IntOffset(x, y)
    }
}
