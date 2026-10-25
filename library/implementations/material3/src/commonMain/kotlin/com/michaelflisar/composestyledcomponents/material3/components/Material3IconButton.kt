package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ui.components.StyledIconButton
import com.michaelflisar.composestyledcomponents.material3.CopyMaterialThemeLocals

class Material3IconButton internal constructor(
    val roleMapping: RoleMapping,
    override val defaults: StyledIconButton.Defaults,
) : StyledIconButton.Implementation {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            roleMapping: RoleMapping = DefaultRoleMapping,
            defaults: StyledIconButton.Defaults = defaults(),
        ) = remember(roleMapping, defaults) { Material3IconButton(roleMapping, defaults) }

        // -------------
        // 2) mapping
        // -------------

        typealias RoleMapping = (StyledIconButton.Role) -> Variant

        val DefaultRoleMapping: RoleMapping = { role ->
            when (role) {
                StyledIconButton.Role.Primary -> Variant.Filled
                StyledIconButton.Role.Secondary -> Variant.Outlined
                StyledIconButton.Role.Tertiary -> Variant.Standard
            }
        }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(shape: Shape = CircleShape) =
            StyledIconButton.Defaults(shape = shape)
    }

    enum class Variant {
        Standard,
        Outlined,
        Filled,
        FilledTonal,
    }

    @Composable
    override fun Render(
        role: StyledIconButton.Role,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        interactionSource: MutableInteractionSource?,
        customization: StyledIconButton.Customization?,
        content: @Composable () -> Unit,
    ) {
        val filledBackground = customization?.colors?.filledBackground?.resolve()
            ?.takeIf { it != Color.Unspecified }
        val filledForeground = customization?.colors?.filledForeground?.resolve()
            ?.takeIf { it != Color.Unspecified }
        val textForeground = customization?.colors?.textForeground?.resolve()
            ?.takeIf { it != Color.Unspecified }
        val borderTint = if (enabled) {
            customization?.border?.resolve()?.takeIf { it != Color.Unspecified }
        } else {
            null
        }
        when (roleMapping(role)) {
            Variant.Standard -> IconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                interactionSource = interactionSource,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = textForeground ?: Color.Unspecified
                ),
            ) {
                CopyMaterialThemeLocals(contentColor = LocalContentColor.current, content = content)
            }

            Variant.Outlined -> OutlinedIconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                interactionSource = interactionSource,
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    contentColor = textForeground ?: Color.Unspecified
                ),
                border = borderTint?.let { BorderStroke(1.dp, it) }
                    ?: IconButtonDefaults.outlinedIconButtonBorder(enabled),
            ) {
                CopyMaterialThemeLocals(contentColor = LocalContentColor.current, content = content)
            }

            Variant.Filled -> FilledIconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                interactionSource = interactionSource,
                colors = IconButtonDefaults.filledIconButtonColors(
                    contentColor = filledForeground ?: Color.Unspecified,
                    containerColor = filledBackground ?: Color.Unspecified,
                ),
            ) {
                CopyMaterialThemeLocals(contentColor = LocalContentColor.current, content = content)
            }

            Variant.FilledTonal -> FilledTonalIconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                interactionSource = interactionSource,
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    contentColor = filledForeground ?: Color.Unspecified,
                    containerColor = filledBackground ?: Color.Unspecified,
                ),
            ) {
                CopyMaterialThemeLocals(contentColor = LocalContentColor.current, content = content)
            }
        }
    }
}
