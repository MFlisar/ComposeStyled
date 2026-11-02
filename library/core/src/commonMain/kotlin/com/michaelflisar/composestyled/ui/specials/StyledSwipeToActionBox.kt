package com.michaelflisar.composestyled.ui.specials

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalPointerSlopOrCancellation
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.tokens.minimumInteractiveSize
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.primitives.StyledText
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Stable
class StyledSwipeActionScope internal constructor(
    val side: StyledSwipeActionSide,
    val progress: Float,
    val willTrigger: Boolean,
)

@Stable
class StyledSwipeAction internal constructor(
    val label: String? = null,
    val enabled: Boolean = true,
    val icon: IconComposable? = null,
    val backgroundColorProvider: (@Composable (scope: StyledSwipeActionScope) -> Color)? = null,
    val contentColorProvider: (@Composable (scope: StyledSwipeActionScope) -> Color)? = null,
    val content: (@Composable (scope: StyledSwipeActionScope) -> Unit)? = null,
) {
    constructor(
        label: String? = null,
        enabled: Boolean = true,
        icon: IconComposable? = null,
        backgroundColor: Color? = null,
        contentColor: Color? = null,
    ) : this(
        label,
        enabled,
        icon,
        backgroundColor?.let { color ->
            { _: StyledSwipeActionScope -> color }
        },
        contentColor?.let { color ->
            { _: StyledSwipeActionScope -> color }
        },
        null,
    )

    constructor(
        label: String? = null,
        enabled: Boolean = true,
        icon: IconComposable? = null,
        backgroundColorProvider: (@Composable (scope: StyledSwipeActionScope) -> Color)?,
        contentColorProvider: (@Composable (scope: StyledSwipeActionScope) -> Color)?,
    ) : this(
        label,
        enabled,
        icon,
        backgroundColorProvider,
        contentColorProvider,
        null,
    )

    constructor(
        enabled: Boolean = true,
        backgroundColor: Color? = null,
        contentColor: Color? = null,
        content: @Composable (scope: StyledSwipeActionScope) -> Unit,
    ) : this(
        null,
        enabled,
        null,
        backgroundColor?.let { color ->
            { _: StyledSwipeActionScope -> color }
        },
        contentColor?.let { color ->
            { _: StyledSwipeActionScope -> color }
        },
        content,
    )

    constructor(
        enabled: Boolean = true,
        backgroundColorProvider: (@Composable (scope: StyledSwipeActionScope) -> Color)?,
        contentColorProvider: (@Composable (scope: StyledSwipeActionScope) -> Color)?,
        content: @Composable (scope: StyledSwipeActionScope) -> Unit,
    ) : this(
        null,
        enabled,
        null,
        backgroundColorProvider,
        contentColorProvider,
        content,
    )

    init {
        require(content != null || label != null || icon != null) {
            "A swipe action must define content, a label, or an icon."
        }
    }
}

@Composable
@OptIn(InternalComposeStyledApi::class)
fun StyledSwipeToActionBox(
    modifier: Modifier = Modifier,
    actionFromStart: StyledSwipeAction? = null,
    actionFromEnd: StyledSwipeAction? = null,
    onSwipe: ((StyledSwipeActionSide) -> Unit)? = null,
    threshold: Dp = StyledSwipeActionsDefaults.threshold,
    shape: Shape = StyledSwipeActionsDefaults.shape,
    backgroundColor: Color = StyledSwipeActionsDefaults.backgroundColor,
    contentColor: Color = StyledSwipeActionsDefaults.contentColor,
    revealBackgroundColor: Color = backgroundColor,
    revealContentColor: Color = contentColor,
    contentPadding: PaddingValues = StyledSwipeActionsDefaults.contentPadding,
    contentAlignment: Alignment = StyledSwipeActionsDefaults.contentAlignment,
    overscrollBehavior: StyledSwipeOverscrollBehavior = StyledSwipeActionsDefaults.overscrollBehavior,
    content: @Composable () -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current
    val currentActionFromStart = rememberUpdatedState(actionFromStart)
    val currentActionFromEnd = rememberUpdatedState(actionFromEnd)
    val currentOnSwipe = rememberUpdatedState(onSwipe)
    require(threshold > 0.dp) { "threshold must be greater than zero." }
    val thresholdInPx = with(LocalDensity.current) { threshold.toPx() }
    val currentThresholdInPx = rememberUpdatedState(thresholdInPx)
    val currentOverscrollBehavior = rememberUpdatedState(overscrollBehavior)
    val scope = rememberCoroutineScope()
    var offsetX by remember { mutableFloatStateOf(0f) }
    var size by remember { mutableStateOf(IntSize.Zero) }
    val settleJob = remember { mutableStateOf<Job?>(null) }

    fun settleToOrigin() {
        settleJob.value?.cancel()
        val start = offsetX
        settleJob.value = scope.launch {
            animate(
                initialValue = start,
                targetValue = 0f,
                animationSpec = tween(300),
            ) { value, _ ->
                offsetX = value
            }
        }
    }

    LaunchedEffect(
        actionFromStart?.enabled,
        actionFromEnd?.enabled,
        layoutDirection,
    ) {
        val activeSide = swipeActionSideForOffset(offsetX, layoutDirection)
        val activeActionEnabled = when (activeSide) {
            StyledSwipeActionSide.Start -> actionFromStart?.enabled
            StyledSwipeActionSide.End -> actionFromEnd?.enabled
            null -> true
        }
        if (offsetX != 0f && activeActionEnabled != true) {
            settleToOrigin()
        }
    }

    Box(
        modifier = modifier
            .minimumInteractiveSize()
            .clip(shape)
            .onSizeChanged { size = it }
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
                        val actionAvailable = when (direction) {
                            StyledSwipeActionSide.Start ->
                                currentActionFromStart.value?.enabled == true
                            StyledSwipeActionSide.End ->
                                currentActionFromEnd.value?.enabled == true
                            null -> false
                        } &&
                            currentOnSwipe.value != null

                        if (actionAvailable) {
                            settleJob.value?.cancel()
                            var rawOffsetX = offsetX

                            fun updateOffset(dragAmount: Float): Boolean {
                                rawOffsetX += dragAmount
                                val limit = currentThresholdInPx.value
                                    .coerceAtMost(size.width.toFloat())
                                val newValue = resolveSwipeOverscrollOffset(
                                    rawOffset = rawOffsetX,
                                    minimumOffset = -limit,
                                    maximumOffset = limit,
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
                                if (abs(offsetX) >= currentThresholdInPx.value) {
                                    when (swipeActionSideForOffset(offsetX, layoutDirection)) {
                                        StyledSwipeActionSide.Start ->
                                            if (currentActionFromStart.value?.enabled == true) {
                                                currentOnSwipe.value?.invoke(StyledSwipeActionSide.Start)
                                            }
                                        StyledSwipeActionSide.End ->
                                            if (currentActionFromEnd.value?.enabled == true) {
                                                currentOnSwipe.value?.invoke(StyledSwipeActionSide.End)
                                            }
                                        null -> Unit
                                    }
                                }
                            }
                            settleToOrigin()
                        }
                    }
                }
            },
        propagateMinConstraints = true,
    ) {
        UpdateStyledThemeLocals(contentColor = contentColor) {
            val progress = (abs(offsetX) / thresholdInPx).coerceIn(0f, 1f)
            val startScope = StyledSwipeActionScope(
                side = StyledSwipeActionSide.Start,
                progress = progress,
                willTrigger = abs(offsetX) >= thresholdInPx &&
                    swipeActionSideForOffset(offsetX, layoutDirection) == StyledSwipeActionSide.Start,
            )
            val endScope = StyledSwipeActionScope(
                side = StyledSwipeActionSide.End,
                progress = progress,
                willTrigger = abs(offsetX) >= thresholdInPx &&
                    swipeActionSideForOffset(offsetX, layoutDirection) == StyledSwipeActionSide.End,
            )

            if (offsetX != 0f) {
                val side = swipeActionSideForOffset(offsetX, layoutDirection)
                val action = when (side) {
                    StyledSwipeActionSide.Start -> actionFromStart
                    StyledSwipeActionSide.End -> actionFromEnd
                    null -> null
                }
                if (action?.enabled == true && side != null) {
                    val actionScope = if (side == StyledSwipeActionSide.Start) startScope else endScope
                    val actionBackgroundColor = action.backgroundColorProvider
                        ?.invoke(actionScope)
                        ?.takeIf { it != Color.Unspecified }
                        ?: revealBackgroundColor
                    val actionContentColor = action.contentColorProvider
                        ?.invoke(actionScope)
                        ?.takeIf { it != Color.Unspecified }
                        ?: revealContentColor

                    StyledSwipeActionSurface(
                        modifier = Modifier.matchParentSize(),
                        shape = shape,
                        backgroundColor = actionBackgroundColor,
                        contentColor = actionContentColor,
                        contentPadding = contentPadding,
                        contentAlignment = revealedContentAlignment(contentAlignment, side),
                    ) {
                        SwipeActionContent(
                            action = action,
                            scope = actionScope,
                            contentColor = actionContentColor,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(offsetX.roundToInt(), 0)
                    }
                    .background(backgroundColor),
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
private fun SwipeActionContent(
    action: StyledSwipeAction,
    scope: StyledSwipeActionScope,
    contentColor: Color,
) {
    val content = action.content
    if (content != null) {
        content(scope)
    } else {
        Row(verticalAlignment = Alignment.CenterVertically) {
            action.icon?.let {
                StyledIcon(
                    icon = it,
                    size = 18.dp,
                    tint = contentColor,
                )
                if (action.label != null) {
                    Spacer(modifier = Modifier.width(StyledTheme.spacing.small))
                }
            }
            action.label?.let {
                StyledText(text = it, color = contentColor)
            }
        }
    }
}

private fun revealedContentAlignment(
    contentAlignment: Alignment,
    side: StyledSwipeActionSide,
): Alignment = object : Alignment {
    override fun align(
        size: IntSize,
        space: IntSize,
        layoutDirection: LayoutDirection,
    ): IntOffset {
        val alignedOffset = contentAlignment.align(size, space, layoutDirection)
        val x = when (side) {
            StyledSwipeActionSide.Start ->
                if (layoutDirection == LayoutDirection.Ltr) 0 else space.width - size.width
            StyledSwipeActionSide.End ->
                if (layoutDirection == LayoutDirection.Ltr) space.width - size.width else 0
        }
        return IntOffset(x, alignedOffset.y)
    }
}
