package com.michaelflisar.composestyled.material3.components

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
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.ui.specials.StyledGroupBox
import com.michaelflisar.composestyled.ui.specials.StyledSidePane
import com.michaelflisar.composestyled.material3.CopyMaterialThemeLocals
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.components.StyledSnackbar
import com.michaelflisar.composestyled.ui.components.StyledTopBar

class Material3IconButton internal constructor(
    val roleMapping: RoleMapping,
) : StyledIconButton.Implementation {

    override val defaults: StyledIconButton.Defaults
        @Composable get() = StyledIconButton.Defaults(shape = CircleShape)

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            roleMapping: RoleMapping = DefaultRoleMapping,
        ) = remember(roleMapping) { Material3IconButton(roleMapping) }

        // -------------
        // 2) mapping
        // -------------

        typealias RoleMapping = (StyledIconButton.Role, StyledContext) -> Variant

        val DefaultRoleMapping: RoleMapping = { role, context ->
            when {
                role == StyledIconButton.Role.Default &&
                    (context is StyledDialog.ActionContext ||
                        context == StyledDialog.Context.DialogAction ||
                        context == StyledDialog.Context.FullscreenAction ||
                        context == StyledDialog.Context.BottomSheetAction ||
                        context == StyledSnackbar.Context.SnackbarAction ||
                        context is StyledTopBar.Context ||
                        context is StyledInput.Context ||
                        context == StyledGroupBox.Context.Clickable ||
                        context == StyledSidePane.Context.IconSlot) -> Variant.Standard
                else -> when (role) {
                    StyledIconButton.Role.Default,
                    StyledIconButton.Role.Primary -> Variant.Filled
                    StyledIconButton.Role.Secondary -> Variant.Outlined
                    StyledIconButton.Role.Tertiary -> Variant.Standard
                }
            }
        }
    }

    enum class Variant {
        Standard,
        Outlined,
        Filled,
        FilledTonal,
    }

    @OptIn(InternalComposeStyledApi::class)
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
        val isTopBarDefault = role == StyledIconButton.Role.Default &&
            LocalStyledContext.current is StyledTopBar.Context
        val borderTint = if (enabled) {
            customization?.border?.resolve()?.takeIf { it != Color.Unspecified }
        } else {
            null
        }
        when (roleMapping(role, LocalStyledContext.current)) {
            Variant.Standard -> IconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                interactionSource = interactionSource,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = textForeground ?: if (isTopBarDefault) {
                        StyledTheme.currentContentColor
                    } else Color.Unspecified
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
