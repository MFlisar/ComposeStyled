package com.michaelflisar.composestyled.ui.components

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
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

    /** Enter and exit transitions for popups below an anchor, with optional transitions above it. */
    @Stable
    data class Animation(
        /** Enter transition when the popup is below its anchor. */
        val enter: EnterTransition,
        /** Exit transition when the popup is below its anchor. */
        val exit: ExitTransition,
        @InternalComposeStyledApi val shadowEnterSpec: FiniteAnimationSpec<Float>? = null,
        @InternalComposeStyledApi val shadowExitSpec: FiniteAnimationSpec<Float>? = null,
        @InternalComposeStyledApi val shadowOriginY: Float = 0f,
        /** Above-anchor transition; defaults to [enter]. */
        val enterAboveAnchor: EnterTransition = enter,
        /** Above-anchor transition; defaults to [exit]. */
        val exitAboveAnchor: ExitTransition = exit,
        @InternalComposeStyledApi
        val shadowEnterSpecAboveAnchor: FiniteAnimationSpec<Float>? = shadowEnterSpec,
        @InternalComposeStyledApi
        val shadowExitSpecAboveAnchor: FiniteAnimationSpec<Float>? = shadowExitSpec,
        @InternalComposeStyledApi
        val shadowOriginYAboveAnchor: Float = shadowOriginY,
        @InternalComposeStyledApi
        val contentRevealEnterSpec: FiniteAnimationSpec<Float>? = null,
        @InternalComposeStyledApi
        val contentRevealExitSpec: FiniteAnimationSpec<Float>? = null,
        @InternalComposeStyledApi
        val contentRevealEnterSpecAboveAnchor: FiniteAnimationSpec<Float>? =
            contentRevealEnterSpec,
        @InternalComposeStyledApi
        val contentRevealExitSpecAboveAnchor: FiniteAnimationSpec<Float>? =
            contentRevealExitSpec,
    ) {
        @InternalComposeStyledApi
        fun forPlacement(isAboveAnchor: Boolean): Animation = if (isAboveAnchor) {
            copy(
                enter = enterAboveAnchor,
                exit = exitAboveAnchor,
                shadowEnterSpec = shadowEnterSpecAboveAnchor,
                shadowExitSpec = shadowExitSpecAboveAnchor,
                shadowOriginY = shadowOriginYAboveAnchor,
                contentRevealEnterSpec = contentRevealEnterSpecAboveAnchor,
                contentRevealExitSpec = contentRevealExitSpecAboveAnchor,
            )
        } else {
            this
        }
    }

    /** Theme-resolved visual properties, animation and height limit for a popup. */
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
        @InternalComposeStyledApi
        val onTransitionFinished: ((Boolean) -> Unit)? = null,
        val animation: Animation = StyledPopupDefaults.Animations.unfoldAwayFromAnchor,
        @InternalComposeStyledApi
        val animationPlacementKnown: Boolean = true,
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

    object Animations {

        private const val unfoldEnterDurationMillis = 120
        private const val unfoldExitDurationMillis = 90

        val windowOverlayLifecycle = StyledPopup.Animation(
            enter = expandVertically(
                animationSpec = tween(unfoldEnterDurationMillis),
                clip = false,
            ),
            exit = shrinkVertically(
                animationSpec = tween(unfoldExitDurationMillis),
                clip = false,
            ),
        )

        val unfoldAwayFromAnchor = StyledPopup.Animation(
            enter = expandVertically(
                animationSpec = tween(unfoldEnterDurationMillis),
                expandFrom = Alignment.Top,
            ),
            exit = shrinkVertically(
                animationSpec = tween(unfoldExitDurationMillis),
                shrinkTowards = Alignment.Top,
            ),
            shadowEnterSpec = tween(unfoldEnterDurationMillis),
            shadowExitSpec = tween(unfoldExitDurationMillis),
            shadowOriginY = 0f,
            enterAboveAnchor = expandVertically(
                animationSpec = tween(unfoldEnterDurationMillis),
                expandFrom = Alignment.Bottom,
            ),
            exitAboveAnchor = shrinkVertically(
                animationSpec = tween(unfoldExitDurationMillis),
                shrinkTowards = Alignment.Bottom,
            ),
            shadowEnterSpecAboveAnchor = tween(unfoldEnterDurationMillis),
            shadowExitSpecAboveAnchor = tween(unfoldExitDurationMillis),
            shadowOriginYAboveAnchor = 1f,
            contentRevealEnterSpec = tween(unfoldEnterDurationMillis),
            contentRevealExitSpec = tween(unfoldExitDurationMillis),
            contentRevealEnterSpecAboveAnchor = tween(unfoldEnterDurationMillis),
            contentRevealExitSpecAboveAnchor = tween(unfoldExitDurationMillis),
        )

    }

    val animation = Animations.unfoldAwayFromAnchor

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
        animation: StyledPopup.Animation = Animations.unfoldAwayFromAnchor,
    ) = StyledPopup.Style(
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        shadowElevation = shadowElevation,
        border = border,
        maxHeight = maxHeight,
        animation = animation,
    )

    /** Positions the popup relative to its anchor using an optional dp offset. */
    @Composable
    fun positionProvider(
        offset: DpOffset = DpOffset(0.dp, 4.dp),
        placement: StyledPopupMenu.Placement = StyledPopupMenu.Placement.Anchor,
        horizontalAlignment: StyledPopupMenu.HorizontalAlignment =
            StyledPopupMenu.HorizontalAlignment.Start,
    ): PopupPositionProvider = positionProvider(
        offset = offset,
        isVisible = true,
        placement = placement,
        horizontalAlignment = horizontalAlignment,
    )

    @Composable
    fun positionProvider(
        isVisible: Boolean,
        placement: StyledPopupMenu.Placement = StyledPopupMenu.Placement.Anchor,
        horizontalAlignment: StyledPopupMenu.HorizontalAlignment =
            StyledPopupMenu.HorizontalAlignment.Start,
    ): PopupPositionProvider = positionProvider(
        offset = DpOffset(0.dp, 4.dp),
        isVisible = isVisible,
        placement = placement,
        horizontalAlignment = horizontalAlignment,
    )

    @Composable
    fun positionProvider(
        offset: DpOffset,
        isVisible: Boolean,
        placement: StyledPopupMenu.Placement = StyledPopupMenu.Placement.Anchor,
        horizontalAlignment: StyledPopupMenu.HorizontalAlignment =
            StyledPopupMenu.HorizontalAlignment.Start,
    ): PopupPositionProvider {
        val density = LocalDensity.current
        val windowSize = LocalWindowInfo.current.containerSize
        // Recreate the provider when IME resizing changes the host window size.
        val provider = remember(density, offset, windowSize, placement, horizontalAlignment) {
            AnchoredPopupPositionProvider(
                with(density) {
                    IntOffset(offset.x.roundToPx(), offset.y.roundToPx())
                },
                placement,
                horizontalAlignment,
            )
        }
        SideEffect { provider.updateVisibility(isVisible) }
        return provider
    }

    @InternalComposeStyledApi
    @Composable
    fun windowOverlayPositionProvider(
        offset: DpOffset,
        isVisible: Boolean,
        placement: StyledPopupMenu.Placement,
        horizontalAlignment: StyledPopupMenu.HorizontalAlignment,
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
            placement,
            horizontalAlignment,
            parentWindowOriginOnScreen,
        ) {
            WindowOverlayPositionProvider(
                with(density) {
                    IntOffset(offset.x.roundToPx(), offset.y.roundToPx())
                },
                placement,
                horizontalAlignment,
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
    placement: StyledPopupMenu.Placement,
    horizontalAlignment: StyledPopupMenu.HorizontalAlignment,
    private val parentWindowOriginOnScreen: IntOffset,
    private val parentWindowSize: State<IntSize>,
    private val visibleWindowBoundsOnScreen: State<IntRect?>,
    private val safeDrawingInsets: State<IntRect>,
    private val geometryRevision: MutableState<Int>,
) : PopupPositionProvider {
    private val anchoredPositionProvider =
        AnchoredPopupPositionProvider(offset, placement, horizontalAlignment)
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

    fun isAboveAnchor(menuHeight: Int): Boolean {
        if (anchoredPositionProvider.placement == StyledPopupMenu.Placement.Submenu) {
            return false
        }
        val bounds = anchorBounds.value ?: return false
        val availableBounds = availableWindowBounds()
        if (availableBounds.width <= 0 || availableBounds.height <= 0) {
            return false
        }
        return anchoredPositionProvider.resolvePlacement(
            anchorBounds = bounds.translate(-availableBounds.left, -availableBounds.top),
            windowSize = IntSize(availableBounds.width, availableBounds.height),
            menuHeight = menuHeight,
        ) == VerticalPlacement.Above
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

/**
 * Shows or hides a theme-rendered popup and reports dismissal through [onDismissRequest].
 * [animation] overrides the transitions in [style] for this popup.
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun StyledPopup(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    popupPositionProvider: PopupPositionProvider = StyledPopupDefaults.positionProvider(),
    properties: PopupProperties = StyledPopupDefaults.properties(),
    style: StyledPopup.Style = StyledPopupDefaults.style(),
    animation: StyledPopup.Animation = style.animation,
    content: @Composable () -> Unit,
) {
    val anchoredPositionProvider = popupPositionProvider as? AnchoredPopupPositionProvider
    SideEffect { anchoredPositionProvider?.updateVisibility(visible) }
    val anchorPlacement =
        anchoredPositionProvider?.verticalPlacement?.value
    val resolvedAnimation = remember(
        animation,
        anchorPlacement,
    ) {
        animation.forPlacement(anchorPlacement == VerticalPlacement.Above)
    }
    LocalStyledRegistry.current[StyledPopup.Key].Render(
        visible = visible,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        popupPositionProvider = popupPositionProvider,
        properties = properties,
        style = style.copy(
            animation = resolvedAnimation,
            animationPlacementKnown =
                anchoredPositionProvider == null ||
                    anchoredPositionProvider.placement == StyledPopupMenu.Placement.Submenu ||
                    anchorPlacement != null,
        ),
        content = content,
    )
}

/**
 * State-backed overload that sets [visible] to false when dismissed.
 * [animation] overrides the transitions in [style] for this popup.
 */
@Composable
fun StyledPopup(
    visible: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    popupPositionProvider: PopupPositionProvider = StyledPopupDefaults.positionProvider(),
    properties: PopupProperties = StyledPopupDefaults.properties(),
    style: StyledPopup.Style = StyledPopupDefaults.style(),
    animation: StyledPopup.Animation = style.animation,
    content: @Composable () -> Unit,
) {
    StyledPopup(
        visible = visible.value,
        onDismissRequest = { visible.value = false },
        modifier = modifier,
        popupPositionProvider = popupPositionProvider,
        properties = properties,
        style = style,
        animation = animation,
        content = content,
    )
}

/**
 * Positions the popup relative to the anchor and clamps it to the window.
 */
private class AnchoredPopupPositionProvider(
    private val offset: IntOffset,
    val placement: StyledPopupMenu.Placement,
    private val horizontalAlignment: StyledPopupMenu.HorizontalAlignment,
) : PopupPositionProvider {
    private var lastAnchorBounds: IntRect? = null
    private var lastWindowSize: IntSize? = null
    val verticalPlacement = mutableStateOf<VerticalPlacement?>(null)
    private var wasVisible = false

    fun updateVisibility(isVisible: Boolean) {
        if (isVisible && !wasVisible) {
            verticalPlacement.value = null
        }
        wasVisible = isVisible
    }

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val rtl = layoutDirection == LayoutDirection.Rtl
        if (placement == StyledPopupMenu.Placement.Submenu) {
            val preferredX = if (rtl) {
                anchorBounds.left - popupContentSize.width - offset.x
            } else {
                anchorBounds.right + offset.x
            }
            val alternateX = if (rtl) {
                anchorBounds.right + offset.x
            } else {
                anchorBounds.left - popupContentSize.width - offset.x
            }
            val maxX = (windowSize.width - popupContentSize.width).coerceAtLeast(0)
            val x = when {
                preferredX in 0..maxX -> preferredX
                alternateX in 0..maxX -> alternateX
                else -> preferredX.coerceIn(0, maxX)
            }
            val maxY = (windowSize.height - popupContentSize.height).coerceAtLeast(0)
            return IntOffset(
                x = x,
                y = (anchorBounds.top + offset.y).coerceIn(0, maxY),
            )
        }
        val dx = if (rtl) -offset.x else offset.x
        val start =
            (if (rtl) anchorBounds.right - popupContentSize.width else anchorBounds.left) + dx
        val end =
            (if (rtl) anchorBounds.left else anchorBounds.right - popupContentSize.width) + dx
        val preferred = if (horizontalAlignment == StyledPopupMenu.HorizontalAlignment.Start) {
            start
        } else {
            end
        }
        val alternate = if (horizontalAlignment == StyledPopupMenu.HorizontalAlignment.Start) {
            end
        } else {
            start
        }
        val maxX = (windowSize.width - popupContentSize.width).coerceAtLeast(0)
        val x = when {
            preferred in 0..maxX -> preferred
            alternate in 0..maxX -> alternate
            else -> preferred.coerceIn(0, maxX)
        }
        val below = anchorBounds.bottom + offset.y
        val above = anchorBounds.top - popupContentSize.height - offset.y
        val maxY = (windowSize.height - popupContentSize.height).coerceAtLeast(0)
        updatePlacementContext(anchorBounds, windowSize, popupContentSize.height)
        val placement = verticalPlacement.value
            ?: preferredPlacement(anchorBounds, windowSize, popupContentSize.height)
                .also { verticalPlacement.value = it }
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
        if (placement == StyledPopupMenu.Placement.Submenu) {
            val top = (anchorBounds.top + offset.y).coerceIn(0, windowSize.height)
            return (windowSize.height - top).coerceAtLeast(0).coerceAtMost(requestedMaxHeight)
        }
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

    fun resolvePlacement(
        anchorBounds: IntRect,
        windowSize: IntSize,
        menuHeight: Int,
    ): VerticalPlacement {
        updatePlacementContext(anchorBounds, windowSize, menuHeight)
        return verticalPlacement.value
            ?: preferredPlacement(anchorBounds, windowSize, menuHeight)
                .also { verticalPlacement.value = it }
    }

    fun invalidatePlacement() {
        verticalPlacement.value = null
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
            if (verticalPlacement.value != newPlacement) {
                verticalPlacement.value = newPlacement
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
