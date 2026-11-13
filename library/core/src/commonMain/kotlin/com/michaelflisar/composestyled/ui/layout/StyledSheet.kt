package com.michaelflisar.composestyled.ui.layout

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.components.StyledSurfaceDefaults
import com.michaelflisar.composestyled.ui.internal.dialogInput
import kotlin.math.roundToInt

object StyledSheet {

    enum class Edge { Start, End, Top, Bottom }

    enum class Value { Collapsed, Expanded }

    sealed interface Size {
        @Immutable
        data class Fixed(val value: Dp) : Size {
            init {
                require(value.value.isFinite() && value > 0.dp) {
                    "Sheet size must be finite and greater than zero"
                }
            }
        }

        @Immutable
        data class Fraction(val value: Float) : Size {
            init {
                require(value.isFinite() && value > 0f && value <= 1f) {
                    "Sheet size fraction must be greater than zero and at most one"
                }
            }
        }
    }
}

@Stable
class StyledSheetState internal constructor(initialValue: StyledSheet.Value) {

    var currentValue by mutableStateOf(initialValue)
        private set

    private val mutableProgress = mutableStateOf(
        if (initialValue == StyledSheet.Value.Expanded) 1f else 0f,
    )
    val progress: Float get() = mutableProgress.value

    internal val progressState: MutableState<Float> get() = mutableProgress

    fun expand() = set(StyledSheet.Value.Expanded)

    fun collapse() = set(StyledSheet.Value.Collapsed)

    fun set(value: StyledSheet.Value) {
        currentValue = value
    }

    companion object {
        val Saver: Saver<StyledSheetState, Boolean> = Saver(
            save = { it.currentValue == StyledSheet.Value.Expanded },
            restore = {
                StyledSheetState(
                    if (it) StyledSheet.Value.Expanded else StyledSheet.Value.Collapsed,
                )
            },
        )
    }
}

@Composable
fun rememberStyledSheetState(
    initialValue: StyledSheet.Value = StyledSheet.Value.Collapsed,
): StyledSheetState = rememberSaveable(saver = StyledSheetState.Saver) {
    StyledSheetState(initialValue)
}

/**
 * Shows a modal sheet over the surrounding content. Use [rememberStyledSheetState] for saveable
 * state; it starts collapsed and can also be opened by swiping inward from the selected edge.
 */
@Composable
fun StyledSheet(
    state: StyledSheetState = rememberStyledSheetState(),
    modifier: Modifier = Modifier,
    edge: StyledSheet.Edge = StyledSheet.Edge.Start,
    expandedSize: StyledSheet.Size = StyledSheet.Size.Fraction(0.8f),
    triggerSize: Dp = 24.dp,
    gesturesEnabled: Boolean = true,
    shape: Shape = StyledSurfaceDefaults.shape,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    scrimColor: Color = Color.Black.copy(alpha = 0.32f),
    shadowElevation: Dp = 8.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    require(triggerSize.value.isFinite() && triggerSize >= 0.dp) {
        "Sheet trigger size must be finite and non-negative"
    }
    val expanded = state.currentValue == StyledSheet.Value.Expanded
    SheetLayout(
        expanded = expanded,
        onExpandedChange = { value ->
            state.set(
                if (value) StyledSheet.Value.Expanded else StyledSheet.Value.Collapsed,
            )
        },
        modifier = modifier,
        edge = edge,
        expandedSize = expandedSize,
        triggerSize = triggerSize,
        gesturesEnabled = gesturesEnabled,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        scrimColor = scrimColor,
        shadowElevation = shadowElevation,
        state = state,
        content = content,
    )
}

@OptIn(
    ExperimentalComposeUiApi::class,
    InternalComposeStyledApi::class,
)
@Composable
private fun SheetLayout(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier,
    edge: StyledSheet.Edge,
    expandedSize: StyledSheet.Size,
    triggerSize: Dp,
    gesturesEnabled: Boolean,
    shape: Shape,
    containerColor: Color,
    contentColor: Color,
    scrimColor: Color,
    shadowElevation: Dp,
    state: StyledSheetState,
    content: @Composable ColumnScope.() -> Unit,
) {
    BackHandler(enabled = expanded) { onExpandedChange(false) }

    val inputModifier = if (expanded) {
        Modifier.dialogInput(
            dismissOnBackPress = true,
            onDismissRequest = { onExpandedChange(false) },
        )
    } else {
        Modifier
    }
    // Keep sibling sharing attached throughout the drag; removing the node as progress changes can cancel it.
    val siblingPointerInputModifier = if (
        !expanded && gesturesEnabled && triggerSize > 0.dp
    ) {
        Modifier.sharePointerInputWithSiblings()
    } else {
        Modifier
    }
    BoxWithConstraints(
        modifier.then(inputModifier).then(siblingPointerInputModifier).fillMaxSize(),
    ) {
        require(constraints.hasBoundedWidth && constraints.hasBoundedHeight) {
            "StyledSheet requires a bounded width and height"
        }
        if (maxWidth == 0.dp || maxHeight == 0.dp) return@BoxWithConstraints
        val density = LocalDensity.current
        val layoutDirection = LocalLayoutDirection.current
        val isHorizontal = edge == StyledSheet.Edge.Start || edge == StyledSheet.Edge.End
        val availableSize = if (isHorizontal) maxWidth else maxHeight
        val sheetSize = when (expandedSize) {
            is StyledSheet.Size.Fixed -> minOf(expandedSize.value, availableSize)
            is StyledSheet.Size.Fraction -> availableSize * expandedSize.value
        }
        val sizePx = with(density) { sheetSize.toPx() }
        val velocityThreshold = with(density) { 400.dp.toPx() }
        val openDirection = when (edge) {
            StyledSheet.Edge.Start ->
                if (layoutDirection == LayoutDirection.Ltr) 1f else -1f

            StyledSheet.Edge.End ->
                if (layoutDirection == LayoutDirection.Ltr) -1f else 1f

            StyledSheet.Edge.Top -> 1f
            StyledSheet.Edge.Bottom -> -1f
        }
        val alignment = when (edge) {
            StyledSheet.Edge.Start ->
                if (layoutDirection == LayoutDirection.Ltr) Alignment.CenterStart else Alignment.CenterEnd

            StyledSheet.Edge.End ->
                if (layoutDirection == LayoutDirection.Ltr) Alignment.CenterEnd else Alignment.CenterStart

            StyledSheet.Edge.Top -> Alignment.TopCenter
            StyledSheet.Edge.Bottom -> Alignment.BottomCenter
        }
        var progress by state.progressState
        var dragging by remember { mutableStateOf(false) }
        var edgeDragging by remember { mutableStateOf(false) }

        LaunchedEffect(expanded, dragging) {
            if (!dragging) {
                animate(
                    initialValue = progress,
                    targetValue = if (expanded) 1f else 0f,
                    animationSpec = tween(durationMillis = 250),
                ) { value, _ -> progress = value }
            }
        }

        val dragState = rememberDraggableState { delta ->
            progress = (progress + delta * openDirection / sizePx).coerceIn(0f, 1f)
        }

        fun settleDrag(velocity: Float) {
            val targetExpanded = when {
                velocity * openDirection >= velocityThreshold -> true
                velocity * openDirection <= -velocityThreshold -> false
                else -> progress >= 0.5f
            }
            dragging = false
            onExpandedChange(targetExpanded)
        }

        val dragModifier = Modifier.draggable(
            state = dragState,
            orientation = if (isHorizontal) Orientation.Horizontal else Orientation.Vertical,
            enabled = gesturesEnabled,
            onDragStarted = { dragging = true },
            onDragStopped = { settleDrag(it) },
        )
        val edgeDragModifier = Modifier.draggable(
            state = dragState,
            orientation = if (isHorizontal) Orientation.Horizontal else Orientation.Vertical,
            enabled = gesturesEnabled,
            onDragStarted = {
                dragging = true
                edgeDragging = true
            },
            onDragStopped = {
                edgeDragging = false
                settleDrag(it)
            },
        )

        if (progress > 0f) {
            Box(
                Modifier
                    .fillMaxSize()
                    .zIndex(1f)
                    .background(scrimColor.copy(alpha = scrimColor.alpha * progress))
                    .clickable(interactionSource = null, indication = null) {
                        onExpandedChange(false)
                    }
                    .semantics {
                        dismiss {
                            onExpandedChange(false)
                            true
                        }
                    },
            )
        }

        if (progress > 0f || expanded) {
            val sheetSizeModifier = if (isHorizontal) {
                Modifier.width(sheetSize).fillMaxHeight()
            } else {
                Modifier.fillMaxWidth().height(sheetSize)
            }
            StyledSurface(
                modifier = Modifier
                    .align(alignment)
                    .then(sheetSizeModifier)
                    .offset {
                        val offset = (-openDirection * sizePx * (1f - progress)).roundToInt()
                        if (isHorizontal) IntOffset(x = offset, y = 0) else IntOffset(x = 0, y = offset)
                    }
                    .zIndex(2f)
                    .then(dragModifier)
                    .semantics {
                        if (expanded) {
                            dismiss {
                                onExpandedChange(false)
                                true
                            }
                        }
                    },
                shape = shape,
                color = containerColor,
                contentColor = contentColor,
                shadowElevation = shadowElevation,
            ) {
                Column(Modifier.fillMaxSize(), content = content)
            }
        }

        if (!expanded && (progress == 0f || edgeDragging) &&
            gesturesEnabled && triggerSize > 0.dp
        ) {
            val triggerModifier = if (isHorizontal) {
                Modifier.width(minOf(triggerSize, maxWidth)).fillMaxHeight()
            } else {
                Modifier.fillMaxWidth().height(minOf(triggerSize, maxHeight))
            }
            Box(
                Modifier
                    .align(alignment)
                    .then(triggerModifier)
                    .zIndex(3f)
                    .then(edgeDragModifier)
                    .semantics {
                        expand {
                            onExpandedChange(true)
                            true
                        }
                    },
            )
        }
    }
}

private fun Modifier.sharePointerInputWithSiblings(): Modifier =
    then(SharePointerInputWithSiblingsElement)

private data object SharePointerInputWithSiblingsElement :
    ModifierNodeElement<SharePointerInputWithSiblingsNode>() {

    override fun create() = SharePointerInputWithSiblingsNode()

    override fun update(node: SharePointerInputWithSiblingsNode) = Unit

    override fun InspectorInfo.inspectableProperties() {
        name = "sharePointerInputWithSiblings"
    }
}

private class SharePointerInputWithSiblingsNode : Modifier.Node(), PointerInputModifierNode {

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize,
    ) = Unit

    override fun onCancelPointerInput() = Unit

    override fun sharePointerInputWithSiblings() = true
}
