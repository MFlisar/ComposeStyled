package com.michaelflisar.composestyled.ui.layout

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.internal.StyledPointerCursor
import com.michaelflisar.composestyled.ui.internal.styledPointerCursor

/**
 * The mutable first-pane size used by [StyledSplitPanel].
 *
 * Set [firstPaneSize] to adjust the split position programmatically.
 */
@Stable
class StyledSplitPanelState internal constructor(initialFirstPaneSize: Dp) {

    private var firstPaneSizeState by mutableStateOf(validatePaneSize(initialFirstPaneSize))

    var firstPaneSize: Dp
        get() = firstPaneSizeState
        set(value) {
            firstPaneSizeState = validatePaneSize(value)
        }

}

/**
 * Creates and remembers a saveable [StyledSplitPanelState].
 */
@Composable
fun rememberStyledSplitPanelState(
    initialFirstPaneSize: Dp = 240.dp,
): StyledSplitPanelState = rememberSaveable(
    initialFirstPaneSize.value,
    saver = StyledSplitPanelStateSaver,
) {
    StyledSplitPanelState(initialFirstPaneSize)
}

private val StyledSplitPanelStateSaver: Saver<StyledSplitPanelState, Float> = Saver(
    save = { it.firstPaneSize.value },
    restore = { StyledSplitPanelState(it.dp) },
)

/**
 * Displays two panes separated by a draggable divider with an interrupted-line grip handle.
 *
 * The available width or height must be bounded. Pane size limits are measured along the split
 * axis. When both minimum sizes cannot fit, the available pane space is divided in proportion to
 * those minimum sizes.
 *
 * [Orientation.Horizontal] places the panes side by side; [Orientation.Vertical] stacks them.
 * Without size limits, the first pane uses [StyledSplitPanelState.firstPaneSize] (240 dp by default),
 * clamped to the available pane space. The second pane fills the remainder after the divider hit area.
 */
@OptIn(ExperimentalFoundationApi::class, InternalComposeStyledApi::class)
@Composable
fun StyledSplitPanel(
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Horizontal,
    state: StyledSplitPanelState = rememberStyledSplitPanelState(),
    firstPaneMinSize: Dp = 0.dp,
    firstPaneMaxSize: Dp = Dp.Infinity,
    secondPaneMinSize: Dp = 0.dp,
    secondPaneMaxSize: Dp = Dp.Infinity,
    dividerHitSize: Dp = 24.dp,
    dividerThickness: Dp = 4.dp,
    firstPaneContent: @Composable BoxScope.() -> Unit,
    secondPaneContent: @Composable BoxScope.() -> Unit,
) {
    validatePaneBounds("firstPane", firstPaneMinSize, firstPaneMaxSize)
    validatePaneBounds("secondPane", secondPaneMinSize, secondPaneMaxSize)
    require(dividerHitSize.value.isFinite() && dividerHitSize > 0.dp) {
        "Divider hit size must be finite and greater than zero"
    }
    require(dividerThickness.value.isFinite() && dividerThickness >= 0.dp) {
        "Divider thickness must be finite and non-negative"
    }

    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val dragHandleColor = StyledTheme.colorScheme.outlineVariant
    val pointerCursorHost = LocalFloatingDialogHost.current
    val resizeCursor = if (orientation == Orientation.Horizontal) {
        StyledPointerCursor.ResizeHorizontal
    } else {
        StyledPointerCursor.ResizeVertical
    }
    var isDragging by remember { mutableStateOf(false) }
    val panelPointerIcon = if (isDragging) {
        styledPointerCursor(resizeCursor)
    } else {
        PointerIcon.Default
    }

    BoxWithConstraints(
        modifier.fillMaxSize().pointerHoverIcon(
            panelPointerIcon,
            overrideDescendants = isDragging,
        ),
    ) {
        val totalExtent = when (orientation) {
            Orientation.Horizontal -> maxWidth
            Orientation.Vertical -> maxHeight
        }
        require(totalExtent.value.isFinite()) {
            "StyledSplitPanel requires a bounded width or height along its split axis"
        }

        val actualDividerHitSize = minOf(dividerHitSize, totalExtent)
        val availablePaneSize = (totalExtent - actualDividerHitSize).coerceAtLeast(0.dp)
        val firstPaneSize = resolveFirstPaneSize(
            requestedSize = state.firstPaneSize,
            availableSize = availablePaneSize,
            firstMinSize = firstPaneMinSize,
            firstMaxSize = firstPaneMaxSize,
            secondMinSize = secondPaneMinSize,
            secondMaxSize = secondPaneMaxSize,
        )
        val secondPaneSize = resolveSecondPaneSize(
            firstSize = firstPaneSize,
            availableSize = availablePaneSize,
            firstMinSize = firstPaneMinSize,
            firstMaxSize = firstPaneMaxSize,
            secondMinSize = secondPaneMinSize,
            secondMaxSize = secondPaneMaxSize,
        )
        val canDrag = canResizeFirstPane(
            availableSize = availablePaneSize,
            firstMinSize = firstPaneMinSize,
            firstMaxSize = firstPaneMaxSize,
            secondMinSize = secondPaneMinSize,
            secondMaxSize = secondPaneMaxSize,
        )
        val dragState = rememberDraggableState { delta ->
            val deltaDp = with(density) { delta.toDp() }
            state.firstPaneSize = resolveFirstPaneSize(
                requestedSize = state.firstPaneSize + deltaDp,
                availableSize = availablePaneSize,
                firstMinSize = firstPaneMinSize,
                firstMaxSize = firstPaneMaxSize,
                secondMinSize = secondPaneMinSize,
                secondMaxSize = secondPaneMaxSize,
            )
        }
        val dividerModifier = Modifier
            .then(
                if (canDrag) {
                    Modifier.pointerHoverIcon(styledPointerCursor(resizeCursor))
                } else {
                    Modifier
                },
            )
            .then(
                if (orientation == Orientation.Horizontal) {
                    Modifier.width(actualDividerHitSize).fillMaxHeight()
                } else {
                    Modifier.fillMaxWidth().height(actualDividerHitSize)
                }
            )
            .draggable(
                state = dragState,
                orientation = orientation,
                enabled = canDrag,
                reverseDirection = orientation == Orientation.Horizontal &&
                    layoutDirection == LayoutDirection.Rtl,
                onDragStarted = {
                    isDragging = true
                    pointerCursorHost?.activePointerCursor = resizeCursor
                },
                onDragStopped = {
                    isDragging = false
                    pointerCursorHost?.activePointerCursor = null
                },
            )
        val dividerContent: @Composable BoxScope.() -> Unit = {
            Canvas(Modifier.fillMaxSize()) {
                drawSplitPanelHandle(
                    orientation = orientation,
                    thickness = dividerThickness,
                    color = dragHandleColor,
                )
            }
        }

        when (orientation) {
            Orientation.Horizontal -> Row(Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.width(firstPaneSize).fillMaxHeight(),
                    content = firstPaneContent,
                )
                Box(dividerModifier, content = dividerContent)
                Box(
                    modifier = Modifier.width(secondPaneSize).fillMaxHeight(),
                    content = secondPaneContent,
                )
            }

            Orientation.Vertical -> Column(Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(firstPaneSize),
                    content = firstPaneContent,
                )
                Box(dividerModifier, content = dividerContent)
                Box(
                    modifier = Modifier.fillMaxWidth().height(secondPaneSize),
                    content = secondPaneContent,
                )
            }
        }
    }
}

private fun DrawScope.drawSplitPanelHandle(
    orientation: Orientation,
    thickness: Dp,
    color: Color,
) {
    val panesAreSideBySide = orientation == Orientation.Horizontal
    if (thickness <= 0.dp || size.width <= 0f || size.height <= 0f) {
        return
    }

    val dotDiameter = minOf(
        thickness.toPx(),
        if (panesAreSideBySide) size.width else size.height,
    ).coerceAtMost((if (panesAreSideBySide) size.width else size.height) / 5f)
    val dotSpacing = dotDiameter * 2f
    val dotGap = dotSpacing - dotDiameter
    val dividerLineWidth = 1.dp.toPx()
    val gapHalfLength = minOf(
        dotSpacing + dotDiameter / 2f + dotGap + dividerLineWidth / 2f,
        (if (panesAreSideBySide) size.height else size.width) / 2f,
    )
    val center = Offset(size.width / 2f, size.height / 2f)

    if (panesAreSideBySide) {
        drawLine(
            color = color,
            start = Offset(center.x, 0f),
            end = Offset(center.x, center.y - gapHalfLength),
            strokeWidth = dividerLineWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(center.x, center.y + gapHalfLength),
            end = Offset(center.x, size.height),
            strokeWidth = dividerLineWidth,
            cap = StrokeCap.Round,
        )
    } else {
        drawLine(
            color = color,
            start = Offset(0f, center.y),
            end = Offset(center.x - gapHalfLength, center.y),
            strokeWidth = 1.dp.toPx(),
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(center.x + gapHalfLength, center.y),
            end = Offset(size.width, center.y),
            strokeWidth = 1.dp.toPx(),
            cap = StrokeCap.Round,
        )
    }

    for (index in -1..1) {
        val dotCenter = if (panesAreSideBySide) {
            Offset(center.x, center.y + dotSpacing * index)
        } else {
            Offset(center.x + dotSpacing * index, center.y)
        }
        drawCircle(
            color = color,
            radius = dotDiameter / 2f,
            center = dotCenter,
        )
    }
}

private fun validatePaneSize(size: Dp): Dp {
    require(size.value.isFinite() && size >= 0.dp) {
        "Pane size must be finite and non-negative"
    }
    return size
}

private fun validatePaneBounds(
    paneName: String,
    minSize: Dp,
    maxSize: Dp,
) {
    require(minSize.value.isFinite() && minSize >= 0.dp) {
        "$paneName minimum size must be finite and non-negative"
    }
    require((maxSize.value.isFinite() || maxSize == Dp.Infinity) && maxSize >= 0.dp) {
        "$paneName maximum size must be non-negative and finite or Dp.Infinity"
    }
    require(maxSize >= minSize) {
        "$paneName maximum size must be greater than or equal to its minimum size"
    }
}

private fun resolveFirstPaneSize(
    requestedSize: Dp,
    availableSize: Dp,
    firstMinSize: Dp,
    firstMaxSize: Dp,
    secondMinSize: Dp,
    secondMaxSize: Dp,
): Dp {
    if (availableSize < firstMinSize + secondMinSize) {
        val largestMinimum = maxOf(firstMinSize.value, secondMinSize.value)
        val firstRatio = (firstMinSize.value / largestMinimum) /
            (firstMinSize.value / largestMinimum + secondMinSize.value / largestMinimum)
        return availableSize * firstRatio
    }

    if (firstMaxSize + secondMaxSize < availableSize) {
        return requestedSize.coerceIn(firstMinSize, firstMaxSize)
    }

    val minimum = maxOf(firstMinSize, availableSize - secondMaxSize)
    val maximum = minOf(firstMaxSize, availableSize - secondMinSize)
    return requestedSize.coerceIn(minimum, maximum)
}

private fun resolveSecondPaneSize(
    firstSize: Dp,
    availableSize: Dp,
    firstMinSize: Dp,
    firstMaxSize: Dp,
    secondMinSize: Dp,
    secondMaxSize: Dp,
): Dp {
    if (availableSize < firstMinSize + secondMinSize) {
        return availableSize - firstSize
    }

    val remainingSize = availableSize - firstSize
    return if (firstMaxSize + secondMaxSize < availableSize) {
        remainingSize.coerceIn(secondMinSize, secondMaxSize)
    } else {
        remainingSize
    }
}

private fun canResizeFirstPane(
    availableSize: Dp,
    firstMinSize: Dp,
    firstMaxSize: Dp,
    secondMinSize: Dp,
    secondMaxSize: Dp,
): Boolean {
    if (availableSize < firstMinSize + secondMinSize) {
        return false
    }

    if (firstMaxSize + secondMaxSize < availableSize) {
        return firstMaxSize > firstMinSize
    }

    val minimum = maxOf(firstMinSize, availableSize - secondMaxSize)
    val maximum = minOf(firstMaxSize, availableSize - secondMinSize)
    return maximum > minimum
}
