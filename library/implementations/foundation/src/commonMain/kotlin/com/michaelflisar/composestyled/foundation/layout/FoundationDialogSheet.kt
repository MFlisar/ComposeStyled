package com.michaelflisar.composestyled.foundation.layout

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.recalculateWindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.platform.SystemBarStyleEffect
import com.michaelflisar.composestyled.platform.systemBarIconStyleFor
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.components.StyledCenterAlignedTopBar
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.components.StyledTopBar
import com.michaelflisar.composestyled.ui.internal.DialogActions
import com.michaelflisar.composestyled.ui.internal.DialogButton
import com.michaelflisar.composestyled.ui.internal.DIALOG_VISIBILITY_ANIMATION_DURATION_MILLIS
import com.michaelflisar.composestyled.ui.internal.dialogInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(InternalComposeStyledApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun FoundationDialogSheet(
    onDismissRequest: () -> Unit,
    onDismissRequestWithoutAnimation: () -> Unit,
    modifier: Modifier,
    shape: Shape,
    properties: StyledDialog.Properties,
    options: StyledDialog.SheetOptions,
    containerColor: Color,
    title: (@Composable () -> Unit)?,
    buttons: StyledDialog.Buttons,
    visible: Boolean,
    isVisible: () -> Boolean,
    content: @Composable ColumnScope.(PaddingValues) -> Unit,
    modal: Boolean,
    verticalActions: Boolean,
    fullscreenActionsInToolbar: Boolean,
    centeredTitle: Boolean,
    presentationMapping: (StyledDialog.ActionSlot) -> StyledDialog.ButtonPresentation,
    positiveActionFirst: Boolean,
    actionSpacing: Dp,
    actionRowSpacing: Dp,
    stackPositiveActionFirst: Boolean,
    footerTopPadding: Dp,
    maxWidth: Dp,
    maxHeightFraction: Float,
    edgeMargin: Dp,
) {
    val dismissRequest by rememberUpdatedState(onDismissRequest)
    val dismissRequestWithoutAnimation by rememberUpdatedState(onDismissRequestWithoutAnimation)
    val currentVisibility by rememberUpdatedState(isVisible)
    if (!modal) {
        BackHandler(enabled = properties.dismissOnBackPress) { dismissRequest() }
    }
    BoxWithConstraints(
        modifier.dialogInput(properties.dismissOnBackPress, onDismissRequest)
            .fillMaxSize()
            .then(if (modal) Modifier else Modifier.recalculateWindowInsets())
            .then(
                if (modal && options.expandToFullscreen) Modifier
                else Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
                ),
            )
            .imePadding().clipToBounds(),
    ) {
        require(constraints.hasBoundedHeight) { "StyledBottomSheet needs a bounded-height parent" }
        val density = LocalDensity.current
        val layoutDirection = LocalLayoutDirection.current
        val viewportHeight = constraints.maxHeight.toFloat()
        val horizontalInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
        val safeHorizontalWidth = if (modal && options.expandToFullscreen) {
            (this.maxWidth -
                with(density) { horizontalInsets.getLeft(this, layoutDirection).toDp() } -
                with(density) { horizontalInsets.getRight(this, layoutDirection).toDp() }).coerceAtLeast(0.dp)
        } else this.maxWidth
        val regularSheetWidth = minOf(
            maxWidth,
            if (safeHorizontalWidth >= 600.dp) {
                (safeHorizontalWidth - edgeMargin * 2).coerceAtLeast(0.dp)
            } else safeHorizontalWidth,
        )
        val topInset = if (modal && options.expandToFullscreen) {
            WindowInsets.safeDrawing.getTop(density).toFloat()
        } else 0f
        val regularViewportHeight = (viewportHeight - topInset).coerceAtLeast(0f)
        val regularSheetHeight = minOf(
            regularViewportHeight * maxHeightFraction,
            (regularViewportHeight - with(density) { edgeMargin.toPx() }).coerceAtLeast(0f),
        )
        val contentMaxHeight = if (options.expandToFullscreen) viewportHeight else regularSheetHeight
        var footerHeight by remember { mutableIntStateOf(0) }
        var headerHeight by remember { mutableIntStateOf(0) }
        var measuredContentHeight by remember { mutableIntStateOf(0) }
        val fixedButtons = options.fixedButtons && !buttons.isEmpty
        val contentPadding = StyledTheme.spacing.large
        val topCornerHeight = remember(shape, regularSheetWidth, contentMaxHeight, density, layoutDirection) {
            val outline = shape.createOutline(
                Size(with(density) { regularSheetWidth.toPx() }, contentMaxHeight), layoutDirection, density,
            )
            if (outline is Outline.Rounded) {
                maxOf(outline.roundRect.topLeftCornerRadius.y, outline.roundRect.topRightCornerRadius.y)
            } else 0f
        }
        val protectedHeight = if (title != null) {
            maxOf(headerHeight + with(density) { contentPadding.toPx() }, topCornerHeight)
        } else topCornerHeight
        val minimumSheetHeight = if (fixedButtons) protectedHeight + footerHeight else 0f
        val measuredSheetHeight = if (measuredContentHeight == 0) {
            contentMaxHeight
        } else {
            maxOf(measuredContentHeight.toFloat(), minimumSheetHeight)
        }
        val sheetHeight = if (options.expandToFullscreen) {
            viewportHeight
        } else {
            minOf(contentMaxHeight, measuredSheetHeight)
        }
        val peekOffset = (sheetHeight - regularViewportHeight * options.peekHeightFraction).coerceAtLeast(0f)
        val drag = rememberSaveable(
            options.initiallyExpanded,
            saver = sheetDragStateSaver(sheetHeight, peekOffset, options),
        ) {
            SheetDragState(sheetHeight, peekOffset, options, options.initiallyExpanded)
        }
        SideEffect {
            drag.updateGeometry(sheetHeight, peekOffset, options)
        }
        DisposableEffect(drag) {
            onDispose { drag.cancel() }
        }
        val scope = rememberCoroutineScope()
        val fullscreenToolbarVisible = options.expandToFullscreen &&
            (peekOffset <= 0f || drag.offset <= topInset + with(density) { 56.dp.toPx() })
        val sheetWidth by animateDpAsState(
            targetValue = if (fullscreenToolbarVisible) this.maxWidth else regularSheetWidth,
        )
        val sheetTop = viewportHeight - sheetHeight + drag.offset
        SystemBarStyleEffect(
            statusBar = if (modal && options.expandToFullscreen && sheetTop <= 0f) {
                systemBarIconStyleFor(StyledTheme.colorScheme.surface, StyledTheme.colorScheme.background)
            } else null,
            navigationBar = if (modal) {
                systemBarIconStyleFor(containerColor, StyledTheme.colorScheme.background)
            } else null,
        )
        val connection = remember(drag, scope) {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    if (source != NestedScrollSource.UserInput) return Offset.Zero
                    drag.beginUserGesture()
                    return if (available.y < 0f) {
                        Offset(0f, drag.drag(available.y))
                    } else {
                        Offset.Zero
                    }
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource,
                ): Offset {
                    if (source != NestedScrollSource.UserInput) return Offset.Zero
                    drag.beginUserGesture()
                    drag.recordContentScroll(consumed.y)
                    return Offset(0f, drag.drag(available.y))
                }

                override suspend fun onPreFling(available: Velocity): Velocity {
                    if (!options.gesturesEnabled || drag.offset <= 0f) return Velocity.Zero
                    if (!drag.canDismissOnSwipe()) return Velocity.Zero
                    drag.settle(
                        scope = scope,
                        velocity = available.y,
                        allowDismiss = true,
                        onDismissRequest = { dismissRequestWithoutAnimation() },
                        isVisible = { currentVisibility() },
                    ).join()
                    return Velocity(0f, available.y)
                }

                override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                    if (!options.gesturesEnabled) return Velocity.Zero
                    drag.settle(
                        scope = scope,
                        velocity = available.y,
                        allowDismiss = drag.canDismissOnSwipe(),
                        onDismissRequest = { dismissRequestWithoutAnimation() },
                        isVisible = { currentVisibility() },
                    ).join()
                    drag.endUserGesture()
                    return Velocity(0f, available.y)
                }
            }
        }
        val visibleHeight = (sheetHeight - drag.offset).coerceAtLeast(0f)
        val footerOffset = (protectedHeight + footerHeight - visibleHeight).coerceAtLeast(0f)
        val bottomInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
        val footerInsets = if (modal && options.expandToFullscreen) {
            WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
        } else bottomInsets
        val sheetHorizontalInsets = if (modal && options.expandToFullscreen) {
            horizontalInsets
        } else WindowInsets(0, 0, 0, 0)
        if (modal) {
            // The native window supplies the dimming; this layer owns outside-tap policy.
            Box(
                Modifier.fillMaxSize().clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = properties.dismissOnClickOutside,
                ) { dismissRequest() },
            )
        }
        val visibility = remember { MutableTransitionState(false) }
        LaunchedEffect(visible) { visibility.targetState = visible }
        AnimatedVisibility(
            visibleState = visibility,
            modifier = Modifier.fillMaxSize(),
            enter = slideInVertically(
                animationSpec = tween(DIALOG_VISIBILITY_ANIMATION_DURATION_MILLIS),
            ) { visibleHeight.roundToInt() },
            exit = slideOutVertically(
                animationSpec = tween(DIALOG_VISIBILITY_ANIMATION_DURATION_MILLIS),
            ) { visibleHeight.roundToInt() },
        ) {
            Box(Modifier.fillMaxSize()) {
                StyledSurface(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .width(sheetWidth)
                        .offset { IntOffset(0, sheetTop.roundToInt()) }
                        .heightIn(max = with(density) { contentMaxHeight.toDp() })
                        .nestedScroll(connection)
                        .draggable(
                            state = rememberDraggableState { drag.drag(it) },
                            orientation = Orientation.Vertical,
                            enabled = options.gesturesEnabled && visible,
                            onDragStarted = { drag.beginUserGesture() },
                            onDragStopped = { velocity ->
                                drag.settle(
                                    scope = scope,
                                    velocity = velocity,
                                    allowDismiss = drag.canDismissOnSwipe(),
                                    onDismissRequest = { dismissRequestWithoutAnimation() },
                                    isVisible = { currentVisibility() },
                                ).join()
                                drag.endUserGesture()
                            },
                        ),
                    shape = if (options.expandToFullscreen && sheetTop <= 0f) RectangleShape else shape,
                    color = containerColor,
                    shadowElevation = 8.dp,
                ) {
                    Box(Modifier.fillMaxWidth()) {
                        Column(
                            Modifier.windowInsetsPadding(sheetHorizontalInsets)
                                .then(
                                    if (options.expandToFullscreen) {
                                        Modifier.height(with(density) { contentMaxHeight.toDp() })
                                    } else {
                                        Modifier.heightIn(
                                            min = with(density) {
                                                minOf(minimumSheetHeight, contentMaxHeight).toDp()
                                            },
                                            max = with(density) { contentMaxHeight.toDp() },
                                        )
                                    },
                                ).then(if (fixedButtons) Modifier else Modifier.windowInsetsPadding(bottomInsets))
                                .onSizeChanged { measuredContentHeight = it.height },
                        ) {
                            AnimatedVisibility(
                                visible = fullscreenToolbarVisible,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically(),
                            ) {
                                val navigation: @Composable () -> Unit = {
                                    if (fullscreenActionsInToolbar) {
                                        DialogButton(
                                            buttons.negative,
                                            StyledDialog.ActionSlot.Negative,
                                            StyledDialog.Context.FullscreenAction,
                                            autoPresentation = presentationMapping(StyledDialog.ActionSlot.Negative),
                                        )
                                    }
                                }
                                val actions: @Composable RowScope.() -> Unit = {
                                    if (fullscreenActionsInToolbar) {
                                        DialogButton(
                                            buttons.positive,
                                            StyledDialog.ActionSlot.Positive,
                                            StyledDialog.Context.FullscreenAction,
                                            autoPresentation = presentationMapping(StyledDialog.ActionSlot.Positive),
                                        )
                                    }
                                }
                                if (centeredTitle) {
                                    StyledCenterAlignedTopBar(
                                        containerColor = containerColor,
                                        contentColor = StyledTheme.contentColorFor(containerColor),
                                        navigationIcon = navigation,
                                        actions = actions,
                                        title = { title?.invoke() },
                                    )
                                } else {
                                    StyledTopBar(
                                        containerColor = containerColor,
                                        contentColor = StyledTheme.contentColorFor(containerColor),
                                        navigationIcon = navigation,
                                        actions = actions,
                                        title = { title?.invoke() },
                                    )
                                }
                            }
                            AnimatedVisibility(
                                visible = !fullscreenToolbarVisible,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically(),
                            ) {
                                Column(Modifier.fillMaxWidth().onSizeChanged { headerHeight = it.height }) {
                                    Box(
                                        Modifier.fillMaxWidth().height(32.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Box(
                                            Modifier.size(width = 48.dp, height = 32.dp)
                                                .clickable(
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    indication = null,
                                                    enabled = options.gesturesEnabled &&
                                                        peekOffset > 0f && visible,
                                                ) {
                                                    drag.animateTo(
                                                        scope,
                                                        if (drag.offset > peekOffset / 2f) 0f else peekOffset,
                                                    )
                                                }.semantics {
                                                    if (options.gesturesEnabled && visible) {
                                                        if (peekOffset > 0f) {
                                                            expand {
                                                                drag.animateTo(scope, 0f)
                                                                true
                                                            }
                                                            collapse {
                                                                drag.animateTo(scope, peekOffset)
                                                                true
                                                            }
                                                        }
                                                        if (options.dismissOnSwipe) {
                                                            dismiss { dismissRequest(); true }
                                                        }
                                                    }
                                                },
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Spacer(
                                                Modifier.size(32.dp, 4.dp)
                                                    .background(
                                                        StyledTheme.colorScheme.outlineVariant,
                                                        RoundedCornerShape(2.dp),
                                                    ),
                                            )
                                        }
                                    }
                                    if (title != null) {
                                        Box(Modifier.padding(horizontal = StyledTheme.spacing.large)) {
                                            UpdateStyledThemeLocals(
                                                textStyle = StyledTheme.typography.titleLarge,
                                            ) {
                                                title()
                                            }
                                        }
                                    }
                                }
                            }
                            Column(
                                Modifier.then(if (options.expandToFullscreen) Modifier.weight(1f) else Modifier)
                                    .fillMaxWidth(),
                            ) {
                                content(
                                    PaddingValues(
                                        start = contentPadding,
                                        top = contentPadding,
                                        end = contentPadding,
                                        bottom = if (fixedButtons) {
                                            with(density) { footerHeight.toDp() }
                                        } else contentPadding,
                                    ),
                                )
                            }
                            if (!fixedButtons) {
                                val moveToToolbar = fullscreenToolbarVisible && fullscreenActionsInToolbar
                                SheetFooterActions(
                                    buttons = buttons,
                                    moveToToolbar = moveToToolbar,
                                    modifier = Modifier.padding(
                                        start = StyledTheme.spacing.large,
                                        end = StyledTheme.spacing.large,
                                        bottom = if (moveToToolbar && buttons.neutral == null) 0.dp
                                        else StyledTheme.spacing.medium,
                                    ),
                                    vertical = verticalActions,
                                    positiveFirst = positiveActionFirst,
                                    itemSpacing = actionSpacing,
                                    rowSpacing = actionRowSpacing,
                                    stackPositiveFirst = stackPositiveActionFirst,
                                )
                            }
                        }
                    }
                }
                // Keep dragged body content covered even after the footer enters the navigation area.
                Spacer(
                    Modifier.align(Alignment.BottomCenter).width(sheetWidth)
                        .windowInsetsBottomHeight(bottomInsets).background(containerColor),
                )
                if (fixedButtons) {
                    StyledSurface(
                        modifier = Modifier.align(Alignment.BottomCenter).width(sheetWidth)
                            .offset { IntOffset(0, footerOffset.roundToInt()) }
                            .onSizeChanged { footerHeight = it.height },
                        shape = RectangleShape,
                        color = containerColor,
                    ) {
                        val moveToToolbar = fullscreenToolbarVisible && fullscreenActionsInToolbar
                        SheetFooterActions(
                            buttons = buttons,
                            moveToToolbar = moveToToolbar,
                            modifier = Modifier.windowInsetsPadding(footerInsets).padding(
                                top = if (moveToToolbar && buttons.neutral == null) 0.dp else footerTopPadding,
                                start = StyledTheme.spacing.large,
                                end = StyledTheme.spacing.large,
                                bottom = if (moveToToolbar && buttons.neutral == null) 0.dp
                                else StyledTheme.spacing.medium,
                            ),
                            vertical = verticalActions,
                            positiveFirst = positiveActionFirst,
                            itemSpacing = actionSpacing,
                            rowSpacing = actionRowSpacing,
                            stackPositiveFirst = stackPositiveActionFirst,
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(InternalComposeStyledApi::class)
private fun SheetFooterActions(
    buttons: StyledDialog.Buttons,
    moveToToolbar: Boolean,
    modifier: Modifier,
    vertical: Boolean,
    positiveFirst: Boolean,
    itemSpacing: Dp,
    rowSpacing: Dp,
    stackPositiveFirst: Boolean,
) {
    Column(modifier) {
        AnimatedVisibility(visible = !moveToToolbar) {
            DialogActions(
                buttons,
                StyledDialog.Context.BottomSheetAction,
                vertical = vertical,
                positiveFirst = positiveFirst,
                itemSpacing = itemSpacing,
                rowSpacing = rowSpacing,
                stackPositiveFirst = stackPositiveFirst,
            )
        }
        AnimatedVisibility(visible = moveToToolbar && buttons.neutral != null) {
            DialogActions(
                StyledDialog.Buttons(neutral = buttons.neutral),
                StyledDialog.Context.FullscreenAction,
                vertical = vertical,
                positiveFirst = positiveFirst,
                itemSpacing = itemSpacing,
                rowSpacing = rowSpacing,
                stackPositiveFirst = stackPositiveFirst,
            )
        }
    }
}

private class SheetDragState(
    private var height: Float,
    private var peekOffset: Float,
    private var options: StyledDialog.SheetOptions,
    initiallyExpanded: Boolean,
) {
    var isExpanded by mutableStateOf(initiallyExpanded || peekOffset <= 0f)
        private set
    var offset by mutableFloatStateOf(if (isExpanded) 0f else peekOffset)
        private set
    private var settling: Job? = null
    private var userGestureStartOffset: Float? = null
    private var contentConsumedDownwardScroll = false

    fun beginUserGesture() {
        if (userGestureStartOffset == null) {
            userGestureStartOffset = offset
            contentConsumedDownwardScroll = false
        }
    }

    fun recordContentScroll(consumedY: Float) {
        if (userGestureStartOffset != null && consumedY > 0f) {
            contentConsumedDownwardScroll = true
        }
    }

    fun canDismissOnSwipe(): Boolean {
        if (!options.dismissOnSwipe) return false
        if (!options.dismissOnSwipeOnlyWhenContentAtTop) return true
        val startOffset = userGestureStartOffset ?: offset
        val startedAtPeek = startOffset >= peekOffset
        return startedAtPeek && !contentConsumedDownwardScroll
    }

    fun endUserGesture() {
        userGestureStartOffset = null
        contentConsumedDownwardScroll = false
    }

    fun updateGeometry(
        height: Float,
        peekOffset: Float,
        options: StyledDialog.SheetOptions,
    ) {
        if (this.height == height && this.peekOffset == peekOffset && this.options == options) return
        settling?.cancel()
        settling = null
        this.height = height
        this.peekOffset = peekOffset
        this.options = options
        if (peekOffset <= 0f) isExpanded = true
        offset = if (isExpanded) 0f else peekOffset
    }

    fun cancel() {
        settling?.cancel()
    }

    fun drag(delta: Float): Float {
        if (!options.gesturesEnabled) return 0f
        settling?.cancel()
        val oldOffset = offset
        offset = (offset + delta).coerceIn(0f, if (options.dismissOnSwipe) height else peekOffset)
        return offset - oldOffset
    }

    fun animateTo(scope: CoroutineScope, target: Float): Job {
        settling?.cancel()
        return scope.launch {
            animate(offset, target) { value, _ -> offset = value }
            isExpanded = target <= peekOffset / 2f
        }.also { settling = it }
    }

    fun settle(
        scope: CoroutineScope,
        velocity: Float,
        allowDismiss: Boolean,
        onDismissRequest: () -> Unit,
        isVisible: () -> Boolean,
    ): Job {
        settling?.cancel()
        val anchors = listOf(0f, peekOffset) +
            if (options.dismissOnSwipe && allowDismiss) listOf(height) else emptyList()
        val projected = offset + velocity * 0.15f
        val target = anchors.minBy { abs(it - projected) }
        return scope.launch {
            animate(offset, target) { value, _ -> offset = value }
            if (target == height) {
                isExpanded = false
                onDismissRequest()
                if (isVisible()) {
                    animate(offset, peekOffset) { value, _ -> offset = value }
                }
            } else {
                isExpanded = target == 0f
            }
        }.also { settling = it }
    }
}

private fun sheetDragStateSaver(
    height: Float,
    peekOffset: Float,
    options: StyledDialog.SheetOptions,
): Saver<SheetDragState, Boolean> = Saver(
    save = { it.isExpanded },
    restore = { SheetDragState(height, peekOffset, options, it) },
)
