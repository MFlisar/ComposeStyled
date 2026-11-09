package com.michaelflisar.composestyled.material3.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.michaelflisar.composestyled.material3.ProvideStyledLocals
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledPopup
import com.michaelflisar.composestyled.ui.components.StyledPopupDefaults
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.layout.StyledVerticalScrollContainer
import kotlin.math.abs
import kotlin.math.roundToInt

class Material3PopupMenu internal constructor() : StyledPopupMenu.Implementation {
    companion object {
        @Composable
        fun create() = remember { Material3PopupMenu() }
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        expanded: Boolean,
        onDismissRequest: () -> Unit,
        modifier: Modifier,
        offset: DpOffset,
        placement: StyledPopupMenu.Placement,
        horizontalAlignment: StyledPopupMenu.HorizontalAlignment,
        properties: PopupProperties,
        scrollState: ScrollState,
        style: StyledPopup.Style,
        contentPadding: androidx.compose.foundation.layout.PaddingValues,
        header: (@Composable () -> Unit)?,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        if (style.windowOverlay) {
            val positionProvider =
                StyledPopupDefaults.windowOverlayPositionProvider(
                    offset = offset,
                    isVisible = expanded,
                    placement = placement,
                    horizontalAlignment = horizontalAlignment,
                    parentWindowOriginOnScreen = style.parentWindowOriginOnScreen,
                )
            val menuSize = remember { mutableStateOf(IntSize.Zero) }
            val menuTransitionState = remember { MutableTransitionState(false) }
            menuTransitionState.targetState = expanded && menuSize.value != IntSize.Zero
            val animation = style.animation.forPlacement(
                isAboveAnchor = positionProvider.isAboveAnchor(menuSize.value.height),
            )
            val shadowUnfoldEnabled =
                animation.shadowEnterSpec != null && animation.shadowExitSpec != null
            val shadowScale by animateFloatAsState(
                targetValue = if (shadowUnfoldEnabled && menuTransitionState.targetState) {
                    1f
                } else if (shadowUnfoldEnabled) {
                    0f
                } else {
                    1f
                },
                animationSpec = if (menuTransitionState.targetState) {
                    animation.shadowEnterSpec ?: tween(0)
                } else {
                    animation.shadowExitSpec ?: tween(0)
                },
                label = "popupMenuShadowScale",
            )
            val outsideTapBounds = positionProvider.outsideTapBounds()
            StyledPopup(
                visible = expanded,
                onDismissRequest = onDismissRequest,
                popupPositionProvider = positionProvider,
                properties = properties,
                style = StyledPopup.Style(
                    shape = RectangleShape,
                    containerColor = Color.Transparent,
                    contentColor = style.contentColor,
                    shadowElevation = 0.dp,
                    border = null,
                    maxHeight = style.maxHeight,
                    windowOverlay = true,
                    windowOverlaySize = positionProvider.popupWindowSize(),
                    onTransitionFinished = style.onTransitionFinished,
                    animation = style.animation,
                ),
            ) {
                Box(
                    Modifier.fillMaxSize().onGloballyPositioned { coordinates ->
                        val position = coordinates.positionOnScreen()
                        positionProvider.updateOverlayBounds(
                            originOnScreen = IntOffset(
                                x = position.x.roundToInt(),
                                y = position.y.roundToInt(),
                            ),
                            size = coordinates.size,
                        )
                    },
                ) {
                    Box(
                        Modifier
                            .offset { IntOffset(outsideTapBounds.left, outsideTapBounds.top) }
                            .size(
                                width = with(LocalDensity.current) {
                                    outsideTapBounds.width.toDp()
                                },
                                height = with(LocalDensity.current) {
                                    outsideTapBounds.height.toDp()
                                },
                            )
                            .pointerInput(onDismissRequest) {
                                awaitEachGesture {
                                    val pass = PointerEventPass.Initial
                                    val down = awaitFirstDown(
                                        requireUnconsumed = false,
                                        pass = pass,
                                    )
                                    down.consume()
                                    var isTap = true
                                    while (true) {
                                        val event = awaitPointerEvent(pass)
                                        val change = event.changes.firstOrNull { it.id == down.id }
                                        event.changes.forEach { it.consume() }
                                        if (change == null) {
                                            break
                                        }
                                        if (
                                            abs(change.position.x - down.position.x) >
                                            viewConfiguration.touchSlop ||
                                            abs(change.position.y - down.position.y) >
                                            viewConfiguration.touchSlop
                                        ) {
                                            isTap = false
                                        }
                                        if (!change.pressed) {
                                            break
                                        }
                                    }
                                    if (isTap) {
                                        onDismissRequest()
                                    }
                                }
                            },
                    )
                    if (positionProvider.hasPositioningBounds) {
                        val maxHeight = if (menuSize.value.height > 0) {
                            val density = LocalDensity.current
                            val requestedMaxHeight = with(density) {
                                style.maxHeight.roundToPx()
                            }
                            with(density) {
                                positionProvider.maxHeightForMenu(
                                    requestedMaxHeight = requestedMaxHeight,
                                    preferredHeight = menuSize.value.height,
                                ).toDp()
                            }
                        } else {
                            style.maxHeight
                        }
                        val currentPopupContent =
                            rememberUpdatedState<@Composable () -> Unit> {
                                ProvideStyledLocals(contentColor = style.contentColor) {
                                    PopupMenuContent(
                                        scrollState = scrollState,
                                        style = style,
                                        maxHeight = maxHeight,
                                        contentPadding = contentPadding,
                                        header = header,
                                        content = content,
                                    )
                                }
                            }
                        val popupContent = remember {
                            movableContentOf { currentPopupContent.value() }
                        }
                        Box(
                            modifier = Modifier
                                .offset { positionProvider.menuPosition(menuSize.value) }
                                .then(modifier)
                                .onSizeChanged {
                                    menuSize.value = it
                                    positionProvider.onMenuSizeChanged()
                                }
                                .graphicsLayer {
                                    alpha = if (menuSize.value == IntSize.Zero) 0f else 1f
                                },
                        ) {
                            if (menuSize.value == IntSize.Zero) {
                                StyledSurface(
                                    modifier = Modifier,
                                    shape = style.shape,
                                    color = style.containerColor,
                                    contentColor = style.contentColor,
                                    shadowElevation = style.shadowElevation,
                                    border = style.border,
                                ) { popupContent() }
                            } else {
                                Box(
                                    modifier = Modifier.requiredSize(
                                        width = with(LocalDensity.current) {
                                            menuSize.value.width.toDp()
                                        },
                                        height = with(LocalDensity.current) {
                                            menuSize.value.height.toDp()
                                        },
                                    ),
                                ) {
                                    if (shadowUnfoldEnabled) {
                                        StyledSurface(
                                            modifier = Modifier.fillMaxSize().graphicsLayer {
                                                scaleY = shadowScale
                                                transformOrigin = TransformOrigin(
                                                    pivotFractionX = 0.5f,
                                                    pivotFractionY = animation.shadowOriginY,
                                                )
                                            },
                                            shape = style.shape,
                                            color = Color.Transparent,
                                            contentColor = style.contentColor,
                                            shadowElevation = style.shadowElevation,
                                            border = null,
                                        ) {}
                                    }
                                    AnimatedVisibility(
                                        visibleState = menuTransitionState,
                                        enter = animation.enter,
                                        exit = animation.exit,
                                    ) {
                                        StyledSurface(
                                            modifier = Modifier.fillMaxSize(),
                                            shape = style.shape,
                                            color = style.containerColor,
                                            contentColor = style.contentColor,
                                            shadowElevation = if (shadowUnfoldEnabled) {
                                                0.dp
                                            } else {
                                                style.shadowElevation
                                            },
                                            border = style.border,
                                        ) { popupContent() }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return
        }

        StyledPopup(
            visible = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            popupPositionProvider = StyledPopupDefaults.positionProvider(
                offset = offset,
                isVisible = expanded,
                placement = placement,
                horizontalAlignment = horizontalAlignment,
            ),
            properties = properties,
            style = style,
        ) {
            ProvideStyledLocals(contentColor = style.contentColor) {
                PopupMenuContent(
                    scrollState = scrollState,
                    style = style,
                    maxHeight = style.maxHeight,
                    contentPadding = contentPadding,
                    header = header,
                    content = content,
                )
            }
        }
    }
}

@Composable
private fun PopupMenuContent(
    scrollState: ScrollState,
    style: StyledPopup.Style,
    maxHeight: androidx.compose.ui.unit.Dp,
    contentPadding: androidx.compose.foundation.layout.PaddingValues,
    header: (@Composable () -> Unit)?,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(Modifier.fillMaxWidth().heightIn(max = maxHeight)) {
        header?.invoke()
        StyledVerticalScrollContainer(
            modifier = Modifier.weight(1f, fill = false).fillMaxWidth(),
            scrollState = scrollState,
        ) {
            Column(
                Modifier.fillMaxWidth().padding(contentPadding),
                content = content,
            )
        }
    }
}
