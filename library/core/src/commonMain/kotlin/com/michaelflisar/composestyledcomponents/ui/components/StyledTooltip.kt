package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledTooltip {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledTooltip")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    class Defaults(
        val shape: Shape,
        val containerColor: Color,
        val contentColor: Color,
        val maxWidth: Dp,
        val shadowElevation: Dp,
        val contentPadding: PaddingValues,
    )

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        val defaults: Defaults

        @Composable
        fun Render(
            state: StyledTooltipState,
            enableUserInput: Boolean,
            focusable: Boolean,
            onDismissRequest: (() -> Unit)?,
            modifier: Modifier,
            shape: Shape,
            containerColor: Color,
            contentColor: Color,
            maxWidth: Dp,
            shadowElevation: Dp,
            contentPadding: PaddingValues,
            tooltip: @Composable () -> Unit,
            content: @Composable () -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledTooltipDefaults {

    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledTooltip.Key].defaults.shape

    val containerColor: Color
        @Composable get() = LocalStyledRegistry.current[StyledTooltip.Key].defaults.containerColor

    val contentColor: Color
        @Composable get() = LocalStyledRegistry.current[StyledTooltip.Key].defaults.contentColor

    val maxWidth: Dp
        @Composable get() = LocalStyledRegistry.current[StyledTooltip.Key].defaults.maxWidth

    val shadowElevation: Dp
        @Composable get() = LocalStyledRegistry.current[StyledTooltip.Key].defaults.shadowElevation

    val contentPadding: PaddingValues
        @Composable get() = LocalStyledRegistry.current[StyledTooltip.Key].defaults.contentPadding
}

// -------------------------
// public API
// -------------------------

@Composable
fun StyledTooltip(
    state: StyledTooltipState = rememberStyledTooltipState(),
    enableUserInput: Boolean = true,
    focusable: Boolean = false,
    onDismissRequest: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    shape: Shape = StyledTooltipDefaults.shape,
    containerColor: Color = StyledTooltipDefaults.containerColor,
    contentColor: Color = StyledTooltipDefaults.contentColor,
    maxWidth: Dp = StyledTooltipDefaults.maxWidth,
    shadowElevation: Dp = StyledTooltipDefaults.shadowElevation,
    contentPadding: PaddingValues = StyledTooltipDefaults.contentPadding,
    tooltip: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    LocalStyledRegistry.current[StyledTooltip.Key].Render(
        state = state,
        enableUserInput = enableUserInput,
        focusable = focusable,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        maxWidth = maxWidth,
        shadowElevation = shadowElevation,
        contentPadding = contentPadding,
        tooltip = tooltip,
        content = content,
    )
}
