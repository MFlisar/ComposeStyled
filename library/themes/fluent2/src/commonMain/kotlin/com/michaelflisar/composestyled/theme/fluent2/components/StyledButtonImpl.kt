package com.michaelflisar.composestyled.theme.fluent2.components

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
        val c = StyledTheme.colors
        return remember(variant, customization, c) {
            when (variant) {
                // Fluent2: Primary Button (filled)
                StyledButton.Variant.Primary -> {
                    StatefulBaseColorDef(
                        normal = BaseColorDef(
                            background = c.primary,
                            foreground = c.onPrimary,
                            border = null
                        ),
                        hovered = BaseColorDef(
                            background = c.primary.copy(alpha = 0.92f),
                            foreground = c.onPrimary,
                            border = null
                        ),
                        pressed = BaseColorDef(
                            background = c.primary.copy(alpha = 0.84f),
                            foreground = c.onPrimary,
                            border = null
                        )
                    )
                }

                // Fluent2: Secondary Button (subtle/neutral)
                StyledButton.Variant.Secondary -> {
                    StatefulBaseColorDef(
                        normal = BaseColorDef(
                            background = c.secondary,
                            foreground = c.onSecondary,
                            border = null
                        ),
                        hovered = BaseColorDef(
                            background = c.secondary.copy(alpha = 0.92f),
                            foreground = c.onSecondary,
                            border = null
                        ),
                        pressed = BaseColorDef(
                            background = c.secondary.copy(alpha = 0.84f),
                            foreground = c.onSecondary,
                            border = null
                        )
                    )
                }

                // Fluent2: Outlined Button
                StyledButton.Variant.Outlined -> {
                    StatefulBaseColorDef(
                        normal = BaseColorDef(
                            background = Color.Transparent,
                            foreground = c.primary,
                            border = c.outline
                        ),
                        hovered = BaseColorDef(
                            background = c.primary.copy(alpha = 0.08f),
                            foreground = c.primary,
                            border = c.outline
                        ),
                        pressed = BaseColorDef(
                            background = c.primary.copy(alpha = 0.16f),
                            foreground = c.primary,
                            border = c.outline
                        )
                    )
                }

                // Fluent2: Text Button (ghost)
                StyledButton.Variant.Text -> {
                    StatefulBaseColorDef(
                        normal = BaseColorDef(
                            background = Color.Transparent,
                            foreground = c.primary,
                            border = null
                        ),
                        hovered = BaseColorDef(
                            background = c.primary.copy(alpha = 0.08f),
                            foreground = c.primary,
                            border = null
                        ),
                        pressed = BaseColorDef(
                            background = c.primary.copy(alpha = 0.16f),
                            foreground = c.primary,
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
