package com.michaelflisar.composestyled.ui.internal

import androidx.compose.foundation.focusable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onGloballyPositioned
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/** Keeps Escape available even when the body has no focusable controls. */
@InternalComposeStyledApi
@Composable
fun Modifier.dialogInput(
    dismissOnBackPress: Boolean,
    onDismissRequest: () -> Unit,
): Modifier {
    val focusRequester = remember { FocusRequester() }
    var placed by remember { mutableStateOf(false) }
    LaunchedEffect(placed) {
        if (placed) focusRequester.requestFocus()
    }
    return this
        .focusRequester(focusRequester)
        .onGloballyPositioned { placed = true }
        .onKeyEvent {
            if (dismissOnBackPress && it.key == Key.Escape && it.type == KeyEventType.KeyUp) {
                onDismissRequest()
                true
            } else false
        }
        .focusable()
}
