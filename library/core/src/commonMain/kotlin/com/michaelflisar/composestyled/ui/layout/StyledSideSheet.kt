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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.components.StyledSurfaceDefaults
import com.michaelflisar.composestyled.ui.internal.dialogInput
import kotlin.math.roundToInt

object StyledSideSheet {

    enum class Side { Start, End }

    sealed interface Width {
        @Immutable
        data class Fixed(val value: Dp) : Width {
            init {
                require(value.value.isFinite() && value > 0.dp) {
                    "Side sheet width must be finite and greater than zero"
                }
            }
        }

        @Immutable
        data class Fraction(val value: Float) : Width {
            init {
                require(value.isFinite() && value > 0f && value <= 1f) {
                    "Side sheet width fraction must be greater than zero and at most one"
                }
            }
        }
    }
}

/**
 * Shows a modal side sheet over the surrounding content. The default state is saveable and starts
 * collapsed; swipe inward from the selected edge to open it.
 */
@Composable
fun StyledSideSheet(
    expanded: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    modifier: Modifier = Modifier,
    side: StyledSideSheet.Side = StyledSideSheet.Side.Start,
    expandedWidth: StyledSideSheet.Width = StyledSideSheet.Width.Fraction(0.8f),
    triggerWidth: Dp = 24.dp,
    gesturesEnabled: Boolean = true,
    shape: Shape = StyledSurfaceDefaults.shape,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    scrimColor: Color = Color.Black.copy(alpha = 0.32f),
    shadowElevation: Dp = 8.dp,
    content: @Composable ColumnScope.() -> Unit,
) = StyledSideSheet(
    expanded = expanded.value,
    onExpandedChange = { expanded.value = it },
    modifier = modifier,
    side = side,
    expandedWidth = expandedWidth,
    triggerWidth = triggerWidth,
    gesturesEnabled = gesturesEnabled,
    shape = shape,
    containerColor = containerColor,
    contentColor = contentColor,
    scrimColor = scrimColor,
    shadowElevation = shadowElevation,
    content = content,
)

/** Controlled side-sheet state overload. */
@Composable
fun StyledSideSheet(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    side: StyledSideSheet.Side = StyledSideSheet.Side.Start,
    expandedWidth: StyledSideSheet.Width = StyledSideSheet.Width.Fraction(0.8f),
    triggerWidth: Dp = 32.dp,
    gesturesEnabled: Boolean = true,
    shape: Shape = StyledSurfaceDefaults.shape,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    scrimColor: Color = Color.Black.copy(alpha = 0.32f),
    shadowElevation: Dp = 8.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    require(triggerWidth.value.isFinite() && triggerWidth >= 0.dp) {
        "Side sheet trigger width must be finite and non-negative"
    }
    SideSheetLayout(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
        side = side,
        expandedWidth = expandedWidth,
        triggerWidth = triggerWidth,
        gesturesEnabled = gesturesEnabled,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        scrimColor = scrimColor,
        shadowElevation = shadowElevation,
        content = content,
    )
}

@OptIn(
    ExperimentalComposeUiApi::class,
    InternalComposeStyledApi::class,
)
@Composable
private fun SideSheetLayout(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier,
    side: StyledSideSheet.Side,
    expandedWidth: StyledSideSheet.Width,
    triggerWidth: Dp,
    gesturesEnabled: Boolean,
    shape: Shape,
    containerColor: Color,
    contentColor: Color,
    scrimColor: Color,
    shadowElevation: Dp,
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
    BoxWithConstraints(modifier.then(inputModifier).fillMaxSize()) {
        require(constraints.hasBoundedWidth && constraints.hasBoundedHeight) {
            "StyledSideSheet requires a bounded width and height"
        }
        if (maxWidth == 0.dp) return@BoxWithConstraints
        val density = LocalDensity.current
        val layoutDirection = LocalLayoutDirection.current
        val sheetWidth = when (expandedWidth) {
            is StyledSideSheet.Width.Fixed -> minOf(expandedWidth.value, maxWidth)
            is StyledSideSheet.Width.Fraction -> maxWidth * expandedWidth.value
        }
        val widthPx = with(density) { sheetWidth.toPx() }
        val velocityThreshold = with(density) { 400.dp.toPx() }
        val openDirection = when (side) {
            StyledSideSheet.Side.Start ->
                if (layoutDirection == LayoutDirection.Ltr) 1f else -1f

            StyledSideSheet.Side.End ->
                if (layoutDirection == LayoutDirection.Ltr) -1f else 1f
        }
        val alignment = when (side) {
            StyledSideSheet.Side.Start -> Alignment.CenterStart
            StyledSideSheet.Side.End -> Alignment.CenterEnd
        }
        var progress by remember { mutableFloatStateOf(if (expanded) 1f else 0f) }
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
            progress = (progress + delta * openDirection / widthPx).coerceIn(0f, 1f)
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
            orientation = Orientation.Horizontal,
            enabled = gesturesEnabled,
            onDragStarted = { dragging = true },
            onDragStopped = { settleDrag(it) },
        )
        val edgeDragModifier = Modifier.draggable(
            state = dragState,
            orientation = Orientation.Horizontal,
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
            StyledSurface(
                modifier = Modifier
                    .align(alignment)
                    .width(sheetWidth)
                    .fillMaxHeight()
                    .offset {
                        IntOffset(
                            x = (-openDirection * widthPx * (1f - progress)).roundToInt(),
                            y = 0,
                        )
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
            gesturesEnabled && triggerWidth > 0.dp
        ) {
            Box(
                Modifier
                    .align(alignment)
                    .width(minOf(triggerWidth, maxWidth))
                    .fillMaxHeight()
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
