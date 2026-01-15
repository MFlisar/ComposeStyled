package com.michaelflisar.composestyled.theme.material3.components

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
import com.michaelflisar.composestyled.theme.material3.Material3Util

private object StyledButtonVariantProviderImpl : StyledButtonVariantProvider {

    @Composable
    override fun resolveColors(
        variant: StyledButton.Variant,
        customization: StyledButton.Customization?,
    ): StatefulBaseColorDef {
        val background = customization?.background?.resolve()
        val foreground = customization?.content?.resolve()
        val border = customization?.border?.resolve()
        val colors = StyledTheme.colors
        return remember(variant, customization, background, foreground, border, colors) {
            when (variant) {
                StyledButton.Variant.Primary -> {
                    StatefulBaseColorDef(
                        normal = BaseColorDef(
                            background = background ?: colors.primary,
                            foreground = foreground ?: colors.onPrimary,
                            border = null
                        ),
                        hovered = BaseColorDef(
                            background = Material3Util.hoverFromBackground(background ?: colors.primary),
                            foreground = foreground ?: colors.onPrimary,
                            border = null
                        )
                    )
                }
                StyledButton.Variant.Secondary -> {
                    StatefulBaseColorDef(
                        normal = BaseColorDef(
                            background = background ?: colors.secondary,
                            foreground = foreground ?: colors.onSecondary,
                            border = null
                        ),
                        hovered = BaseColorDef(
                            background = Material3Util.hoverFromBackground(background ?: colors.secondary),
                            foreground = foreground ?: colors.onSecondary,
                            border = null
                        )
                    )
                }
                StyledButton.Variant.Outlined -> {
                    StatefulBaseColorDef(
                        normal = BaseColorDef(
                            background = background ?: Color.Transparent,
                            foreground = foreground ?: colors.onBackground,
                            border = border ?: colors.outlineVariant
                        ),
                        hovered = BaseColorDef(
                            background = Material3Util.hoverFromForeground(foreground ?: colors.onBackground),
                            foreground = foreground ?: colors.onBackground,
                            border = border ?: colors.outlineVariant
                        ),
                    )
                }
                StyledButton.Variant.Text -> {
                    StatefulBaseColorDef(
                        normal = BaseColorDef(
                            background = background ?: Color.Transparent,
                            foreground = foreground ?: colors.onBackground,
                            border = null
                        ),
                        hovered = BaseColorDef(
                            background = Material3Util.hoverFromForeground(foreground ?: colors.onBackground),
                            foreground = foreground ?: colors.onBackground,
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