package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.foundation.components.FoundationListItem
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledListItem

class CupertinoFlatListItem internal constructor() : StyledListItem.Implementation {

    override val defaults: StyledListItem.Defaults
        @Composable get() = StyledListItem.Defaults(
            shape = RectangleShape,
            minHeight = 44.dp,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            spacing = 12.dp,
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoFlatListItem() }
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        onClick: (() -> Unit)?,
        modifier: Modifier,
        enabled: Boolean,
        interactionSource: MutableInteractionSource?,
        shape: Shape,
        minHeight: Dp,
        contentPadding: PaddingValues,
        spacing: Dp,
        containerColor: Color,
        contentColor: Color,
        supportingColor: Color,
        borderColor: Color,
        leading: (@Composable () -> Unit)?,
        actions: (@Composable RowScope.() -> Unit)?,
        content: StyledListItem.Content,
    ) {
        FoundationListItem(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            shape = shape,
            minHeight = minHeight,
            twoLineMinHeight = 60.dp,
            threeLineMinHeight = 76.dp,
            contentPadding = contentPadding,
            spacing = spacing,
            containerColor = containerColor,
            contentColor = contentColor,
            supportingColor = supportingColor,
            borderColor = borderColor,
            leading = leading,
            actions = actions,
            content = content,
            defaultContainerColor = StyledTheme.colorScheme.surface,
            defaultContentColor = StyledTheme.colorScheme.onSurface,
            defaultSupportingColor = StyledTheme.supportingOnColor(StyledTheme.colorScheme.surface),
            headlineStyle = StyledTheme.typography.bodyLarge,
            supportingStyle = StyledTheme.typography.bodySmall,
            overlineStyle = StyledTheme.typography.labelSmall,
        )
    }
}
