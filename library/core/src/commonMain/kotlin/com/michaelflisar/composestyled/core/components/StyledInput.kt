package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.composeunstyled.theme.Theme
import com.composeunstyled.theme.ThemeBuilder
import com.composeunstyled.theme.ThemeProperty
import com.composeunstyled.theme.ThemeToken
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState
import com.michaelflisar.composestyled.core.tokens.StyledColors

object StyledInput : BaseStyledComponent {

    internal val Property = ThemeProperty<StatefulBaseColorDef>("input")

    internal val TokenFilled = ThemeToken<StatefulBaseColorDef>("input.filled.default")
    internal val TokenOutlined = ThemeToken<StatefulBaseColorDef>("input.outlined.default")

    sealed class Variant {

        companion object {
            val Filled: Variant = Token(TokenFilled)
            val Outlined: Variant = Token(TokenOutlined)

            fun custom(colorDef: StatefulBaseColorDef): Variant = Custom(colorDef)
        }

        internal data class Token(
            val token: ThemeToken<StatefulBaseColorDef>,
        ) : Variant()

        internal data class Custom(
            val colorDef: StatefulBaseColorDef,
        ) : Variant()
    }

    @Composable
    override fun registerStyle(builder: ThemeBuilder, colors: StyledColors) {
        with(builder) {
            properties[Property] = createDefaultKeyMap(colors)
        }
    }

    @Composable
    private fun createDefaultKeyMap(colors: StyledColors): Map<ThemeToken<StatefulBaseColorDef>, StatefulBaseColorDef> {
        val filled = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = colors.surface,
                foreground = colors.onSurface,
                border = null
            ),
            focused = BaseColorDef(
                background = colors.surface,
                foreground = colors.onSurface,
                border = colors.primary
            ),
            error = BaseColorDef(
                background = colors.surface,
                foreground = colors.onSurface,
                border = colors.error
            )
        )

        val outlined = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onSurface,
                border = colors.outlineVariant
            ),
            focused = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onSurface,
                border = colors.primary
            ),
            error = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onSurface,
                border = colors.error
            )
        )

        return mapOf(
            TokenFilled to filled,
            TokenOutlined to outlined,
        )
    }
}

// ----------------------
// Defaults
// ----------------------

object StyledInputDefaults {

    // currently no defaults

}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledInput(
    value: String,
    onValueChange: (String) -> Unit,
    variant: StyledInput.Variant = StyledInput.Variant.Filled,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = StyledTheme.shapes.input,
) {
    val colorDef = when (variant) {
        is StyledInput.Variant.Token -> Theme[StyledInput.Property][variant.token]
        is StyledInput.Variant.Custom -> variant.colorDef
    }

    val resolveState = rememberStyledResolveState(
        interactionSource = interactionSource,
        enabled = enabled,
        isError = isError
    )
    val colors = colorDef.resolve(resolveState)

    LocalStyledComponents.current.Input(
        value = value,
        onValueChange = onValueChange,
        colors = colors,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
    )
}