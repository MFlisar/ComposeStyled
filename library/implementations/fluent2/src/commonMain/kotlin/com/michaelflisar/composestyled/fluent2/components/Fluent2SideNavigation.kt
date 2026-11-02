package com.michaelflisar.composestyled.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.foundation.components.FoundationSideNavigation
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.internal.LocalStyledSelectionColors

@OptIn(InternalComposeStyledApi::class)
class Fluent2SideNavigation internal constructor() : FoundationSideNavigation(showSelectionBackground = false) {

    override val itemHorizontalPadding: Dp
        @Composable get() = 8.dp

    override val itemContentPadding: Dp
        @Composable get() = 8.dp

    override val iconColumnWidth: Dp
        @Composable get() = 36.dp

    override val defaults: StyledSideNavigation.Defaults
        @Composable get() = StyledSideNavigation.Defaults(compactWidth = 80.dp, expandedWidth = 320.dp)

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Fluent2SideNavigation() }
    }

    @Composable
    override fun SelectionIndicator(selected: Boolean, enabled: Boolean, modifier: Modifier) {
        val progress by animateFloatAsState(if (selected) 1f else 0f, tween(160))
        val base = LocalStyledSelectionColors.current?.indicatorColor?.takeUnless { it == Color.Unspecified }
            ?: StyledTheme.colorScheme.primary
        val color = if (enabled) base else base.copy(
            alpha = base.alpha * StyledTheme.colorScheme.disableFactors.default,
        )
        Canvas(modifier) {
            val width = 3.dp.toPx()
            val height = minOf(20.dp.toPx(), size.height) * progress
            val inset = 4.dp.toPx()
            drawRoundRect(
                color = color.copy(alpha = color.alpha * progress),
                topLeft = Offset(
                    if (layoutDirection == LayoutDirection.Ltr) inset else size.width - inset - width,
                    (size.height - height) / 2,
                ),
                size = Size(width, height),
                cornerRadius = CornerRadius(width / 2),
            )
        }
    }
}
