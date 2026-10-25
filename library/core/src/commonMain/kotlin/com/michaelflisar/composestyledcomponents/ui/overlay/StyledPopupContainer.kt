package com.michaelflisar.composestyledcomponents.ui.overlay

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.IntState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned

@Stable
class StyledPopupContainerScope internal constructor(
    private val width: IntState,
) {
    val containerWidth: Int
        get() = width.intValue
}

@Composable
fun StyledPopupContainer(
    modifier: Modifier = Modifier,
    popup: @Composable StyledPopupContainerScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    val width = remember { mutableIntStateOf(0) }
    val scope = remember { StyledPopupContainerScope(width) }

    Box(
        modifier = modifier.onGloballyPositioned {
            width.intValue = it.size.width
        },
    ) {
        content()
        scope.popup()
    }
}