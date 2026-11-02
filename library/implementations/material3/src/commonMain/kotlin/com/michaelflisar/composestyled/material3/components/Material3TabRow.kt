package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
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
        containerColor: Color,
        contentColor: Color,
        content: @Composable () -> Unit,
    ) {
        val scrollState = rememberScrollState()
        val resolvedContentColor = if (contentColor == Color.Unspecified) {
            StyledTheme.contentColorFor(containerColor)
        } else {
            contentColor
        }
        val usesDefaultPalette = containerColor == StyledTheme.colorScheme.surface &&
            resolvedContentColor == StyledTheme.colorScheme.onSurface
        val defaultSelectedContentColor = StyledTheme.colorScheme.primary
        val selectionColors = remember(
            containerColor,
            resolvedContentColor,
            defaultSelectedContentColor,
        ) {
            StyledSelectionColors(
                selectedContentColor = if (usesDefaultPalette) {
                    defaultSelectedContentColor
                } else {
                    resolvedContentColor
                },
                unselectedContentColor = if (usesDefaultPalette) {
                    resolvedContentColor
                } else {
                    resolvedContentColor.copy(alpha = resolvedContentColor.alpha * 0.74f)
                },
                indicatorColor = if (usesDefaultPalette) {
                    defaultSelectedContentColor
                } else {
                    resolvedContentColor
                },
            )
        }
        CompositionLocalProvider(LocalStyledSelectionColors provides selectionColors) {
            when (variant) {
                StyledTabRow.Variant.Primary -> {
                    if (scrollable) {
                        ScrollableTabRowFrame(modifier, scrollState) {
                            PrimaryScrollableTabRow(
                                selectedTabIndex = selectedTabIndex,
                                modifier = Modifier.fillMaxWidth(),
                                scrollState = scrollState,
                                containerColor = containerColor,
                                contentColor = resolvedContentColor,
                                indicator = {
                                    TabRowDefaults.PrimaryIndicator(
                                        modifier = Modifier.tabIndicatorOffset(
                                            selectedTabIndex,
                                            matchContentSize = true,
                                        ),
                                        width = Dp.Unspecified,
                                        color = resolvedContentColor,
                                    )
                                },
                                tabs = content,
                            )
                        }
                    } else {
                        PrimaryTabRow(
                            selectedTabIndex = selectedTabIndex,
                            modifier = modifier,
                            containerColor = containerColor,
                            contentColor = resolvedContentColor,
                            indicator = {
                                TabRowDefaults.PrimaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(
                                        selectedTabIndex,
                                        matchContentSize = true,
                                    ),
                                    width = Dp.Unspecified,
                                    color = resolvedContentColor,
                                )
                            },
                            tabs = content,
                        )
                    }
                }

                StyledTabRow.Variant.Secondary -> {
                    if (scrollable) {
                        ScrollableTabRowFrame(modifier, scrollState) {
                            SecondaryScrollableTabRow(
                                selectedTabIndex = selectedTabIndex,
                                modifier = Modifier.fillMaxWidth(),
                                scrollState = scrollState,
                                containerColor = containerColor,
                                contentColor = resolvedContentColor,
                                indicator = {
                                    TabRowDefaults.SecondaryIndicator(
                                        modifier = Modifier.tabIndicatorOffset(
                                            selectedTabIndex,
                                            matchContentSize = false,
                                        ),
                                        color = resolvedContentColor,
                                    )
                                },
                                tabs = content,
                            )
                        }
                    } else {
                        SecondaryTabRow(
                            selectedTabIndex = selectedTabIndex,
                            modifier = modifier,
                            containerColor = containerColor,
                            contentColor = resolvedContentColor,
                            indicator = {
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(
                                        selectedTabIndex,
                                        matchContentSize = false,
                                    ),
                                    color = resolvedContentColor,
                                )
                            },
                            tabs = content,
                        )
                    }
                }
            }
        }
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
