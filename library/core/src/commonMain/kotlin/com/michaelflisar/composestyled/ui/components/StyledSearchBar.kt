package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.platform.currentPlatform
import com.michaelflisar.composestyled.ui.overlay.StyledPopupContainer
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.primitives.StyledInputAction
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// -------------------------
// public API
// -------------------------

@Composable
fun StyledSearchBar(
    value: MutableState<String>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    placeholder: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    onClear: (() -> Unit)? = null,
    onSearch: (() -> Unit)? = null,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenuDefaults.widthConfig,
    suggestions: (@Composable ColumnScope.(selectSuggestion: (String) -> Unit) -> Unit)? = null,
) {
    StyledSearchBar(
        value = value.value,
        onValueChange = { value.value = it },
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        customization = customization,
        placeholder = placeholder,
        interactionSource = interactionSource,
        onClear = onClear,
        onSearch = onSearch,
        popupWidthConfig = popupWidthConfig,
        suggestions = suggestions,
    )
}

@Composable
fun StyledSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    placeholder: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    onClear: (() -> Unit)? = null,
    onSearch: (() -> Unit)? = null,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenuDefaults.widthConfig,
    suggestions: (@Composable ColumnScope.(selectSuggestion: (String) -> Unit) -> Unit)? = null,
) {
    val actualInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val focused by actualInteractionSource.collectIsFocusedAsState()
    val popupMaxHeight = if (currentPlatform() == Platform.Android) {
        val windowHeight = LocalWindowInfo.current.containerSize.height
        if (windowHeight > 0) {
            with(LocalDensity.current) { windowHeight.toDp() }
        } else {
            StyledPopupMenuDefaults.maxHeight
        }
    } else {
        StyledPopupMenuDefaults.maxHeight
    }
    val scope = rememberCoroutineScope()
    var pendingDismissal by remember { mutableStateOf<Job?>(null) }
    var suggestionsDismissed by remember { mutableStateOf(false) }
    LaunchedEffect(focused, enabled) {
        pendingDismissal?.cancel()
        suggestionsDismissed = false
    }
    var editingValue by remember { mutableStateOf(TextFieldValue(value, TextRange(value.length))) }
    if (editingValue.text != value) {
        editingValue = TextFieldValue(value, TextRange(value.length))
        suggestionsDismissed = true
    }

    StyledPopupContainer(
        popup = {
            // Remove dismissed content immediately instead of filtering it during an exit animation.
            if (enabled && focused && !suggestionsDismissed && suggestions != null) {
                StyledPopupMenu(
                    expanded = true,
                    onDismissRequest = {
                        pendingDismissal?.cancel()
                        // Outside-click notification can precede dispatch to the input in the same frame.
                        pendingDismissal = scope.launch {
                            withFrameNanos { }
                            suggestionsDismissed = true
                        }
                    },
                    properties = StyledPopupDefaults.properties(focusable = false),
                    outsideTapBehavior =
                        StyledPopupMenu.OutsideTapBehavior.DismissAndConsume,
                    widthConfig = popupWidthConfig,
                    maxHeight = popupMaxHeight,
                ) {
                    suggestions.invoke(this) { suggestion ->
                        pendingDismissal?.cancel()
                        suggestionsDismissed = true
                        editingValue = TextFieldValue(suggestion, TextRange(suggestion.length))
                        if (suggestion != value) onValueChange(suggestion)
                    }
                }
            }
        },
        content = {
            StyledInput(
                value = editingValue,
                onValueChange = {
                    val textChanged = it.text != editingValue.text
                    editingValue = it
                    if (textChanged) {
                        pendingDismissal?.cancel()
                        suggestionsDismissed = false
                        onValueChange(it.text)
                    }
                },
                modifier = modifier.fillMaxWidth()
                    .pointerInput(enabled, suggestions != null) {
                        if (enabled && suggestions != null) {
                            awaitEachGesture {
                                awaitFirstDown(
                                    requireUnconsumed = false,
                                    pass = PointerEventPass.Initial,
                                )
                                pendingDismissal?.cancel()
                                suggestionsDismissed = false
                            }
                        }
                    }
                    .onPreviewKeyEvent {
                        if (enabled && focused && !suggestionsDismissed &&
                            suggestions != null && it.key == Key.Escape &&
                            it.type == KeyEventType.KeyDown
                        ) {
                            pendingDismissal?.cancel()
                            suggestionsDismissed = true
                            true
                        } else {
                            false
                        }
                    },
                enabled = enabled,
                shape = shape,
                customization = customization,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch?.invoke() },
                ),
                interactionSource = actualInteractionSource,
                placeholder = placeholder,
                leadingIcon = {
                    if (onSearch != null) {
                        StyledInputAction(
                            icon = StyledIcons.Search,
                            contentDescription = "Search",
                            enabled = enabled,
                            onClick = onSearch,
                        )
                    } else {
                        StyledIcon(
                            icon = StyledIcons.Search,
                            contentDescription = null,
                        )
                    }
                },
                trailingIcon = if (value.isNotEmpty()) {
                    {
                        StyledInputAction(
                            icon = StyledIcons.Clear,
                            contentDescription = "Clear",
                            enabled = enabled,
                            onClick = {
                                onValueChange("")
                                onClear?.invoke()
                            },
                        )
                    }
                } else {
                    null
                },
            )
        },
    )
}
