package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.composeunstyled.theme.Theme
import com.composeunstyled.theme.ThemeProperty
import com.composeunstyled.theme.ThemeToken
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalThemeBuilder
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

object StyledButton : BaseStyledComponent {

    internal val Property = ThemeProperty<StatefulBaseColorDef>("button")

    private val TokenFilledPrimary = ThemeToken<StatefulBaseColorDef>("button.filled.primary")
    private val TokenOutlined = ThemeToken<StatefulBaseColorDef>("button.outlined.default")
    private val TokenText = ThemeToken<StatefulBaseColorDef>("button.text.default")

    sealed class Variant {

        companion object {

            val FilledPrimary: Variant = Token(TokenFilledPrimary)
            val Outlined: Variant = Token(TokenOutlined)
            val Text: Variant = Token(TokenText)

            fun custom(colorDef: StatefulBaseColorDef): Variant = Custom(colorDef)
        }

        internal data class Token(
            val token: ThemeToken<StatefulBaseColorDef>,
        ) : Variant()

        internal data class Custom(val colorDef: StatefulBaseColorDef) : Variant()
    }

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        primary: StatefulBaseColorDef,
        outlined: StatefulBaseColorDef,
        text: StatefulBaseColorDef,
    ) {
        with(LocalThemeBuilder.current) {
            properties[Property] = mapOf(
                TokenFilledPrimary to primary,
                TokenOutlined to outlined,
                TokenText to text,
            )
        }
    }
}

// ----------------------
// Defaults
// ----------------------

object StyledButtonDefaults {
    @Composable
    fun contentPadding(
        horizontal: Dp = StyledTheme.paddings.large,
        vertical: Dp = StyledTheme.paddings.small,
    ) = PaddingValues(horizontal = horizontal, vertical = vertical)
}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledButton(
    variant: StyledButton.Variant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = StyledTheme.shapes.button,
    contentPadding: PaddingValues = StyledButtonDefaults.contentPadding(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val color = when (variant) {
        is StyledButton.Variant.Token -> Theme[StyledButton.Property][variant.token]
        is StyledButton.Variant.Custom -> variant.colorDef
    }
    val resolveState = rememberStyledResolveState(
        interactionSource,
        enabled,
        isError = false /* no error state for buttons */
    )
    val colors = color.resolve(resolveState)

    LocalStyledComponents.current.Button(
        colors = colors,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}
