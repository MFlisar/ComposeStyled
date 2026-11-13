package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.ui.specials.StyledGroupBox
import com.michaelflisar.composestyled.ui.specials.StyledSidePane
import com.michaelflisar.composestyled.material3.ProvideStyledLocals
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.components.StyledSnackbar
import com.michaelflisar.composestyled.ui.components.StyledTopBar

class Material3IconButton internal constructor(
    val variantMapping: VariantMapping,
) : StyledIconButton.Implementation {

    override val defaults: StyledIconButton.Defaults
        @Composable get() = StyledIconButton.Defaults(shape = CircleShape)

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            variantMapping: VariantMapping = DefaultVariantMapping,
        ) = remember(variantMapping) { Material3IconButton(variantMapping) }

        // -------------
        // 2) mapping
        // -------------

        typealias VariantMapping = (StyledButton.Variant, StyledContext) -> Variant

        val DefaultVariantMapping: VariantMapping = { variant, context ->
            when {
                variant == StyledButton.Variant.Auto &&
                    (context is StyledDialog.ActionContext ||
                        context == StyledDialog.Context.DialogAction ||
                        context == StyledDialog.Context.FullscreenAction ||
                        context == StyledDialog.Context.BottomSheetAction ||
                        context == StyledSnackbar.Context.SnackbarAction ||
                        context is StyledTopBar.Context ||
                        context is StyledInput.Context ||
                        context == StyledGroupBox.Context.Clickable ||
                        context == StyledSidePane.Context.IconSlot) -> Variant.Standard
                else -> when (variant) {
                    StyledButton.Variant.Auto,
                    StyledButton.Variant.Plain -> Variant.Standard
                    StyledButton.Variant.Filled -> Variant.Filled
                    StyledButton.Variant.Outlined -> Variant.Outlined
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
        variant: StyledButton.Variant,
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
        val context = LocalStyledContext.current
        val inheritsStyledContentColor = variant == StyledButton.Variant.Plain ||
            (variant == StyledButton.Variant.Auto &&
                (context is StyledTopBar.Context ||
                    context is StyledInput.Context ||
                    context is StyledDialog.ActionContext &&
                    context.context == StyledDialog.Context.FullscreenAction))
        val borderTint = if (enabled) {
            customization?.border?.resolve()?.takeIf { it != Color.Unspecified }
        } else {
            null
        }
        @Composable
        fun styledSlotColor(color: Color) =
            if (enabled) color else color.copy(
                alpha = color.alpha * StyledTheme.colorScheme.disableFactors.content,
            )
        when (variantMapping(variant, LocalStyledContext.current)) {
            Variant.Standard -> IconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                interactionSource = interactionSource,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = textForeground ?: if (inheritsStyledContentColor) {
                        LocalStyledContentColor.current
                    } else StyledTheme.colorScheme.onSurfaceVariant
                ),
            ) {
                ProvideStyledLocals(
                    contentColor = styledSlotColor(
                        textForeground ?: if (inheritsStyledContentColor) {
                            LocalStyledContentColor.current
                        } else StyledTheme.colorScheme.onSurfaceVariant
                    ),
                    content = content,
                )
            }

            Variant.Outlined -> OutlinedIconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                interactionSource = interactionSource,
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    contentColor = textForeground ?: StyledTheme.colorScheme.onSurfaceVariant
                ),
                border = borderTint?.let { BorderStroke(1.dp, it) }
                    ?: IconButtonDefaults.outlinedIconButtonBorder(enabled),
            ) {
                ProvideStyledLocals(
                    contentColor = styledSlotColor(
                        textForeground ?: StyledTheme.colorScheme.onSurfaceVariant
                    ),
                    content = content,
                )
            }

            Variant.Filled -> FilledIconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                interactionSource = interactionSource,
                colors = IconButtonDefaults.filledIconButtonColors(
                    contentColor = filledForeground ?: StyledTheme.colorScheme.onPrimary,
                    containerColor = filledBackground ?: Color.Unspecified,
                ),
            ) {
                ProvideStyledLocals(
                    contentColor = styledSlotColor(
                        filledForeground ?: StyledTheme.colorScheme.onPrimary
                    ),
                    content = content,
                )
            }

            Variant.FilledTonal -> FilledTonalIconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                interactionSource = interactionSource,
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    contentColor = filledForeground ?: StyledTheme.colorScheme.onSecondarySurface,
                    containerColor = filledBackground ?: Color.Unspecified,
                ),
            ) {
                ProvideStyledLocals(
                    contentColor = styledSlotColor(
                        filledForeground ?: StyledTheme.colorScheme.onSecondarySurface
                    ),
                    content = content,
                )
            }
        }
    }
}
