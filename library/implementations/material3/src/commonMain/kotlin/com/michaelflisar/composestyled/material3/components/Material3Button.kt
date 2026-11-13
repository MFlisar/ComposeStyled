package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.material3.CopyStyledLocals
import com.michaelflisar.composestyled.material3.ProvideStyledLocals
import com.michaelflisar.composestyled.material3.components.Material3Button.Companion.DefaultVariantMapping
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledMenuBar
import com.michaelflisar.composestyled.ui.components.StyledTopBar
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.components.StyledSnackbar
import com.michaelflisar.composestyled.ui.specials.StyledGroupBox
import com.michaelflisar.composestyled.ui.specials.StyledSidePane
import com.michaelflisar.composestyled.ui.specials.StyledSwipeRevealActionBox

class Material3Button internal constructor(
    val variantMapping: VariantMapping,
) : StyledButton.Implementation {

    override val defaults: StyledButton.Defaults
        @Composable get() = StyledButton.Defaults(shape = RoundedCornerShape(percent = 50))

    companion object {

        // -------------
        // 1) create
        // -------------

        /**
         * Creates a Material3Button implementation for StyledButton with the given variant mapping.
         * @param variantMapping The mapping of StyledButton variants to MaterialButton variants. Defaults to [DefaultVariantMapping].
         * @return A Material3Button implementation for StyledButton.
         */
        @Composable
        fun create(
            variantMapping: VariantMapping = DefaultVariantMapping,
        ) = remember(variantMapping) {
            Material3Button(variantMapping = variantMapping)
        }

        // -------------
        // 2) mapping
        // -------------

        typealias VariantMapping = (StyledButton.Variant, StyledContext) -> Variant

        /**
         * Maps Auto to context-appropriate styles; explicit variants keep their
         * visual meaning in every context.
         */
        val DefaultVariantMapping: VariantMapping = { variant, context ->
            when {
                variant == StyledButton.Variant.Auto &&
                    context == StyledGroupBox.Context.Clickable -> Variant.Outlined
                variant == StyledButton.Variant.Auto &&
                    context == StyledSidePane.Context.IconSlot -> Variant.Text
                variant == StyledButton.Variant.Auto &&
                    context == StyledSwipeRevealActionBox.Context.RevealAction -> Variant.Text
                variant == StyledButton.Variant.Auto &&
                    (context is StyledDialog.ActionContext ||
                        context == StyledSnackbar.Context.SnackbarAction ||
                        context == StyledDialog.Context.DialogAction ||
                        context == StyledDialog.Context.FullscreenAction ||
                        context == StyledDialog.Context.BottomSheetAction ||
                        context is StyledTopBar.Context ||
                        context == StyledMenuBar.Context.MenuItem) -> Variant.Text
                else -> when (variant) {
                    StyledButton.Variant.Auto,
                    StyledButton.Variant.Filled -> Variant.Primary
                    StyledButton.Variant.Outlined -> Variant.Outlined
                    StyledButton.Variant.Plain -> Variant.Text
                }
            }
        }
    }

    enum class Variant {
        Primary,
        Secondary,
        FilledTonal,
        Elevated,
        Outlined,
        Text
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
        customization: StyledButton.Customization?,
        content: @Composable (RowScope.() -> Unit),
    ) {
        val context = LocalStyledContext.current
        val rendererVariant = variantMapping(variant, context)
        val isGroupBox = context == StyledGroupBox.Context.Clickable
        val buttonModifier = if (context == StyledMenuBar.Context.MenuItem) {
            modifier.width(IntrinsicSize.Min)
        } else {
            modifier
        }
        val inheritsStyledContentColor = variant == StyledButton.Variant.Plain ||
            (variant == StyledButton.Variant.Auto &&
                (context is StyledTopBar.Context ||
                    context == StyledMenuBar.Context.MenuItem))
        @Composable
        fun styledContentColor(color: Color) =
            if (enabled) color else color.copy(
                alpha = color.alpha * StyledTheme.colorScheme.disableFactors.content,
            )
        val textForeground = customization?.colors?.textForeground?.resolve()
            ?.takeUnless { it == Color.Unspecified }

        when (rendererVariant) {
            Variant.FilledTonal -> {
                FilledTonalButton(
                    onClick = onClick,
                    modifier = buttonModifier,
                    enabled = enabled,
                    shape = shape,
                    interactionSource = interactionSource,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = customization?.colors?.filledBackground?.resolve()
                            ?: StyledTheme.colorScheme.secondarySurface,
                        contentColor = customization?.colors?.filledForeground?.resolve()
                            ?: StyledTheme.colorScheme.onSecondarySurface,
                    ),
                    contentPadding = if (isGroupBox) {
                        PaddingValues(0.dp)
                    } else {
                        ButtonDefaults.ContentPadding
                    },
                ) {
                    ProvideStyledLocals(
                        contentColor = styledContentColor(
                            customization?.colors?.filledForeground?.resolve()
                                ?.takeUnless { it == Color.Unspecified }
                                ?: StyledTheme.colorScheme.onSecondarySurface
                        ),
                        textStyle = StyledTheme.typography.labelLarge,
                    ) {
                        content()
                    }
                }
            }

            Variant.Primary -> {
                Button(
                    onClick = onClick,
                    modifier = buttonModifier,
                    enabled = enabled,
                    shape = shape,
                    interactionSource = interactionSource,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = customization?.colors?.filledBackground?.resolve()
                            ?: StyledTheme.colorScheme.primary,
                        contentColor = customization?.colors?.filledForeground?.resolve()
                            ?: StyledTheme.colorScheme.onPrimary
                    ),
                    contentPadding = if (isGroupBox) {
                        PaddingValues(0.dp)
                    } else {
                        ButtonDefaults.ContentPadding
                    },
                ) {
                    ProvideStyledLocals(
                        contentColor = styledContentColor(
                            customization?.colors?.filledForeground?.resolve()
                                ?.takeUnless { it == Color.Unspecified }
                                ?: StyledTheme.colorScheme.onPrimary
                        ),
                        textStyle = StyledTheme.typography.labelLarge,
                    ) {
                        content()
                    }
                }
            }

            Variant.Secondary -> {
                Button(
                    onClick = onClick,
                    modifier = buttonModifier,
                    enabled = enabled,
                    shape = shape,
                    interactionSource = interactionSource,
                    // default: primary button

                    colors = ButtonDefaults.buttonColors(
                        containerColor = customization?.colors?.filledBackground?.resolve()
                            ?: StyledTheme.colorScheme.secondary,
                        contentColor = customization?.colors?.filledForeground?.resolve()
                            ?: StyledTheme.colorScheme.onSecondary
                    ),
                    contentPadding = if (isGroupBox) {
                        PaddingValues(0.dp)
                    } else {
                        ButtonDefaults.ContentPadding
                    },
                ) {
                    ProvideStyledLocals(
                        contentColor = styledContentColor(
                            customization?.colors?.filledForeground?.resolve()
                                ?.takeUnless { it == Color.Unspecified }
                                ?: StyledTheme.colorScheme.onSecondary
                        ),
                        textStyle = StyledTheme.typography.labelLarge,
                    ) {
                        content()
                    }
                }
            }

            Variant.Elevated -> {
                ElevatedButton(
                    onClick = onClick,
                    modifier = buttonModifier,
                    enabled = enabled,
                    shape = shape,
                    interactionSource = interactionSource,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = customization?.colors?.filledBackground?.resolve()
                            ?: Color.Unspecified,
                        contentColor = customization?.colors?.filledForeground?.resolve()
                            ?: StyledTheme.colorScheme.primary
                    ),
                    contentPadding = if (isGroupBox) {
                        PaddingValues(0.dp)
                    } else {
                        ButtonDefaults.ContentPadding
                    },
                ) {
                    ProvideStyledLocals(
                        contentColor = styledContentColor(
                            customization?.colors?.filledForeground?.resolve()
                                ?.takeUnless { it == Color.Unspecified }
                                ?: StyledTheme.colorScheme.primary
                        ),
                        textStyle = StyledTheme.typography.labelLarge,
                    ) {
                        content()
                    }
                }
            }

            Variant.Outlined -> {
                // button does not allow to change border color via parameter
                // so we change the base local color instead

                CopyStyledLocals(
                    outlineVariant = customization?.border?.resolve()
                ) {
                    OutlinedButton(
                        onClick = onClick,
                        modifier = buttonModifier,
                        enabled = enabled,
                        shape = shape,
                        interactionSource = interactionSource,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Unspecified,
                            contentColor = customization?.colors?.textForeground?.resolve()
                                ?: StyledTheme.colorScheme.onSurfaceVariant
                        ),
                        border = if (isGroupBox) {
                            null
                        } else {
                            ButtonDefaults.outlinedButtonBorder(enabled)
                        },
                        contentPadding = if (isGroupBox) {
                            PaddingValues(0.dp)
                        } else {
                            ButtonDefaults.ContentPadding
                        },
                    ) {
                        ProvideStyledLocals(
                            contentColor = styledContentColor(
                                customization?.colors?.textForeground?.resolve()
                                    ?.takeUnless { it == Color.Unspecified }
                                    ?: StyledTheme.colorScheme.onSurfaceVariant
                            ),
                            textStyle = StyledTheme.typography.labelLarge,
                        ) {
                            content()
                        }
                    }
                }
            }

            Variant.Text -> {
                TextButton(
                    onClick = onClick,
                    modifier = buttonModifier,
                    enabled = enabled,
                    shape = shape,
                    interactionSource = interactionSource,
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color.Unspecified,
                        contentColor = textForeground
                            ?: if (inheritsStyledContentColor) LocalStyledContentColor.current
                            else StyledTheme.colorScheme.primary
                    ),
                    contentPadding = if (isGroupBox) {
                        PaddingValues(0.dp)
                    } else {
                        ButtonDefaults.TextButtonContentPadding
                    },
                ) {
                    ProvideStyledLocals(
                        contentColor = styledContentColor(
                            textForeground
                                ?: if (inheritsStyledContentColor) LocalStyledContentColor.current
                                else StyledTheme.colorScheme.primary
                        ),
                        textStyle = StyledTheme.typography.labelLarge,
                    ) {
                        content()
                    }
                }

            }
        }
    }
}
