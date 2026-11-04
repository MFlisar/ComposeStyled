package com.michaelflisar.composestyled.foundation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledTopBar
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
class FoundationTopBar(
    private val minimumHeight: Dp,
    private val centerTitle: Boolean = false,
) : StyledTopBar.Implementation {

    @Composable
    override fun Render(
        variant: StyledTopBar.Variant,
        modifier: Modifier,
        containerColor: Color,
        contentColor: Color,
        windowInsets: WindowInsets,
        navigationIcon: @Composable () -> Unit,
        actions: @Composable RowScope.() -> Unit,
        title: @Composable () -> Unit,
    ) {
        val gap = StyledTheme.spacing.medium
        val hasMeasuredNavigation = remember { mutableStateOf(false) }
        val navigationHasContent = remember { mutableStateOf(true) }
        val animateNavigationGap = remember { mutableStateOf(false) }
        val targetGap = if (navigationHasContent.value) gap else 0.dp
        val navigationGap = animateDpAsState(
            targetValue = targetGap,
            animationSpec = if (animateNavigationGap.value) spring() else snap(),
        ).value
        UpdateStyledThemeLocals(
            contentColor = contentColor,
        ) {
            Layout(
                modifier = modifier
                    .fillMaxWidth()
                    .background(containerColor)
                    .windowInsetsPadding(windowInsets)
                    .heightIn(min = minimumHeight)
                    .padding(
                        start = StyledTheme.padding.content.calculateStartPadding(LocalLayoutDirection.current),
                        end = StyledTheme.padding.content.calculateEndPadding(LocalLayoutDirection.current),
                    ),
                content = {
                        Box(
                            modifier = Modifier.onSizeChanged { size ->
                                val hasContent = size.width > 0
                                if (
                                    hasMeasuredNavigation.value &&
                                    navigationHasContent.value != hasContent
                                ) {
                                    animateNavigationGap.value = true
                                }
                                navigationHasContent.value = hasContent
                                hasMeasuredNavigation.value = true
                            },
                            contentAlignment = Alignment.Center,
                        ) {
                            navigationIcon()
                        }
                        Box {
                            UpdateStyledThemeLocals(
                                textStyle = StyledTheme.typography.titleLarge,
                            content = title,
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, content = actions)
                },
            ) { measurables, constraints ->
                val loose = constraints.copy(minWidth = 0, minHeight = 0)
                val navigation = measurables[0].measure(loose)
                val action = measurables[2].measure(
                    loose.copy(maxWidth = (constraints.maxWidth - navigation.width).coerceAtLeast(0))
                )
                val start = navigation.width + navigationGap.roundToPx()
                val end = action.width + if (action.width > 0) gap.roundToPx() else 0
                val centered = centerTitle || variant == StyledTopBar.Variant.CenterAligned
                val titlePadding = if (centered) 2 * maxOf(start, end) else start + end
                val titlePlaceable = measurables[1].measure(
                    loose.copy(maxWidth = (constraints.maxWidth - titlePadding).coerceAtLeast(0))
                )
                val width = if (constraints.hasBoundedWidth) constraints.maxWidth else {
                    maxOf(constraints.minWidth, start + titlePlaceable.width + end)
                }
                val height = maxOf(
                    constraints.minHeight, navigation.height, action.height, titlePlaceable.height,
                )
                layout(width, height) {
                    navigation.placeRelative(0, (height - navigation.height) / 2)
                    action.placeRelative(width - action.width, (height - action.height) / 2)
                    titlePlaceable.placeRelative(
                        if (centered) (width - titlePlaceable.width) / 2 else start,
                        (height - titlePlaceable.height) / 2,
                    )
                }
            }
        }
    }
}
