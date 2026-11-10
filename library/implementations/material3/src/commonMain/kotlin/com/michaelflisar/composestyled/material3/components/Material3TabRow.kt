package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledTabRow
import com.michaelflisar.composestyled.ui.internal.LocalStyledSelectionColors
import com.michaelflisar.composestyled.ui.internal.StyledSelectionColors
import com.michaelflisar.composestyled.ui.layout.StyledHorizontalScrollbarLayout

class Material3TabRow internal constructor() : StyledTabRow.Implementation {

    companion object {
        @Composable
        fun create() = remember { Material3TabRow() }
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        variant: StyledTabRow.Variant,
        scrollable: Boolean,
        selectedTabIndex: Int,
        modifier: Modifier,
        scrollState: ScrollState?,
        containerColor: Color,
        contentColor: Color,
        edgePadding: Dp,
        minTabWidth: Dp,
        divider: @Composable () -> Unit,
        tabs: @Composable () -> Unit,
    ) {
        val resolvedContentColor = if (contentColor == Color.Unspecified) {
            StyledTheme.contentColorFor(containerColor)
        } else {
            contentColor
        }
        val selectionColors = selectionColors(variant, containerColor, resolvedContentColor)
        val indicatorColor = selectionColors.indicatorColor ?: resolvedContentColor
        CompositionLocalProvider(LocalStyledSelectionColors provides selectionColors) {
            when (variant) {
                StyledTabRow.Variant.Primary -> {
                    if (scrollable) {
                        val activeScrollState = requireNotNull(scrollState) {
                            "Scrollable tab rows require a ScrollState"
                        }
                        ScrollableTabRowFrame(modifier, activeScrollState) {
                            PrimaryScrollableTabRow(
                                selectedTabIndex = selectedTabIndex,
                                modifier = Modifier.fillMaxWidth(),
                                scrollState = activeScrollState,
                                containerColor = containerColor,
                                contentColor = resolvedContentColor,
                                edgePadding = edgePadding,
                                divider = divider,
                                minTabWidth = minTabWidth,
                                indicator = {
                                    TabRowDefaults.PrimaryIndicator(
                                        modifier = Modifier.tabIndicatorOffset(
                                            selectedTabIndex,
                                            matchContentSize = true,
                                        ),
                                        width = Dp.Unspecified,
                                        color = indicatorColor,
                                    )
                                },
                                tabs = tabs,
                            )
                        }
                    } else {
                        PrimaryTabRow(
                            selectedTabIndex = selectedTabIndex,
                            modifier = modifier,
                            containerColor = containerColor,
                            contentColor = resolvedContentColor,
                            divider = divider,
                            indicator = {
                                TabRowDefaults.PrimaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(
                                        selectedTabIndex,
                                        matchContentSize = true,
                                    ),
                                    width = Dp.Unspecified,
                                    color = indicatorColor,
                                )
                            },
                            tabs = tabs,
                        )
                    }
                }

                StyledTabRow.Variant.Secondary -> {
                    if (scrollable) {
                        val activeScrollState = requireNotNull(scrollState) {
                            "Scrollable tab rows require a ScrollState"
                        }
                        ScrollableTabRowFrame(modifier, activeScrollState) {
                            SecondaryScrollableTabRow(
                                selectedTabIndex = selectedTabIndex,
                                modifier = Modifier.fillMaxWidth(),
                                scrollState = activeScrollState,
                                containerColor = containerColor,
                                contentColor = resolvedContentColor,
                                edgePadding = edgePadding,
                                divider = divider,
                                minTabWidth = minTabWidth,
                                indicator = {
                                    TabRowDefaults.SecondaryIndicator(
                                        modifier = Modifier.tabIndicatorOffset(
                                            selectedTabIndex,
                                            matchContentSize = false,
                                        ),
                                        color = indicatorColor,
                                    )
                                },
                                tabs = tabs,
                            )
                        }
                    } else {
                        SecondaryTabRow(
                            selectedTabIndex = selectedTabIndex,
                            modifier = modifier,
                            containerColor = containerColor,
                            contentColor = resolvedContentColor,
                            divider = divider,
                            indicator = {
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(
                                        selectedTabIndex,
                                        matchContentSize = false,
                                    ),
                                    color = indicatorColor,
                                )
                            },
                            tabs = tabs,
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(InternalComposeStyledApi::class)
private fun selectionColors(
    variant: StyledTabRow.Variant,
    containerColor: Color,
    contentColor: Color,
): StyledSelectionColors {
    val defaultContainerColor = when (variant) {
        StyledTabRow.Variant.Primary -> StyledTheme.colorScheme.primarySurface
        StyledTabRow.Variant.Secondary -> StyledTheme.colorScheme.secondarySurface
    }
    val defaultContentColor = when (variant) {
        StyledTabRow.Variant.Primary -> StyledTheme.colorScheme.onPrimarySurface
        StyledTabRow.Variant.Secondary -> StyledTheme.colorScheme.onSecondarySurface
    }
    val usesDefaultPalette = containerColor == defaultContainerColor &&
        contentColor == defaultContentColor
    val defaultSelectedContentColor = StyledTheme.colorScheme.primary
    return remember(
        variant,
        containerColor,
        contentColor,
        defaultSelectedContentColor,
    ) {
        StyledSelectionColors(
            selectedContentColor = if (usesDefaultPalette) {
                defaultSelectedContentColor
            } else {
                contentColor
            },
            unselectedContentColor = if (usesDefaultPalette) {
                contentColor
            } else {
                contentColor.copy(alpha = contentColor.alpha * 0.74f)
            },
            indicatorColor = if (usesDefaultPalette) {
                defaultSelectedContentColor
            } else {
                contentColor
            },
        )
    }
}

@OptIn(InternalComposeStyledApi::class)
@Composable
private fun ScrollableTabRowFrame(
    modifier: Modifier,
    scrollState: ScrollState,
    content: @Composable () -> Unit,
) {
    StyledHorizontalScrollbarLayout(
        modifier = modifier,
        state = scrollState,
        contentModifier = Modifier.fillMaxWidth(),
    ) { contentModifier ->
        Box(contentModifier) {
            content()
        }
    }
}
