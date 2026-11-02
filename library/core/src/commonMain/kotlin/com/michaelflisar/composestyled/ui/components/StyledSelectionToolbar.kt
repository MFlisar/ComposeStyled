package com.michaelflisar.composestyled.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.SelectionState
import com.michaelflisar.composestyled.ui.icons.StyledIcons

object StyledSelectionToolbarDefaults {
    val shape: Shape
        @Composable get() = StyledTheme.shapes.large
}

/** Shows selection actions while [selection] is active, with a default action to clear and close it. */
@Composable
fun StyledSelectionToolbar(
    selection: SelectionState<*>,
    modifier: Modifier = Modifier,
    shape: Shape = StyledSelectionToolbarDefaults.shape,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    onDismiss: () -> Unit = selection::clearAndClose,
    title: @Composable (selectedCount: Int) -> Unit = { count -> StyledText("$count") },
    actions: @Composable RowScope.() -> Unit = {},
) {
    AnimatedVisibility(
        visible = selection.isActive,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        StyledSurface(
            modifier = modifier,
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
        ) {
            StyledTopBar(
                containerColor = containerColor,
                contentColor = contentColor,
                windowInsets = windowInsets,
                navigationIcon = {
                    StyledIconButton(
                        onClick = onDismiss,
                        icon = StyledIcons.Close,
                        contentDescription = "Close selection",
                    )
                },
                actions = actions,
                title = { title(selection.selectedCount) },
            )
        }
    }
}
