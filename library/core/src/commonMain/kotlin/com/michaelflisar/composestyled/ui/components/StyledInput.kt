package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.ColorToken
import com.michaelflisar.composestyled.classes.CustomColors
import com.michaelflisar.composestyled.classes.asColorToken
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.ui.primitives.StyledInputAction
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.utils.number.NumberUtil

internal val LocalStyledInputEnabled = staticCompositionLocalOf { true }

object StyledInput {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledInput")
        val DefaultsKey = StyledKey<Defaults>("StyledInputDefaults")
    // -------------
    // 2) defaults
    // -------------

    @Stable
    data class Defaults(
        val shape: Shape,
        val showConditionalClearIcon: Boolean = true,
        val showConditionalClearIconForNumericInputs: Boolean = false,
        val showConditionalClearIconForNumericOptionalInputs: Boolean = false,
        val actionIconButtonShape: Shape = CircleShape,
    )

    // -------------
    // 3) roles / variants / context
    // -------------

    enum class Variant { Default, Alternative }

    enum class Context : StyledContext {
        LeadingContent,
        TrailingContent,
        NumberPicker,
    }

    // -------------
    // 4) customization
    // -------------

    @Immutable
    class Customization internal constructor(
        val colors: CustomColors,
        val border: ColorToken?,
    )

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation>, StyledComponentWithDefaults<Defaults> {
        override val key: StyledKey<Implementation>
            get() = Key

        override val defaultsKey: StyledKey<Defaults>
            get() = DefaultsKey

        @Composable
        fun topInsetToBorder(): Dp

        @Composable
        fun Render(
            value: TextFieldValue,
            onValueChange: (TextFieldValue) -> Unit,
            modifier: Modifier,
            variant: Variant,
            enabled: Boolean,
            readOnly: Boolean,
            interactionEnabled: Boolean,
            isError: Boolean,
            errorMessage: String?,
            singleLine: Boolean,
            minLines: Int,
            maxLines: Int,
            shape: Shape,
            customization: Customization?,
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
        )
    }
}

@Composable
@OptIn(InternalComposeStyledApi::class)
internal fun Modifier.withStyledInputPointerIcon(): Modifier =
    if (LocalStyledContext.current is StyledInput.Context) {
        pointerHoverIcon(PointerIcon.Default)
    } else {
        this
    }

// -------------------------
// defaults
// -------------------------

object StyledInputDefaults {
    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledInput.DefaultsKey].shape

    val actionIconButtonShape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledInput.DefaultsKey].actionIconButtonShape

    val showConditionalClearIcon: Boolean
        @Composable get() = LocalStyledRegistry.current        [StyledInput.DefaultsKey].showConditionalClearIcon
    val showConditionalClearIconForNumericInputs: Boolean
        @Composable get() = LocalStyledRegistry.current[StyledInput.DefaultsKey].showConditionalClearIconForNumericInputs

    val showConditionalClearIconForNumericOptionalInputs: Boolean
        @Composable get() = LocalStyledRegistry.current[StyledInput.DefaultsKey].showConditionalClearIconForNumericOptionalInputs

    fun customize(colors: CustomColors) = StyledInput.Customization(colors, null)

    fun customize(colors: CustomColors, border: Color) =
        customize(colors, border.asColorToken())

    fun customize(colors: CustomColors, border: ColorToken) =
        StyledInput.Customization(colors, border)

    @Composable
    internal fun trailingIcon(
        value: String,
        onValueChange: (value: String) -> Unit,
    ): @Composable (() -> Unit)? {
        return if (showConditionalClearIcon && value.isNotEmpty()) {
            {
                StyledInputAction(
                    icon = StyledIcons.Clear,
                    onClick = { onValueChange("") }
                )
            }
        } else null
    }

    @Composable
    @PublishedApi
    internal fun <T : Number> trailingNumericOptionalIcon(
        value: T?,
        onValueChange: (value: T?) -> Unit,
    ): @Composable (() -> Unit)? {
        return if (showConditionalClearIconForNumericOptionalInputs && value != null) {
            {
                StyledInputAction(
                    icon = StyledIcons.Clear,
                    onClick = { onValueChange(null) }
                )
            }
        } else null
    }

    @Composable
    internal fun <T : Number> trailingNumericIcon(
        value: T,
        onValueChange: (value: T) -> Unit,
    ): @Composable (() -> Unit)? {
        return if (showConditionalClearIconForNumericInputs && value != NumberUtil.zero(value)) {
            {
                StyledInputAction(
                    icon = StyledIcons.Clear,
                    onClick = { onValueChange(NumberUtil.zero(value)) }
                )
            }
        } else null
    }
}

// -------------------------
// public API
// -------------------------

@Composable
@kotlin.jvm.JvmName("StyledInputTextFieldValueState")
fun StyledInput(
    value: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    interactionEnabled: Boolean = enabled,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingIcon(
        value = value.value.text,
        onValueChange = { value.value = TextFieldValue(it) },
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledInput(
        value = value.value,
        onValueChange = { value.value = it },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        interactionEnabled = interactionEnabled,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
    )
}

@Composable
fun StyledInput(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    interactionEnabled: Boolean = enabled,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingIcon(
        value = value.text,
        onValueChange = { onValueChange(TextFieldValue(it)) },
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledInputImpl(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        interactionEnabled = interactionEnabled,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
    )
}

@Composable
fun StyledInput(
    value: MutableState<String>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingIcon(
        value = value.value,
        onValueChange = { value.value = it }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledInput(
        value = value.value,
        onValueChange = { value.value = it },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
    )
}

@Composable
fun StyledInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    interactionEnabled: Boolean = enabled,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingIcon(
        value = value,
        onValueChange = onValueChange
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledInputImpl(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        interactionEnabled = interactionEnabled,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        variant = StyledInput.Variant.Default,
    )
}

@Composable
fun StyledAlternativeInput(
    value: MutableState<String>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingIcon(
        value = value.value,
        onValueChange = { value.value = it }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledAlternativeInput(
        value = value.value,
        onValueChange = { value.value = it },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
    )
}

@Composable
fun StyledAlternativeInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    interactionEnabled: Boolean = enabled,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingIcon(
        value = value,
        onValueChange = onValueChange
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledInputImpl(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        interactionEnabled = interactionEnabled,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        variant = StyledInput.Variant.Alternative,
    )
}

@Composable
fun <T : Number> StyledInput(
    value: MutableState<T>,
    modifier: Modifier = Modifier,
    valid: MutableState<Boolean> = remember { mutableStateOf(true) },
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = if (NumberUtil.supportsDecimal(value.value)) KeyboardType.Decimal else KeyboardType.Number
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingNumericIcon(
        value = value.value,
        onValueChange = { value.value = it }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledInput(
        value = value.value,
        onValueChange = {
            valid.value = it != null
            value.value = it ?: NumberUtil.zero(value.value)
        },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
    )
}

@Composable
fun <T : Number> StyledInput(
    value: T,
    onValueChange: (T?) -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = if (NumberUtil.supportsDecimal(value)) KeyboardType.Decimal else KeyboardType.Number
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingNumericIcon(
        value = value,
        onValueChange = { onValueChange(it) }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledInputImpl(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        variant = StyledInput.Variant.Default,
    )
}

@Composable
fun <T : Number> StyledAlternativeInput(
    value: MutableState<T>,
    modifier: Modifier = Modifier,
    valid: MutableState<Boolean> = remember { mutableStateOf(true) },
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = if (NumberUtil.supportsDecimal(value.value)) KeyboardType.Decimal else KeyboardType.Number
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingNumericIcon(
        value = value.value,
        onValueChange = { value.value = it }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledAlternativeInput(
        value = value.value,
        onValueChange = {
            valid.value = it != null
            value.value = it ?: NumberUtil.zero(value.value)
        },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
    )
}

@Composable
fun <T : Number> StyledAlternativeInput(
    value: T,
    onValueChange: (T?) -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = if (NumberUtil.supportsDecimal(value)) KeyboardType.Decimal else KeyboardType.Number
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingNumericIcon(
        value = value,
        onValueChange = { onValueChange(it) }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledInputImpl(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        variant = StyledInput.Variant.Alternative,
    )
}

@Composable
fun <T : Number> StyledOptionalInput(
    value: MutableState<T?>,
    sample: T,
    modifier: Modifier = Modifier,
    valid: MutableState<Boolean> = remember { mutableStateOf(true) },
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = if (NumberUtil.supportsDecimal(sample)) KeyboardType.Decimal else KeyboardType.Number
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingNumericOptionalIcon(
        value = value.value,
        onValueChange = { value.value = it }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledOptionalInput(
        value = value.value,
        sample = sample,
        onValueChange = {
            valid.value = it != null
            value.value = it
        },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
    )
}

@Composable
fun <T : Number> StyledOptionalInput(
    value: T?,
    sample: T,
    onValueChange: (T?) -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = if (NumberUtil.supportsDecimal(sample)) KeyboardType.Decimal else KeyboardType.Number
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingNumericOptionalIcon<T>(
        value = value,
        onValueChange = { onValueChange(it) }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledOptionalInputImpl(
        value = value,
        sample = sample,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        variant = StyledInput.Variant.Default,
    )
}

@Composable
fun <T : Number> StyledAlternativeOptionalInput(
    value: MutableState<T?>,
    sample: T,
    modifier: Modifier = Modifier,
    valid: MutableState<Boolean> = remember { mutableStateOf(true) },
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = if (NumberUtil.supportsDecimal(sample)) KeyboardType.Decimal else KeyboardType.Number
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingNumericOptionalIcon(
        value = value.value,
        onValueChange = { value.value = it }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledAlternativeOptionalInput(
        value = value.value,
        sample = sample,
        onValueChange = {
            valid.value = it != null
            value.value = it
        },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
    )
}

// -------------------------

@Composable
fun <T : Number> StyledAlternativeOptionalInput(
    value: T?,
    sample: T,
    onValueChange: (T?) -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = if (NumberUtil.supportsDecimal(sample)) KeyboardType.Decimal else KeyboardType.Number
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingNumericOptionalIcon<T>(
        value = value,
        onValueChange = { onValueChange(it) }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledOptionalInputImpl(
        value = value,
        sample = sample,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        variant = StyledInput.Variant.Alternative,
    )
}

// private API
// -------------------------

@Composable
@OptIn(InternalComposeStyledApi::class)
private fun StyledInputImpl(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    variant: StyledInput.Variant = StyledInput.Variant.Default,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    interactionEnabled: Boolean = enabled,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingIcon(
        value = value,
        onValueChange = onValueChange
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    var editingValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(value))
    }
    val fieldValue = editingValue.copy(text = value)
    SideEffect {
        if (editingValue.selection != fieldValue.selection ||
            editingValue.composition != fieldValue.composition
        ) {
            editingValue = fieldValue
        }
    }
    var lastTextValue by remember(value) { mutableStateOf(value) }
    StyledInputImpl(
        value = fieldValue,
        onValueChange = {
            editingValue = it
            val textChanged = lastTextValue != it.text
            lastTextValue = it.text
            if (textChanged) onValueChange(it.text)
        },
        modifier = modifier,
        variant = variant,
        enabled = enabled,
        readOnly = readOnly,
        interactionEnabled = interactionEnabled,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
    )
}

@Composable
@OptIn(InternalComposeStyledApi::class)
private fun StyledInputImpl(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    variant: StyledInput.Variant = StyledInput.Variant.Default,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    interactionEnabled: Boolean = enabled,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
) {
    require(minLines > 0 && maxLines >= minLines) {
        "minLines must be positive and maxLines must be at least minLines"
    }
    val scopedLeadingIcon: (@Composable () -> Unit)? = leadingIcon?.let { content ->
        {
            CompositionLocalProvider(
                LocalStyledContext provides StyledInput.Context.LeadingContent,
                LocalStyledInputEnabled provides enabled,
                content = content,
            )
        }
    }
    val scopedTrailingIcon: (@Composable () -> Unit)? = trailingIcon?.let { content ->
        {
            CompositionLocalProvider(
                LocalStyledContext provides StyledInput.Context.TrailingContent,
                LocalStyledInputEnabled provides enabled,
                content = content,
            )
        }
    }
    val inputModifier = if (interactionEnabled) {
        modifier
    } else {
        modifier.pointerHoverIcon(PointerIcon.Default, overrideDescendants = true)
    }
    LocalStyledRegistry.current[StyledInput.Key].Render(
        value = value,
        onValueChange = onValueChange,
        modifier = inputModifier,
        variant = variant,
        enabled = enabled,
        readOnly = readOnly,
        interactionEnabled = interactionEnabled,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = scopedLeadingIcon,
        trailingIcon = scopedTrailingIcon,
        supportingText = supportingText,
    )
}

@Composable
private fun StyledInputImpl(
    value: MutableState<String>,
    modifier: Modifier = Modifier,
    variant: StyledInput.Variant = StyledInput.Variant.Default,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingIcon(
        value = value.value,
        onValueChange = { value.value = it }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledInputImpl(
        value = value.value,
        onValueChange = { value.value = it },
        modifier = modifier,
        variant = variant,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
    )
}

@Composable
private fun <T : Number> StyledInputImpl(
    value: T,
    onValueChange: (T?) -> Unit = {},
    modifier: Modifier = Modifier,
    variant: StyledInput.Variant = StyledInput.Variant.Default,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = if (NumberUtil.supportsDecimal(value)) KeyboardType.Decimal else KeyboardType.Number
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingNumericIcon(
        value = value,
        onValueChange = { onValueChange(it) }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    NumberUtil.requireSupported(value)
    val text = rememberSaveable { mutableStateOf(value.toString()) }
    val onTextValueChange: (String) -> Unit = { newValue ->
        val filtered = NumberUtil.clearText(newValue, NumberUtil.supportsDecimal(value))
        text.value = filtered
        onValueChange(NumberUtil.parse(value, filtered))
    }

    StyledInputImpl(
        value = text.value,
        onValueChange = onTextValueChange,
        modifier = modifier,
        variant = variant,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
    )
}

@Composable
private fun <T : Number> StyledOptionalInputImpl(
    value: T?,
    sample: T,
    onValueChange: (T?) -> Unit = {},
    modifier: Modifier = Modifier,
    variant: StyledInput.Variant = StyledInput.Variant.Default,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = if (NumberUtil.supportsDecimal(sample)) KeyboardType.Decimal else KeyboardType.Number
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingNumericOptionalIcon<T>(
        value = value,
        onValueChange = { onValueChange(it) }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    NumberUtil.requireSupported(sample)
    value?.let { NumberUtil.requireSupported(it) }
    val text = rememberSaveable { mutableStateOf(value?.toString() ?: "") }
    val onTextValueChange: (String) -> Unit = { newValue ->
        val filtered = NumberUtil.clearText(newValue, NumberUtil.supportsDecimal(sample))
        text.value = filtered
        onValueChange(NumberUtil.parse(sample, filtered))
    }

    StyledInputImpl(
        value = text.value,
        onValueChange = onTextValueChange,
        modifier = modifier,
        variant = variant,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
    )
}

@Composable
private fun <T : Number> StyledInputImpl(
    value: MutableState<T>,
    modifier: Modifier = Modifier,
    valid: MutableState<Boolean> = remember { mutableStateOf(true) },
    variant: StyledInput.Variant = StyledInput.Variant.Default,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = if (NumberUtil.supportsDecimal(value.value)) KeyboardType.Decimal else KeyboardType.Number
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingNumericIcon(
        value = value.value,
        onValueChange = { value.value = it }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    StyledInputImpl(
        value = value.value,
        onValueChange = {
            valid.value = it != null
            value.value = it ?: NumberUtil.zero(value.value)
        },
        modifier = modifier,
        variant = variant,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
    )
}

@Composable
private fun <T : Number> StyledOptionalInputImpl(
    value: MutableState<T?>,
    sample: T,
    modifier: Modifier = Modifier,
    valid: MutableState<Boolean> = remember { mutableStateOf(true) },
    variant: StyledInput.Variant = StyledInput.Variant.Default,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = if (NumberUtil.supportsDecimal(sample)) KeyboardType.Decimal else KeyboardType.Number
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = StyledInputDefaults.trailingNumericOptionalIcon(
        value = value.value,
        onValueChange = { value.value = it }
    ),
    supportingText: @Composable (() -> Unit)? = null,
) {
    NumberUtil.requireSupported(sample)
    value.value?.let { NumberUtil.requireSupported(it) }
    val text = rememberSaveable { mutableStateOf(value.value?.toString() ?: "") }
    val onTextValueChange: (String) -> Unit = { newValue ->
        val filtered = NumberUtil.clearText(newValue, NumberUtil.supportsDecimal(sample))
        text.value = filtered
        val parsedValue = NumberUtil.parse(sample, filtered)
        valid.value = parsedValue != null
        value.value = parsedValue
    }

    StyledInputImpl(
        value = text.value,
        onValueChange = onTextValueChange,
        modifier = modifier,
        variant = variant,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = shape,
        customization = customization,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
    )
}
