package com.michaelflisar.composestyled.ui.layout

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.ui.internal.dialogButtons
import com.michaelflisar.composestyled.ui.state.DialogState

object StyledBottomSheet {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledBottomSheet")
    val DefaultsKey = StyledKey<Defaults>("StyledBottomSheetDefaults")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    data class Defaults(
        val shape: Shape,
        val maxWidth: Dp = 640.dp,
        val maxHeightFraction: Float = 0.9f,
        val edgeMargin: Dp = 24.dp,
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
        fun Render(
            onDismissRequest: () -> Unit,
            onDismissRequestWithoutAnimation: () -> Unit,
            modifier: Modifier,
            properties: StyledDialog.Properties,
            style: StyledDialog.Style.BottomSheet,
            title: (@Composable () -> Unit)?,
            buttons: StyledDialog.Buttons,
            visible: Boolean,
            isVisible: () -> Boolean,
            content: @Composable ColumnScope.(PaddingValues) -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledBottomSheetDefaults {
    val defaults: StyledBottomSheet.Defaults
        @Composable get() = LocalStyledRegistry.current[StyledBottomSheet.DefaultsKey]
}

// -------------------------
// public API
// -------------------------

/** Non-modal overlay. Place last in a bounded Box to draw above sibling content. */
/** Shows a non-modal bottom-sheet overlay using the active dialog renderer and [state]. */
@Composable
fun StyledBottomSheet(
    state: DialogState,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = state::dismiss,
    properties: StyledDialog.Properties = StyledDialog.Properties(),
    style: StyledDialog.Style.BottomSheet = StyledDialog.Style.BottomSheet(),
    title: (@Composable () -> Unit)? = null,
    buttons: StyledDialog.Buttons = StyledDialog.Buttons(),
    content: @Composable ColumnScope.(PaddingValues) -> Unit,
) {
    val visible = state.visible
    val skipExitAnimation = remember(state) { mutableStateOf(false) }
    LaunchedEffect(state, visible) {
        if (visible) skipExitAnimation.value = false
    }
    val retained = com.michaelflisar.composestyled.ui.internal.rememberDialogCompositionVisible(
        state = state,
        animateExit = !skipExitAnimation.value,
    )
    val dismissWithoutAnimation = {
        skipExitAnimation.value = true
        onDismissRequest()
        if (state.visible) skipExitAnimation.value = false
    }
    if (retained) {
        LocalStyledRegistry.current[StyledBottomSheet.Key].Render(
            onDismissRequest, dismissWithoutAnimation, modifier, properties, style, title,
            dialogButtons(state, properties, buttons, onDismissRequest),
            visible, { state.visible }, content,
        )
    }
}
