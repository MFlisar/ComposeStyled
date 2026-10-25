package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledInput
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

class Material3Input internal constructor(
    val variantMapping: VariantMapping,
    override val defaults: StyledInput.Defaults,
) : StyledInput.Implementation {
    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            variantMapping: VariantMapping = DefaultVariantMapping,
            defaults: StyledInput.Defaults = defaults(),
        ) = remember(variantMapping, defaults) { Material3Input(variantMapping, defaults) }

        // -------------
        // 2) mapping
        // -------------

        typealias VariantMapping = (StyledInput.Variant) -> Variant

        val DefaultVariantMapping: VariantMapping = { variant ->
            when (variant) {
                StyledInput.Variant.Default -> Variant.Default
                StyledInput.Variant.Alternative -> Variant.Alternative
            }
        }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(
            shape: Shape = RoundedCornerShape(4.dp),
            actionIconButtonShape: Shape = CircleShape,
        ) = StyledInput.Defaults(shape = shape, actionIconButtonShape = actionIconButtonShape)
    }

    enum class Variant { Default, Alternative }

    @Composable
    override fun topInsetToBorder(): Dp = minimizedLabelHalfHeight()

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier,
        variant: StyledInput.Variant,
        enabled: Boolean,
        readOnly: Boolean,
        interactionEnabled: Boolean,
        isError: Boolean,
        errorMessage: String?,
        singleLine: Boolean,
        minLines: Int,
        maxLines: Int,
        shape: Shape,
        customization: StyledInput.Customization?,
        textStyle: TextStyle,
        keyboardOptions: KeyboardOptions,
        keyboardActions: KeyboardActions,
        visualTransformation: VisualTransformation,
        interactionSource: MutableInteractionSource?,
        label: @Composable (() -> Unit)?,
        placeholder: @Composable (() -> Unit)?,
        leadingIcon: @Composable (() -> Unit)?,
        trailingIcon: @Composable (() -> Unit)?,
        supportingText: @Composable (() -> Unit)?,
    ) {
        val mappedVariant = variantMapping(variant)
        val filledBackground = if (mappedVariant == Variant.Alternative) {
            customization?.colors?.filledBackground?.resolve()
                ?.takeIf { it != Color.Unspecified } ?: StyledTheme.colorScheme.surfaceVariant
        } else {
            Color.Unspecified
        }
        val contentColor = if (mappedVariant == Variant.Alternative) {
            customization?.colors?.filledForeground?.resolve()
        } else {
            customization?.colors?.textForeground?.resolve()
        }?.takeIf { it != Color.Unspecified } ?: Color.Unspecified
        val customBorder = customization?.border?.resolve()
            ?.takeIf { it != Color.Unspecified } ?: Color.Unspecified
        val textColor = contentColor.takeIf { it != Color.Unspecified }
            ?: MaterialTheme.colorScheme.onSurface
        val unfocusedBorderColor = customBorder.takeIf { it != Color.Unspecified }
            ?: MaterialTheme.colorScheme.outline
        val unfocusedDecorationColor = MaterialTheme.colorScheme.onSurfaceVariant
        val fieldModifier = modifier.semantics {
            if (isError && errorMessage != null) error(errorMessage)
        }
        when (mappedVariant) {
            Variant.Default -> OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = fieldModifier,
                enabled = enabled && interactionEnabled,
                readOnly = readOnly,
                isError = isError,
                singleLine = singleLine,
                minLines = minLines,
                maxLines = maxLines,
                shape = shape,
                textStyle = textStyle,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                label = styledInputSlot(label),
                placeholder = styledInputSlot(placeholder),
                leadingIcon = styledInputSlot(leadingIcon),
                trailingIcon = styledInputSlot(trailingIcon),
                supportingText = styledInputSlot(supportingText),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Unspecified,
                    unfocusedContainerColor = Color.Unspecified,
                    disabledContainerColor = Color.Unspecified,
                    errorContainerColor = Color.Unspecified,
                    focusedTextColor = contentColor,
                    unfocusedTextColor = contentColor,
                    disabledTextColor = textColor,
                    errorTextColor = contentColor,
                    focusedBorderColor = customBorder,
                    unfocusedBorderColor = unfocusedBorderColor,
                    disabledBorderColor = if (isError) MaterialTheme.colorScheme.error else unfocusedBorderColor,
                    disabledLabelColor = unfocusedDecorationColor,
                    disabledPlaceholderColor = unfocusedDecorationColor,
                    disabledLeadingIconColor = unfocusedDecorationColor,
                    disabledTrailingIconColor = unfocusedDecorationColor,
                    disabledSupportingTextColor = unfocusedDecorationColor,
                ),
            )
            Variant.Alternative -> TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = fieldModifier,
                enabled = enabled && interactionEnabled,
                readOnly = readOnly,
                isError = isError,
                singleLine = singleLine,
                minLines = minLines,
                maxLines = maxLines,
                shape = shape,
                textStyle = textStyle,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                label = styledInputSlot(label),
                placeholder = styledInputSlot(placeholder),
                leadingIcon = styledInputSlot(leadingIcon),
                trailingIcon = styledInputSlot(trailingIcon),
                supportingText = styledInputSlot(supportingText),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = filledBackground,
                    unfocusedContainerColor = filledBackground,
                    disabledContainerColor = filledBackground,
                    errorContainerColor = filledBackground,
                    focusedTextColor = contentColor,
                    unfocusedTextColor = contentColor,
                    disabledTextColor = textColor,
                    errorTextColor = contentColor,
                    focusedIndicatorColor = customBorder,
                    unfocusedIndicatorColor = unfocusedDecorationColor,
                    disabledIndicatorColor = unfocusedDecorationColor,
                    disabledLabelColor = unfocusedDecorationColor,
                    disabledPlaceholderColor = unfocusedDecorationColor,
                    disabledLeadingIconColor = unfocusedDecorationColor,
                    disabledTrailingIconColor = unfocusedDecorationColor,
                    disabledSupportingTextColor = unfocusedDecorationColor,
                ),
            )
        }
    }
}

@OptIn(InternalComposeStyledApi::class)
private fun styledInputSlot(content: (@Composable () -> Unit)?): (@Composable () -> Unit)? =
    if (content == null) null else {
        {
            ProvideCustomStyledContentLocals(
                contentColor = LocalContentColor.current,
                textStyle = LocalTextStyle.current,
                content = content,
            )
        }
    }

@Composable
private fun minimizedLabelHalfHeight(): Dp {
    val compositionLocalValue = MaterialTheme.typography.bodySmall.lineHeight
    val fallbackValue = 16.sp//TypeScaleTokens.BodySmallLineHeight
    val value = if (compositionLocalValue.isSp) compositionLocalValue else fallbackValue
    return with(LocalDensity.current) { value.toDp() / 2 }
}