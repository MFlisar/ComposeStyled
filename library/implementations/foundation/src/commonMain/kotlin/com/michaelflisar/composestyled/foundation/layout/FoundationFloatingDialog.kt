package com.michaelflisar.composestyled.foundation.layout

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.backhandler.BackHandler
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.internal.DialogActions
import com.michaelflisar.composestyled.ui.internal.StyledPointerCursor
import com.michaelflisar.composestyled.ui.internal.styledPointerCursor
import com.michaelflisar.composestyled.ui.layout.LocalFloatingDialogEntry
import com.michaelflisar.composestyled.ui.layout.LocalFloatingDialogHost
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.primitives.StyledText

private class FloatingWindowOutsideClickActions {
    var minimize: (() -> Unit)? = null
}

@Composable
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class, InternalComposeStyledApi::class)
internal fun FoundationFloatingDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier,
    properties: StyledDialog.Properties,
    style: StyledDialog.Style.FloatingWindow,
    initialSize: DpSize,
    minSize: DpSize,
    initiallyMinimized: Boolean,
    shape: Shape,
    edgeMargin: Dp,
    containerColor: Color,
    title: (@Composable () -> Unit)?,
    buttons: StyledDialog.Buttons,
    positiveActionFirst: Boolean,
    actionSpacing: Dp,
    actionRowSpacing: Dp,
    stackPositiveActionFirst: Boolean,
    content: @Composable ColumnScope.(PaddingValues) -> Unit,
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val host = LocalFloatingDialogHost.current
    val entry = LocalFloatingDialogEntry.current
    var dragInProgress by remember { mutableStateOf(false) }
    var minimized by rememberSaveable { mutableStateOf(initiallyMinimized) }
    val outsideClickActions = remember { FloatingWindowOutsideClickActions() }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val backgroundMode = style.backgroundMode
        val interceptOutsideClick =
            backgroundMode != StyledDialog.FloatingWindowBackgroundMode.Interactive
        val showScrim = when (backgroundMode) {
            StyledDialog.FloatingWindowBackgroundMode.Interactive -> false
            is StyledDialog.FloatingWindowBackgroundMode.Block -> backgroundMode.showScrim
            is StyledDialog.FloatingWindowBackgroundMode.Minimize -> backgroundMode.showScrim
            is StyledDialog.FloatingWindowBackgroundMode.Dismiss -> backgroundMode.showScrim
        }
        if (interceptOutsideClick && !minimized) {
            Box(
                Modifier.matchParentSize()
                    .then(
                        if (showScrim) Modifier.background(Color.Black.copy(alpha = 0.32f))
                        else Modifier,
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            when (backgroundMode) {
                                StyledDialog.FloatingWindowBackgroundMode.Interactive -> Unit
                                is StyledDialog.FloatingWindowBackgroundMode.Block -> Unit
                                is StyledDialog.FloatingWindowBackgroundMode.Minimize ->
                                    outsideClickActions.minimize?.invoke()
                                is StyledDialog.FloatingWindowBackgroundMode.Dismiss ->
                                    onDismissRequest()
                            }
                        },
                    )
                    .semantics { hideFromAccessibility() },
            )
        }
        BoxWithConstraints(Modifier.safeDrawingPadding().imePadding().fillMaxSize()) {
            requireFloatingWindowSize(initialSize, minSize, edgeMargin)
            requireFloatingPosition(style.initialPosition, "initialPosition")
            style.initialBubblePosition?.let {
                requireFloatingPosition(it, "initialBubblePosition")
            }
            val areaWidth = (maxWidth - edgeMargin * 2).coerceAtLeast(0.dp)
            val areaHeight = (maxHeight - edgeMargin * 2).coerceAtLeast(0.dp)
            val effectiveMin = DpSize(
                minSize.width.coerceAtMost(areaWidth),
                minSize.height.coerceAtMost(areaHeight),
            )
            val startSize = DpSize(
                initialSize.width.coerceIn(effectiveMin.width, areaWidth),
                initialSize.height.coerceIn(effectiveMin.height, areaHeight),
            )
            val bubbleSize = DpSize(48.dp, 48.dp)
            val availableSpace = DpSize(areaWidth, areaHeight)
            val startPosition = style.initialPosition.resolve(
                availableSpace,
                startSize,
                edgeMargin,
                density,
                layoutDirection,
            ).clamp(edgeMargin, areaWidth, areaHeight, startSize)
            val startBubblePosition = style.initialBubblePosition?.resolve(
                availableSpace,
                bubbleSize,
                edgeMargin,
                density,
                layoutDirection,
            )?.clamp(edgeMargin, areaWidth, areaHeight, bubbleSize) ?: DpOffset(
                startPosition.x + (startSize.width - bubbleSize.width) / 2f,
                startPosition.y + (startSize.height - bubbleSize.height) / 2f,
            ).clamp(edgeMargin, areaWidth, areaHeight, bubbleSize)
            var windowSize by rememberSaveable(stateSaver = DpSizeSaver) {
                mutableStateOf(startSize)
            }
            var windowPositionFraction by rememberSaveable(
                stateSaver = FloatingPositionFractionSaver,
            ) {
                mutableStateOf(startPosition.toFraction(availableSpace, startSize, edgeMargin))
            }
            var bubblePositionFraction by rememberSaveable(
                stateSaver = FloatingPositionFractionSaver,
            ) {
                mutableStateOf(startBubblePosition.toFraction(availableSpace, bubbleSize, edgeMargin))
            }
            var hasBubblePosition by rememberSaveable {
                mutableStateOf(style.initialBubblePosition != null || initiallyMinimized)
            }
            var maximized by rememberSaveable { mutableStateOf(false) }
            val windowPosition = windowPositionFraction.toPosition(availableSpace, windowSize, edgeMargin)
            val bubblePosition = bubblePositionFraction.toPosition(availableSpace, bubbleSize, edgeMargin)
            SideEffect {
                val boundedSize = DpSize(
                    windowSize.width.coerceIn(effectiveMin.width, areaWidth),
                    windowSize.height.coerceIn(effectiveMin.height, areaHeight),
                )
                if (boundedSize != windowSize) windowSize = boundedSize
            }
    
            val targetSize = when {
                minimized -> bubbleSize
                maximized -> DpSize(areaWidth, areaHeight)
                else -> windowSize
            }
            val targetPosition = when {
                minimized -> bubblePosition
                maximized -> DpOffset(edgeMargin, edgeMargin)
                else -> windowPosition
            }
            fun minimizeWindow() {
                if (!hasBubblePosition) {
                    bubblePositionFraction = DpOffset(
                        targetPosition.x + (targetSize.width - bubbleSize.width) / 2f,
                        targetPosition.y + (targetSize.height - bubbleSize.height) / 2f,
                    ).clamp(edgeMargin, areaWidth, areaHeight, bubbleSize)
                        .toFraction(
                            availableSpace,
                            bubbleSize,
                            edgeMargin,
                            bubblePositionFraction,
                        )
                    hasBubblePosition = true
                }
                minimized = true
            }
            fun snapMinimizedBubbleToEdge() {
                if (!minimized || !style.snapToEdge) return
                val position = bubblePositionFraction.toPosition(
                    availableSpace,
                    bubbleSize,
                    edgeMargin,
                )
                bubblePositionFraction = position.snapToHorizontalEdge(
                    availableSpace = availableSpace,
                    itemSize = bubbleSize,
                    margin = edgeMargin,
                ).toFraction(
                    availableSpace,
                    bubbleSize,
                    edgeMargin,
                    bubblePositionFraction,
                )
            }
            SideEffect { outsideClickActions.minimize = ::minimizeWindow }
            val visualSize = DpSize(
                animateDpAsState(
                    targetSize.width,
                    if (dragInProgress) snap() else tween(220),
                    label = "floatingWindowWidth",
                ).value,
                animateDpAsState(
                    targetSize.height,
                    if (dragInProgress) snap() else tween(220),
                    label = "floatingWindowHeight",
                ).value,
            )
            val visualPosition = DpOffset(
                animateDpAsState(
                    targetPosition.x,
                    if (dragInProgress) snap() else tween(220),
                    label = "floatingWindowX",
                ).value,
                animateDpAsState(
                    targetPosition.y,
                    if (dragInProgress) snap() else tween(220),
                    label = "floatingWindowY",
                ).value,
            )
            val bubble = style.bubble
            val bubbleColor = bubble.containerColor ?: StyledTheme.colorScheme.surfaceVariant
            val windowContentColor = StyledTheme.contentColorFor(containerColor)
            val bubbleContentColor = bubble.contentColor ?: if (bubble.containerColor != null) {
                StyledTheme.contentColorFor(bubbleColor)
            } else {
                windowContentColor
            }
            val visualColor by animateColorAsState(
                if (minimized) bubbleColor else containerColor,
                tween(220),
                label = "floatingWindowColor",
            )
            val visualHeaderColor by animateColorAsState(
                if (minimized) bubbleColor else StyledTheme.colorScheme.surfaceVariant,
                tween(220),
                label = "floatingWindowHeaderColor",
            )
            val visualHeaderContentColor by animateColorAsState(
                if (minimized) bubbleContentColor else
                    StyledTheme.contentColorFor(StyledTheme.colorScheme.surfaceVariant),
                tween(220),
                label = "floatingWindowHeaderContentColor",
            )
            val visualContentColor by animateColorAsState(
                if (minimized) bubbleContentColor else windowContentColor,
                tween(220),
                label = "floatingWindowContentColor",
            )
            val borderColor = bubble.borderColor ?: Color.Transparent
            val visualBorderColor by animateColorAsState(
                if (minimized) borderColor else borderColor.copy(alpha = 0f),
                tween(220),
                label = "floatingWindowBorderColor",
            )
            val visualShape = animatedWindowShape(shape, bubble.shape ?: shape, minimized, visualSize)
            val visualElevation by animateDpAsState(
                if (minimized) bubble.shadowElevation ?: 12.dp else 12.dp,
                tween(220),
                label = "floatingWindowElevation",
            )
    
            if (properties.dismissOnBackPress && !minimized) {
                BackHandler(onBack = onDismissRequest)
            }
            val bringToFrontModifier = Modifier.pointerInput(host, entry) {
                if (host != null && entry != null) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        host.bringToFront(entry)
                    }
                }
            }
            Box(
                Modifier
                    .offset(visualPosition.x, visualPosition.y)
                    .size(visualSize)
                    .then(bringToFrontModifier),
            ) {
                StyledSurface(
                    modifier = Modifier.fillMaxSize().then(modifier),
                    shape = visualShape,
                    color = visualColor,
                    contentColor = visualContentColor,
                    border = if (bubble.borderColor != null) BorderStroke(1.dp, visualBorderColor) else null,
                    shadowElevation = visualElevation,
                ) {
                    Column(Modifier.fillMaxSize()) {
                        Row(
                            Modifier.fillMaxWidth().height(if (minimized) 48.dp else 44.dp)
                                .background(visualHeaderColor),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                Modifier.weight(1f).fillMaxHeight()
                                .pointerInput(
                                    style.draggable,
                                    style.snapToEdge,
                                    minimized,
                                    maximized,
                                    availableSpace,
                                    windowSize,
                                    edgeMargin,
                                ) {
                                    if (style.draggable && !maximized) {
                                        detectDragGestures(
                                            onDragStart = {
                                                dragInProgress = true
                                            },
                                            onDragEnd = {
                                                snapMinimizedBubbleToEdge()
                                                dragInProgress = false
                                            },
                                            onDragCancel = {
                                                snapMinimizedBubbleToEdge()
                                                dragInProgress = false
                                            },
                                            ) { change, dragAmount ->
                                                change.consume()
                                                val delta = with(density) {
                                                    DpOffset(dragAmount.x.toDp(), dragAmount.y.toDp())
                                                }
                                                if (minimized) {
                                                    val position = bubblePositionFraction.toPosition(
                                                        availableSpace,
                                                        bubbleSize,
                                                        edgeMargin,
                                                    )
                                                    bubblePositionFraction = DpOffset(
                                                        position.x + delta.x,
                                                        position.y + delta.y,
                                                    ).clamp(edgeMargin, areaWidth, areaHeight, bubbleSize)
                                                        .toFraction(
                                                            availableSpace,
                                                            bubbleSize,
                                                            edgeMargin,
                                                            bubblePositionFraction,
                                                        )
                                                } else {
                                                    val position = windowPositionFraction.toPosition(
                                                        availableSpace,
                                                        windowSize,
                                                        edgeMargin,
                                                    )
                                                    windowPositionFraction = DpOffset(
                                                        position.x + delta.x,
                                                        position.y + delta.y,
                                                    ).clamp(edgeMargin, areaWidth, areaHeight, windowSize)
                                                        .toFraction(
                                                            availableSpace,
                                                            windowSize,
                                                            edgeMargin,
                                                            windowPositionFraction,
                                                        )
                                                }
                                            }
                                        }
                                    }
                                    .combinedClickable(
                                        enabled = minimized || style.showMaximizeButton,
                                        interactionSource = null,
                                        indication = if (minimized) LocalIndication.current else null,
                                        onClick = {
                                            if (minimized) {
                                                minimized = false
                                            }
                                        },
                                        onDoubleClick = {
                                            if (style.showMaximizeButton && !minimized) {
                                                maximized = !maximized
                                            }
                                        },
                                    )
                                    .padding(
                                        horizontal = if (minimized) 0.dp else StyledTheme.spacing.large,
                                    ),
                                contentAlignment = if (minimized) {
                                    Alignment.Center
                                } else {
                                    Alignment.CenterStart
                                },
                            ) {
                                UpdateStyledThemeLocals(contentColor = visualHeaderContentColor) {
                                    ProvideWindowTitle(title, minimized, style.icon)
                                }
                            }
                            if (!minimized) {
                                if (style.showMinimizeButton) {
                                    WindowControlButton(
                                        "Minimize",
                                        minimizeIcon,
                                        visualHeaderContentColor,
                                    ) {
                                        minimizeWindow()
                                    }
                                }
                                if (style.showMaximizeButton) {
                                    WindowControlButton(
                                        if (maximized) "Restore" else "Maximize",
                                        if (maximized) restoreIcon else maximizeIcon,
                                        visualHeaderContentColor,
                                    ) { maximized = !maximized }
                                }
                                if (style.showCloseButton) {
                                    WindowControlButton(
                                        "Close",
                                        closeIcon,
                                        visualHeaderContentColor,
                                        onDismissRequest,
                                    )
                                }
                            }
                        }
                        Column(
                            Modifier.fillMaxWidth()
                                .weight(1f)
                                .then(
                                    if (minimized) Modifier.height(0.dp).alpha(0f)
                                    else Modifier,
                                ),
                        ) {
                            content(PaddingValues(StyledTheme.spacing.large))
                        }
                        if (!minimized && !buttons.isEmpty) {
                            DialogActions(
                                buttons,
                                StyledDialog.Context.DialogAction,
                                Modifier.fillMaxWidth().padding(
                                    start = StyledTheme.spacing.large,
                                    end = StyledTheme.spacing.large,
                                    bottom = StyledTheme.spacing.medium,
                                ),
                                positiveFirst = positiveActionFirst,
                                itemSpacing = actionSpacing,
                                rowSpacing = actionRowSpacing,
                                stackPositiveFirst = stackPositiveActionFirst,
                            )
                        }
                    }
                }
                if (style.resizable && !maximized && !minimized) {
                    FloatingResizeHandles(
                        onDragStart = { edge ->
                            dragInProgress = true
                            host?.activePointerCursor = edge.pointerCursor
                        },
                        onDragEnd = {
                            dragInProgress = false
                            host?.activePointerCursor = null
                        },
                        onDragCancel = {
                            dragInProgress = false
                            host?.activePointerCursor = null
                        },
                        onDrag = { edge, deltaX, deltaY ->
                            val delta = with(density) { DpOffset(deltaX.toDp(), deltaY.toDp()) }
                            val resized = resizeWindow(
                                edge,
                                windowPositionFraction.toPosition(availableSpace, windowSize, edgeMargin),
                                windowSize,
                                delta,
                                effectiveMin,
                                DpSize(areaWidth, areaHeight), edgeMargin,
                            )
                            windowPositionFraction = resized.first.toFraction(
                                availableSpace,
                                resized.second,
                                edgeMargin,
                                windowPositionFraction,
                            )
                            windowSize = resized.second
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun animatedWindowShape(
    windowShape: Shape,
    bubbleShape: Shape,
    minimized: Boolean,
    size: DpSize,
): Shape {
    val fraction by animateFloatAsState(
        if (minimized) 1f else 0f,
        tween(220),
        label = "floatingWindowShape",
    )
    if (windowShape !is RoundedCornerShape || bubbleShape !is RoundedCornerShape) {
        return if (minimized) bubbleShape else windowShape
    }
    val density = LocalDensity.current
    val pixelSize = with(density) { Size(size.width.toPx(), size.height.toPx()) }
    fun corner(start: CornerSize, end: CornerSize): CornerSize = CornerSize(
        lerp(start.toPx(pixelSize, density), end.toPx(pixelSize, density), fraction),
    )
    return RoundedCornerShape(
        topStart = corner(windowShape.topStart, bubbleShape.topStart),
        topEnd = corner(windowShape.topEnd, bubbleShape.topEnd),
        bottomEnd = corner(windowShape.bottomEnd, bubbleShape.bottomEnd),
        bottomStart = corner(windowShape.bottomStart, bubbleShape.bottomStart),
    )
}

@Composable
@OptIn(InternalComposeStyledApi::class)
private fun ProvideWindowTitle(
    title: (@Composable () -> Unit)?,
    minimized: Boolean,
    icon: IconComposable?,
) {
    if (minimized && icon != null) {
        StyledIcon(
            icon = icon,
            modifier = Modifier.semantics { contentDescription = "Restore window" },
            contentDescription = "Restore window",
        )
    } else {
        UpdateStyledThemeLocals(textStyle = StyledTheme.typography.titleMedium) {
            if (title != null) title() else StyledText("Window")
        }
    }
}

@Composable
private fun WindowControlButton(
    description: String,
    icon: IconComposable,
    tint: Color,
    onClick: () -> Unit,
) {
    StyledIconButton(
        onClick = onClick,
        modifier = Modifier.size(36.dp).semantics { contentDescription = description },
        variant = StyledButton.Variant.Plain,
        icon = icon,
        contentDescription = description,
        iconSize = 16.dp,
        iconPaddingValues = androidx.compose.foundation.layout.PaddingValues(0.dp),
        tint = tint,
    )
}

@Composable
@OptIn(InternalComposeStyledApi::class)
private fun BoxScope.FloatingResizeHandles(
    onDragStart: (ResizeEdge) -> Unit,
    onDragEnd: () -> Unit,
    onDragCancel: () -> Unit,
    onDrag: (ResizeEdge, Float, Float) -> Unit,
) {
    val handleSize = 8.dp
    val cornerSize = 18.dp
    ResizeHandle(ResizeEdge.Left, Modifier.align(Alignment.CenterStart).width(handleSize).fillMaxHeight(), onDragStart, onDragEnd, onDragCancel, onDrag)
    ResizeHandle(ResizeEdge.Right, Modifier.align(Alignment.CenterEnd).width(handleSize).fillMaxHeight(), onDragStart, onDragEnd, onDragCancel, onDrag)
    ResizeHandle(ResizeEdge.Top, Modifier.align(Alignment.TopCenter).fillMaxWidth().height(handleSize), onDragStart, onDragEnd, onDragCancel, onDrag)
    ResizeHandle(ResizeEdge.Bottom, Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(handleSize), onDragStart, onDragEnd, onDragCancel, onDrag)
    ResizeHandle(ResizeEdge.TopLeft, Modifier.align(Alignment.TopStart).size(cornerSize), onDragStart, onDragEnd, onDragCancel, onDrag)
    ResizeHandle(ResizeEdge.TopRight, Modifier.align(Alignment.TopEnd).size(cornerSize), onDragStart, onDragEnd, onDragCancel, onDrag)
    ResizeHandle(ResizeEdge.BottomLeft, Modifier.align(Alignment.BottomStart).size(cornerSize), onDragStart, onDragEnd, onDragCancel, onDrag)
    ResizeHandle(ResizeEdge.BottomRight, Modifier.align(Alignment.BottomEnd).size(cornerSize), onDragStart, onDragEnd, onDragCancel, onDrag)
}

@Composable
@OptIn(InternalComposeStyledApi::class)
private fun BoxScope.ResizeHandle(
    edge: ResizeEdge,
    modifier: Modifier,
    onDragStart: (ResizeEdge) -> Unit,
    onDragEnd: () -> Unit,
    onDragCancel: () -> Unit,
    onDrag: (ResizeEdge, Float, Float) -> Unit,
) {
    Spacer(
        modifier
            .pointerHoverIcon(styledPointerCursor(edge.pointerCursor))
            .pointerInput(edge, onDrag) {
                detectDragGestures(
                    onDragStart = { onDragStart(edge) },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragCancel() },
                ) { change, dragAmount ->
                    change.consume()
                    onDrag(edge, dragAmount.x, dragAmount.y)
                }
            },
    )
}

@OptIn(InternalComposeStyledApi::class)
private enum class ResizeEdge(
    val left: Boolean = false,
    val right: Boolean = false,
    val top: Boolean = false,
    val bottom: Boolean = false,
    val pointerCursor: StyledPointerCursor,
) {
    Left(left = true, pointerCursor = StyledPointerCursor.ResizeHorizontal),
    Right(right = true, pointerCursor = StyledPointerCursor.ResizeHorizontal),
    Top(top = true, pointerCursor = StyledPointerCursor.ResizeVertical),
    Bottom(bottom = true, pointerCursor = StyledPointerCursor.ResizeVertical),
    TopLeft(left = true, top = true, pointerCursor = StyledPointerCursor.ResizeNorthWestSouthEast),
    TopRight(right = true, top = true, pointerCursor = StyledPointerCursor.ResizeNorthEastSouthWest),
    BottomLeft(left = true, bottom = true, pointerCursor = StyledPointerCursor.ResizeNorthEastSouthWest),
    BottomRight(right = true, bottom = true, pointerCursor = StyledPointerCursor.ResizeNorthWestSouthEast),
}

internal data class DpOffset(val x: Dp, val y: Dp) {
    fun clamp(margin: Dp, maxWidth: Dp, maxHeight: Dp, size: DpSize) = DpOffset(
        x.coerceIn(margin, margin + (maxWidth - size.width).coerceAtLeast(0.dp)),
        y.coerceIn(margin, margin + (maxHeight - size.height).coerceAtLeast(0.dp)),
    )

    fun snapToHorizontalEdge(availableSpace: DpSize, itemSize: DpSize, margin: Dp): DpOffset {
        val leftEdge = margin
        val rightEdge = margin + (availableSpace.width - itemSize.width).coerceAtLeast(0.dp)
        return copy(x = if (x - leftEdge <= rightEdge - x) leftEdge else rightEdge)
    }
}

internal data class FloatingPositionFraction(val x: Float, val y: Float) {
    fun toPosition(availableSpace: DpSize, itemSize: DpSize, margin: Dp) = DpOffset(
        margin + (availableSpace.width - itemSize.width).coerceAtLeast(0.dp) * x.coerceIn(0f, 1f),
        margin + (availableSpace.height - itemSize.height).coerceAtLeast(0.dp) * y.coerceIn(0f, 1f),
    )
}

internal fun DpOffset.toFraction(
    availableSpace: DpSize,
    itemSize: DpSize,
    margin: Dp,
    previous: FloatingPositionFraction = FloatingPositionFraction(0f, 0f),
) = FloatingPositionFraction(
    x = normalizedCoordinate(
        position = x,
        margin = margin,
        range = (availableSpace.width - itemSize.width).coerceAtLeast(0.dp),
        previous = previous.x,
    ),
    y = normalizedCoordinate(
        position = y,
        margin = margin,
        range = (availableSpace.height - itemSize.height).coerceAtLeast(0.dp),
        previous = previous.y,
    ),
)

private fun normalizedCoordinate(position: Dp, margin: Dp, range: Dp, previous: Float) =
    if (range > 0.dp) ((position - margin) / range).coerceIn(0f, 1f) else previous

private fun StyledDialog.FloatingPosition.resolve(
    availableSpace: DpSize,
    itemSize: DpSize,
    edgeMargin: Dp,
    density: Density,
    layoutDirection: LayoutDirection,
): DpOffset {
    val availablePixels = with(density) {
        IntSize(availableSpace.width.roundToPx(), availableSpace.height.roundToPx())
    }
    val itemPixels = with(density) {
        IntSize(itemSize.width.roundToPx(), itemSize.height.roundToPx())
    }
    val aligned = alignment.align(itemPixels, availablePixels, layoutDirection)
    return with(density) {
        DpOffset(
            edgeMargin + aligned.x.toDp() + offset.x,
            edgeMargin + aligned.y.toDp() + offset.y,
        )
    }
}

private val DpSizeSaver: Saver<DpSize, List<Float>> = Saver(
    save = { listOf(it.width.value, it.height.value) },
    restore = { DpSize(it[0].dp, it[1].dp) },
)

private val FloatingPositionFractionSaver: Saver<FloatingPositionFraction, List<Float>> = Saver(
    save = { listOf(1f, it.x, it.y) },
    restore = {
        if (it.size == 3 && it[0] == 1f) {
            FloatingPositionFraction(it[1], it[2])
        } else {
            null
        }
    },
)

private fun resizeWindow(
    edge: ResizeEdge,
    position: DpOffset,
    size: DpSize,
    delta: DpOffset,
    minimum: DpSize,
    available: DpSize,
    margin: Dp,
): Pair<DpOffset, DpSize> {
    val nextWidth = when {
        edge.left -> (size.width - delta.x).coerceIn(
            minimum.width,
            (size.width + position.x - margin).coerceAtMost(available.width),
        )
        edge.right -> (size.width + delta.x).coerceIn(
            minimum.width,
            (margin + available.width - position.x).coerceAtMost(available.width),
        )
        else -> size.width
    }
    val nextHeight = when {
        edge.top -> (size.height - delta.y).coerceIn(
            minimum.height,
            (size.height + position.y - margin).coerceAtMost(available.height),
        )
        edge.bottom -> (size.height + delta.y).coerceIn(
            minimum.height,
            (margin + available.height - position.y).coerceAtMost(available.height),
        )
        else -> size.height
    }
    val nextSize = DpSize(nextWidth, nextHeight)
    val nextPosition = DpOffset(
        if (edge.left) position.x + size.width - nextWidth else position.x,
        if (edge.top) position.y + size.height - nextHeight else position.y,
    ).clamp(margin, available.width, available.height, nextSize)
    return nextPosition to nextSize
}

private fun requireFloatingWindowSize(initial: DpSize, minimum: DpSize, margin: Dp) {
    require(initial.width.value.isFinite() && initial.height.value.isFinite() &&
        initial.width > 0.dp && initial.height > 0.dp) {
        "initialSize dimensions must be finite and positive"
    }
    require(minimum.width.value.isFinite() && minimum.height.value.isFinite() &&
        minimum.width > 0.dp && minimum.height > 0.dp) {
        "minSize dimensions must be finite and positive"
    }
    require(initial.width >= minimum.width && initial.height >= minimum.height) {
        "initialSize dimensions must be greater than or equal to minSize"
    }
    require(margin.value.isFinite() && margin >= 0.dp) {
        "edgeMargin must be finite and non-negative"
    }
}

private fun requireFloatingPosition(position: StyledDialog.FloatingPosition, name: String) {
    require(position.offset.x.value.isFinite() && position.offset.y.value.isFinite()) {
        "$name offset must be finite"
    }
}

private val minimizeIcon: IconComposable = { _, modifier, tint ->
    Canvas(modifier) {
        drawLine(tint, Offset(size.width * 0.2f, size.height * 0.75f), Offset(size.width * 0.8f, size.height * 0.75f), 1.5.dp.toPx(), cap = StrokeCap.Round)
    }
}

private val maximizeIcon: IconComposable = { _, modifier, tint ->
    Canvas(modifier) {
        drawRect(tint, topLeft = Offset(size.width * 0.22f, size.height * 0.22f), size = androidx.compose.ui.geometry.Size(size.width * 0.56f, size.height * 0.56f), style = Stroke(1.5.dp.toPx()))
    }
}

private val restoreIcon: IconComposable = { _, modifier, tint ->
    Canvas(modifier) {
        drawRect(tint, topLeft = Offset(size.width * 0.32f, size.height * 0.18f), size = androidx.compose.ui.geometry.Size(size.width * 0.48f, size.height * 0.48f), style = Stroke(1.5.dp.toPx()))
        drawRect(tint, topLeft = Offset(size.width * 0.18f, size.height * 0.34f), size = androidx.compose.ui.geometry.Size(size.width * 0.48f, size.height * 0.48f), style = Stroke(1.5.dp.toPx()))
    }
}

private val closeIcon: IconComposable = { _, modifier, tint ->
    Canvas(modifier) {
        drawLine(tint, Offset(size.width * 0.25f, size.height * 0.25f), Offset(size.width * 0.75f, size.height * 0.75f), 1.5.dp.toPx(), cap = StrokeCap.Round)
        drawLine(tint, Offset(size.width * 0.75f, size.height * 0.25f), Offset(size.width * 0.25f, size.height * 0.75f), 1.5.dp.toPx(), cap = StrokeCap.Round)
    }
}
