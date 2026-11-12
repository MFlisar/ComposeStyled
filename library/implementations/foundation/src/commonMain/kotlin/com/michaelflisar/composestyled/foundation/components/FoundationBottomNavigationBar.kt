package com.michaelflisar.composestyled.foundation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyled.ui.internal.LocalStyledBottomNavigationIndicatorTint
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.layout.StyledHorizontalScrollContainer

@InternalComposeStyledApi
class FoundationBottomNavigationBar(
    private val minimumHeight: Dp,
    private val indicatorStyle: IndicatorStyle = IndicatorStyle.Background,
    private val addVerticalContentPadding: Boolean = true,
) : StyledBottomNavigationBar.Implementation {

    @InternalComposeStyledApi
    enum class IndicatorStyle {
        Background,
        Bottom,
        None,
    }

    @Composable
    override fun Render(
        modifier: Modifier,
        scrollable: Boolean,
        scrollState: ScrollState,
        showScrollbar: Boolean,
        containerColor: Color,
        contentColor: Color,
        indicatorTint: Color,
        windowInsets: WindowInsets,
        content: @Composable RowScope.() -> Unit,
    ) {
        val resolvedContentColor = if (contentColor == Color.Unspecified) {
            StyledTheme.contentColorFor(containerColor)
        } else {
            contentColor
        }
        val resolvedIndicatorTint = indicatorTint.takeUnless { it == Color.Unspecified }
            ?: StyledTheme.colorScheme.primary
        UpdateStyledThemeLocals(
            contentColor = resolvedContentColor,
            textStyle = StyledTheme.typography.labelLarge,
        ) {
            CompositionLocalProvider(
                LocalStyledBottomNavigationIndicatorTint provides resolvedIndicatorTint,
            ) {
                val items: @Composable () -> Unit = {
                    val verticalContentPadding = if (addVerticalContentPadding) {
                        StyledTheme.padding.small
                    } else {
                        0.dp
                    }
                    Row(
                        modifier = (if (scrollable) Modifier else Modifier.fillMaxWidth())
                            .selectableGroup()
                            .heightIn(min = minimumHeight)
                            .padding(
                                start = StyledTheme.padding.content.calculateStartPadding(LocalLayoutDirection.current),
                                end = StyledTheme.padding.content.calculateEndPadding(LocalLayoutDirection.current),
                                top = verticalContentPadding,
                                bottom = verticalContentPadding,
                            ),
                        horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
                        verticalAlignment = Alignment.CenterVertically,
                        content = content,
                    )
                }
                Box(modifier.fillMaxWidth().background(containerColor).windowInsetsPadding(windowInsets)) {
                    if (scrollable) {
                        StyledHorizontalScrollContainer(
                            scrollState = scrollState,
                            showScrollbar = showScrollbar,
                        ) { items() }
                    } else items()
                }
            }
        }
    }

    @Composable
    override fun RenderItem(
        rowScope: RowScope,
        scrollable: Boolean,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        alwaysShowLabel: Boolean,
        interactionSource: MutableInteractionSource?,
        icon: @Composable () -> Unit,
        label: @Composable (() -> Unit)?,
    ) {
        if (scrollable) {
            Row(modifier.width(112.dp)) {
                RenderItem(
                    rowScope = this,
                    scrollable = false,
                    selected = selected,
                    onClick = onClick,
                    modifier = Modifier,
                    enabled = enabled,
                    alwaysShowLabel = alwaysShowLabel,
                    interactionSource = interactionSource,
                    icon = icon,
                    label = label,
                )
            }
            return
        }
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val focused by source.collectIsFocusedAsState()
        val pressed by source.collectIsPressedAsState()
        val keyboardFocus = enabled && focused &&
            LocalInputModeManager.current.inputMode == InputMode.Keyboard
        val barContentColor = LocalStyledContentColor.current
        val indicatorTint = LocalStyledBottomNavigationIndicatorTint.current
            .takeUnless { it == Color.Unspecified }
            ?: StyledTheme.colorScheme.primary
        val indicatorColor = if (enabled) indicatorTint else indicatorTint.copy(
            alpha = indicatorTint.alpha * StyledTheme.colorScheme.disableFactors.default,
        )
        val bottomIndicatorProgress = if (indicatorStyle == IndicatorStyle.Bottom) {
            animateFloatAsState(
                targetValue = if (selected) 1f else 0f,
                animationSpec = tween(160),
            ).value
        } else {
            0f
        }
        val baseIconColor = when {
            selected && indicatorStyle == IndicatorStyle.Background ->
                StyledTheme.contentColorFor(indicatorTint)
            selected && indicatorStyle == IndicatorStyle.None -> indicatorTint
            else -> barContentColor
        }
        val baseTextColor = if (selected && indicatorStyle == IndicatorStyle.None) {
            indicatorTint
        } else {
            barContentColor
        }
        val iconColor = if (enabled) baseIconColor else baseIconColor.copy(
            alpha = baseIconColor.alpha * StyledTheme.colorScheme.disableFactors.content
        )
        val textColor = if (enabled) baseTextColor else baseTextColor.copy(
            alpha = baseTextColor.alpha * StyledTheme.colorScheme.disableFactors.content
        )
        val shape = RoundedCornerShape(8.dp)
        UpdateStyledThemeLocals(
            contentColor = textColor,
            textStyle = StyledTheme.typography.labelMedium,
        ) {
            @Composable
            fun itemContent(modifier: Modifier) {
                Box(modifier, contentAlignment = Alignment.Center) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = if (indicatorStyle == IndicatorStyle.Bottom) {
                                    3.dp + StyledTheme.spacing.small
                                } else {
                                    0.dp
                                },
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            StyledTheme.spacing.small, Alignment.CenterVertically,
                        ),
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    if (selected && indicatorStyle == IndicatorStyle.Background) {
                                        indicatorColor
                                    } else {
                                        Color.Transparent
                                    },
                                    shape,
                                )
                                .padding(
                                    horizontal = StyledTheme.padding.medium,
                                    vertical = StyledTheme.padding.small,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            UpdateStyledThemeLocals(contentColor = iconColor, content = icon)
                        }
                        if (label != null && (alwaysShowLabel || selected)) {
                            UpdateStyledThemeLocals(
                                contentColor = textColor,
                                textStyle = StyledTheme.typography.labelMedium.copy(textAlign = TextAlign.Center),
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    label()
                                }
                            }
                        }
                    }
                    if (indicatorStyle == IndicatorStyle.Bottom) {
                        Box(
                            Modifier
                                .align(Alignment.BottomCenter)
                                .width(24.dp * bottomIndicatorProgress)
                                .height(3.dp)
                                .clip(RoundedCornerShape(percent = 50))
                                .background(
                                    indicatorColor.copy(alpha = indicatorColor.alpha * bottomIndicatorProgress),
                                ),
                        )
                    }
                }
            }
            with(rowScope) {
                val itemModifier = modifier
                    .weight(1f)
                    .sizeIn(
                        minWidth = StyledTheme.sizes.minimumInteractiveSize,
                        minHeight = maxOf(minimumHeight, StyledTheme.sizes.minimumInteractiveSize),
                    )
                    .clip(shape)
                    .selectable(
                        selected = selected,
                        enabled = enabled,
                        role = Role.Tab,
                        interactionSource = source,
                        indication = null,
                        onClick = onClick,
                    )
                    .then(
                        if (keyboardFocus) Modifier.border(2.dp, textColor, shape) else Modifier
                    )
                    .background(
                        if (enabled && pressed) baseTextColor.copy(alpha = 0.12f) else Color.Transparent
                    )
                    .padding(StyledTheme.padding.small)
                itemContent(itemModifier)
            }
        }
    }
}
