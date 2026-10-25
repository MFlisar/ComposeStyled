package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.classes.ColorToken
import com.michaelflisar.composestyledcomponents.classes.CustomColors
import com.michaelflisar.composestyledcomponents.classes.asColorToken
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.runtime.LocalStyledContext
import com.michaelflisar.composestyledcomponents.runtime.StyledContext
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledInputAction
import com.michaelflisar.composestyledcomponents.utils.number.NumberUtil

internal val LocalStyledInputEnabled = staticCompositionLocalOf { true }

object StyledInput {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledInput")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    class Defaults(
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

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        val defaults: Defaults

        @Composable
        fun topInsetToBorder(): Dp

        @Composable
        fun Render(
            value: String,
            onValueChange: (String) -> Unit,
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
        @Composable get() = LocalStyledRegistry.current[StyledInput.Key].defaults.shape

    val actionIconButtonShape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledInput.Key].defaults.actionIconButtonShape

    val showConditionalClearIcon: Boolean
        @Composable get() = LocalStyledRegistry.current[StyledInput.Key].defaults.showConditionalClearIcon

    val showConditionalClearIconForNumericInputs: Boolean
        @Composable get() = LocalStyledRegistry.current[StyledInput.Key].defaults.showConditionalClearIconForNumericInputs

    val showConditionalClearIconForNumericOptionalInputs: Boolean
        @Composable get() = LocalStyledRegistry.current[StyledInput.Key].defaults.showConditionalClearIconForNumericOptionalInputs

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
                    icon = Icons.Default.Clear.toIconComposable(),
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
                    icon = Icons.Default.Clear.toIconComposable(),
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
                    icon = Icons.Default.Clear.toIconComposable(),
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
    val text = remember { mutableStateOf(value.toString()) }
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
    val text = remember { mutableStateOf(value?.toString() ?: "") }
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
    val text = remember { mutableStateOf(value.value?.toString() ?: "") }
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
