package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.composeunstyled.theme.ThemeProperty
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.IVariant
import com.michaelflisar.composestyled.core.classes.TokenMap
import com.michaelflisar.composestyled.core.classes.colors.BaseColor
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenRenderer
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

object StyledTextField {

    // properties
    private val Property = ThemeProperty<StatefulBaseColorDef>("input")

    // tokens
    internal val Tokens = TokenMap.create(Property, Variant.entries.toSet())

    // variants
    enum class Variant(
        override val id: String,
    ) : IVariant {
        Filled("input.filled.default"),
        Outlined("input.outlined.default"),
    }

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        filled: StatefulBaseColorDef,
        outlined: StatefulBaseColorDef,
    ) {
        Tokens.registerStyles(
            styles = mapOf(
                Variant.Filled to filled,
                Variant.Outlined to outlined,
            )
        )
    }

    fun customize(
        background: Color? = null,
        content: Color? = null,
        border: Color? = null,
    ) = Customization(background, content, border)

    @Immutable
    class Customization internal constructor(
        val background: Color?,
        val content: Color?,
        val border: Color?,
    )
}

// ----------------------
// Renderer
// ----------------------

interface StyledTextFieldTokenRenderer : StyledTokenRenderer {

    @Composable
    fun Render(
        value: String,
        onValueChange: (String) -> Unit,
        colors: BaseColor,
        modifier: Modifier,
        enabled: Boolean,
        readOnly: Boolean,
        textStyle: TextStyle,
        label: @Composable (() -> Unit)?,
        placeholder: @Composable (() -> Unit)?,
        leadingIcon: @Composable (() -> Unit)?,
        trailingIcon: @Composable (() -> Unit)?,
        prefix: @Composable (() -> Unit)?,
        suffix: @Composable (() -> Unit)?,
        supportingText: @Composable (() -> Unit)?,
        isError: Boolean,
        visualTransformation: VisualTransformation,
        keyboardOptions: KeyboardOptions,
        keyboardActions: KeyboardActions,
        singleLine: Boolean,
        maxLines: Int,
        minLines: Int,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        shape: Shape,
    )
}

interface StyledTextFieldWrapperRenderer {

    @Composable
    fun Render(
        variant: StyledTextField.Variant,
        customization: StyledTextField.Customization?,
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        readOnly: Boolean,
        textStyle: TextStyle,
        label: @Composable (() -> Unit)?,
        placeholder: @Composable (() -> Unit)?,
        leadingIcon: @Composable (() -> Unit)?,
        trailingIcon: @Composable (() -> Unit)?,
        prefix: @Composable (() -> Unit)?,
        suffix: @Composable (() -> Unit)?,
        supportingText: @Composable (() -> Unit)?,
        isError: Boolean,
        visualTransformation: VisualTransformation,
        keyboardOptions: KeyboardOptions,
        keyboardActions: KeyboardActions,
        singleLine: Boolean,
        maxLines: Int,
        minLines: Int,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        shape: Shape,
    )
}

// ----------------------
// Defaults
// ----------------------

object StyledTextFieldDefaults {

    val DefaultVariant: StyledTextField.Variant = StyledTextField.Variant.Filled

    @Composable
    fun contentPadding() = PaddingValues(
        horizontal = StyledTheme.paddings.medium,
        vertical = StyledTheme.paddings.small,
    )
}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    variant: StyledTextField.Variant = StyledTextFieldDefaults.DefaultVariant,
    customization: StyledTextField.Customization? = null,
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
    when (val components = LocalStyledComponents.current) {

        is StyledTokenComponents -> {
            val state = rememberStyledResolveState(
                interactionSource = interactionSource,
                enabled = enabled,
                isError = isError,
            )

            val def = StyledTextField.Tokens.resolveVariantData(variant)
            val defCustomised = def.customise(
                background = customization?.background,
                foreground = customization?.content,
                border = customization?.border,
            )
            val colors = defCustomised.resolve(state)

            components.textField.Render(
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
                contentPadding = StyledTextFieldDefaults.contentPadding(),
                interactionSource = interactionSource,
                shape = shape,
            )
        }

        is StyledWrapperComponents -> {
            components.textField.Render(
                variant = variant,
                customization = customization,
                value = value,
                onValueChange = onValueChange,
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
                contentPadding = StyledTextFieldDefaults.contentPadding(),
                interactionSource = interactionSource,
                shape = shape,
            )
        }
    }
}
