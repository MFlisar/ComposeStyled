package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.foundation.components.FoundationListItem
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.material3.ProvideStyledLocals
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledListItem

class Material3ListItem internal constructor() : StyledListItem.Implementation {

    override val defaults: StyledListItem.Defaults
        @Composable get() = StyledListItem.Defaults(
            shape = ListItemDefaults.shape,
            minHeight = 56.dp,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            spacing = 16.dp,
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3ListItem() }
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
        val semantic = content as? StyledListItem.Content.Semantic
        // The native API has fixed padding and slot spacing; custom layout values use the shared layout.
        val nativeContent: (@Composable (Modifier, Color, Color) -> Unit)? =
            if (semantic != null &&
                contentPadding == PaddingValues(horizontal = 16.dp, vertical = 8.dp) &&
                spacing == 16.dp
            ) {
                { bodyModifier, foreground, secondary ->
                    ListItem(
                        modifier = bodyModifier.heightIn(min = minHeight),
                        headlineContent = {
                            ProvideStyledLocals(
                                contentColor = foreground,
                                textStyle = StyledTheme.typography.bodyLarge,
                                content = semantic.headline,
                            )
                        },
                        supportingContent = semantic.supporting?.let { slot ->
                            {
                                ProvideStyledLocals(
                                    contentColor = secondary,
                                    textStyle = StyledTheme.typography.bodyMedium,
                                    content = slot,
                                )
                            }
                        },
                        overlineContent = semantic.overline?.let { slot ->
                            {
                                ProvideStyledLocals(
                                    contentColor = secondary,
                                    textStyle = StyledTheme.typography.labelSmall,
                                    content = slot,
                                )
                            }
                        },
                        leadingContent = leading?.let { slot ->
                            {
                                ProvideStyledLocals(
                                    contentColor = foreground,
                                    content = slot,
                                )
                            }
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent,
                            headlineColor = foreground,
                            leadingIconColor = foreground,
                            overlineColor = secondary,
                            supportingColor = secondary,
                        ),
                    )
                }
            } else null

        FoundationListItem(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            shape = shape,
            minHeight = minHeight,
            twoLineMinHeight = 72.dp,
            threeLineMinHeight = 88.dp,
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
            defaultSupportingColor = StyledTheme.colorScheme.onSurfaceVariant,
            headlineStyle = StyledTheme.typography.bodyLarge,
            supportingStyle = StyledTheme.typography.bodyMedium,
            overlineStyle = StyledTheme.typography.labelSmall,
            nativeContent = nativeContent,
        )
    }
}
