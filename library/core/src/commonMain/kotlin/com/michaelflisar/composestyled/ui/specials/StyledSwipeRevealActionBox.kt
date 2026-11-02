package com.michaelflisar.composestyled.ui.specials

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalPointerSlopOrCancellation
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.CustomColors
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledButtonDefaults
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.primitives.StyledText
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

object StyledSwipeRevealActionBox {
    enum class Context : StyledContext {
        RevealAction,
    }
}

@Stable
class StyledSwipeRevealActionContext internal constructor(
    val side: StyledSwipeActionSide,
    val progress: Float,
    val isOpen: Boolean,
)

@Stable
class StyledSwipeRevealActionScope internal constructor(
    val side: StyledSwipeActionSide,
    val progress: Float,
    val isOpen: Boolean,
)

@Stable
class StyledSwipeRevealAction internal constructor(
    val label: String?,
    val onClick: (StyledSwipeRevealActionContext) -> Unit,
    val enabled: Boolean,
    val backgroundColor: Color?,
    val contentColor: Color?,
    val icon: IconComposable?,
    val content: (@Composable (scope: StyledSwipeRevealActionScope) -> Unit)?,
) {
    constructor(
        label: String,
        onClick: (StyledSwipeRevealActionContext) -> Unit,
        enabled: Boolean = true,
        backgroundColor: Color? = null,
        contentColor: Color? = null,
        icon: IconComposable? = null,
    ) : this(
        label = label,
        onClick = onClick,
        enabled = enabled,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        icon = icon,
        content = null,
    )

    constructor(
        onClick: (StyledSwipeRevealActionContext) -> Unit,
        enabled: Boolean = true,
        backgroundColor: Color? = null,
        contentColor: Color? = null,
        content: @Composable (scope: StyledSwipeRevealActionScope) -> Unit,
    ) : this(
        label = null,
        onClick = onClick,
        enabled = enabled,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        icon = null,
        content = content,
    )
}

/** Reveals one or more actions from either edge while keeping the content in place. */
@Composable
@OptIn(InternalComposeStyledApi::class)
fun StyledSwipeRevealActionBox(
    modifier: Modifier = Modifier,
    actionsFromStart: List<StyledSwipeRevealAction> = emptyList(),
    actionsFromEnd: List<StyledSwipeRevealAction> = emptyList(),
    shape: Shape = StyledSwipeActionsDefaults.shape,
    backgroundColor: Color = StyledSwipeActionsDefaults.backgroundColor,
    contentColor: Color = StyledSwipeActionsDefaults.contentColor,
    revealBackgroundColor: Color = backgroundColor,
    revealContentColor: Color = contentColor,
    contentPadding: PaddingValues = StyledSwipeActionsDefaults.contentPadding,
    contentAlignment: Alignment = StyledSwipeActionsDefaults.contentAlignment,
    actionSpacing: Dp = StyledSwipeActionsDefaults.actionSpacing,
    overscrollBehavior: StyledSwipeOverscrollBehavior = StyledSwipeActionsDefaults.overscrollBehavior,
    content: @Composable () -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current
    val scope = rememberCoroutineScope()
    val currentOverscrollBehavior = rememberUpdatedState(overscrollBehavior)
    val currentStartActions = rememberUpdatedState(actionsFromStart)
    val currentEndActions = rememberUpdatedState(actionsFromEnd)
    var offsetX by remember { mutableFloatStateOf(0f) }
    var size by remember { mutableStateOf(IntSize.Zero) }
    var startActionsWidth by remember { mutableIntStateOf(0) }
    var endActionsWidth by remember { mutableIntStateOf(0) }
    var openSide by remember { mutableStateOf<StyledSwipeActionSide?>(null) }
    val settleJob = remember { mutableStateOf<Job?>(null) }
    val revealSide = swipeActionSideForOffset(offsetX, layoutDirection) ?: openSide
    val activeActionWidth = when (revealSide) {
        StyledSwipeActionSide.Start -> startActionsWidth
        StyledSwipeActionSide.End -> endActionsWidth
        null -> 0
    }
    val revealProgress = if (activeActionWidth > 0) {
        (abs(offsetX) / activeActionWidth).coerceIn(0f, 1f)
    } else {
        0f
    }
    val actionVerticalPadding = if (actionsFromStart.isNotEmpty() || actionsFromEnd.isNotEmpty()) {
        contentPadding.calculateTopPadding() + contentPadding.calculateBottomPadding()
    } else {
        0.dp
    }
    require(actionSpacing >= 0.dp) { "actionSpacing must not be negative." }

    fun animateTo(target: Float, side: StyledSwipeActionSide?) {
        settleJob.value?.cancel()
        openSide = side
        val start = offsetX
        settleJob.value = scope.launch {
            animate(
                initialValue = start,
                targetValue = target,
                animationSpec = tween(240),
            ) { value, _ ->
                offsetX = value
            }
        }
    }

    fun close() {
        animateTo(0f, null)
    }

    LaunchedEffect(actionsFromStart.isEmpty(), actionsFromEnd.isEmpty()) {
        if (
            (openSide == StyledSwipeActionSide.Start && actionsFromStart.isEmpty()) ||
            (openSide == StyledSwipeActionSide.End && actionsFromEnd.isEmpty())
        ) {
            close()
        }
    }

    LaunchedEffect(layoutDirection) {
        when (openSide) {
            StyledSwipeActionSide.Start ->
                offsetX = swipeOffsetForSide(
                    StyledSwipeActionSide.Start,
                    startActionsWidth.coerceAtMost(size.width).toFloat(),
                    layoutDirection,
                )
            StyledSwipeActionSide.End ->
                offsetX = swipeOffsetForSide(
                    StyledSwipeActionSide.End,
                    endActionsWidth.coerceAtMost(size.width).toFloat(),
                    layoutDirection,
                )
            null -> Unit
        }
    }

    Box(
        modifier = modifier
            .heightIn(min = StyledTheme.sizes.minimumInteractiveSize + actionVerticalPadding)
            .clip(shape)
            .background(backgroundColor)
            .onSizeChanged {
                size = it
                if (openSide == StyledSwipeActionSide.Start) {
                    offsetX = swipeOffsetForSide(
                        StyledSwipeActionSide.Start,
                        startActionsWidth.coerceAtMost(it.width).toFloat(),
                        layoutDirection,
                    )
                } else if (openSide == StyledSwipeActionSide.End) {
                    offsetX = swipeOffsetForSide(
                        StyledSwipeActionSide.End,
                        endActionsWidth.coerceAtMost(it.width).toFloat(),
                        layoutDirection,
                    )
                }
            }
            .pointerInput(layoutDirection) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)

                    var overSlop = 0f
                    val drag = awaitHorizontalPointerSlopOrCancellation(
                        down.id,
                        down.type,
                    ) { change, over ->
                        change.consume()
                        overSlop = over
                    }

                    if (drag != null) {
                        val direction = swipeActionSideForOffset(overSlop, layoutDirection)
                        val hasActionInDirection = when (direction) {
                            StyledSwipeActionSide.Start -> currentStartActions.value.isNotEmpty()
                            StyledSwipeActionSide.End -> currentEndActions.value.isNotEmpty()
                            null -> false
                        }
                        val isClosingOpenSide = when {
                            openSide == StyledSwipeActionSide.Start ->
                                direction == StyledSwipeActionSide.End
                            openSide == StyledSwipeActionSide.End ->
                                direction == StyledSwipeActionSide.Start
                            else -> false
                        }

                        if (hasActionInDirection || isClosingOpenSide) {
                            settleJob.value?.cancel()
                            openSide = null
                            var rawOffsetX = offsetX

                            fun updateOffset(dragAmount: Float): Boolean {
                                val negativeSide = swipeActionSideForOffset(-1f, layoutDirection)
                                val minOffset = when (negativeSide) {
                                    StyledSwipeActionSide.Start ->
                                        if (currentStartActions.value.isNotEmpty()) {
                                            -startActionsWidth.coerceAtMost(size.width).toFloat()
                                        } else {
                                            0f
                                        }
                                    StyledSwipeActionSide.End ->
                                        if (currentEndActions.value.isNotEmpty()) {
                                            -endActionsWidth.coerceAtMost(size.width).toFloat()
                                        } else {
                                            0f
                                        }
                                    null -> 0f
                                }
                                val positiveSide = swipeActionSideForOffset(1f, layoutDirection)
                                val maxOffset = when (positiveSide) {
                                    StyledSwipeActionSide.Start ->
                                        if (currentStartActions.value.isNotEmpty()) {
                                            startActionsWidth.coerceAtMost(size.width).toFloat()
                                        } else {
                                            0f
                                        }
                                    StyledSwipeActionSide.End ->
                                        if (currentEndActions.value.isNotEmpty()) {
                                            endActionsWidth.coerceAtMost(size.width).toFloat()
                                        } else {
                                            0f
                                        }
                                    null -> 0f
                                }
                                rawOffsetX += dragAmount
                                val newValue = resolveSwipeOverscrollOffset(
                                    rawOffset = rawOffsetX,
                                    minimumOffset = minOffset,
                                    maximumOffset = maxOffset,
                                    maximumDistance = size.width.toFloat(),
                                    behavior = currentOverscrollBehavior.value,
                                )
                                if (newValue == offsetX) return false
                                offsetX = newValue
                                if (currentOverscrollBehavior.value != StyledSwipeOverscrollBehavior.Sticky) {
                                    rawOffsetX = newValue
                                }
                                return true
                            }

                            updateOffset(overSlop)
                            if (horizontalDrag(drag.id) { change: PointerInputChange ->
                                    if (updateOffset(change.positionChange().x)) {
                                        change.consume()
                                    }
                                }
                            ) {
                                val startWidth = startActionsWidth.coerceAtMost(size.width)
                                val endWidth = endActionsWidth.coerceAtMost(size.width)
                                when (val side = swipeActionSideForOffset(offsetX, layoutDirection)) {
                                    StyledSwipeActionSide.Start ->
                                        if (startWidth > 0 && abs(offsetX) >= startWidth / 2f) {
                                            animateTo(
                                                swipeOffsetForSide(
                                                    side,
                                                    startWidth.toFloat(),
                                                    layoutDirection,
                                                ),
                                                side,
                                            )
                                        } else {
                                            close()
                                        }
                                    StyledSwipeActionSide.End ->
                                        if (endWidth > 0 && abs(offsetX) >= endWidth / 2f) {
                                            animateTo(
                                                swipeOffsetForSide(
                                                    side,
                                                    endWidth.toFloat(),
                                                    layoutDirection,
                                                ),
                                                side,
                                            )
                                        } else {
                                            close()
                                        }
                                    null -> close()
                                }
                            } else {
                                close()
                            }
                        }
                    }
                }
            },
        propagateMinConstraints = true,
    ) {
        UpdateStyledThemeLocals(contentColor = contentColor) {
            Box(modifier = Modifier.matchParentSize()) {
                if (actionsFromStart.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .fillMaxHeight()
                            .onSizeChanged {
                                startActionsWidth = it.width
                                if (openSide == StyledSwipeActionSide.Start) {
                                    offsetX = swipeOffsetForSide(
                                        StyledSwipeActionSide.Start,
                                        startActionsWidth.coerceAtMost(size.width).toFloat(),
                                        layoutDirection,
                                    )
                                }
                            },
                        horizontalArrangement = Arrangement.spacedBy(actionSpacing),
                    ) {
                        actionsFromStart.forEach { action ->
                            val actionScope = StyledSwipeRevealActionScope(
                                side = StyledSwipeActionSide.Start,
                                progress = if (revealSide == StyledSwipeActionSide.Start) revealProgress else 0f,
                                isOpen = openSide == StyledSwipeActionSide.Start,
                            )
                            SwipeRevealActionButton(
                                action = action,
                                defaultBackgroundColor = revealBackgroundColor,
                                defaultContentColor = revealContentColor,
                                scope = actionScope,
                                foregroundBackgroundColor = backgroundColor,
                                isRevealVisible = revealSide == StyledSwipeActionSide.Start,
                                contentPadding = contentPadding,
                                onClick = {
                                    close()
                                    action.onClick(
                                        StyledSwipeRevealActionContext(
                                            side = StyledSwipeActionSide.Start,
                                            progress = 1f,
                                            isOpen = true,
                                        ),
                                    )
                                },
                            )
                        }
                        Spacer(modifier = Modifier.width(actionSpacing))
                    }
                }
                if (actionsFromEnd.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                            .onSizeChanged {
                                endActionsWidth = it.width
                                if (openSide == StyledSwipeActionSide.End) {
                                    offsetX = swipeOffsetForSide(
                                        StyledSwipeActionSide.End,
                                        endActionsWidth.coerceAtMost(size.width).toFloat(),
                                        layoutDirection,
                                    )
                                }
                            },
                        horizontalArrangement = Arrangement.spacedBy(actionSpacing),
                    ) {
                        Spacer(modifier = Modifier.width(actionSpacing))
                        actionsFromEnd.forEach { action ->
                            val actionScope = StyledSwipeRevealActionScope(
                                side = StyledSwipeActionSide.End,
                                progress = if (revealSide == StyledSwipeActionSide.End) revealProgress else 0f,
                                isOpen = openSide == StyledSwipeActionSide.End,
                            )
                            SwipeRevealActionButton(
                                action = action,
                                defaultBackgroundColor = revealBackgroundColor,
                                defaultContentColor = revealContentColor,
                                scope = actionScope,
                                foregroundBackgroundColor = backgroundColor,
                                isRevealVisible = revealSide == StyledSwipeActionSide.End,
                                contentPadding = contentPadding,
                                onClick = {
                                    close()
                                    action.onClick(
                                        StyledSwipeRevealActionContext(
                                            side = StyledSwipeActionSide.End,
                                            progress = 1f,
                                            isOpen = true,
                                        ),
                                    )
                                },
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .offset { IntOffset(offsetX.roundToInt(), 0) }
                    .background(backgroundColor)
                    .pointerInput(openSide) {
                        if (openSide != null) {
                            awaitEachGesture {
                                val down = awaitFirstDown(
                                    requireUnconsumed = false,
                                    pass = PointerEventPass.Initial,
                                )
                                var moved = false
                                while (true) {
                                    val event = awaitPointerEvent(PointerEventPass.Initial)
                                    val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                    if (
                                        abs(change.position.x - down.position.x) > viewConfiguration.touchSlop ||
                                        abs(change.position.y - down.position.y) > viewConfiguration.touchSlop
                                    ) {
                                        moved = true
                                    }
                                    if (!change.pressed) {
                                        if (!moved) close()
                                        break
                                    }
                                }
                            }
                        }
                    },
                contentAlignment = contentAlignment,
            ) {
                Box(modifier = Modifier.padding(contentPadding)) {
                    content()
                }
            }
        }
    }
}

@Composable
@OptIn(InternalComposeStyledApi::class)
private fun SwipeRevealActionButton(
    action: StyledSwipeRevealAction,
    defaultBackgroundColor: Color,
    defaultContentColor: Color,
    scope: StyledSwipeRevealActionScope,
    foregroundBackgroundColor: Color,
    isRevealVisible: Boolean,
    contentPadding: PaddingValues,
    onClick: () -> Unit,
) {
    val actionBackgroundColor = action.backgroundColor
        ?.takeIf { it != Color.Unspecified }
        ?: defaultBackgroundColor
    val visibleBackgroundColor = if (!isRevealVisible) {
        foregroundBackgroundColor
    } else {
        actionBackgroundColor
    }
    val actionContentColor = action.contentColor
        ?.takeIf { it != Color.Unspecified }
        ?: defaultContentColor

    StyledSwipeActionSurface(
        modifier = Modifier
            .fillMaxHeight()
            .graphicsLayer {
                alpha = if (isRevealVisible) 1f else 0f
            }
            .then(
                if (isRevealVisible) Modifier else Modifier.clearAndSetSemantics {},
            ),
        shape = RectangleShape,
        backgroundColor = visibleBackgroundColor,
        contentColor = actionContentColor,
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(
            LocalStyledContext provides StyledSwipeRevealActionBox.Context.RevealAction,
        ) {
            StyledButton(
                modifier = Modifier.fillMaxHeight(),
                onClick = onClick,
                enabled = action.enabled && isRevealVisible,
                role = StyledButton.Role.Default,
                shape = RectangleShape,
                customization = StyledButtonDefaults.customize(
                    colors = CustomColors(
                        filledBackground = actionBackgroundColor,
                        filledForeground = actionContentColor,
                        textForeground = actionContentColor,
                    ),
                ),
            ) {
                val actionContent = action.content
                if (actionContent != null) {
                    Box(modifier = Modifier.padding(contentPadding)) {
                        actionContent(scope)
                    }
                } else {
                    Row(
                        modifier = Modifier.padding(contentPadding),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        action.icon?.let {
                            StyledIcon(
                                icon = it,
                                size = 18.dp,
                                tint = actionContentColor,
                            )
                            if (action.label != null) {
                                Spacer(modifier = Modifier.width(StyledTheme.spacing.small))
                            }
                        }
                        action.label?.let {
                            StyledText(
                                text = it,
                                color = actionContentColor,
                            )
                        }
                    }
                }
            }
        }
    }
}
