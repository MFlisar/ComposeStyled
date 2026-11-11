package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.lerp as lerpTextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.material3.ProvideStyledLocals
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledExpandableTopBar
import com.michaelflisar.composestyled.ui.components.StyledExpandableTopBar.DefaultExpandedHeight
import com.michaelflisar.composestyled.ui.state.StyledExpandableTopBarScrollBehavior

class Material3ExpandableTopBar internal constructor() :
    StyledExpandableTopBar.Implementation {

    @OptIn(ExperimentalMaterial3Api::class, InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        behavior: StyledExpandableTopBarScrollBehavior,
        modifier: Modifier,
        expandedTitlePadding: Dp,
        expandedHeight: Dp?,
        containerColor: Color,
        contentColor: Color,
        expandedContainerColor: Color,
        expandedContentColor: Color,
        windowInsets: WindowInsets,
        navigationIcon: @Composable () -> Unit,
        actions: @Composable RowScope.() -> Unit,
        title: @Composable () -> Unit,
    ) {
        val fraction = behavior.state.collapsedFraction
        val expandedTitleStyle = StyledTheme.typography.headlineMedium
        val compactTitleStyle = StyledTheme.typography.titleLarge
        val animatedTitleStyle = lerpTextStyle(expandedTitleStyle, compactTitleStyle, fraction)
        val animatedContainerColor = lerp(expandedContainerColor, containerColor, fraction)
        val animatedContentColor = lerp(expandedContentColor, contentColor, fraction)
        val density = LocalDensity.current
        val titleLineHeightPx = with(density) {
            val fontSizePx = expandedTitleStyle.fontSize.toPx()
            when {
                expandedTitleStyle.lineHeight.isSp -> expandedTitleStyle.lineHeight.toPx()
                expandedTitleStyle.lineHeight.isEm ->
                    expandedTitleStyle.lineHeight.value * fontSizePx
                else -> fontSizePx
            }
        }
        val titlePaddingPx = with(density) { expandedTitlePadding.roundToPx() }
        val topInsetPx = windowInsets.getTop(density)
        val collapsedHeight = 64.dp
        val minimumExpandedHeight = maxOf(
            DefaultExpandedHeight,
            collapsedHeight + with(density) {
                (titleLineHeightPx + 2 * titlePaddingPx).toInt().toDp()
            },
        )
        val nativeExpandedHeight = expandedHeight?.let { requestedHeight ->
            with(density) {
                (requestedHeight.roundToPx() - topInsetPx)
                    .coerceAtLeast(minimumExpandedHeight.roundToPx())
                    .toDp()
            }
        } ?: minimumExpandedHeight
        val collapseRangePx = with(density) {
            (nativeExpandedHeight - collapsedHeight).roundToPx().coerceAtLeast(0)
        }
        val nativeTitleBottomPadding = 28.dp
        val topAppBarState = rememberTopAppBarState()
        val nativeScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            state = topAppBarState,
        )
        SideEffect {
            behavior.state.updateScrollRange(collapseRangePx)
            topAppBarState.heightOffsetLimit = -collapseRangePx.toFloat()
            topAppBarState.heightOffset =
                topAppBarState.heightOffsetLimit * behavior.state.collapsedFraction
        }
        val colors = TopAppBarDefaults.topAppBarColors(
            containerColor = animatedContainerColor,
            scrolledContainerColor = animatedContainerColor,
            navigationIconContentColor = animatedContentColor,
            titleContentColor = animatedContentColor,
            actionIconContentColor = animatedContentColor,
        )
        ProvideStyledLocals(contentColor = animatedContentColor) {
            LargeTopAppBar(
                title = {
                    ProvideStyledLocals(
                        contentColor = animatedContentColor,
                        textStyle = animatedTitleStyle,
                    ) {
                        Box(
                            modifier = Modifier.offset(
                                y = (nativeTitleBottomPadding - expandedTitlePadding) *
                                    (1f - fraction),
                            ),
                            content = { title() },
                        )
                    }
                },
                modifier = modifier,
                navigationIcon = navigationIcon,
                actions = actions,
                windowInsets = windowInsets,
                expandedHeight = nativeExpandedHeight,
                colors = colors,
                scrollBehavior = nativeScrollBehavior,
            )
        }
    }

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3ExpandableTopBar() }
    }
}
