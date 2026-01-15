package com.michaelflisar.composestyled.theme.cupertino.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledButtonVariantProvider
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.shared.UnstyledButtonTokenDefaultRenderer

private object StyledButtonVariantProviderImpl : StyledButtonVariantProvider {

    @Composable
    override fun resolveColors(
        variant: StyledButton.Variant,
        customization: StyledButton.Customization?,
    ): StatefulBaseColorDef {
        // NOTE: keep content behavior identical to the previous registerVariantStyles implementation
        val c = StyledTheme.colors

        fun pressedOverlay(onTopOf: Color, alpha: Float) =
            onTopOf.copy(alpha = alpha) // simple; can be replaced by proper blending later

        return remember(variant, customization, c) {
            when (variant) {
                // Primary = tinted text (Standard iOS)
                StyledButton.Variant.Primary -> {
                    StatefulBaseColorDef(
                        normal = BaseColorDef(
                            background = Color.Transparent,
                            foreground = c.primary, // iOS: systemBlue / tint
                            border = null
                        ),
                        hovered = BaseColorDef(
                            background = pressedOverlay(c.primary, 0.08f), // optional (Pointer)
                            foreground = c.primary,
                            border = null
                        )
                    )
                }

                // Secondary = weaker tinted/label
                StyledButton.Variant.Secondary -> {
                    StatefulBaseColorDef(
                        normal = BaseColorDef(
                            background = Color.Transparent,
                            foreground = c.secondary, // iOS: secondaryLabel
                            border = null
                        ),
                        hovered = BaseColorDef(
                            background = pressedOverlay(c.secondary, 0.08f),
                            foreground = c.secondary,
                            border = null
                        )
                    )
                }

                // Outlined = bordered (optional iOS 15+)
                StyledButton.Variant.Outlined -> {
                    StatefulBaseColorDef(
                        normal = BaseColorDef(
                            background = Color.Transparent,
                            foreground = c.onBackground,
                            border = c.outline // iOS: separator
                        ),
                        hovered = BaseColorDef(
                            background = pressedOverlay(c.onBackground, 0.06f),
                            foreground = c.onBackground,
                            border = c.outline
                        )
                    )
                }

                // Text = plain label (neutral)
                StyledButton.Variant.Text -> {
                    StatefulBaseColorDef(
                        normal = BaseColorDef(
                            background = Color.Transparent,
                            foreground = c.onBackground, // iOS: label
                            border = null
                        ),
                        hovered = BaseColorDef(
                            background = pressedOverlay(c.onBackground, 0.06f),
                            foreground = c.onBackground,
                            border = null
                        )
                    )
                }
            }
        }
    }
}

@OptIn(InternalComposeStyledApi::class)
internal object StyledButtonImpl : UnstyledButtonTokenDefaultRenderer() {

    override val variantProvider: StyledButtonVariantProvider = StyledButtonVariantProviderImpl

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun registerVariantStyles() {
        // --
    }
}
