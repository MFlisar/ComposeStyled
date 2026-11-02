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
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
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
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.internal.DialogActions
import com.michaelflisar.composestyled.ui.internal.StyledPointerCursor
import com.michaelflisar.composestyled.ui.internal.styledPointerCursor
import com.michaelflisar.composestyled.ui.layout.LocalFloatingDialogEntry
import com.michaelflisar.composestyled.ui.layout.LocalFloatingDialogHost
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.primitives.StyledText

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
        var windowPosition by rememberSaveable(stateSaver = DpOffsetSaver) {
            mutableStateOf(startPosition)
        }
        var bubblePosition by rememberSaveable(stateSaver = DpOffsetSaver) {
            mutableStateOf(startBubblePosition)
        }
        var hasBubblePosition by rememberSaveable {
            mutableStateOf(style.initialBubblePosition != null || initiallyMinimized)
        }
        var minimized by rememberSaveable { mutableStateOf(initiallyMinimized) }
        var maximized by rememberSaveable { mutableStateOf(false) }
        SideEffect {
            val boundedSize = DpSize(
                windowSize.width.coerceIn(effectiveMin.width, areaWidth),
                windowSize.height.coerceIn(effectiveMin.height, areaHeight),
            )
            val boundedPosition = windowPosition.clamp(edgeMargin, areaWidth, areaHeight, boundedSize)
            val boundedBubble = bubblePosition.clamp(
                edgeMargin,
                areaWidth,
                areaHeight,
                DpSize(48.dp, 48.dp),
            )
            if (boundedSize != windowSize) windowSize = boundedSize
            if (boundedPosition != windowPosition) windowPosition = boundedPosition
            if (boundedBubble != bubblePosition) bubblePosition = boundedBubble
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
                            .pointerInput(style.draggable, minimized, maximized) {
                                if (style.draggable && !maximized) {
                                    detectDragGestures(
                                        onDragStart = {
                                            dragInProgress = true
                                        },
                                        onDragEnd = { dragInProgress = false },
                                        onDragCancel = { dragInProgress = false },
                                        ) { change, dragAmount ->
                                            change.consume()
                                            val delta = with(density) {
                                                DpOffset(dragAmount.x.toDp(), dragAmount.y.toDp())
                                            }
                                            if (minimized) {
                                                bubblePosition = DpOffset(
                                                    bubblePosition.x + delta.x,
                                                    bubblePosition.y + delta.y,
                                                ).clamp(edgeMargin, areaWidth, areaHeight, bubbleSize)
                                            } else {
                                                windowPosition = DpOffset(
                                                    windowPosition.x + delta.x,
                                                    windowPosition.y + delta.y,
                                                ).clamp(edgeMargin, areaWidth, areaHeight, windowSize)
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
                            ProvideWindowTitle(title, minimized, style.icon)
                        }
                        if (!minimized) {
                            if (style.showMinimizeButton) {
                                WindowControlButton("Minimize", minimizeIcon) {
                                    if (!hasBubblePosition) {
                                        bubblePosition = DpOffset(
                                            targetPosition.x + (targetSize.width - bubbleSize.width) / 2f,
                                            targetPosition.y + (targetSize.height - bubbleSize.height) / 2f,
                                        ).clamp(edgeMargin, areaWidth, areaHeight, bubbleSize)
                                        hasBubblePosition = true
                                    }
                                    minimized = true
                                }
                            }
                            if (style.showMaximizeButton) {
                                WindowControlButton(
                                    if (maximized) "Restore" else "Maximize",
                                    if (maximized) restoreIcon else maximizeIcon,
                                ) { maximized = !maximized }
                            }
                            if (style.showCloseButton) {
                                WindowControlButton("Close", closeIcon, onDismissRequest)
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
                            edge, windowPosition, windowSize, delta, effectiveMin,
                            DpSize(areaWidth, areaHeight), edgeMargin,
                        )
                        windowPosition = resized.first
                        windowSize = resized.second
                    },
                )
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
    onClick: () -> Unit,
) {
    StyledIconButton(
        onClick = onClick,
        modifier = Modifier.size(36.dp).semantics { contentDescription = description },
        role = StyledIconButton.Role.Tertiary,
        icon = icon,
        contentDescription = description,
        iconSize = 16.dp,
        iconPaddingValues = androidx.compose.foundation.layout.PaddingValues(0.dp),
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

private data class DpOffset(val x: Dp, val y: Dp) {
    fun clamp(margin: Dp, maxWidth: Dp, maxHeight: Dp, size: DpSize) = DpOffset(
        x.coerceIn(margin, margin + (maxWidth - size.width).coerceAtLeast(0.dp)),
        y.coerceIn(margin, margin + (maxHeight - size.height).coerceAtLeast(0.dp)),
    )
}

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

private val DpOffsetSaver: Saver<DpOffset, List<Float>> = Saver(
    save = { listOf(it.x.value, it.y.value) },
    restore = { DpOffset(it[0].dp, it[1].dp) },
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
