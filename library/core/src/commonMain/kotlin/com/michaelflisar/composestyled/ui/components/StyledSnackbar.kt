package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey

object StyledSnackbar {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledSnackbar")
        val DefaultsKey = StyledKey<Defaults>("StyledSnackbarDefaults")
    // -------------
    // 2) defaults
    // -------------

    @Stable
    data class Defaults(
        val shape: Shape,
        val contentPadding: PaddingValues = PaddingValues(),
    )

    // -------------
    // 3) roles / variants / context
    // -------------

    enum class Context : StyledContext { SnackbarAction }

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
            modifier: Modifier,
            shape: Shape,
            containerColor: Color,
            contentColor: Color,
            actionOnNewLine: Boolean,
            action: @Composable (() -> Unit)?,
            dismissAction: @Composable (() -> Unit)?,
            content: @Composable () -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledSnackbarDefaults {
    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledSnackbar.DefaultsKey].shape

    val contentPadding: PaddingValues
        @Composable get() = LocalStyledRegistry.current[StyledSnackbar.DefaultsKey].contentPadding
}

// -------------------------
// public API
// -------------------------

/** Renders a themed snackbar and scopes its action slots for theme-specific styling. */
@Composable
@OptIn(InternalComposeStyledApi::class)
fun StyledSnackbar(
    modifier: Modifier = Modifier,
    shape: Shape = StyledSnackbarDefaults.shape,
    containerColor: Color = StyledTheme.colorScheme.onSurface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor, checkInversely = true),
    actionOnNewLine: Boolean = false,
    action: @Composable (() -> Unit)? = null,
    dismissAction: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val scopedAction: (@Composable () -> Unit)? = action?.let { content ->
        {
            CompositionLocalProvider(
                LocalStyledContext provides StyledSnackbar.Context.SnackbarAction,
                content = content,
            )
        }
    }
    val scopedDismissAction: (@Composable () -> Unit)? = dismissAction?.let { content ->
        {
            CompositionLocalProvider(
                LocalStyledContext provides StyledSnackbar.Context.SnackbarAction,
                content = content,
            )
        }
    }
    LocalStyledRegistry.current[StyledSnackbar.Key].Render(
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        actionOnNewLine = actionOnNewLine,
        action = scopedAction,
        dismissAction = scopedDismissAction,
        content = content,
    )
}
