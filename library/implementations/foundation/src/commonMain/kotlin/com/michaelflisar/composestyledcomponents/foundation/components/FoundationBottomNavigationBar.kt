package com.michaelflisar.composestyledcomponents.foundation.components

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
class FoundationBottomNavigationBar(
    private val minimumHeight: Dp,
    private val showIndicator: Boolean = true,
) : StyledBottomNavigationBar.Implementation {

    @Composable
    override fun Render(
        modifier: Modifier,
        containerColor: Color,
        contentColor: Color,
        windowInsets: WindowInsets,
        content: @Composable RowScope.() -> Unit,
    ) {
        ProvideCustomStyledContentLocals(
            contentColor = contentColor,
            textStyle = StyledTheme.typography.labelLarge,
        ) {
            Row(
                modifier = modifier.fillMaxWidth()
                    .background(containerColor)
                    .windowInsetsPadding(windowInsets)
                    .selectableGroup()
                    .heightIn(min = minimumHeight)
                    .padding(
                        start = StyledTheme.padding.content.calculateStartPadding(LocalLayoutDirection.current),
                        end = StyledTheme.padding.content.calculateEndPadding(LocalLayoutDirection.current),
                        top = StyledTheme.padding.small,
                        bottom = StyledTheme.padding.small,
                    ),
                horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
                verticalAlignment = Alignment.CenterVertically,
                content = content,
            )
        }
    }

    @Composable
    override fun RenderItem(
        rowScope: RowScope,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        alwaysShowLabel: Boolean,
        selectedContentColor: Color,
        unselectedContentColor: Color,
        indicatorColor: Color,
        interactionSource: MutableInteractionSource?,
        icon: @Composable () -> Unit,
        label: @Composable (() -> Unit)?,
    ) {
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val focused by source.collectIsFocusedAsState()
        val pressed by source.collectIsPressedAsState()
        val keyboardFocus = enabled && focused &&
            LocalInputModeManager.current.inputMode == InputMode.Keyboard
        val baseColor = if (selected) selectedContentColor else unselectedContentColor
        val contentColor = if (enabled) baseColor else baseColor.copy(
            alpha = baseColor.alpha * StyledTheme.colorScheme.disableFactors.content
        )
        val shape = RoundedCornerShape(8.dp)
        ProvideCustomStyledContentLocals(
            contentColor = contentColor,
            textStyle = StyledTheme.typography.labelMedium,
        ) {
            with(rowScope) {
                Column(
                    modifier = modifier
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
                            if (keyboardFocus) Modifier.border(2.dp, contentColor, shape) else Modifier
                        )
                        .background(
                            if (enabled && pressed) baseColor.copy(alpha = 0.12f) else Color.Transparent
                        )
                        .padding(StyledTheme.padding.small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        StyledTheme.spacing.small, Alignment.CenterVertically,
                    ),
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                if (selected && showIndicator) {
                                    if (enabled) indicatorColor else indicatorColor.copy(
                                        alpha = indicatorColor.alpha * StyledTheme.colorScheme.disableFactors.default
                                    )
                                } else Color.Transparent,
                                shape,
                            )
                            .padding(
                                horizontal = StyledTheme.padding.medium,
                                vertical = StyledTheme.padding.small,
                            ),
                        contentAlignment = Alignment.Center,
                    ) { icon() }
                    if (label != null && (alwaysShowLabel || selected)) label()
                }
            }
        }
    }
}
