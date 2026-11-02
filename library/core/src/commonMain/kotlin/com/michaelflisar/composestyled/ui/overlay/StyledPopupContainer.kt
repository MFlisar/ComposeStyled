package com.michaelflisar.composestyled.ui.overlay

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.IntState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.layout.positionOnScreen

@Stable
/** Layout scope exposing measured container geometry to popup content. */
class StyledPopupContainerScope internal constructor(
    private val width: IntState,
    private val windowOriginOnScreen: androidx.compose.runtime.MutableState<Offset?>,
) {
    val containerWidth: Int
        get() = width.intValue

    internal val parentWindowOriginOnScreen: Offset?
        get() = windowOriginOnScreen.value
}

/** Places popup content and its anchor content in a shared measured container. */
@Composable
fun StyledPopupContainer(
    modifier: Modifier = Modifier,
    popup: @Composable StyledPopupContainerScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    val width = remember { mutableIntStateOf(0) }
    val windowOriginOnScreen = remember { mutableStateOf<Offset?>(null) }
    val scope = remember { StyledPopupContainerScope(width, windowOriginOnScreen) }

    Box(
        modifier = modifier.onGloballyPositioned {
            width.intValue = it.size.width
            val inWindow = it.positionInWindow()
            val onScreen = it.positionOnScreen()
            val origin = Offset(
                x = onScreen.x - inWindow.x,
                y = onScreen.y - inWindow.y,
            )
            if (origin.x.isFinite() && origin.y.isFinite()) {
                windowOriginOnScreen.value = origin
            }
        },
    ) {
        content()
        scope.popup()
    }
}