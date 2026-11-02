package com.michaelflisar.composestyled.ui.specials

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.recalculateWindowInsets
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.runtime.StyledContext

@Immutable
sealed interface StyledPaneWidthLimit {
    @Immutable
    data class Percentage(val value: Float) : StyledPaneWidthLimit {
        init {
            require(value in 0f..100f) { "Percentage must be between 0 and 100." }
        }
    }

    @Immutable
    data class Fixed(val width: Dp) : StyledPaneWidthLimit {
        init {
            require(width.value.isFinite() && width >= 0.dp) { "Width must be a non-negative finite value." }
        }
    }
}

@Stable
class StyledSidePane internal constructor(
    val label: String,
    val expanded: MutableState<Boolean>,
    val icon: @Composable () -> Unit,
    val content: @Composable () -> Unit,
) {
    enum class Context : StyledContext {
        /** Applied to the pane toggle and icon slot for context-aware default button roles. */
        IconSlot,
    }
}

/**
 * Creates a pane whose expanded state is saved across supported host recreation.
 */
@Composable
fun rememberStyledSidePane(
    label: String,
    expanded: Boolean = false,
    icon: @Composable () -> Unit = { DefaultPaneIcon(label) },
    content: @Composable () -> Unit,
): StyledSidePane = StyledSidePane(
    label = label,
    expanded = rememberSaveable(expanded) { mutableStateOf(expanded) },
    icon = icon,
    content = content,
)

/**
 * [windowInsets] protects each pane's top, bottom and outer horizontal edge.
 * Insets are consumed only inside that pane. The main content receives no padding;
 * its inset consumption is recalculated from its bounds for nested inset-aware layouts.
 */
@Composable
fun StyledPaneContainer(
    modifier: Modifier = Modifier,
    left: StyledSidePane? = null,
    right: StyledSidePane? = null,
    maxWidthLeft: StyledPaneWidthLimit = StyledPaneWidthLimit.Percentage(20f),
    maxWidthRight: StyledPaneWidthLimit = StyledPaneWidthLimit.Percentage(20f),
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        val resolvedMaxWidthLeft = maxWidthLeft.resolve(maxWidth)
        val resolvedMaxWidthRight = maxWidthRight.resolve(maxWidth)
        Row(modifier = Modifier.fillMaxSize()) {
            if (left != null) {
                StyledPaneSide(
                    modifier = Modifier.sizeIn(maxWidth = resolvedMaxWidthLeft),
                    side = StyledPaneSide.Side.Left,
                    label = left.label,
                    expanded = left.expanded,
                    icon = left.icon,
                    windowInsets = windowInsets.only(WindowInsetsSides.Vertical + WindowInsetsSides.Start),
                ) {
                    left.content()
                }
            }
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight().recalculateWindowInsets(),
            ) {
                content()
            }
            if (right != null) {
                StyledPaneSide(
                    modifier = Modifier.sizeIn(maxWidth = resolvedMaxWidthRight),
                    side = StyledPaneSide.Side.Right,
                    label = right.label,
                    expanded = right.expanded,
                    icon = right.icon,
                    windowInsets = windowInsets.only(WindowInsetsSides.Vertical + WindowInsetsSides.End),
                ) {
                    right.content()
                }
            }
        }
    }
}

private fun StyledPaneWidthLimit.resolve(containerWidth: Dp): Dp = when (this) {
    is StyledPaneWidthLimit.Percentage -> containerWidth * (value / 100f)
    is StyledPaneWidthLimit.Fixed -> width
}
