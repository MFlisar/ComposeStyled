package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledTab

@InternalComposeStyledApi
class FoundationTab : StyledTab.Implementation {

    @Composable
    override fun Render(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        selectedContentColor: Color,
        unselectedContentColor: Color,
        interactionSource: MutableInteractionSource?,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val focused by source.collectIsFocusedAsState()
        val keyboardFocus = enabled && focused &&
            LocalInputModeManager.current.inputMode == InputMode.Keyboard
        val drawIndicator = selected && !LocalFoundationTabRowHasIndicator.current
        val baseColor = if (selected) selectedContentColor else unselectedContentColor
        val contentColor = if (enabled) baseColor else baseColor.copy(
            alpha = baseColor.alpha * StyledTheme.colorScheme.disableFactors.content
        )

        ProvideCustomStyledContentLocals(
            contentColor = contentColor,
            textStyle = StyledTheme.typography.labelLarge,
        ) {
            Column(
                modifier = modifier
                    .defaultMinSize(minHeight = StyledTheme.sizes.minimumInteractiveSize)
                    .selectable(
                        selected = selected,
                        enabled = enabled,
                        role = Role.Tab,
                        interactionSource = source,
                        indication = null,
                        onClick = onClick,
                    )
                    .drawBehind {
                        if (drawIndicator) {
                            val strokeWidth = 2.dp.toPx()
                            drawLine(
                                color = contentColor,
                                start = Offset(0f, size.height - strokeWidth / 2),
                                end = Offset(size.width, size.height - strokeWidth / 2),
                                strokeWidth = strokeWidth,
                            )
                        }
                        if (keyboardFocus) {
                            val strokeWidth = 2.dp.toPx()
                            drawRect(
                                color = contentColor,
                                topLeft = Offset.Zero,
                                size = size,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth),
                            )
                        }
                    }
                    .padding(
                        horizontal = StyledTheme.padding.medium,
                        vertical = StyledTheme.padding.small,
                    ),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                content = content,
            )
        }
    }
}
