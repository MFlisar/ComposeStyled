package com.michaelflisar.composestyled.foundation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledPopup
import com.michaelflisar.composestyled.ui.components.StyledPopupDefaults
import com.michaelflisar.composestyled.ui.components.StyledSurface

@InternalComposeStyledApi
class FoundationPopup : StyledPopup.Implementation {

    companion object {
        @Composable
        fun create() = remember { FoundationPopup() }
    }

    @Composable
    override fun Render(
        visible: Boolean,
        onDismissRequest: () -> Unit,
        modifier: Modifier,
        popupPositionProvider: PopupPositionProvider,
        properties: PopupProperties,
        style: StyledPopup.Style,
        content: @Composable () -> Unit,
    ) {
        val transitionState = remember { MutableTransitionState(false) }
        val contentMeasured = remember { mutableStateOf(false) }
        val measuredSize = remember { mutableStateOf(IntSize.Zero) }
        val currentContent = rememberUpdatedState(content)
        val popupContent = remember { movableContentOf { currentContent.value() } }
        val isMeasuringContent =
            !style.windowOverlay && visible &&
                (!contentMeasured.value || !style.animationPlacementKnown)
        transitionState.targetState = visible &&
            (style.windowOverlay || contentMeasured.value && style.animationPlacementKnown)
        val shadowEnterSpec = style.animation.shadowEnterSpec
        val shadowExitSpec = style.animation.shadowExitSpec
        val contentRevealEnterSpec = style.animation.contentRevealEnterSpec
        val contentRevealExitSpec = style.animation.contentRevealExitSpec
        val contentRevealEnabled =
            contentRevealEnterSpec != null && contentRevealExitSpec != null
        val shadowUnfoldEnabled =
            !style.windowOverlay && shadowEnterSpec != null && shadowExitSpec != null
        val shadowScale by animateFloatAsState(
            targetValue = if (shadowUnfoldEnabled) {
                if (transitionState.targetState) 1f else 0f
            } else {
                1f
            },
            animationSpec = if (transitionState.targetState) {
                shadowEnterSpec ?: tween(0)
            } else {
                shadowExitSpec ?: tween(0)
            },
            label = "popupShadowScale",
        )
        LaunchedEffect(
            transitionState.currentState,
            transitionState.targetState,
            isMeasuringContent,
            style.onTransitionFinished,
        ) {
            if (transitionState.isIdle && !isMeasuringContent) {
                if (!style.windowOverlay && !transitionState.currentState) {
                    contentMeasured.value = false
                }
                style.onTransitionFinished?.invoke(transitionState.currentState)
            }
        }

        if (transitionState.currentState || transitionState.targetState || isMeasuringContent) {
            val overlayModifier = if (style.windowOverlay) {
                val density = LocalDensity.current
                with(density) {
                    Modifier.size(
                        width = style.windowOverlaySize.width.toDp(),
                        height = style.windowOverlaySize.height.toDp(),
                    )
                }
            } else {
                Modifier
            }
            val popupProperties = if (style.windowOverlay) {
                PopupProperties(
                    focusable = false,
                    dismissOnBackPress = properties.dismissOnBackPress,
                    dismissOnClickOutside = false,
                    clippingEnabled = false,
                    usePlatformDefaultWidth = false,
                )
            } else {
                properties
            }
            Popup(
                popupPositionProvider = popupPositionProvider,
                onDismissRequest = onDismissRequest,
                properties = popupProperties,
            ) {
                if (isMeasuringContent) {
                    StyledSurface(
                        modifier = if (style.windowOverlay) {
                            Modifier.fillMaxSize().then(modifier)
                        } else {
                            Modifier.width(IntrinsicSize.Max)
                                .heightIn(max = style.maxHeight)
                                .then(modifier)
                                .onGloballyPositioned {
                                    measuredSize.value = it.size
                                    contentMeasured.value = true
                                }
                                .graphicsLayer { alpha = 0f }
                        },
                        shape = style.shape,
                        color = style.containerColor,
                        contentColor = style.contentColor,
                        shadowElevation = style.shadowElevation,
                        border = style.border,
                        content = popupContent,
                    )
                } else {
                    if (style.windowOverlay) {
                        AnimatedVisibility(
                            modifier = overlayModifier,
                            visibleState = transitionState,
                            enter = StyledPopupDefaults.Animations.windowOverlayLifecycle.enter,
                            exit = StyledPopupDefaults.Animations.windowOverlayLifecycle.exit,
                        ) {
                            StyledSurface(
                                modifier = Modifier.fillMaxSize().then(modifier),
                                shape = style.shape,
                                color = style.containerColor,
                                contentColor = style.contentColor,
                                shadowElevation = style.shadowElevation,
                                border = style.border,
                                content = popupContent,
                            )
                        }
                    } else if (measuredSize.value != IntSize.Zero) {
                        Box(
                            modifier = Modifier
                                .then(modifier)
                                .requiredSize(
                                    width = with(LocalDensity.current) {
                                        measuredSize.value.width.toDp()
                                    },
                                    height = with(LocalDensity.current) {
                                        measuredSize.value.height.toDp()
                                    },
                                )
                        ) {
                            if (shadowUnfoldEnabled) {
                                StyledSurface(
                                    modifier = Modifier.fillMaxSize().graphicsLayer {
                                        scaleY = shadowScale
                                        transformOrigin = TransformOrigin(
                                            pivotFractionX = 0.5f,
                                            pivotFractionY = style.animation.shadowOriginY,
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
                                visibleState = transitionState,
                                enter = if (contentRevealEnabled) {
                                    EnterTransition.None
                                } else {
                                    style.animation.enter
                                },
                                exit = if (contentRevealEnabled) {
                                    ExitTransition.None
                                } else {
                                    style.animation.exit
                                },
                            ) {
                                val revealProgress by transition.animateFloat(
                                    transitionSpec = {
                                        if (targetState == EnterExitState.Visible) {
                                            contentRevealEnterSpec ?: tween(0)
                                        } else {
                                            contentRevealExitSpec ?: tween(0)
                                        }
                                    },
                                    label = "popupContentReveal",
                                ) { state ->
                                    if (state == EnterExitState.Visible) 1f else 0f
                                }
                                val revealModifier = if (contentRevealEnabled) {
                                    Modifier.drawWithContent {
                                        val revealHeight =
                                            size.height * revealProgress.coerceIn(0f, 1f)
                                        val revealTop = if (style.animation.shadowOriginY <= 0.5f) {
                                            0f
                                        } else {
                                            size.height - revealHeight
                                        }
                                        clipRect(
                                            left = 0f,
                                            top = revealTop,
                                            right = size.width,
                                            bottom = revealTop + revealHeight,
                                        ) {
                                            this@drawWithContent.drawContent()
                                        }
                                    }
                                } else {
                                    Modifier
                                }
                                StyledSurface(
                                    modifier = Modifier.fillMaxSize().then(revealModifier),
                                    shape = style.shape,
                                    color = style.containerColor,
                                    contentColor = style.contentColor,
                                    shadowElevation = if (shadowUnfoldEnabled) {
                                        0.dp
                                    } else {
                                        style.shadowElevation
                                    },
                                    border = style.border,
                                    content = popupContent,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
