package com.michaelflisar.composestyled.foundation.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledPopup
import com.michaelflisar.composestyled.ui.components.StyledPopupDefaults
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.layout.StyledVerticalScrollbarContainer
import kotlin.math.abs
import kotlin.math.roundToInt

@InternalComposeStyledApi
class FoundationPopupMenu : StyledPopupMenu.Implementation {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        expanded: Boolean,
        onDismissRequest: () -> Unit,
        modifier: Modifier,
        offset: DpOffset,
        placement: StyledPopupMenu.Placement,
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
                    parentWindowOriginOnScreen = style.parentWindowOriginOnScreen,
                )
            val menuSize = remember { mutableStateOf(IntSize.Zero) }
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
                        StyledSurface(
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
                            shape = style.shape,
                            color = style.containerColor,
                            contentColor = style.contentColor,
                            shadowElevation = style.shadowElevation,
                            border = style.border,
                        ) {
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
                }
            }
            return
        }

        StyledPopup(
            visible = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            popupPositionProvider = StyledPopupDefaults.positionProvider(offset, expanded, placement),
            properties = properties,
            style = style,
        ) {
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

@Composable
private fun PopupMenuContent(
    scrollState: ScrollState,
    style: StyledPopup.Style,
    maxHeight: androidx.compose.ui.unit.Dp,
    contentPadding: androidx.compose.foundation.layout.PaddingValues,
    header: (@Composable () -> Unit)?,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(Modifier.heightIn(max = maxHeight)) {
        header?.invoke()
        StyledVerticalScrollbarContainer(
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
