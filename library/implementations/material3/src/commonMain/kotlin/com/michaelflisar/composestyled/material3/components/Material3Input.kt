package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.material3.ProvideStyledLocals
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.tokens.LocalStyledTextStyle
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext

class Material3Input internal constructor(
    val variantMapping: VariantMapping,
) : StyledInput.Implementation {

    override val defaults: StyledInput.Defaults
        @Composable get() = StyledInput.Defaults(
            shape = RoundedCornerShape(4.dp),
            actionIconButtonShape = CircleShape,
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            variantMapping: VariantMapping = DefaultVariantMapping,
        ) = remember(variantMapping) { Material3Input(variantMapping) }

        // -------------
        // 2) mapping
        // -------------

        typealias VariantMapping = (StyledInput.Variant, StyledContext) -> Variant

        val DefaultVariantMapping: VariantMapping = { variant, context ->
            when {
                variant == StyledInput.Variant.Auto &&
                    context == StyledInput.Context.DropdownFilter -> Variant.Outlined
                else -> when (variant) {
                    StyledInput.Variant.Auto,
                    StyledInput.Variant.Filled -> Variant.Filled
                    StyledInput.Variant.Outlined -> Variant.Outlined
                }
            }
        }
    }

    enum class Variant { Filled, Outlined }

    @Composable
    override fun topInsetToBorder(): Dp = minimizedLabelHalfHeight()

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        value: TextFieldValue,
        onValueChange: (TextFieldValue) -> Unit,
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
        val mappedVariant = variantMapping(variant, LocalStyledContext.current)
        val resolvedInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
        val isFocused = resolvedInteractionSource.collectIsFocusedAsState().value
        val isFieldEnabled = enabled && interactionEnabled
        val filledBackground = if (mappedVariant == Variant.Filled) {
            customization?.colors?.filledBackground?.resolve()
                ?.takeIf { it != Color.Unspecified } ?: StyledTheme.colorScheme.surfaceVariant
        } else {
            Color.Unspecified
        }
        val contentColor = if (mappedVariant == Variant.Filled) {
            customization?.colors?.filledForeground?.resolve()
        } else {
            customization?.colors?.textForeground?.resolve()
        }?.takeIf { it != Color.Unspecified } ?: Color.Unspecified
        val customBorder = customization?.border?.resolve()
            ?.takeIf { it != Color.Unspecified } ?: Color.Unspecified
        val textColor = contentColor.takeIf { it != Color.Unspecified }
            ?: StyledTheme.colorScheme.onSurface
        val iconContentColor = if (isFieldEnabled) {
            textColor
        } else {
            textColor.copy(
                alpha = textColor.alpha * StyledTheme.colorScheme.disableFactors.content,
            )
        }
        val unfocusedBorderColor = customBorder.takeIf { it != Color.Unspecified }
            ?: StyledTheme.colorScheme.outline
        val unfocusedDecorationColor = StyledTheme.colorScheme.onSurfaceVariant
        val disabledDecorationColor = unfocusedDecorationColor.copy(
            alpha = unfocusedDecorationColor.alpha * StyledTheme.colorScheme.disableFactors.content,
        )
        val labelColor = when {
            !isFieldEnabled -> disabledDecorationColor
            isError -> StyledTheme.colorScheme.error
            isFocused -> StyledTheme.colorScheme.primary
            else -> unfocusedDecorationColor
        }
        val styledSupportingColor = when {
            !isFieldEnabled -> disabledDecorationColor
            isError -> StyledTheme.colorScheme.error
            else -> unfocusedDecorationColor
        }
        val fieldModifier = modifier.semantics {
            if (isError && errorMessage != null) error(errorMessage)
        }
        when (mappedVariant) {
            Variant.Outlined -> OutlinedTextField(
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
                interactionSource = resolvedInteractionSource,
                label = styledInputSlot(label, labelColor, StyledTheme.typography.bodySmall),
                placeholder = styledInputSlot(
                    placeholder,
                    if (isFieldEnabled) unfocusedDecorationColor else disabledDecorationColor,
                    textStyle,
                ),
                leadingIcon = styledInputSlot(leadingIcon, iconContentColor),
                trailingIcon = styledInputSlot(trailingIcon, iconContentColor),
                supportingText = styledInputSlot(
                    supportingText,
                    styledSupportingColor,
                    StyledTheme.typography.bodySmall,
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Unspecified,
                    unfocusedContainerColor = Color.Unspecified,
                    disabledContainerColor = Color.Unspecified,
                    errorContainerColor = Color.Unspecified,
                    focusedTextColor = contentColor,
                    unfocusedTextColor = contentColor,
                    disabledTextColor = iconContentColor,
                    errorTextColor = contentColor,
                    focusedLabelColor = StyledTheme.colorScheme.primary,
                    unfocusedLabelColor = unfocusedDecorationColor,
                    errorLabelColor = StyledTheme.colorScheme.error,
                    focusedPlaceholderColor = unfocusedDecorationColor,
                    unfocusedPlaceholderColor = unfocusedDecorationColor,
                    errorPlaceholderColor = unfocusedDecorationColor,
                    focusedSupportingTextColor = unfocusedDecorationColor,
                    unfocusedSupportingTextColor = unfocusedDecorationColor,
                    errorSupportingTextColor = StyledTheme.colorScheme.error,
                    focusedLeadingIconColor = iconContentColor,
                    unfocusedLeadingIconColor = iconContentColor,
                    disabledLeadingIconColor = iconContentColor,
                    errorLeadingIconColor = iconContentColor,
                    focusedTrailingIconColor = iconContentColor,
                    unfocusedTrailingIconColor = iconContentColor,
                    disabledTrailingIconColor = iconContentColor,
                    errorTrailingIconColor = iconContentColor,
                    focusedBorderColor = customBorder,
                    unfocusedBorderColor = unfocusedBorderColor,
                    disabledBorderColor = if (isError) StyledTheme.colorScheme.error else unfocusedBorderColor,
                    disabledLabelColor = disabledDecorationColor,
                    disabledPlaceholderColor = disabledDecorationColor,
                    disabledSupportingTextColor = disabledDecorationColor,
                ),
            )
            Variant.Filled -> TextField(
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
                interactionSource = resolvedInteractionSource,
                label = styledInputSlot(label, labelColor, StyledTheme.typography.bodySmall),
                placeholder = styledInputSlot(
                    placeholder,
                    if (isFieldEnabled) unfocusedDecorationColor else disabledDecorationColor,
                    textStyle,
                ),
                leadingIcon = styledInputSlot(leadingIcon, iconContentColor),
                trailingIcon = styledInputSlot(trailingIcon, iconContentColor),
                supportingText = styledInputSlot(
                    supportingText,
                    styledSupportingColor,
                    StyledTheme.typography.bodySmall,
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = filledBackground,
                    unfocusedContainerColor = filledBackground,
                    disabledContainerColor = filledBackground,
                    errorContainerColor = filledBackground,
                    focusedTextColor = contentColor,
                    unfocusedTextColor = contentColor,
                    disabledTextColor = iconContentColor,
                    errorTextColor = contentColor,
                    focusedLabelColor = StyledTheme.colorScheme.primary,
                    unfocusedLabelColor = unfocusedDecorationColor,
                    errorLabelColor = StyledTheme.colorScheme.error,
                    focusedPlaceholderColor = unfocusedDecorationColor,
                    unfocusedPlaceholderColor = unfocusedDecorationColor,
                    errorPlaceholderColor = unfocusedDecorationColor,
                    focusedSupportingTextColor = unfocusedDecorationColor,
                    unfocusedSupportingTextColor = unfocusedDecorationColor,
                    errorSupportingTextColor = StyledTheme.colorScheme.error,
                    focusedLeadingIconColor = iconContentColor,
                    unfocusedLeadingIconColor = iconContentColor,
                    disabledLeadingIconColor = iconContentColor,
                    errorLeadingIconColor = iconContentColor,
                    focusedTrailingIconColor = iconContentColor,
                    unfocusedTrailingIconColor = iconContentColor,
                    disabledTrailingIconColor = iconContentColor,
                    errorTrailingIconColor = iconContentColor,
                    focusedIndicatorColor = customBorder,
                    unfocusedIndicatorColor = unfocusedDecorationColor,
                    disabledIndicatorColor = unfocusedDecorationColor,
                    disabledLabelColor = disabledDecorationColor,
                    disabledPlaceholderColor = disabledDecorationColor,
                    disabledSupportingTextColor = disabledDecorationColor,
                ),
            )
        }
    }
}

@OptIn(InternalComposeStyledApi::class)
private fun styledInputSlot(
    content: (@Composable () -> Unit)?,
    contentColor: Color? = null,
    textStyle: TextStyle? = null,
): (@Composable () -> Unit)? =
    if (content == null) null else {
        {
            ProvideStyledLocals(
                contentColor = contentColor ?: LocalStyledContentColor.current,
                textStyle = textStyle ?: LocalStyledTextStyle.current,
                content = content,
            )
        }
    }

@Composable
private fun minimizedLabelHalfHeight(): Dp {
    val compositionLocalValue = StyledTheme.typography.bodySmall.lineHeight
    val fallbackValue = 16.sp//TypeScaleTokens.BodySmallLineHeight
    val value = if (compositionLocalValue.isSp) compositionLocalValue else fallbackValue
    return with(LocalDensity.current) { value.toDp() / 2 }
}