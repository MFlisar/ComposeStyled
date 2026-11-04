package com.michaelflisar.composestyled.foundation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledPopup
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
        val currentContent = rememberUpdatedState(content)
        val popupContent = remember { movableContentOf { currentContent.value() } }
        transitionState.targetState = visible && (style.windowOverlay || contentMeasured.value)
        val isMeasuringContent =
            !style.windowOverlay && visible && !contentMeasured.value

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
                    AnimatedVisibility(
                        modifier = overlayModifier,
                        visibleState = transitionState,
                        enter = if (style.windowOverlay) fadeIn() else style.animation.enter,
                        exit = if (style.windowOverlay) fadeOut() else style.animation.exit,
                    ) {
                        StyledSurface(
                            modifier = if (style.windowOverlay) {
                                Modifier.fillMaxSize().then(modifier)
                            } else {
                                Modifier.width(IntrinsicSize.Max)
                                    .heightIn(max = style.maxHeight)
                                    .then(modifier)
                            },
                            shape = style.shape,
                            color = style.containerColor,
                            contentColor = style.contentColor,
                            shadowElevation = style.shadowElevation,
                            border = style.border,
                            content = popupContent,
                        )
                    }
                }
            }
        }
    }
}
