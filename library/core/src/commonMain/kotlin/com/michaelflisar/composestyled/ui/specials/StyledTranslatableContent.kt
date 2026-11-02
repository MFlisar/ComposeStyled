package com.michaelflisar.composestyled.ui.specials

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout

object StyledTranslatableContent {
    enum class Side {
        Left,
        Right,
        Top,
        Bottom,
    }
}

@LayoutScopeMarker
@Immutable
interface StyledTranslatableSideScope {
    @Stable
    fun Modifier.stretch(stretch: Boolean): Modifier
}

internal class StyledTranslatableSideScopeInstance(
    private val side: StyledTranslatableContent.Side,
) : StyledTranslatableSideScope {
    override fun Modifier.stretch(stretch: Boolean): Modifier {
        if (!stretch) return this
        return when (side) {
            StyledTranslatableContent.Side.Left,
            StyledTranslatableContent.Side.Right -> fillMaxHeight()
            StyledTranslatableContent.Side.Top,
            StyledTranslatableContent.Side.Bottom -> fillMaxWidth()
        }
    }
}

/** Places content that can be translated to reveal edge actions or adjacent content. */
@Composable
fun StyledTranslatableContent(
    modifier: Modifier = Modifier,
    side: StyledTranslatableContent.Side = StyledTranslatableContent.Side.Left,
    translation: Float = 0f,
    content: @Composable StyledTranslatableSideScope.() -> Unit,
) {
    Layout(
        modifier = modifier.clipToBounds(),
        content = {
            Box {
                StyledTranslatableSideScopeInstance(side).content()
            }
        },
    ) { measurables, constraints ->
        val placeable = measurables.single().measure(constraints)
        val width = when (side) {
            StyledTranslatableContent.Side.Left,
            StyledTranslatableContent.Side.Right -> (placeable.width * translation).toInt()
                .coerceAtLeast(0)
            StyledTranslatableContent.Side.Top,
            StyledTranslatableContent.Side.Bottom -> placeable.width
        }
        val height = when (side) {
            StyledTranslatableContent.Side.Left,
            StyledTranslatableContent.Side.Right -> placeable.height
            StyledTranslatableContent.Side.Top,
            StyledTranslatableContent.Side.Bottom -> (placeable.height * translation).toInt()
                .coerceAtLeast(0)
        }
        val (x, y) = when (side) {
            StyledTranslatableContent.Side.Left -> width - placeable.width to 0
            StyledTranslatableContent.Side.Right -> 0 to 0
            StyledTranslatableContent.Side.Top -> 0 to height - placeable.height
            StyledTranslatableContent.Side.Bottom -> 0 to 0
        }
        layout(width, height) {
            placeable.placeRelative(x, y)
        }
    }
}
