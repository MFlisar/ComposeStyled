package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.composeunstyled.theme.ThemeProperty
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.IVariantId
import com.michaelflisar.composestyled.core.classes.TokenMap
import com.michaelflisar.composestyled.core.classes.Variant
import com.michaelflisar.composestyled.core.classes.colors.BaseColor
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.classes.customDataOrNull
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenCompontents
import com.michaelflisar.composestyled.core.renderer.StyledTokenRenderer
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

typealias StyledTextFieldVariant = Variant<StyledTextField.VariantId, StatefulBaseColorDef>

object StyledTextField {

    // properties
    private val Property = ThemeProperty<StatefulBaseColorDef>("input")

    // variant ids
    enum class VariantId(
        override val id: String,
    ) : IVariantId {
        Filled("input.filled.default"),
        Outlined("input.outlined.default"),
    }

    // variants
    object Variants {
        val Filled: StyledTextFieldVariant = Variant.Token(VariantId.Filled)
        val Outlined: StyledTextFieldVariant = Variant.Token(VariantId.Outlined)
        fun custom(
            variantId: VariantId,
            colorDef: StatefulBaseColorDef,
        ) = Variant.Custom(variantId, colorDef)
    }

    // tokens
    internal val Tokens = TokenMap.create<VariantId, StatefulBaseColorDef>(Property)

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        filled: StatefulBaseColorDef,
        outlined: StatefulBaseColorDef,
    ) {
        Tokens.registerStyles(
            styles = mapOf(
                VariantId.Filled to filled,
                VariantId.Outlined to outlined,
            )
        )
    }
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
        request: Request,
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

    data class Request(
        val variant: StyledTextField.VariantId,
        val customColors: StatefulBaseColorDef?,
    )
}

// ----------------------
// Defaults
// ----------------------

object StyledTextFieldDefaults {

    val DefaultVariant: StyledTextFieldVariant = StyledTextField.Variants.Filled

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
    variant: StyledTextFieldVariant = StyledTextFieldDefaults.DefaultVariant,
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

        is StyledTokenCompontents -> {
            val state = rememberStyledResolveState(
                interactionSource = interactionSource,
                enabled = enabled,
                isError = isError,
            )

            val def = StyledTextField.Tokens.resolveVariantData(variant)
            val colors = def.resolve(state)

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
                request = StyledTextFieldWrapperRenderer.Request(
                    variant = variant.variantId,
                    customColors = variant.customDataOrNull(),
                ),
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
