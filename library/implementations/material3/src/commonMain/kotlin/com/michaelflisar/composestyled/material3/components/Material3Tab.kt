package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.material3.ProvideStyledLocals
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.internal.LocalStyledSelectionColors
import com.michaelflisar.composestyled.ui.components.StyledTab

class Material3Tab internal constructor() : StyledTab.Implementation {

    companion object {
        @Composable
        fun create() = remember { Material3Tab() }
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        selectedContentColor: Color,
        unselectedContentColor: Color,
        interactionSource: MutableInteractionSource?,
        text: @Composable ColumnScope.() -> Unit,
    ) {
        val selectionColors = LocalStyledSelectionColors.current
        val resolvedSelectedContentColor = selectedContentColor
            .takeUnless { it == Color.Unspecified }
            ?: selectionColors?.selectedContentColor?.takeUnless { it == Color.Unspecified }
            ?: StyledTheme.colorScheme.primary
        val resolvedUnselectedContentColor = unselectedContentColor
            .takeUnless { it == Color.Unspecified }
            ?: selectionColors?.unselectedContentColor?.takeUnless { it == Color.Unspecified }
            ?: LocalStyledContentColor.current
        val styledContentColor = (if (selected) {
            resolvedSelectedContentColor
        } else {
            resolvedUnselectedContentColor
        }).let { color ->
            if (enabled) color else color.copy(
                alpha = color.alpha * StyledTheme.colorScheme.disableFactors.content,
            )
        }
        Tab(
            selected = selected,
            onClick = onClick,
            modifier = modifier
                .defaultMinSize(minHeight = StyledTheme.sizes.minimumInteractiveSize),
            enabled = enabled,
            selectedContentColor = resolvedSelectedContentColor,
            unselectedContentColor = resolvedUnselectedContentColor,
            interactionSource = interactionSource,
            content = {
                ProvideStyledLocals(
                    contentColor = styledContentColor,
                    textStyle = StyledTheme.typography.labelLarge,
                    content = { text() },
                )
            },
        )
    }
}
