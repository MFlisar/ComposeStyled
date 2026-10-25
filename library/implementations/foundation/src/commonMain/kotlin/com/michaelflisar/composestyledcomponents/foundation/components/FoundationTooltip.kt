package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.onLongClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledPopup
import com.michaelflisar.composestyledcomponents.ui.components.StyledPopupDefaults
import com.michaelflisar.composestyledcomponents.ui.components.StyledTooltip
import com.michaelflisar.composestyledcomponents.ui.components.StyledTooltipState
import com.michaelflisar.composestyledcomponents.ui.overlay.StyledPopupContainer
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

@InternalComposeStyledApi
class FoundationTooltip internal constructor(
    override val defaults: StyledTooltip.Defaults,
) : StyledTooltip.Implementation {

    companion object {
        @Composable
        fun create(
            defaults: StyledTooltip.Defaults = defaults(),
        ) = remember(
            defaults.shape,
            defaults.containerColor,
            defaults.contentColor,
            defaults.maxWidth,
            defaults.shadowElevation,
            defaults.contentPadding,
        ) {
            FoundationTooltip(defaults)
        }

        @Composable
        fun defaults(): StyledTooltip.Defaults {
            val shape = StyledTheme.shapes.small
            val containerColor = StyledTheme.colorScheme.surfaceVariant
            val contentColor = StyledTheme.colorScheme.onSurfaceVariant
            val padding = StyledTheme.padding.small
            val contentPadding = remember(padding) {
                PaddingValues(horizontal = padding, vertical = padding)
            }
            return remember(shape, containerColor, contentColor, contentPadding) {
                StyledTooltip.Defaults(
                    shape = shape,
                    containerColor = containerColor,
                    contentColor = contentColor,
                    maxWidth = 240.dp,
                    shadowElevation = 4.dp,
                    contentPadding = contentPadding,
                )
            }
        }
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        state: StyledTooltipState,
        enableUserInput: Boolean,
        focusable: Boolean,
        onDismissRequest: (() -> Unit)?,
        modifier: Modifier,
        shape: Shape,
        containerColor: Color,
        contentColor: Color,
        maxWidth: Dp,
        shadowElevation: Dp,
        contentPadding: PaddingValues,
        tooltip: @Composable () -> Unit,
        content: @Composable () -> Unit,
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        var isHovered by remember(interactionSource) { mutableStateOf(false) }
        var hasFocus by remember { mutableStateOf(false) }
        var isLongPressed by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val inputModeManager = LocalInputModeManager.current
        val positionProvider = tooltipPositionProvider()

        LaunchedEffect(interactionSource, enableUserInput) {
            if (enableUserInput) {
                interactionSource.interactions.collect { interaction ->
                    when (interaction) {
                        is HoverInteraction.Enter -> isHovered = true
                        is HoverInteraction.Exit -> isHovered = false
                    }
                }
            } else {
                isHovered = false
            }
        }

        LaunchedEffect(state, enableUserInput, inputModeManager) {
            snapshotFlow {
                Triple(
                    isHovered,
                    hasFocus && inputModeManager.inputMode == InputMode.Keyboard,
                    isLongPressed,
                )
            }
                .distinctUntilChanged()
                .collect { (hovered, keyboardFocused, longPressed) ->
                    if (!enableUserInput) {
                        return@collect
                    }
                    val priority = when {
                        keyboardFocused || longPressed -> MutatePriority.PreventUserInput
                        hovered -> MutatePriority.UserInput
                        else -> null
                    }
                    if (priority == null) {
                        state.dismiss()
                    } else {
                        launch {
                            state.show(priority)
                        }
                    }
                }
        }

        LaunchedEffect(state, state.isVisible, state.isPersistent) {
            if (state.isVisible && !state.isPersistent) {
                delay(TooltipDurationMillis)
                state.updateVisibilityFromRenderer(false)
            }
        }

        StyledPopupContainer(
            popup = {
                StyledPopup(
                    visible = state.isVisible,
                    onDismissRequest = {
                        state.dismiss()
                        onDismissRequest?.invoke()
                    },
                    modifier = Modifier.widthIn(max = maxWidth),
                    popupPositionProvider = positionProvider,
                    properties = StyledPopupDefaults.properties(
                        focusable = focusable,
                        dismissOnBackPress = focusable,
                        dismissOnClickOutside = true,
                        usePlatformDefaultWidth = false,
                    ),
                    style = StyledPopup.Style(
                        shape = shape,
                        containerColor = containerColor,
                        contentColor = contentColor,
                        shadowElevation = shadowElevation,
                        border = null,
                        maxHeight = 320.dp,
                    ),
                ) {
                    Box(Modifier.padding(contentPadding)) {
                        tooltip()
                    }
                }
            },
            content = {
                var anchorModifier = modifier
                if (enableUserInput) {
                    anchorModifier = anchorModifier
                        .hoverable(interactionSource = interactionSource)
                        .onFocusChanged {
                            hasFocus = it.isFocused || it.hasFocus
                        }
                        .focusGroup()
                        .pointerInput(state) {
                            awaitEachGesture {
                                val pass = PointerEventPass.Initial
                                val down = awaitFirstDown(
                                    requireUnconsumed = false,
                                    pass = pass,
                                )
                                if (down.type == PointerType.Touch ||
                                    down.type == PointerType.Stylus
                                ) {
                                    val longPressDetected =
                                        withTimeoutOrNull(viewConfiguration.longPressTimeoutMillis) {
                                            waitForUpOrCancellation(pass)
                                            false
                                        } ?: true
                                    if (longPressDetected) {
                                        isLongPressed = true
                                        val showJob = coroutineScope.launch(
                                            start = CoroutineStart.UNDISPATCHED,
                                        ) {
                                            state.show(MutatePriority.PreventUserInput)
                                        }
                                        try {
                                            val up = waitForUpOrCancellation(pass)
                                            up?.consume()
                                            if (up == null || state.isPersistent) {
                                                state.dismiss()
                                                showJob.cancel()
                                            }
                                        } finally {
                                            isLongPressed = false
                                        }
                                    }
                                }
                            }
                        }
                        .semantics {
                            onLongClick(action = {
                                coroutineScope.launch {
                                    state.show(MutatePriority.PreventUserInput)
                                }
                                true
                            })
                        }
                }
                Box(modifier = anchorModifier) {
                    content()
                }
            },
        )
    }
}

@Composable
private fun tooltipPositionProvider(): PopupPositionProvider {
    val density = LocalDensity.current
    return remember(density) {
        FoundationTooltipPositionProvider(
            with(density) { 8.dp.roundToPx() },
        )
    }
}

private class FoundationTooltipPositionProvider(
    private val spacing: Int,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val maxX = (windowSize.width - popupContentSize.width).coerceAtLeast(0)
        val x = (anchorBounds.left + (anchorBounds.width - popupContentSize.width) / 2)
            .coerceIn(0, maxX)

        val above = anchorBounds.top - popupContentSize.height - spacing
        val below = anchorBounds.bottom + spacing
        val maxY = (windowSize.height - popupContentSize.height).coerceAtLeast(0)
        val y = when {
            above >= 0 -> above
            below + popupContentSize.height <= windowSize.height -> below
            anchorBounds.top >= windowSize.height - anchorBounds.bottom -> above
            else -> below
        }.coerceIn(0, maxY)

        return IntOffset(x, y)
    }
}

private const val TooltipDurationMillis = 1_500L
