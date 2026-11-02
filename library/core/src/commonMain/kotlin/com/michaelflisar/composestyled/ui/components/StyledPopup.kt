package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.ui.internal.rememberVisibleWindowBoundsOnScreen

/** Registry-backed popup primitive used by dropdowns, menus and other anchored overlays. */
object StyledPopup {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledPopup")

    // -------------
    // 5) implementation
    // -------------

    /** Theme-resolved visual properties and height limit for a popup. */
    @Stable
    data class Style(
        val shape: Shape,
        val containerColor: Color,
        val contentColor: Color,
        val shadowElevation: Dp,
        val border: BorderStroke?,
        val maxHeight: Dp,
        @InternalComposeStyledApi val windowOverlay: Boolean = false,
        @InternalComposeStyledApi val windowOverlaySize: IntSize = IntSize.Zero,
        @InternalComposeStyledApi
        val parentWindowOriginOnScreen: IntOffset = IntOffset.Zero,
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

    /** Builds Compose popup window properties with platform-friendly defaults. */
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

    /** Creates popup styling from the active theme and the supplied overrides. */
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

    /** Positions the popup relative to its anchor using an optional dp offset. */
    @Composable
    fun positionProvider(
        offset: DpOffset = DpOffset(0.dp, 4.dp),
    ): PopupPositionProvider = positionProvider(offset, isVisible = true)

    @Composable
    fun positionProvider(
        isVisible: Boolean,
    ): PopupPositionProvider = positionProvider(
        offset = DpOffset(0.dp, 4.dp),
        isVisible = isVisible,
    )

    @Composable
    fun positionProvider(
        offset: DpOffset,
        isVisible: Boolean,
    ): PopupPositionProvider {
        val density = LocalDensity.current
        val windowSize = LocalWindowInfo.current.containerSize
        // Recreate the provider when IME resizing changes the host window size.
        return remember(density, offset, windowSize, isVisible) {
            AnchoredPopupPositionProvider(
                with(density) {
                    IntOffset(offset.x.roundToPx(), offset.y.roundToPx())
                },
            )
        }
    }

    @InternalComposeStyledApi
    @Composable
    fun windowOverlayPositionProvider(
        offset: DpOffset,
        isVisible: Boolean,
        parentWindowOriginOnScreen: IntOffset,
    ): WindowOverlayPositionProvider {
        val density = LocalDensity.current
        val layoutDirection = LocalLayoutDirection.current
        val windowSize = LocalWindowInfo.current.containerSize
        val parentWindowSize = rememberUpdatedState(windowSize)
        val visibleWindowBounds = rememberVisibleWindowBoundsOnScreen()
        val safeDrawingInsets = IntRect(
            left = WindowInsets.safeDrawing.getLeft(density, layoutDirection),
            top = WindowInsets.safeDrawing.getTop(density),
            right = WindowInsets.safeDrawing.getRight(density, layoutDirection),
            bottom = WindowInsets.safeDrawing.getBottom(density),
        )
        val geometryRevision = remember { mutableStateOf(0) }
        val visibleWindowBoundsOnScreen = rememberUpdatedState(visibleWindowBounds)
        val safeDrawingInsetsState = rememberUpdatedState(safeDrawingInsets)
        val positionProvider = remember(
            density,
            offset,
            isVisible,
            parentWindowOriginOnScreen,
        ) {
            WindowOverlayPositionProvider(
                with(density) {
                    IntOffset(offset.x.roundToPx(), offset.y.roundToPx())
                },
                parentWindowOriginOnScreen,
                parentWindowSize,
                visibleWindowBoundsOnScreen,
                safeDrawingInsetsState,
                geometryRevision,
            )
        }
        LaunchedEffect(windowSize, visibleWindowBounds, safeDrawingInsets, isVisible) {
            if (isVisible) {
                geometryRevision.value++
            }
        }
        return positionProvider
    }
}

@InternalComposeStyledApi
@Stable
class WindowOverlayPositionProvider internal constructor(
    offset: IntOffset,
    private val parentWindowOriginOnScreen: IntOffset,
    private val parentWindowSize: State<IntSize>,
    private val visibleWindowBoundsOnScreen: State<IntRect?>,
    private val safeDrawingInsets: State<IntRect>,
    private val geometryRevision: MutableState<Int>,
) : PopupPositionProvider {
    private val anchoredPositionProvider = AnchoredPopupPositionProvider(offset)
    private val anchorBounds = mutableStateOf<IntRect?>(null)
    private val overlayOrigin = mutableStateOf(IntOffset.Zero)
    private val overlaySize = mutableStateOf(IntSize.Zero)
    private val layoutDirection = mutableStateOf(LayoutDirection.Ltr)
    private var lastGeometryRevision: Int? = null

    val hasPositioningBounds: Boolean
        get() {
            val bounds = availableWindowBounds()
            return anchorBounds.value != null &&
                overlaySize.value != IntSize.Zero &&
                bounds.width > 0 &&
                bounds.height > 0
        }

    fun updateOverlayBounds(originOnScreen: IntOffset, size: IntSize) {
        val origin = IntOffset(
            x = originOnScreen.x - parentWindowOriginOnScreen.x,
            y = originOnScreen.y - parentWindowOriginOnScreen.y,
        )
        overlayOrigin.value = origin
        overlaySize.value = size
    }

    fun onMenuSizeChanged() {
        geometryRevision.value++
    }

    fun outsideTapBounds(): IntRect {
        val bounds = availableWindowBounds()
        val origin = overlayOrigin.value
        val size = overlaySize.value
        val left = (bounds.left - origin.x).coerceIn(0, size.width)
        val top = (bounds.top - origin.y).coerceIn(0, size.height)
        val right = (bounds.right - origin.x).coerceIn(left, size.width)
        val bottom = (bounds.bottom - origin.y).coerceIn(top, size.height)
        return IntRect(left, top, right, bottom)
    }

    fun popupWindowSize(): IntSize {
        val size = parentWindowSize.value
        val visibleBounds = visibleWindowBoundsOnScreen.value ?: return size
        return IntSize(
            width = (visibleBounds.right - parentWindowOriginOnScreen.x)
                .coerceIn(0, size.width),
            height = (visibleBounds.bottom - parentWindowOriginOnScreen.y)
                .coerceIn(0, size.height),
        )
    }

    fun menuPosition(menuSize: IntSize): IntOffset {
        if (menuSize == IntSize.Zero) {
            return IntOffset.Zero
        }
        val bounds = anchorBounds.value ?: return IntOffset.Zero
        val origin = overlayOrigin.value
        val availableBounds = availableWindowBounds()
        if (availableBounds.width <= 0 || availableBounds.height <= 0) {
            return IntOffset.Zero
        }
        val windowSize = IntSize(availableBounds.width, availableBounds.height)
        val positionInParentWindow = anchoredPositionProvider.calculatePosition(
            anchorBounds = bounds.translate(-availableBounds.left, -availableBounds.top),
            windowSize = windowSize,
            layoutDirection = layoutDirection.value,
            popupContentSize = menuSize,
        )
        return IntOffset(
            x = positionInParentWindow.x + availableBounds.left - origin.x,
            y = positionInParentWindow.y + availableBounds.top - origin.y,
        )
    }

    fun maxHeightForMenu(
        requestedMaxHeight: Int,
        preferredHeight: Int,
    ): Int {
        val currentGeometryRevision = geometryRevision.value
        if (lastGeometryRevision != currentGeometryRevision) {
            lastGeometryRevision = currentGeometryRevision
            anchoredPositionProvider.invalidatePlacement()
        }
        val bounds = anchorBounds.value ?: return requestedMaxHeight
        val availableBounds = availableWindowBounds()
        if (availableBounds.width <= 0 || availableBounds.height <= 0) {
            return requestedMaxHeight
        }
        return anchoredPositionProvider.maxHeightForMenu(
            anchorBounds = bounds.translate(-availableBounds.left, -availableBounds.top),
            windowSize = IntSize(availableBounds.width, availableBounds.height),
            requestedMaxHeight = requestedMaxHeight,
            preferredHeight = preferredHeight,
        )
    }

    private fun availableWindowBounds(): IntRect {
        val size = parentWindowSize.value
        val insets = safeDrawingInsets.value
        val insetLeft = insets.left.coerceIn(0, size.width)
        val insetTop = insets.top.coerceIn(0, size.height)
        val insetBounds = IntRect(
            left = insetLeft,
            top = insetTop,
            right = (size.width - insets.right).coerceIn(insetLeft, size.width),
            bottom = (size.height - insets.bottom).coerceIn(insetTop, size.height),
        )
        val visibleBounds = visibleWindowBoundsOnScreen.value ?: return insetBounds
        val left = maxOf(
            insetBounds.left,
            visibleBounds.left - parentWindowOriginOnScreen.x,
        )
        val top = maxOf(
            insetBounds.top,
            visibleBounds.top - parentWindowOriginOnScreen.y,
        )
        val right = minOf(
            insetBounds.right,
            visibleBounds.right - parentWindowOriginOnScreen.x,
        )
        val bottom = minOf(
            insetBounds.bottom,
            visibleBounds.bottom - parentWindowOriginOnScreen.y,
        )
        if (right <= left || bottom <= top) {
            return IntRect(0, 0, 0, 0)
        }
        return IntRect(left, top, right, bottom)
    }

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        this.anchorBounds.value = anchorBounds
        this.layoutDirection.value = layoutDirection
        return IntOffset.Zero
    }
}

// -------------------------
// public API
// -------------------------

/** Shows or hides a theme-rendered popup and reports dismissal through [onDismissRequest]. */
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

/** State-backed overload that sets [visible] to false when the popup is dismissed. */
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
    private var lastAnchorBounds: IntRect? = null
    private var lastWindowSize: IntSize? = null
    private var verticalPlacement: VerticalPlacement? = null

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
        updatePlacementContext(anchorBounds, windowSize, popupContentSize.height)
        val placement = verticalPlacement
            ?: preferredPlacement(anchorBounds, windowSize, popupContentSize.height)
                .also { verticalPlacement = it }
        val y = when (placement) {
            VerticalPlacement.Below -> below
            VerticalPlacement.Above -> above
        }.coerceIn(0, maxY)
        return IntOffset(x, y)
    }

    fun maxHeightForMenu(
        anchorBounds: IntRect,
        windowSize: IntSize,
        requestedMaxHeight: Int,
        preferredHeight: Int,
    ): Int {
        val placement = resolvePlacement(
            anchorBounds = anchorBounds,
            windowSize = windowSize,
            menuHeight = preferredHeight,
        )
        return when (placement) {
            VerticalPlacement.Below ->
                (windowSize.height - anchorBounds.bottom - offset.y).coerceAtLeast(0)
            VerticalPlacement.Above ->
                (anchorBounds.top - offset.y).coerceAtLeast(0)
        }.coerceAtMost(requestedMaxHeight)
    }

    private fun resolvePlacement(
        anchorBounds: IntRect,
        windowSize: IntSize,
        menuHeight: Int,
    ): VerticalPlacement {
        updatePlacementContext(anchorBounds, windowSize, menuHeight)
        return verticalPlacement ?: preferredPlacement(anchorBounds, windowSize, menuHeight)
            .also { verticalPlacement = it }
    }

    fun invalidatePlacement() {
        verticalPlacement = null
    }

    private fun updatePlacementContext(
        anchorBounds: IntRect,
        windowSize: IntSize,
        menuHeight: Int,
    ) {
        if (lastAnchorBounds != anchorBounds || lastWindowSize != windowSize) {
            lastAnchorBounds = anchorBounds
            lastWindowSize = windowSize
            val newPlacement = preferredPlacement(anchorBounds, windowSize, menuHeight)
            if (verticalPlacement != newPlacement) {
                verticalPlacement = newPlacement
            }
        }
    }

    private fun preferredPlacement(
        anchorBounds: IntRect,
        windowSize: IntSize,
        menuHeight: Int,
    ): VerticalPlacement {
        val below = anchorBounds.bottom + offset.y
        val above = anchorBounds.top - menuHeight - offset.y
        val maxY = (windowSize.height - menuHeight).coerceAtLeast(0)
        if (below in 0..maxY) return VerticalPlacement.Below
        if (above in 0..maxY) return VerticalPlacement.Above

        val spaceBelow = windowSize.height - anchorBounds.bottom - offset.y
        val spaceAbove = anchorBounds.top - offset.y
        return if (spaceBelow >= spaceAbove) {
            VerticalPlacement.Below
        } else {
            VerticalPlacement.Above
        }
    }
}

private enum class VerticalPlacement { Above, Below }
