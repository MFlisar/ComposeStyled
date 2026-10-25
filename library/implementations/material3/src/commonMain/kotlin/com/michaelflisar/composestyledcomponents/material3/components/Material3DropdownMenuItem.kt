package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledDropdownMenuItem
import com.michaelflisar.composestyledcomponents.ui.components.StyledMenuItemColors
import com.michaelflisar.composestyledcomponents.material3.CopyMaterialThemeLocals
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

class Material3DropdownMenuItem internal constructor(
    override val defaults: StyledDropdownMenuItem.Defaults,
) : StyledDropdownMenuItem.Implementation {
    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            defaults: StyledDropdownMenuItem.Defaults = defaults(),
        ) = remember(defaults) { Material3DropdownMenuItem(defaults) }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(
            minHeight: Dp = 48.dp,
            itemPadding: PaddingValues = PaddingValues(horizontal = StyledTheme.padding.small),
            itemShape: Shape = StyledTheme.shapes.small,
        ) = StyledDropdownMenuItem.Defaults(minHeight, itemPadding, itemShape)
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        text: @Composable () -> Unit,
        onClick: () -> Unit,
        modifier: Modifier,
        leadingIcon: (@Composable () -> Unit)?,
        trailingIcon: (@Composable () -> Unit)?,
        enabled: Boolean,
        colors: StyledMenuItemColors,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource?,
        shape: Shape,
        itemPadding: PaddingValues,
        minHeight: Dp,
    ) {
        DropdownMenuItem(
            text = { CopyMaterialThemeLocals(contentColor = LocalContentColor.current, content = text) },
            onClick = onClick,
            modifier = modifier
                .heightIn(min = minHeight)
                .padding(itemPadding)
                .clip(shape),
            leadingIcon = leadingIcon?.let { slot ->
                { CopyMaterialThemeLocals(contentColor = LocalContentColor.current, content = slot) }
            },
            trailingIcon = trailingIcon?.let { slot ->
                { CopyMaterialThemeLocals(contentColor = LocalContentColor.current, content = slot) }
            },
            enabled = enabled,
            colors = MenuDefaults.itemColors(
                textColor = colors.textColor,
                leadingIconColor = colors.leadingIconColor,
                trailingIconColor = colors.trailingIconColor,
                disabledTextColor = colors.disabledTextColor,
                disabledLeadingIconColor = colors.disabledLeadingIconColor,
                disabledTrailingIconColor = colors.disabledTrailingIconColor,
            ),
            contentPadding = contentPadding,
            interactionSource = interactionSource,
        )
    }
}
