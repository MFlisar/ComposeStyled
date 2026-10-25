package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.classes.IconComposable
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.runtime.LocalStyledContext
import com.michaelflisar.composestyledcomponents.ui.icons.MaterialIconsAdd
import com.michaelflisar.composestyledcomponents.ui.icons.MaterialIconsRemove
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledInputAction
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.composestyledcomponents.utils.number.NumberParser
import com.michaelflisar.composestyledcomponents.utils.number.NumberUtil
import com.michaelflisar.composestyledcomponents.utils.number.NumberValidator
import com.michaelflisar.composestyledcomponents.utils.repeatingClickable

// -------------------------
// public API
// -------------------------

@Composable
fun <T : Number> StyledNumberPicker(
    value: MutableState<T>,
    validator: NumberValidator<T>,
    parser: NumberParser<T>,
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    showButtons: Boolean = true,
    overrun: Pair<T, T>? = null,
    allowManualInput: Boolean = true,
    clearFocusOnButtonPress: Boolean = true,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    onCurrentValueIsInvalid: (text: String) -> Unit = {},
) {
    StyledNumberPicker(
        value = value.value,
        validator = validator,
        parser = parser,
        onValueChange = { value.value = it },
        modifier = modifier,
        label = label,
        enabled = enabled,
        showButtons = showButtons,
        overrun = overrun,
        allowManualInput = allowManualInput,
        clearFocusOnButtonPress = clearFocusOnButtonPress,
        textStyle = textStyle,
        prefix = prefix,
        suffix = suffix,
        onCurrentValueIsInvalid = onCurrentValueIsInvalid,
    )
}

@Composable
fun <T : Number> StyledNumberPicker(
    value: T,
    validator: NumberValidator<T>,
    parser: NumberParser<T>,
    onValueChange: (T) -> Unit = {},
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    showButtons: Boolean = true,
    overrun: Pair<T, T>? = null,
    allowManualInput: Boolean = true,
    clearFocusOnButtonPress: Boolean = true,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    onCurrentValueIsInvalid: (text: String) -> Unit = {},
) = StyledNumberPickerImpl(
    value = value,
    validator = validator,
    parser = parser,
    onValueChange = onValueChange,
    modifier = modifier,
    label = label,
    enabled = enabled,
    showButtons = showButtons,
    overrun = overrun,
    allowManualInput = allowManualInput,
    clearFocusOnButtonPress = clearFocusOnButtonPress,
    textStyle = textStyle,
    prefix = prefix,
    suffix = suffix,
    onCurrentValueIsInvalid = onCurrentValueIsInvalid,
    variant = StyledInput.Variant.Default,
)

@Composable
fun <T : Number> StyledAlternativeNumberPicker(
    value: MutableState<T>,
    validator: NumberValidator<T>,
    parser: NumberParser<T>,
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    showButtons: Boolean = true,
    overrun: Pair<T, T>? = null,
    allowManualInput: Boolean = true,
    clearFocusOnButtonPress: Boolean = true,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    onCurrentValueIsInvalid: (text: String) -> Unit = {},
) {
    StyledAlternativeNumberPicker(
        value = value.value,
        validator = validator,
        parser = parser,
        onValueChange = { value.value = it },
        modifier = modifier,
        label = label,
        enabled = enabled,
        showButtons = showButtons,
        overrun = overrun,
        allowManualInput = allowManualInput,
        clearFocusOnButtonPress = clearFocusOnButtonPress,
        textStyle = textStyle,
        prefix = prefix,
        suffix = suffix,
        onCurrentValueIsInvalid = onCurrentValueIsInvalid,
    )
}

@Composable
fun <T : Number> StyledAlternativeNumberPicker(
    value: T,
    validator: NumberValidator<T>,
    parser: NumberParser<T>,
    onValueChange: (T) -> Unit = {},
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    showButtons: Boolean = true,
    overrun: Pair<T, T>? = null,
    allowManualInput: Boolean = true,
    clearFocusOnButtonPress: Boolean = true,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    onCurrentValueIsInvalid: (text: String) -> Unit = {},
) {
    StyledNumberPickerImpl(
        value = value,
        validator = validator,
        parser = parser,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        enabled = enabled,
        showButtons = showButtons,
        overrun = overrun,
        allowManualInput = allowManualInput,
        clearFocusOnButtonPress = clearFocusOnButtonPress,
        textStyle = textStyle,
        prefix = prefix,
        suffix = suffix,
        onCurrentValueIsInvalid = onCurrentValueIsInvalid,
        variant = StyledInput.Variant.Alternative,
    )
}

// -------------------------
// private API
// -------------------------

@Composable
@OptIn(InternalComposeStyledApi::class)
private fun <T : Number> StyledNumberPickerImpl(
    value: T,
    validator: NumberValidator<T>,
    parser: NumberParser<T>,
    onValueChange: (T) -> Unit = {},
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    showButtons: Boolean = true,
    overrun: Pair<T, T>? = null,
    allowManualInput: Boolean = true,
    clearFocusOnButtonPress: Boolean = true,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    onCurrentValueIsInvalid: (text: String) -> Unit = {},
    variant: StyledInput.Variant = StyledInput.Variant.Default,
) {
    val normalizedValue = parser.normalize(value)
    var currentValue by remember { mutableStateOf(normalizedValue) }
    var inputText by remember { mutableStateOf(parser.format(normalizedValue)) }
    var lastEmittedValue by remember { mutableStateOf<T?>(null) }
    var parseError by remember { mutableStateOf(false) }

    LaunchedEffect(normalizedValue, parser, validator) {
        if (lastEmittedValue == normalizedValue) {
            lastEmittedValue = null
        } else {
            lastEmittedValue = null
            inputText = parser.format(normalizedValue)
            parseError = false
        }
        currentValue = normalizedValue
        validator.update(normalizedValue)
    }

    val focusManager = LocalFocusManager.current
    val errorMessage = validator.error.value

    fun canChange(next: T): Boolean = validator.check(
        value = parser.normalize(next),
        updateError = false,
    )

    fun updateValue(next: T) {
        val normalized = parser.normalize(next)
        if (!validator.check(normalized)) return

        currentValue = normalized
        parseError = false
        inputText = parser.format(normalized)
        lastEmittedValue = normalized
        onValueChange(normalized)
        if (clearFocusOnButtonPress) {
            focusManager.clearFocus()
        }
    }

    fun decrease() {
        val candidate = parser.normalize(parser.decrement(currentValue))
        val next = if (overrun != null && !canChange(candidate)) {
            parser.normalize(overrun.second)
        } else {
            candidate
        }
        updateValue(next)
    }

    fun increase() {
        val candidate = parser.normalize(parser.increment(currentValue))
        val next = if (overrun != null && !canChange(candidate)) {
            parser.normalize(overrun.first)
        } else {
            candidate
        }
        updateValue(next)
    }

    val leadingIcon: @Composable (() -> Unit)? =
        if (showButtons || prefix != null) {
            {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (showButtons) {
                        PickerButton(
                            onClick = ::decrease,
                            enabled = enabled && (
                                    overrun != null ||
                                            canChange(parser.decrement(currentValue))
                                    ),
                            label = "Decrease",
                            icon = MaterialIconsRemove.toIconComposable(),
                        )
                    }
                    prefix?.invoke()
                }
            }
        } else {
            null
        }

    val trailingIcon: @Composable (() -> Unit)? =
        if (showButtons || suffix != null) {
            {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    suffix?.invoke()
                    if (showButtons) {
                        PickerButton(
                            onClick = ::increase,
                            enabled = enabled && (
                                    overrun != null ||
                                            canChange(parser.increment(currentValue))
                                    ),
                            label = "Increase",
                            icon = MaterialIconsAdd.toIconComposable(),
                        )
                    }
                }
            }
        } else {
            null
        }

    val styledLabel: @Composable (() -> Unit)? = if (label.isNotEmpty()) {
        { StyledText(label) }
    } else {
        null
    }

    val onInputValueChange: (String) -> Unit = { text ->
        inputText = text
        val parsed = parser.parse(text)?.let(parser.normalize)
        if (parsed == null) {
            parseError = true
            lastEmittedValue = null
            validator.check(null)
            onCurrentValueIsInvalid(text)
        } else if (validator.check(parsed)) {
            parseError = false
            currentValue = parsed
            lastEmittedValue = parsed
            onValueChange(parsed)
        } else {
            parseError = false
            lastEmittedValue = null
            onCurrentValueIsInvalid(text)
        }
    }
    val supportingText: (@Composable () -> Unit)? = if (errorMessage != null) {
        {
            AnimatedVisibility(visible = enabled && !validator.isValid.value) {
                StyledText(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMessage,
                    textAlign = TextAlign.Center,
                )
            }
        }
    } else {
        null
    }
    CompositionLocalProvider(LocalStyledContext provides StyledInput.Context.NumberPicker) {
        if (variant == StyledInput.Variant.Alternative) {
            StyledAlternativeInput(
                value = inputText,
                onValueChange = onInputValueChange,
                modifier = modifier,
                enabled = enabled,
                readOnly = !allowManualInput,
                interactionEnabled = enabled && allowManualInput,
                isError = parseError || !validator.isValid.value,
                errorMessage = errorMessage,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = if (NumberUtil.supportsDecimal(normalizedValue)) {
                        KeyboardType.Decimal
                    } else {
                        KeyboardType.Number
                    }
                ),
                textStyle = textStyle,
                label = styledLabel,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                supportingText = supportingText,
            )
        } else {
            StyledInput(
                value = inputText,
                onValueChange = onInputValueChange,
                modifier = modifier,
                enabled = enabled,
                readOnly = !allowManualInput,
                interactionEnabled = enabled && allowManualInput,
                isError = parseError || !validator.isValid.value,
                errorMessage = errorMessage,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = if (NumberUtil.supportsDecimal(normalizedValue)) {
                        KeyboardType.Decimal
                    } else {
                        KeyboardType.Number
                    }
                ),
                textStyle = textStyle,
                label = styledLabel,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                supportingText = supportingText,
            )
        }
    }
}

// -------------------------
// private functions
// -------------------------

@Composable
private fun PickerButton(
    onClick: () -> Unit,
    enabled: Boolean,
    label: String,
    icon: IconComposable,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var hasRepeated by remember { mutableStateOf(false) }

    StyledInputAction(
        icon = icon,
        onClick = {
            if (!hasRepeated) {
                onClick()
            }
            hasRepeated = false
        },
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = Modifier
            .semantics { contentDescription = label }
            .repeatingClickable(
                interactionSource = interactionSource,
                enabled = enabled,
                initialDelayMillis = 200,
                maxDelayMillis = 200,
                minDelayMillis = 5,
                delayDecayFactor = 0.20f,
                onPress = { hasRepeated = false },
                onRepeat = { hasRepeated = true },
                onClick = onClick,
            )
    )
}
