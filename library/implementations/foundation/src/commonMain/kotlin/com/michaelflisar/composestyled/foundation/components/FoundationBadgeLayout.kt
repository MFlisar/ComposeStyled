package com.michaelflisar.composestyled.foundation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
@Composable
fun FoundationBadgeLayout(
    modifier: Modifier,
    alignment: Alignment,
    offset: DpOffset,
    badge: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = {
            Box(propagateMinConstraints = true) { content() }
            Box { badge() }
        },
    ) { measurables, constraints ->
        val anchor = measurables[0].measure(constraints)
        // The overlay must not inherit the anchor's size constraints or affect its layout size.
        val badgePlaceable = measurables[1].measure(Constraints())
        val point = alignment.align(
            size = IntSize.Zero,
            space = IntSize(anchor.width, anchor.height),
            layoutDirection = layoutDirection,
        )
        val offsetX = offset.x.roundToPx() * if (layoutDirection == LayoutDirection.Ltr) 1 else -1
        val offsetY = offset.y.roundToPx()
        layout(
            width = anchor.width,
            height = anchor.height,
            alignmentLines = mapOf(
                FirstBaseline to anchor[FirstBaseline],
                LastBaseline to anchor[LastBaseline],
            ),
        ) {
            anchor.place(0, 0)
            badgePlaceable.place(
                x = point.x - badgePlaceable.width / 2 + offsetX,
                y = point.y - badgePlaceable.height / 2 + offsetY,
            )
        }
    }
}
